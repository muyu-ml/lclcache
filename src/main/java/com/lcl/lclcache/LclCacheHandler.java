package com.lcl.lclcache;

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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {

        String[] args = message.split(CRLF);
        log.info("LclCacheHandler => {}", String.join(",", args));
        String cmd = args[2].toUpperCase();
        if("COMMAND".equals(cmd)){
            String s1 = "*2"
                    + CRLF + "$7"
                    + CRLF + "COMMAND"
                    + CRLF + "$4"
                    + CRLF + "PING"
                    + CRLF;
            bulkString(ctx, s1);
            return;
        } if("PING".equals(cmd)) {
            String ret = "PONG";
            if(args.length >= 5){
                ret = args[4];
            }
            simpleString(ctx, ret);
        } else if("INFO".equals(cmd)) {
            bulkString(ctx, INFO);
        }else {
            simpleString(ctx, OK);
        }
    }

    private void bulkString(ChannelHandlerContext ctx, String content){
        writeByteBuf(ctx, "$" + content.getBytes().length + CRLF + content + CRLF);
    }

    private void simpleString(ChannelHandlerContext ctx, String content){
        writeByteBuf(ctx, STRING_PREFIX + content + CRLF);
    }

    private void writeByteBuf(ChannelHandlerContext ctx, String content){
        log.info("wrap byte buffer and reply: {}", content);
        ByteBuf byteBuf = Unpooled.buffer(128);
        byteBuf.writeBytes(content.getBytes());
        ctx.writeAndFlush(byteBuf);
    }
}
