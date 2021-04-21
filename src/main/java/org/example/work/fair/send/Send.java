package org.example.work.fair.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/*
* 工作队列-公平-生产者
* */
public class Send {
    //队列名称
    private final static String QUEUE_NAME = "work_fair";

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
                /*
                * 绑定队列 队列名
                * 持久化
                * 排他队列（基于连接课件，仅对首次声明它的连接可见，名字不能相同，连接关闭之后排他队列自动删除不管持久化）
                * 自动删除
                * 参数
                * */
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                //发多条消息
                for(int i=0;i<20;i++){
                    //message内容
                    String message = "Hello World!Work"+i;
                    //发送消息 交换机
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                    System.out.println(" [x] Sent '" + message + "'"+i);
                }
        }
    }
}