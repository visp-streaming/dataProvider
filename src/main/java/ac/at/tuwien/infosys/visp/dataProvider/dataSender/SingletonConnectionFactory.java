package ac.at.tuwien.infosys.visp.dataProvider.dataSender;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

public class SingletonConnectionFactory {

    private static CachingConnectionFactory connectionFactory = null;

    public static CachingConnectionFactory getConnection(String host, String username, String password){

        if (connectionFactory == null) {
            createFactory(host, username, password);
        }

        return connectionFactory;
    }

    private static void createFactory(String host, String username, String password) {
        connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setConnectionCacheSize(15);
    }
}