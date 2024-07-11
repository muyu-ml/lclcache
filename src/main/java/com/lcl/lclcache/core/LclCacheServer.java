package com.lcl.lclcache.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author conglongli
 * @date 2024/6/22 18:30
 */
@Component
@Slf4j
public class LclCacheServer implements LclPlugin {

    int port = 6379;
    EventLoopGroup bossGroup;
    EventLoopGroup workGroup;
    Channel channel;

    @Override
    public void init() {
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("redis-boss"));
        workGroup = new NioEventLoopGroup(16, new DefaultThreadFactory("redis-work"));
    }

    @Override
    public void startup() {
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_RCVBUF, 32*1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32*1024)
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LclCacheDecoder());
                            ch.pipeline().addLast(new LclCacheHandler());
                        }
                    });
            channel = bootstrap.bind(port).sync().channel();
            log.info("开启 netty redis 服务器，端口为 {}", port);
            channel.closeFuture().sync();
        } catch (Exception e) {
            throw  new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void shutdown() {
        if(channel != null){
            channel.close();
            channel = null;
        }

        if(bossGroup != null){
            bossGroup.shutdownGracefully();
            bossGroup = null;
        }

        if(workGroup != null){
            workGroup.shutdownGracefully();
            workGroup = null;
        }
    }
}
