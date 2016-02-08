package vispDataProvider.job;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import entities.Location;
import entities.Message;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import vispDataProvider.service.HelloWorldService1;
import vispDataProvider.service.HelloWorldService2;
import vispDataProvider.service.HelloWorldService3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

@PersistJobDataAfterExecution
public class HelloWorldJob implements Job {

    @Autowired
    private HelloWorldService1 service1;

    @Autowired
    private HelloWorldService2 service2;

    @Autowired
    private HelloWorldService3 service3;

    private String lastTime = "111";
    private Integer amount = 10;
    private Integer iteration = 1;
    private String direction = "up";

    private String basedir = "/Users/hochi/Desktop/log/";

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldJob.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {


        JobDataMap jdMap = jobExecutionContext.getJobDetail().getJobDataMap();
        if (jdMap.get("lastTime") != null) {
            lastTime = jdMap.get("lastTime").toString();
        }

        if (jdMap.get("amount") != null) {
            amount = Integer.parseInt(jdMap.get("amount").toString());
        }

        if (jdMap.get("iteration") != null) {
            iteration = Integer.parseInt(jdMap.get("iteration").toString());
        }

        if (jdMap.get("direction") != null) {
            direction = jdMap.get("direction").toString();
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
            resultSet = statement.executeQuery("SELECT * FROM Taxi WHERE time IN (select * FROM (SELECT DISTINCT time FROM Taxi WHERE time > \"" + lastTime + "\" ORDER BY time ASC LIMIT " + amount + ") sub ) ORDER BY time ASC");

            if (!resultSet.isBeforeFirst()) {
                iterate();
                jdMap.put("lastTime", lastTime);
                jdMap.put("amount", amount.toString());
                jdMap.put("iteration", iteration.toString());
                jdMap.put("direction", direction);
                return;
            }

            Integer count = 0;
            while (resultSet.next()) {
                location = new Location(resultSet.getString("id"), resultSet.getString("taxiId"), resultSet.getString("time"), resultSet.getString("latitude"), resultSet.getString("longitude"));

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                Message msg = new Message(ow.writeValueAsString(location));

                ConnectionThread con1 = new ConnectionThread(service1, msg);
                new Thread(con1).start();
/*
                ConnectionThread con2 = new ConnectionThread(service2, msg);
                new Thread(con2).start();

                ConnectionThread con3 = new ConnectionThread(service3, msg);
                new Thread(con3).start();
*/
                count++;
            }
            LOG.info(location.getTime());
//            logData(new DateTime().toString() + "," + count);
//TODO reactivate again

            resultSet.close();
            statement.close();
            connect.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        iterate();
        jdMap.put("lastTime", location.getTime());
        jdMap.put("amount", amount.toString());
        jdMap.put("iteration", iteration.toString());
        jdMap.put("direction", direction);
    }

    public void iterate() {

        if (direction.equals("up")) {
            if (iteration>30) {
                if (amount<10) {
                    amount++;
                    iteration = 1;
                } else {
                    amount--;
                    iteration = 1;
                    direction = "down";
                }
            } else {
                iteration++;
            }
        }

        if (direction.equals("down")) {
            if (iteration>30) {
                if (amount>2) {
                    amount--;
                    iteration = 1;
                } else {
                    amount++;
                    iteration = 1;
                    direction = "up";
                }
            } else {
                iteration++;
            }
        }
    }

    private void logData(String data) {

        data = data + "\n";

        File file = new File(basedir + "dataProducer.txt");
        try {

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file.getAbsolutePath(), true);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.write(data);
            bufferWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
