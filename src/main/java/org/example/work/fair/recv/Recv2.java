package org.example.work.fair.recv;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/*
* 工作队列-工平 消费者1
* */
public class Recv2 {

    private final static String QUEUE_NAME = "work_fair";

    public static void main(String[] argv) throws Exception {
        //创建连接工厂 port默认不用配置
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.94.232.61");
        factory.setUsername("liu");
        factory.setPassword("liujiaxiang55");
        factory.setVirtualHost("/liu");
        //用连接工厂 创建连接 Connection继承了Closeable接口可以自动关闭
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //绑定队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //限制消费者每次只能接收一（prefetchCount）条消息，处理完才能接收下一条
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //拿到消息
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            //手动确认 参数：multiple是否确认多条-》false逐条确认
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        //监听队列 消费消息
        //队列名称 自动回执
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
    }
}