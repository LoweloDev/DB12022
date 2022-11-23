package org.db1.data;
import org.db1.ui.InputReader;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Mapped SQL-Datentypen auf SET, GET und READ Methoden von verschiedenen Klassen.
 */
public class Mapper {
    private final HashMap<String, Method> statementSetMethodByType;
    private final HashMap<String, Method> inputReadMethodByType;
    private final HashMap<String, Method> resultGetMethodByType;
    private final HashMap<String, HashMap<Integer, MetaData>> tableData;
    private final ArrayList<String> tableNames;
    private final Connection connection;
    private static Mapper instance;

    /**
     * @param url Verbindungs-Url
     * @param user Nutzername
     * @param pass Passwort
     * <code>Mapper</code> erstellt Bequemlichkeits-Assoziationen von Klassen und Methoden.
     */
    private Mapper(String url, String user, String pass) {
        this.tableNames = new ArrayList<>();
        this.tableData = new HashMap<>();
        this.statementSetMethodByType = new HashMap<>();
        this.inputReadMethodByType = new HashMap<>();
        this.resultGetMethodByType = new HashMap<>();

        connection = Database.getInstance(url, user, pass);
        collectTableMetaData();
        connectDatatypesWithMethods();
    }

    /**
     * Erstellt eine Instanz von <code>Mapper</code> wenn noch keine existiert sonst returned es die existierende Instanz. (Singleton Pattern)
     * @param url Verbindungs-Url
     * @param user Nutzername
     * @param pass Passwort
     * @return instance of <Code>Mapper</Code>
     */
    public static Mapper getInstance(String url, String user, String pass) {
        if (instance == null) instance = new Mapper(url, user, pass);

        return instance;
    }

    /**
     * Verbindet die SQL Datenttypen mit Methoden über Erstellung einer HashMap einmal mit den Methoden zum lesen der Nutzereingaben,
     * mit den Methoden zum setzen der Parameter in einem Prepared Statement und zum holen der Werte aus einem ResultSet.
     */
    private void connectDatatypesWithMethods(){
        try {
            inputReadMethodByType.put("NUMBER", InputReader.class.getMethod("readLong"));
            inputReadMethodByType.put("VARCHAR2", InputReader.class.getMethod("readString"));
            inputReadMethodByType.put("DATE", InputReader.class.getMethod("readDate"));
            inputReadMethodByType.put("CHAR", InputReader.class.getMethod("readChar"));

            statementSetMethodByType.put("NUMBER", PreparedStatement.class.getMethod("setLong", int.class, long.class));
            statementSetMethodByType.put("VARCHAR2", PreparedStatement.class.getMethod("setString", int.class, String.class));
            statementSetMethodByType.put("DATE", PreparedStatement.class.getMethod("setDate", int.class, Date.class));
            statementSetMethodByType.put("CHAR", PreparedStatement.class.getMethod("setString", int.class, String.class));

            resultGetMethodByType.put("NUMBER", ResultSet.class.getMethod("getLong", int.class));
            resultGetMethodByType.put("VARCHAR2", ResultSet.class.getMethod("getString", int.class));
            resultGetMethodByType.put("DATE", ResultSet.class.getMethod("getDate", int.class));
            resultGetMethodByType.put("CHAR", ResultSet.class.getMethod("getString", int.class));
        } catch (Exception ignored) {}
    }

    /**
     * Sammelt die Namen und Spaltennamen sowie Datenttypen von Tabellen um später damit dynamisch die korrekte Methode für den korrekten Datentypen aus den HashMaps zu holen und
     * Queries vorzubereiten.
     */
    private void collectTableMetaData(){
        try {

            DatabaseMetaData tables = connection.getMetaData();

            ResultSet dbResult = tables.getTables(connection.getCatalog(), connection.getSchema(), "%", null);

            while (dbResult.next()) {
                tableNames.add(dbResult.getString(3));
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Herzlichen Glückwunsch du hast ein Easter Egg gefunden!");
        }

        for (String tableName : tableNames) {
            tableData.put(tableName, collectColumnMetaData(tableName));
        }

    }

    /**
     * Sammelt die Primary-Keys einer Tabelle und returned diese.
     * @param tableName Tabellenname
     * @return <code>ArrayList<\String></code> primaryKeysList
     * @throws Exception
     */
    private ArrayList<String> getPrimaryKeys(String tableName) throws Exception {
        DatabaseMetaData tables = getCon().getMetaData();
        ResultSet primaryKeys = tables.getPrimaryKeys(connection.getCatalog(), connection.getSchema(), tableName);

        ArrayList<String> primaryKeysList = new ArrayList<>();
        while (primaryKeys.next()) {
            primaryKeysList.add(primaryKeys.getString("COLUMN_NAME"));
        }

        return primaryKeysList;
    }

    /**
     * Sammelt die Spaltennamen und Datentypen um sie in die Hashmap mit den korrekten Tabellennamen zu packen um später darauf dynamisch zugreifen zu könenn und dann wiederum die korrekten Methoden für den Datenttypen aus den weiteren Hashmaps ziehen zu können.
     * @param tableName Tabellenname
     * @return <code>HashMap</code>
     */
    private HashMap<Integer, MetaData> collectColumnMetaData(String tableName) {

        HashMap<Integer, MetaData> map = new HashMap<>();

        try {
            ArrayList<String> primaryKeysList = getPrimaryKeys(tableName);
            String statement = "SELECT * FROM " + tableName;
            PreparedStatement stmt = connection.prepareStatement(statement);

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                boolean isPrimaryKey = false;
                for (String primaryKey :
                        primaryKeysList) {
                    if (metaData.getColumnName(i).equals(primaryKey))
                        isPrimaryKey = true;
                }

                MetaData meta = new MetaData(metaData.getColumnName(i), metaData.getColumnTypeName(i),
                        metaData.getColumnType(i), metaData.isNullable(i) == ResultSetMetaData.columnNullable, isPrimaryKey);
                map.put(i - 1, meta);
            }
        } catch (Exception ignored) {}

        return map;
    }

    /**
     * Getter für die Datenbankverbindung.
     * @return <code>Connection</code>
     */
    public Connection getCon() {
        return connection;
    }

    /**
     * Getter für die korrekte SET Methode für <code>PreparedStatement</code>s.
     * Holt aus der HashMap die korrekte SET Methode für den Datenttypen.
     * @param dataType SQL-Datentyp bspw. CHAR
     * @return <code>Method</code>
     */
    public Method getStatementSetMethod(String dataType) {
        return statementSetMethodByType.get(dataType);
    }

    /**
     * Getter für die korrekte GET Methode für ResultSet Parameter nach erfolgtem Query auf Datenbank.
     * Holt aus der HashMap die korrekte GET Methode für den Datentypen.
     * @param dataType SQL-Datentyp bspw. CHAR
     * @return <code>Method</code>
     */
    public Method getResultGetMethod(String dataType) {
        return resultGetMethodByType.get(dataType);
    }

    /**
     * Getter für die korrekte READ Methode für Nutzereingaben basierend auf dem SQL Datentypen.
     * Holt aus der HashMap die korrekte READ Methode für den jeweiligen Datentypen.
     * @param dataType
     * @return <code>Method</code>
     */
    public Method getInputReadMethod(String dataType) {
        return inputReadMethodByType.get(dataType);
    }

    /**
     * Getter für die Spalten-Metadaten.
     * Holt durch angabe des Tables die Metadaten für die Spalten (Name und Datentyp).
     * @param table Tabellenname
     * @return <code>HashMap</code>
     */
    public HashMap<Integer, MetaData> getColumnMetaData(String table){
        return tableData.get(table);
    }

    /**
     * Getter für die Liste der Tabellennamen.
     * @return <code>ArrayList</code>
     */
    public ArrayList<String> getTableNames() {
        return tableNames;
    }

    /**
     * Schließt die Datenbankverbindung. Wird von anderen Close Methoden aufgerufen und zieht sich quasi rekursiv bis hier hin durch.
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}