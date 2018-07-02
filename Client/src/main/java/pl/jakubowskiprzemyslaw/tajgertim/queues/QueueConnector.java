package pl.jakubowskiprzemyslaw.tajgertim.queues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class QueueConnector {

    Connection connection;
    Channel channel;
    String queueName;

    public void connectToQueue(String queueName) throws IOException, TimeoutException {
        createConnection();
        createChannel();
        declareQueue(queueName);
    }

    private void createConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("Admin1234@!");
        factory.setHost("10.30.1.140");

        connection = factory.newConnection();
    }

    private void createChannel() throws IOException {
        channel = connection.createChannel();
    }

    private void declareQueue(String queueName) throws IOException {
        boolean queueAutoDelete = false;
        boolean queueExclusive = false;
        boolean queueDurable = false;
        this.queueName = queueName;
        channel.queueDeclare(queueName, queueDurable, queueExclusive, queueAutoDelete, null);
    }

    void sendMessage(String queueName, String json) throws IOException {
        channel.basicPublish("", queueName, null, json.getBytes());
    }

    public void closeConnection() throws IOException, TimeoutException {
        if(channel.isOpen()){
            channel.close();
        }
        if (connection.isOpen()) {
            connection.close();
        }
    }

    public GetResponse get(String playerQueueName) throws IOException {
        return channel.basicGet(playerQueueName, true);
    }

}