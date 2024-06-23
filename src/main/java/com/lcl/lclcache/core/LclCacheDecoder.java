package com.lcl.lclcache.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author conglongli
 * @date 2024/6/22 21:11
 */
@Slf4j
public class LclCacheDecoder extends ByteToMessageDecoder {
    AtomicLong counter = new AtomicLong();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        log.info("LclCacheDecoder decoderCount:{}", counter.incrementAndGet());

        int count = in.readableBytes();
        if(count <= 0){
            return;
        }
        int index = in.readerIndex();
        log.info("count: {}, index: {}", count, index);

        byte[] bytes = new byte[count];
        in.readBytes(bytes);
        String ret = new String(bytes);
        log.info("ret: {}", ret);
        out.add(ret);
    }
}
