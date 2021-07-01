package com.mrhualiang.provider.server;

import com.mrhualiang.provider.annotation.RpcService;
import com.mrhualiang.provider.config.ZkConfig;
import com.mrhualiang.provider.register.IServiceRegister;
import com.mrhualiang.provider.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class ZkRpcServer implements ApplicationContextAware, InitializingBean {

    @Resource
    private ZkConfig zkConfig;

    @Value("${server.port}")
    private String port;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private IServiceRegister registerCenter;

    //key 为对应的接口类名，value 为具体的实例
    private Map<String, Object> beanMappings = new HashMap<>();

    //当rpc server端初始化完成后,就会开启监听 这里也可以改成Socket调用
    @Override
    public void afterPropertiesSet() throws Exception {
        //nettyRpc();
        socketRpc();
    }

//        private void nettyRpc() throws InterruptedException {
//            //定义主线程池
//            EventLoopGroup bossGroup = new NioEventLoopGroup();
//            //定义工作线程池
//            EventLoopGroup workerGroup = new NioEventLoopGroup();
//            //类似于ServerSocket
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//            serverBootstrap.group(workerGroup, bossGroup)
//                    .channel(NioServerSocketChannel.class)
//                    //定义工作线程的处理函数
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            //添加编码/解码器 用于转化对应的传输数据  从字节流到目标对象称之为解码 反之则为编码
//                            ChannelPipeline pipeline = socketChannel.pipeline();
//                            //自定义协议解码器
//                            /**
//                             *  入参有5个，分别解释如下
//                             *  maxFrameLength：框架的最大长度。如果帧的长度大于此值，则将抛出TooLongFrameException。
//                             *  lengthFieldOffset：长度字段的偏移量：即对应的长度字段在整个消息数据中得位置
//                             *  lengthFieldLength：长度字段的长度。如：长度字段是int型表示，那么这个值就是4（long型就是8）
//                             *  lengthAdjustment：要添加到长度字段值的补偿值
//                             *  initialBytesToStrip：从解码帧中去除的第一个字节数
//                             */
//                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
//                                    //自定义协议编码器
//                                    .addLast(new LengthFieldPrepender(4))
//                                    //对象参数类型编码器
//                                    .addLast(new ObjectEncoder())
//                                    //对象参数类型解码器
//                                    .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
//                                    .addLast(new RpcServerHandler(beanMappings));
//                        }
//                    })
//                    //boss线程池的最大线程数
//                    .option(ChannelOption.SO_BACKLOG, 128)
//                    //工作线程保持长连接
//                    .childOption(ChannelOption.SO_KEEPALIVE, true);
//            //绑定端口启动netty服务端
//            ChannelFuture future = serverBootstrap.bind(ZKConfig.SERVER_PORT).sync();
//            System.out.println("netty服务端启动,端口为:" + ZKConfig.SERVER_PORT + "....");
//            future.channel().closeFuture().sync();
//        }

    private void socketRpc() {
        ServerSocket serverSocket = null;
        try {
            //创建socket
            serverSocket = new ServerSocket(Integer.parseInt(port));
            log.info("开始监听{}端口",port);
            while (true) {
                //监听端口，是个阻塞的方法
                Socket socket = serverSocket.accept();
                //处理rpc请求，这里使用线程池来处理
                executor.submit(new RpcServerHandler(beanMappings, socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context){
        //从spring上下文中获取添加了RegisterService的注解的bean
        String[] beanNames = context.getBeanNamesForAnnotation(RpcService.class);
        log.info("从spring上下文中获取添加了RegisterService的注解的bean,数量为{}",beanNames.length);
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            RpcService annotation = bean.getClass().getAnnotation(RpcService.class);
            //获取注解属性
            Class interfaceClass = annotation.interfaceClass();
            String serviceName = annotation.serviceName();
            //将接口的类名和对应的实例bean的映射关系保存起来
            beanMappings.put(interfaceClass.getName(), bean);
            //注册实例到zk
            registerCenter.register(serviceName, IPUtil.getLocalHost(), Integer.parseInt(port));
        }
    }
}
