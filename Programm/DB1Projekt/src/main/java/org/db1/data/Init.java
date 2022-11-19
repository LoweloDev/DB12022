package org.db1.data;
import org.db1.ui.UserInput;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Init {
    private final HashMap<String, Method> methodByTypeSQLInsert;
    private final HashMap<String, Method> methodByTypeUI;
    private final HashMap<String, Method> methodByTypeSQLGet;
    private final HashMap<String, HashMap<Integer, MetaData>> tableData;
    private final ArrayList<String> tableNames;
    private Connection con;
    private static Init instance;


    public Init(String url, String user, String pass) {
        this.tableNames = new ArrayList<>();
        this.tableData = new HashMap<>();
        this.methodByTypeSQLInsert = new HashMap<>();
        this.methodByTypeUI = new HashMap<>();
        this.methodByTypeSQLGet = new HashMap<>();

        con = Database.getInstance(url, user, pass);
        initTableNames();
        initMethodSet();
    }

    public static Init getInstance(String url, String user, String pass) {
        if (instance == null) instance = new Init(url, user, pass);

        return instance;
    }

    private void initMethodSet(){
        try {
            methodByTypeUI.put("NUMBER", UserInput.class.getMethod("readLong"));
            methodByTypeUI.put("VARCHAR2", UserInput.class.getMethod("readString"));
            methodByTypeUI.put("DATE", UserInput.class.getMethod("readDate"));
            methodByTypeUI.put("TIMESTAMP", UserInput.class.getMethod("readTimestamp"));
            methodByTypeUI.put("CHAR", UserInput.class.getMethod("readChar"));

            methodByTypeSQLInsert.put("NUMBER", PreparedStatement.class.getMethod("setLong", int.class, long.class));
            methodByTypeSQLInsert.put("VARCHAR2", PreparedStatement.class.getMethod("setString", int.class, String.class));
            methodByTypeSQLInsert.put("DATE", PreparedStatement.class.getMethod("setDate", int.class, Date.class));
            methodByTypeSQLInsert.put("TIMESTAMP", PreparedStatement.class.getMethod("setTimestamp", int.class, Timestamp.class));
            methodByTypeSQLInsert.put("CHAR", PreparedStatement.class.getMethod("setString", int.class, String.class));

            methodByTypeSQLGet.put("NUMBER", ResultSet.class.getMethod("getLong", int.class));
            methodByTypeSQLGet.put("VARCHAR2", ResultSet.class.getMethod("getString", int.class));
            methodByTypeSQLGet.put("DATE", ResultSet.class.getMethod("getDate", int.class));
            methodByTypeSQLGet.put("TIMESTAMP", ResultSet.class.getMethod("getTimestamp", int.class));
            methodByTypeSQLGet.put("CHAR", ResultSet.class.getMethod("getString", int.class));

        } catch (Exception e) {

        }
    }

    private void initTableNames(){
        try {

            DatabaseMetaData tables = con.getMetaData();

            ResultSet dbResult = tables.getTables(con.getCatalog(), con.getSchema(), "%", null);

            while (dbResult.next()) {
                tableNames.add(dbResult.getString(3));
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Herzlichen Glückwunsch du hast ein Easter Egg gefunden!");
        }

        for (int i = 0; i < tableNames.size(); i++) {
            String tableName = tableNames.get(i);
            tableData.put(tableName, initColNamesNTypes(tableName));
        }

    }

    private ArrayList<String> getPrimaryKeys(String tableName) throws Exception{
        DatabaseMetaData tables = getCon().getMetaData();
        ResultSet primaryKeys = tables.getPrimaryKeys(con.getCatalog(), con.getSchema(), tableName);

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
            PreparedStatement stmt = con.prepareStatement(statement);

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
        } catch (Exception e) {
            //Passiert hoffentlich nie
            e.printStackTrace();
        }

        return map;
    }

    public Connection getCon() {
        return con;
    }

    public Method getSQLMethodInsert(String dataType) {
        return methodByTypeSQLInsert.get(dataType);
    }

    public Method getSQLMethodGet(String dataType) {
        return methodByTypeSQLGet.get(dataType);
    }

    public Method getUserInputMethod(String dataType) {
        return methodByTypeUI.get(dataType);
    }

    public HashMap<Integer, MetaData> getColNamesNTypes(String table){
        return tableData.get(table);
    }

    public ArrayList<String> getTableNames() {
        return tableNames;
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}