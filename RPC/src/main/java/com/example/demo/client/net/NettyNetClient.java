package com.example.demo.client.net;

import com.example.demo.discovery.ServiceInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @description netty实现底层通信，也可以利用bio、原生nio等实现
 */
public class NettyNetClient implements NetClient {
    private static Logger logger = LoggerFactory.getLogger(NettyNetClient.class);

    @Override
    public byte[] sendRequest(final byte[] bytes, ServiceInfo serviceInfo) throws Throwable {
        String[] addressInfoArray = serviceInfo.getAddress().split(":");
        final SendHandler sendHandler = new SendHandler(bytes);
        byte[] respData = null;
        // 配置客户端
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler());
                            p.addLast(sendHandler);
                        }
                    });

            // 启动客户端连接
            b.connect(addressInfoArray[0], Integer.valueOf(addressInfoArray[1])).sync();
            respData = (byte[]) sendHandler.responseData();
            logger.info("收到响应消息: " + respData);

        } finally {
            // 释放线程组资源
            group.shutdownGracefully();
        }

        return respData;
    }


    private class SendHandler extends ChannelInboundHandlerAdapter {

        private CountDownLatch cdl = null;
        private Object responseMsg = null;
        private byte[] data;

        public SendHandler(byte[] bytes) {
            cdl = new CountDownLatch(1);
            data = bytes;
        }

        @Override
        public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
            logger.info("连接服务端成功");
            ByteBuf reqBuf = Unpooled.buffer(data.length);
            reqBuf.writeBytes(data);
            logger.info("客户端发送消息：" + reqBuf);
            channelHandlerContext.writeAndFlush(reqBuf);

        }

        public Object responseData() {
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return responseMsg;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.info("client sub 读取到响应信息：" + msg);
            ByteBuf byteBuf = (ByteBuf) msg;
            byte[] resp = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(resp);
            responseMsg = resp;
            cdl.countDown();
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            logger.error("发生异常", cause);
            ctx.close();
        }


    }
}