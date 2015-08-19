package vispDataProvider.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import entities.Location;

import java.sql.*;

@Service
public class HelloWorldService {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldService.class);

    @Value("${rabbitMQ.outgoingQueue}")
    private String QUEUE_NAME;

    @Value("${spring.rabbitmq.host}")
    private String RABBITMQ_HOST;

    public String hello(String lastDate) {
        Connection connect = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(RABBITMQ_HOST);

        try {
            Location location = new Location();

            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/taxiData","root", "password");

            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Taxi WHERE time = (SELECT time FROM Taxi WHERE time > \"" + lastDate + "\" ORDER BY time ASC LIMIT 1)");

            while (resultSet.next()) {
                location = new Location(resultSet.getString("id"), resultSet.getString("taxiId"), resultSet.getString("time"), resultSet.getString("latitude"), resultSet.getString("longitude"));

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                Message msg = new Message(ow.writeValueAsString(location));

                RabbitTemplate template = new RabbitTemplate(connectionFactory);
                template.setRoutingKey(QUEUE_NAME);
                template.setQueue(QUEUE_NAME);
                template.convertAndSend(msg);
                LOG.info("Message sent: " + msg.toString());

            }
            connectionFactory.destroy();


            return location.getTime();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return lastDate;
    }

}
