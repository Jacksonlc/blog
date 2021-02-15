package com.jackson.user.message;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MqMessage {
    //默认连接用户名
    private static final String USERNAME = "admin";//ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = "admin";//ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址，默认端口为61616
    private static final String BROKERURL = "tcp://127.0.0.1:61616";//ActiveMQConnection.DEFAULT_BROKER_URL;
    //发送的消息数量
    private static final int SENDNUM = 10;
    public static void main(String[] args) {
        //连接工厂
        ConnectionFactory connectionFactory;
        //连接
        Connection connection = null;
        //会话,接收或者发送消息的线程
        Session session;
        //消息的目的地
        Destination destination;
        //消息生产者
        MessageProducer messageProducer;
        //实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(MqMessage.USERNAME, MqMessage.PASSWORD, MqMessage.BROKERURL);
        try {
            //通过连接工厂获取连接
            connection = connectionFactory.createConnection();
            //启动连接
            connection.start();
            //创建session
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建一个名称为Hello World!的消息队列
            destination = session.createQueue("Hello World");
            //创建消息生产者
            messageProducer = session.createProducer(destination);
            //发送消息
            sendMessage(session,messageProducer);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(connection != null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 发送消息
     * @param session
     * @param messageProducer 消息生产者
     * @throws Exception
     */
    public static void sendMessage(Session session,MessageProducer messageProducer) throws Exception{
        for (int i = 0; i < MqMessage.SENDNUM; i++) {
            //创建一条文本消息
            TextMessage message = session.createTextMessage("activemq 发送消息:" + i);
            System.err.println("发送消息:activemq 发送消息:" + i);
            //通过消息生产者发出消息
            messageProducer.send(message);
        }
    }
}
