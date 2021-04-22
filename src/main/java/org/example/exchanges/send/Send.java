package org.example.exchanges.send;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/*
* 发布/订阅队列-广播模式-生产者
* */
public class Send {
    //交换机名称
    private final static String EXCHANGE_NAME = "ps_fanout";

    public static void main(String[] argv) throws Exception {
        //创建连接工厂 port默认不用配置
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.94.232.61");
        factory.setUsername("liu");
        factory.setPassword("liujiaxiang55");
        factory.setVirtualHost("/liu");
        //try cache新写法
        try (
                //用连接工厂 创建连接 Connection继承了Closeable接口可以自动关闭
                Connection connection = factory.newConnection();
                //创建信道
                Channel channel = connection.createChannel()) {
                    //绑定交换机 交换机名字 交换机类型：广播
                    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
                    //message内容
                    String message = "Hello World!ps_fanout";
                    //发送消息 交换机
                    channel.basicPublish(EXCHANGE_NAME,"", null, message.getBytes(StandardCharsets.UTF_8));
                    System.out.println(" [x] Sent '" + message + "'");
                }
    }
}