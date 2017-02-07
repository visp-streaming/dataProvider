package ac.at.tuwien.infosys.visp.dataProvider.job;


import ac.at.tuwien.infosys.visp.common.Message;
import ac.at.tuwien.infosys.visp.common.cloud.Location;
import ac.at.tuwien.infosys.visp.dataProvider.dataSender.RabbitMQSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.joda.time.DateTime;

import java.sql.*;

public class TaxiDataGeneratorJob extends DataGeneratorJob {

    protected RabbitMQSender sender;

    public void customDataGeneration() {
        sender = new RabbitMQSender(host, user, password);

        String lastTime = "111";

        if (jdMap.get("lastTime") != null) {
            lastTime = jdMap.get("lastTime").toString();
        }

        Connection connect = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Location location = new Location();


        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/taxiData", "root", "password");

            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Taxi WHERE time IN (select * FROM (SELECT DISTINCT time FROM Taxi WHERE time > \"" + lastTime + "\" ORDER BY time ASC LIMIT " + state.getAmount() + ") sub ) ORDER BY time ASC");

            if (!resultSet.isBeforeFirst()) {
                jdMap.put("lastTime", lastTime);
                return;
            }

            Integer count = 0;
            while (resultSet.next()) {
                location = new Location(resultSet.getString("id"), resultSet.getString("taxiId"), resultSet.getString("time"), resultSet.getString("latitude"), resultSet.getString("longitude"));

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                Message msg = new Message(ow.writeValueAsString(location));

                ConnectionThread con1 = new ConnectionThread(sender, msg, "source");
                new Thread(con1).start();
                count++;
            }

            state.setTime(new DateTime().toString());
            state.setActualAmount(count);
            //logger.save(state);

            resultSet.close();
            statement.close();
            connect.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        jdMap.put("lastTime", location.getTime());
    }

}
