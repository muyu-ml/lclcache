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
            if(value == null) {
                simpleString(ctx, null);
            } else {
                bulkString(ctx, value);
            }
        } else {
            simpleString(ctx, OK);
        }
    }




    private void bulkString(ChannelHandlerContext ctx, String content){
        writeByteBuf(ctx, "$" + content.getBytes().length + CRLF + content + CRLF);
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
