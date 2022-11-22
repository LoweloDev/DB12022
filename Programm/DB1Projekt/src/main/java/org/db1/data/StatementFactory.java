package org.db1.data;

import org.db1.ui.UserInput;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Managed die Datenbank die ihm im DatabaseInit übergeben wurde.
 */

public class StatementFactory {
    private UserInput userInput;
    private Mapper dbMapper;

    public StatementFactory(String url, String user, String pass) {
        userInput = new UserInput();
        dbMapper = Mapper.getInstance(url,user,pass);
    }

    public PreparedStatement buildInsertStatement(String table) throws SQLException{
        HashMap<Integer, MetaData> meta = dbMapper.getColNamesNTypes(table);
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" VALUES(");
        sb.append("?");
        for (int i = 1; i < meta.size(); i++) {
            sb.append(",?");
        }
        sb.append(")");
        System.out.println(sb);

        return dbMapper.getCon().prepareStatement(sb.toString());
    }

    public PreparedStatement buildUpdateStatement(String table) throws SQLException{
        HashMap<Integer, MetaData> meta = dbMapper.getColNamesNTypes(table);
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        ArrayList<String> primaryKeys = new ArrayList<>();
        sb.append("UPDATE ").append(table).append(" SET ");

        for (int i = 0; i < meta.size(); i++) {
            if(meta.get(i).isPrimaryKey()) {
                primaryKeys.add(meta.get(i).getName());
            } else {
                if(first) {
                    sb.append(meta.get(i).getName()).append(" = ?");
                    first = false;
                } else {
                    sb.append(",").append(meta.get(i).getName()).append(" = ?");
                }
            }
        }
        sb.append(" WHERE ");
        sb.append(primaryKeys.get(0)).append(" = ?");
        if(primaryKeys.size() > 1) {
            for (int i = 1; i < primaryKeys.size(); i++) {
                sb.append(" AND ").append(primaryKeys.get(i)).append(" = ?");
            }
        }
        System.out.println(sb);
        return dbMapper.getCon().prepareStatement(sb.toString());
    }

    public PreparedStatement buildDeleteStatement(String table) throws SQLException{
        boolean first = true;
        HashMap<Integer, MetaData> meta = dbMapper.getColNamesNTypes(table);
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(table).append(" WHERE ");
        for (int i = 0; i < meta.size(); i++) {
            if(first && meta.get(i).isPrimaryKey()) {
                sb.append(meta.get(i).getName()).append(" = ?");
                first = false;
            } else if(meta.get(i).isPrimaryKey()){
                sb.append(" AND ").append(meta.get(i).getName()).append(" = ?");
            }
        }

        return dbMapper.getCon().prepareStatement(sb.toString());
    }

    public PreparedStatement buildShowAllStatement(String table) throws SQLException{
        HashMap<Integer, MetaData> meta = dbMapper.getColNamesNTypes(table);
        StringBuilder statement = new StringBuilder();
        statement.append("SELECT * FROM ").append(table).append(" ORDER BY ");
        for (int i = 0; i < meta.size(); i++) {
            //Break immer sehr unschön hier jedoch unserer Meinung nach keine andere Möglichkeit
            //Außer man returnt direkt in der If Anweisung und returnt Standardmäßig null
            if(meta.get(i).isPrimaryKey()) {
                statement.append(meta.get(i).getName());
                break;
            }
        }
        return dbMapper.getCon().prepareStatement(statement.toString());
    }

    public UserInput getUserInput() {
        return userInput;
    }

    /**
     * @return returns dbInit
     */

    public Mapper getDbInit() {
        return dbMapper;
    }

    public void handleSqlException(SQLException e) {
        System.out.println();
        System.err.println("Es wurde nichts in der Datenbank verändert");
        if(e.getErrorCode() == 1) {
            System.err.println("Eine eingabe die du getroffen hast und unique ist bereits in der Datenbank");
        }
        if(e.getErrorCode() == 2290) {
            System.err.println("Eine Check Bedingung wurde nicht erfüllt");
        }
        if(e.getErrorCode() == 1400) {
            System.err.println("Du hast eine NULL Value angegeben bei einer Value die nicht NULL sein darf");
        }
        char eingabe;
        do {
            System.out.println("Möchtest du den vollständigen error code sehen? (y/n)");
            eingabe = userInput.readChar().charAt(0);
        } while (eingabe != 'y' && eingabe != 'n');

        if(eingabe == 'y') {
            e.printStackTrace();
        }
    }

    public void close() {
        dbMapper.close();
        userInput.close();
    }
}