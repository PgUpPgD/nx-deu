package com.itcast.nettychathm.server.handler;

import com.itcast.nettychathm.message.RpcRequestMessage;
import com.itcast.nettychathm.message.RpcResponseMessage;
import com.itcast.nettychathm.server.service.HelloService;
import com.itcast.nettychathm.server.service.ServicesFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage message) throws Exception {
        RpcResponseMessage response = new RpcResponseMessage();
        response.setSequenceId(message.getSequenceId());
        try {
            HelloService service = (HelloService)
                    ServicesFactory.getService(Class.forName(message.getInterfaceName()));
            Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());
            Object invoke = method.invoke(service, message.getParameterValue());
            response.setReturnValue(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            //e.getCause() 获取真正的出错原因日志
            String msg = e.getCause().getMessage();
            response.setExceptionValue(new Exception("远程调用出错:" + msg));
        }
        ctx.writeAndFlush(response);
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RpcRequestMessage massage = new RpcRequestMessage(
                1,
                "com.itcast.nettychathm.server.service.HelloService",
                "sayHello",
                String.class,
                new Class[]{String.class},
                new Object[]{"张三"}
        );

        HelloService service = (HelloService)ServicesFactory.getService(Class.forName(massage.getInterfaceName()));
        Method method = service.getClass().getMethod(massage.getMethodName(), massage.getParameterTypes());
        Object invoke = method.invoke(service, massage.getParameterValue());
        System.out.println(invoke);
    }
}
