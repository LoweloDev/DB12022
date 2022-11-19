package org.db1.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
    private static Connection instance;

    public static Connection getInstance(String url, String user, String password) {
        if (instance == null) instance = initConnection(url, user, password);

        return instance;
    }

    /**
     * @param url
     * @param user
     * @param pass
     * Initialisierung der Verbindung mittels url, username und password und entsprechendem Treiber
     */
    private static Connection initConnection(String url, String user, String pass){
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, user, pass);
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
