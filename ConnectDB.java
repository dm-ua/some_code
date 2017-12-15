package sample.jobScene.jobDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.db.Connector;
import sample.jobScene.dialogJobWindow.Job;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Макс on 30.08.2017.
 */
public class ConnectDB implements JobRepository {
    private final static Logger LOGGER = Logger.getLogger("JobDB.class");
    private ObservableList<Job> jobObservableList;
    private Connection connection;
    static ObservableList arraynameOfEmployee ;


    public static void main(String[] args) {
        initDB();
    }

    public static Connection getConnection() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        Connection connection = null;
        try {
            Class.forName(properties.getProperty("db.driver"));
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void remove(String nameOfMachin, String nameOfEmployee) {
        String sql = "DELETE FROM job WHERE NAMEOFMACHIN = ? AND NAMEOFEMPLOYEE = ?";
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nameOfMachin);
            statement.setString(2, nameOfEmployee);
            statement.execute();
        } catch (SQLException sqlexception) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
    }

    public void updateIntoDB(String nameOfMachin, String nameOfEmployee, String time, int cost, int costOfCarriage,
                             String nameOfJob, String payment, boolean noOrYesPayment) {
        String sql = "UPDATE job SET NAMEOFMACHIN=?, NAMEOFEMPLOYEE=?, TIME=?, COST=?, COSTOFCARRIEGE=?, " +
                "NAMEOFJOB=?,PAYMENT=?, NOORYESPAYMENT=? WHERE NAMEOFMACHIN = ? AND NAMEOFEMPLOYEE = ?";
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nameOfMachin);
            statement.setString(2, nameOfEmployee);
            statement.setString(3, time);
            statement.setInt(4, cost);
            statement.setInt(5, costOfCarriage);
            statement.setString(6, nameOfJob);
            statement.setString(7, payment);
            statement.setBoolean(8, noOrYesPayment);

            int rs = statement.executeUpdate();
        } catch (SQLException sqlexception) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
    }


    public static void initDB() {
        String sql = "CREATE TABLE IF NOT EXISTS job " +
                "(id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "NAMEOFMACHIN VARCHAR(255) NOT NULL," +
                "NAMEOFEMPLOYEE VARCHAR(255) NOT NULL, " +
                "TIMES VARCHAR(255) NOT NULL," +
                "COSTS INT NOT NULL," +
                "COSTOFCARRIEGE INT NOT NULL," +
                "NAMEOFJOB VARCHAR(255) NOT NULL," +
                "PAYMENT VARCHAR(255) NOT NULL," +
                "NOORYESPAYMENT BOOLEAN  NOT NULL)";
        try (Connection connection = Connector.getConnection();
             Statement statement = connection.createStatement();) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertIntoDB(Job job) {
        this.insertIntoDB(job.getNameOfMachin(), job.getNameOfEmployee(), job.getTime(), job.getCost(),
                job.getCostOfCarriage(), job.getNameOfEmployee(), job.getPayment(), job.isNoOrYesPayment());
    }

    public void insertIntoDB(String nameOfMachin, String nameOfEmployee, String time, int cost,
                             int costOfCarriage, String nameOfJob, String payment, boolean noOrYesPayment) {
        String sql = "INSERT INTO job(NAMEOFMACHIN,NAMEOFEMPLOYEE,TIMES,COSTS,COSTOFCARRIEGE, " +
                "NAMEOFJOB,PAYMENT,NOORTESPAYMENT) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nameOfMachin);
            statement.setString(2, nameOfEmployee);
            statement.setString(3, time);
            statement.setInt(4, cost);
            statement.setInt(5, costOfCarriage);
            statement.setString(6, nameOfJob);
            statement.setString(7, payment);
            statement.setBoolean(8, noOrYesPayment);
            statement.execute();
        } catch (SQLException sqlexception) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
    }

    public void fillDB() {
        try (Connection connection = this.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO job(NAMEOFMACHIN,NAMEOFEMPLOYEE,TIMES,COSTS,COSTOFCARRIEGE, " +
                    "NAMEOFJOB,PAYMENT,NOORTESPAYMENT) " +
                    "VALUES(\'Камаз\', \'Петро\', \'18-00\', 1500, 500, \'Перевезення\', \'Нал\',\'true\')");

        } catch (SQLException sqlexception) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
    }

    @Override
    public ObservableList<Job> getJobRepositoryList() {
        jobObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM job";
        try(Connection connection = this.getConnection();
            Statement statement = connection.createStatement();){

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String nameOfMachin = resultSet.getString("NAMEOFMACHIN");
                String nameOfEmployee = resultSet.getString("NAMEOFEMPLOYEE");
                String time = resultSet.getString("TIMES");
                int cost = resultSet.getInt("COSTS");
                int costOfCarriage = resultSet.getInt("COSTOFCARRIEGE");
                String nameOfJob = resultSet.getString("NAMEOFJOB");
                String payment = resultSet.getString("PAYMENT");
                boolean noOrYesPayment = resultSet.getBoolean("NOORTESPAYMENT");
                Job j = new Job( nameOfMachin, nameOfEmployee, time, cost,
                 costOfCarriage,nameOfJob, payment, noOrYesPayment);
                jobObservableList.add(j);
            }

        }catch (SQLException sqlexception) {
            LOGGER.log(Level.WARNING, sqlexception.getMessage(), sqlexception);
        }
        return jobObservableList;
    }
}