package com.lcl.lclcache.core;

import com.lcl.lclcache.LclCache;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.redis.ArrayHeaderRedisMessage;
import io.netty.handler.codec.redis.BulkStringHeaderRedisMessage;
import io.netty.handler.codec.redis.DefaultBulkStringRedisContent;
import io.netty.handler.codec.redis.RedisMessage;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * @Author conglongli
 * @date 2024/6/22 19:12
 */
@Slf4j
public class LclCacheHandler extends SimpleChannelInboundHandler<String> {

    private static final String CRLF = "\r\n";
    private static final String STRING_PREFIX = "+";
    private static final String BULK_PREFIX = "$";
    private static final String OK = "OK";
    private static final String INFO = "LclCache Server[v1.0.0], created by lcl" + CRLF
                                     + "Mock Redis Server at " + LocalDate.now() + " in Beijing" + CRLF;

    private static final LclCache CACHE = new LclCache();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {

        String[] args = message.split(CRLF);
        log.info("LclCacheHandler => {}", String.join(",", args));
        String cmd = args[2].toUpperCase();
        if("COMMAND".equals(cmd)){
            bulkString(ctx, "*2"
                    + CRLF + "$7"
                    + CRLF + "COMMAND"
                    + CRLF + "$4"
                    + CRLF + "PING"
                    + CRLF);
        } else if("PING".equals(cmd)) {
            String ret = "PONG";
            if(args.length >= 5){
                ret = args[4];
            }
            simpleString(ctx, ret);
        } else if("INFO".equals(cmd)) {
            bulkString(ctx, INFO);
        } else if("SET".equals(cmd)) {
            // set 收到的报文如下
            //*3
            //    $3
            //            set
            //    $2
            //            k1
            //    $5
            //            aa100
            //
            CACHE.set(args[4], args[6]);
            simpleString(ctx, OK);
        } else if("GET".equals(cmd)) {
            // get 收到的报文如下
            //*2
            //    $3
            //            get
            //    $2
            //            k1
            String value = CACHE.get(args[4]);
            bulkString(ctx, value);
        } else if("STRLEN".equals(cmd)) {
            // strlen 收到的报文如下
            //*2
            //$6
            //strlen
            //$2
            //k1
            String value = CACHE.get(args[4]);
            integer(ctx, value == null ? 0 : value.length());
        } else if("DEL".equals(cmd)) {
            // strlen 收到的报文如下（允许删除多个）
            //*5,$3,del,$2,k1,$2,k2,$2,k3,$2,k4
            int len = (args.length -3)/2;
            String[] keys = new String[len];
            for(int i=0;i<len; i++){
                keys[i] = args[4+i*2];
            }
            int del = CACHE.del(keys);
            integer(ctx, del);
        } else if("EXISTS".equals(cmd)) {
            // EXISTS 收到的报文如下（允许删除多个）
            //*2,$6,EXISTS,$2,k1,$2,k2,$2,k3,$2,k4
            int len = (args.length -3)/2;
            String[] keys = new String[len];
            for(int i=0;i<len; i++){
                keys[i] = args[4+i*2];
            }
            integer(ctx, CACHE.exists(keys));
        } else if("MGET".equals(cmd)) {
            int len = (args.length -3)/2;
            String[] keys = new String[len];
            for(int i=0;i<len; i++){
                keys[i] = args[4+i*2];
            }
            array(ctx, CACHE.mget(keys));
        } else if("MSET".equals(cmd)) {
            int len = (args.length -3)/4;
            String[] keys = new String[len];
            String[] values = new String[len];
            for(int i=0;i<len; i++){
                keys[i] = args[4+i*4];
                values[i] = args[6+i*4];
            }
            CACHE.mset(keys, values);
            simpleString(ctx, OK);
        } else {
            simpleString(ctx, OK);
        }
    }



    private void array(ChannelHandlerContext ctx, String[] array){
        StringBuilder sb = new StringBuilder();
        if(array == null) {
            sb.append("*-1").append(CRLF);
        } else if (array.length == 0) {
            sb.append("*0").append(CRLF);
        } else {
            sb.append("*" + array.length + CRLF);
            for (int i=0;i< array.length;i++){
                sb.append("$").append(array[i].length()).append(CRLF).append(array[i]).append(CRLF);
            }
        }
        writeByteBuf(ctx, sb.toString());
    }
    private void integer(ChannelHandlerContext ctx, int i){
        writeByteBuf(ctx, ":" + i + CRLF);
    }

    private void bulkString(ChannelHandlerContext ctx, String content){
        String ret;
        // 对 null 、空字符串做单独处理
        if(content == null){
            ret = "-1";
        } else if(content.isEmpty()){
            ret = "0";
        } else {
            ret = content.getBytes().length + CRLF + content;
        }
        writeByteBuf(ctx, BULK_PREFIX + ret + CRLF);
    }

    private void simpleString(ChannelHandlerContext ctx, String content){
        String ret;
        // 对 null 、空字符串做单独处理
        if(content == null){
            ret = "$-1";
        } else if(content.isEmpty()){
            ret = "$0";
        } else {
            ret = STRING_PREFIX + content;
        }
        writeByteBuf(ctx, ret + CRLF);
    }

    private void writeByteBuf(ChannelHandlerContext ctx, String content){
        log.info("wrap byte buffer and reply: {}", content);
        ByteBuf byteBuf = Unpooled.buffer(128);
        byteBuf.writeBytes(content.getBytes());
        ctx.writeAndFlush(byteBuf);
    }
}
