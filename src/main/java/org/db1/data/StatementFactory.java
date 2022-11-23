package org.db1.data;

import org.db1.ui.InputReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Baut SQL Statements/Queries zusammen
 */

public class StatementFactory {
    private final InputReader inputReader;
    private final Mapper mapper;

    /**
     * Konsumiert im Folgenden erklärte Parameter um einmal die Datenbankverbindung zu holen und erstellt eine Instanz von unserem InputReader um Nutzereingaben zu lesen.
     * @param url Verbindungs-Url
     * @param user Nutzername
     * @param pass Passwort
     */
    public StatementFactory(String url, String user, String pass) {
        inputReader = new InputReader();
        mapper = Mapper.getInstance(url,user,pass);
    }

    /**
     * Bereitet das Insertstatement für den jeweiligen Table vor.
     * @param table Tabellenname
     * @return <code>PreparedStatement</code>
     * @throws SQLException
     */
    public PreparedStatement buildInsertStatement(String table) throws SQLException{
        HashMap<Integer, MetaData> meta = mapper.getColumnMetaData(table);
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" VALUES(");
        sb.append("?");
        for (int i = 1; i < meta.size(); i++) {
            sb.append(",?");
        }
        sb.append(")");
        System.out.println(sb);

        return mapper.getCon().prepareStatement(sb.toString());
    }

    /**
     * Bereitet das Update Statement vor.
     * @param table Tabellenname
     * @return <code>PreparedStatement</code>
     * @throws SQLException
     */
    public PreparedStatement buildUpdateStatement(String table) throws SQLException{
        HashMap<Integer, MetaData> meta = mapper.getColumnMetaData(table);
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
        return mapper.getCon().prepareStatement(sb.toString());
    }

    /**
     * Bereitet das Delete Statement vor.
     * @param table Tabellenname
     * @return <code>PerparedStatement</code>
     * @throws SQLException
     */
    public PreparedStatement buildDeleteStatement(String table) throws SQLException{
        boolean first = true;
        HashMap<Integer, MetaData> meta = mapper.getColumnMetaData(table);
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

        return mapper.getCon().prepareStatement(sb.toString());
    }

    /**
     * Bereitet das SELECT * FROM Statement vor um alles aus dem Tablle zu nehmen und zu printen.
     * @param table Tabellenname
     * @return <code>PreparedStatement</code>
     * @throws SQLException
     */
    public PreparedStatement buildShowAllStatement(String table) throws SQLException{
        HashMap<Integer, MetaData> meta = mapper.getColumnMetaData(table);
        StringBuilder statement = new StringBuilder();
        statement.append("SELECT * FROM ").append(table).append(" ORDER BY ");
        for (int i = 0; i < meta.size(); i++) {
            if(meta.get(i).isPrimaryKey()) {
                statement.append(meta.get(i).getName());
                // Bricht Loop ab sobald wir den Primary key haben
                break;
            }
        }
        return mapper.getCon().prepareStatement(statement.toString());
    }

    /**
     * Getter für den InputReader
     * @return <code>InputReader</code>
     */
    public InputReader getInputReader() {
        return inputReader;
    }

    /**
     * Getter für den Mapper
     * @return <code>Mapper</code>
     */
    public Mapper getMapper() {
        return mapper;
    }

    /**
     * Verarbeitet SQL Fehler damit das Programm lauffähig bleibt und ggf. der Nutzer weiß woran es hapert bzw. was zu tun ist.
     *
     * @see <a href="https://www.techonthenet.com/oracle/errors/ora00001.php">ORA-00001</a>
     * 1 = ORA-00001 - Unique constraint violated
     * @see <a href="https://www.techonthenet.com/oracle/errors/ora02290.php">ORA-02290</a>
     * 1 = ORA-02290 - Check constraint violated
     * @see <a href="https://www.techonthenet.com/oracle/errors/ora01400.php">ORA-01400</a>
     * 1 = ORA-1400 - Cannot insert NULL
     * @param e <code>SQLException</code>
     */
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
            eingabe = inputReader.readChar().charAt(0);
        } while (eingabe != 'y' && eingabe != 'n');

        if(eingabe == 'y') {
            e.printStackTrace();
        }
    }

    /**
     * Ruft die close() Methode vom Mapper und InputReader auf. Schließt Datenbankverbindung und Reader.
     */
    public void close() {
        mapper.close();
        inputReader.close();
    }
}