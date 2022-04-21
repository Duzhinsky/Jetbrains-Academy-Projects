package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class H2Utils {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/";

    public static void init() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL+Main.dbName);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static <T> Optional<List<T>> getObjectList(String query, RSFunction<T> constructor) {
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            List<T> result = new ArrayList<>();
            var resultSet = statement.executeQuery(query);
            while(resultSet.next())
                    result.add(constructor.applyThrows(resultSet));
            if(result.isEmpty()) return Optional.empty();
            return Optional.of(result);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static void execute(String query) {
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTables() {
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.addBatch(
                    "CREATE TABLE COMPANY(\n" +
                    "    ID INTEGER AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    NAME VARCHAR(128) NOT NULL UNIQUE\n" +
                    ");");
            statement.addBatch("CREATE TABLE CAR(\n" +
                    "                ID INTEGER AUTO_INCREMENT PRIMARY KEY,\n" +
                    "                NAME VARCHAR(128) NOT NULL UNIQUE,\n" +
                    "                COMPANY_ID INTEGER NOT NULL,\n" +
                    "                FOREIGN KEY (COMPANY_ID)\n" +
                    "                REFERENCES COMPANY(ID)\n" +
                    "            );");
            statement.addBatch("CREATE TABLE CUSTOMER(\n" +
                            "                ID INTEGER AUTO_INCREMENT PRIMARY KEY,\n" +
                            "                NAME VARCHAR(128) NOT NULL UNIQUE,\n" +
                            "                RENTED_CAR_ID INTEGER,\n" +
                            "                FOREIGN KEY (RENTED_CAR_ID)\n" +
                            "                REFERENCES CAR(ID)\n" +
                            "            );");
            statement.executeBatch();
        } catch (SQLException e) {
//            e.printStackTrace();
            // just ignore, trust it
            // TODO not ignore
        }
     }

     public static void dropTables() {
        try(Connection connection = getConnection();
            Statement  statement = connection.createStatement()) {
            statement.addBatch("DELETE FROM CUSTOMER;");
            statement.addBatch("DELETE FROM CAR;");
            statement.addBatch("DELETE FROM COMPANY;");
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
     }
}


