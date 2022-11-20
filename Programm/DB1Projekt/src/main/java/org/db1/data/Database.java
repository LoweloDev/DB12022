package org.db1.data;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.*;
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
            Database.dropAndCreate(connection);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }



        return connection;
    }

    // TODO create Tables before running script
    private static void dropAndCreate(Connection connection) {
        try {
            ScriptRunner scriptRunner = new ScriptRunner(connection, false, true);
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("\\src\\main\\java\\org\\db1\\data\\db_init.sql");

            System.out.println("PFAD");
            System.out.println(filePath);

            Reader reader = new BufferedReader(new FileReader(filePath));
            scriptRunner.runScript(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * Diese Methode wird initialisiert, um die im SQL angewendeten Drop und create methoden zu nutzen.<br>
         * Mit drop werden die Tabellen gelöscht und mit Create werden die Tabellen wieder erstellt.<br>
         *
         */
    }
}
