package org.db1.data;

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


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

            preventDropException(connection);

            Database.dropAndCreate(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    private static void preventDropException(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String[] tablenames = {"unternehmen", "kategorie", "einkaufswagen", "nutzer", "produkt", "einkaufswagen_produkt", "nutzer_einkaufswagen", "kategorie_business", "bestellung", "bestellung_produkt"};

            for (String tablename : tablenames) {
                String query = "CREATE TABLE " + tablename + "( id INTEGER NOT NULL PRIMARY KEY )";
                statement.execute(query);
            }
        } catch (Exception ignored) {}
    }

    /**
     * Diese Methode wird initialisiert, um die im SQL angewendeten Drop und create methoden zu nutzen.<br>
     * Mit drop werden die Tabellen gelöscht und mit Create werden die Tabellen wieder erstellt.<br>
     *
     */
    private static void dropAndCreate(Connection connection) {
        try {
            ScriptRunner scriptRunner = new ScriptRunner(connection, false, true);
            String filePath = new File("").getAbsolutePath();
            filePath = filePath.concat("\\src\\main\\java\\org\\db1\\data\\db_init.sql");

            Reader reader = new BufferedReader(new FileReader(filePath));
            scriptRunner.runScript(reader);

            System.out.println();
            System.out.println("DROPPPED AND RECREATED DATABASE SUCCESSFULLY");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
