package org.example.direct.recv;

import com.rabbitmq.client.*;

/*
* 路由队列-消费者1
* */
public class Recv {
    private final static String EXCHANGE_NAME = "routing_direct";

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
        //绑定交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //获取队列名
        String queueName = channel.queueDeclare().getQueue();
        //将交换机和队列绑定
        String errorRoutingkey="error";
        channel.queueBind(queueName, EXCHANGE_NAME, errorRoutingkey);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            //拿到消息
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        //监听队列 消费消息
        //队列名称 自动回执
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}