package org.db1.data;
import org.db1.ui.UserInput;
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
     * @param url Url der Datenbank mit der sich verbunden werden soll
     * @param user User mit dem sich in der Datenbank eingeloggt werden soll
     * @param pass Passwort des Benutzers der Datenbank
     * <code>Mapper</code> erstellt Bequemlichkeits-Assoziationen von Klassen und Methoden.
     */
    private Mapper(String url, String user, String pass) {
        this.tableNames = new ArrayList<>();
        this.tableData = new HashMap<>();
        this.statementSetMethodByType = new HashMap<>();
        this.inputReadMethodByType = new HashMap<>();
        this.resultGetMethodByType = new HashMap<>();

        connection = Database.getInstance(url, user, pass);
        initTableNames();
        connectDatatypesWithMethods();
    }

    public static Mapper getInstance(String url, String user, String pass) {
        if (instance == null) instance = new Mapper(url, user, pass);

        return instance;
    }

    private void connectDatatypesWithMethods(){
        try {
            inputReadMethodByType.put("NUMBER", UserInput.class.getMethod("readLong"));
            inputReadMethodByType.put("VARCHAR2", UserInput.class.getMethod("readString"));
            inputReadMethodByType.put("DATE", UserInput.class.getMethod("readDate"));
            inputReadMethodByType.put("CHAR", UserInput.class.getMethod("readChar"));

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

    private void initTableNames(){
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
            tableData.put(tableName, initColNamesNTypes(tableName));
        }

    }

    private ArrayList<String> getPrimaryKeys(String tableName) throws Exception{
        DatabaseMetaData tables = getCon().getMetaData();
        ResultSet primaryKeys = tables.getPrimaryKeys(connection.getCatalog(), connection.getSchema(), tableName);

        ArrayList<String> primaryKeysList = new ArrayList<>();
        while (primaryKeys.next()) {
            primaryKeysList.add(primaryKeys.getString("COLUMN_NAME"));
        }

        return primaryKeysList;
    }

    private HashMap<Integer, MetaData> initColNamesNTypes(String tableName) {

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

    public Connection getCon() {
        return connection;
    }

    public Method getStatementSetMethod(String dataType) {
        return statementSetMethodByType.get(dataType);
    }

    public Method getResultGetMethod(String dataType) {
        return resultGetMethodByType.get(dataType);
    }

    public Method getInputReadMethod(String dataType) {
        return inputReadMethodByType.get(dataType);
    }

    public HashMap<Integer, MetaData> getColNamesNTypes(String table){
        return tableData.get(table);
    }

    public ArrayList<String> getTableNames() {
        return tableNames;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}