package org.db1.data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Stellt Datenbankverbindung her und bietet Startoptionen an bspw Drop and Create.
 * 
 */
public class Database {
    private static Connection instance;

    /**
     *
     * @param url Verbindungs-Url
     * @param user Nutzername
     * @param password Passwort
     * @return instanceOfDatabase
     *
     * Erstellt eine Instanz der Datenbankverbindung, wenn noch nicht vorhanden. (Singleton Pattern)
     */

    public static Connection getInstance(String url, String user, String password) {
        if (instance == null) instance = connect(url, user, password);

        return instance;
    }

    /**
     * @param url Verbindungs-Url
     * @param user Nutzername
     * @param pass Passwort
     * Verbindet mit dem Datenbank-Server
     */
    private static Connection connect(String url, String user, String pass){
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, user, pass);

//            preventDropException(connection);
//            Database.dropAndCreate(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

//    /**
//     *
//     * @param connection
//     *
//     * Präventiert die Exceptions die geschmissen werden, wenn veruscht wird ein Table zu Droppen der nicht vorhanden ist.
//     * Für das Installationsskript und dessen wiederholte ausführbarkeit ist diese Exception belanglos, für Java jedoch nicht.
//     * Wir erstellen die Datenbank aus bequemlichkeit bei jedem Start des Java-Programms neu.
//     */
//    private static void preventDropException(Connection connection) {
//        try {
//            Statement statement = connection.createStatement();
//            String[] tablenames = {"unternehmen", "kategorie", "einkaufswagen", "nutzer", "produkt", "einkaufswagen_produkt", "nutzer_einkaufswagen", "kategorie_business", "bestellung", "bestellung_produkt"};
//
//            for (String tablename : tablenames) {
//                String query = "CREATE TABLE " + tablename + "( id INTEGER NOT NULL PRIMARY KEY )";
//                statement.execute(query);
//            }
//        } catch (Exception ignored) {}
//    }
//
//    /**
//     *
//     * @param connection
//     *
//     * Löscht und erstellt die Tabellen neu und befüllt sie mit Dummy-Daten
//     */
//    private static void dropAndCreate(Connection connection) {
//        try {
//            ScriptRunner scriptRunner = new ScriptRunner(connection, false, true);
//            String filePath = new File("").getAbsolutePath();
//            filePath = filePath.concat("\\src\\main\\java\\org\\db1\\data\\db_init.sql");
//
//            Reader reader = new BufferedReader(new FileReader(filePath));
//            scriptRunner.runScript(reader);
//
//            System.out.println();
//            System.out.println("DROPPPED AND RECREATED DATABASE SUCCESSFULLY");
//            System.out.println();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
