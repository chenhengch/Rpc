package com.example.demo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @description
 */
public class NettyRpcServer extends RpcServer {

    private Channel channel;

    public NettyRpcServer(int port, String protocol, RequestHandler handler) {
        super(protocol, port, handler);
    }

    private static Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);


    @Override
    public void start() {
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossLoopGroup, workLoopGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();

                        p.addLast(new LoggingHandler());
                        p.addLast(new ChannelRequestHandler());
                    }
                });
        try {
            //启动服务
            ChannelFuture future = serverBootstrap.bind(port).sync();
            logger.info("绑定成功");
            channel = future.channel();
            // 等待服务通道关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放线程组资源
            bossLoopGroup.shutdownGracefully();
            workLoopGroup.shutdownGracefully();

        }

    }

    @Override
    public void stop() {
        this.channel.close();
    }


    private class ChannelRequestHandler extends ChannelInboundHandlerAdapter {


        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("激活");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.info("服务端收到消息：" + msg);
            ByteBuf byteBuf = (ByteBuf) msg;
            byte[] req = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(req);
            byte[] res = handler.handlerRequest(req);
            logger.info("服务端对消息：" + msg + "响应");
            ByteBuf rpsBuf = Unpooled.buffer(res.length);
            rpsBuf.writeBytes(res);
            ctx.write(rpsBuf);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            logger.error("发生异常：" + cause.getMessage());
            ctx.close();
        }


    }
}