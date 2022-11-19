package org.db1.data;
import org.db1.ui.UserInput;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Managed die Datenbank die ihm im DatabaseInit übergeben wurde.
 */

public class DatabaseManager {
    private final UserInput userInput;
    private final Init dbInit;
    private final StatementFactory statementFactory;

    public DatabaseManager(String url, String user, String pass) {
        userInput = new UserInput();
        dbInit = Init.getInstance(url, user, pass);
        statementFactory = new StatementFactory(url, user, pass);
    }

    public void showTable(String table) {
        try {
            HashMap<Integer, MetaData> colNamesNTypes = dbInit.getColNamesNTypes(table);

            PreparedStatement statement = statementFactory.buildShowAllStatement(table);
            ResultSet rs = statement.executeQuery();
            System.out.println("----" + table + "----");
            while (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append(table).append("[");
                for (int i = 0; i < colNamesNTypes.size(); i++) {
                    sb.append(colNamesNTypes.get(i).getName()).append(": ");
                    Method get = dbInit.getSQLMethodGet(colNamesNTypes.get(i).getTypeName());
                    Object reading = get.invoke(rs,i+1);

                    if(rs.wasNull())
                        reading = null;

                    sb.append(reading);
                    if(i + 1 < colNamesNTypes.size())
                        sb.append(", ");
                }
                sb.append("]");
                System.out.println(sb);
            }

        } catch (SQLException e) {
            statementFactory.handleSqlException(e);
        } catch (InvocationTargetException | IllegalAccessException ignored) {

        }
    }

    public void insertIntoTable(String table) {
        try {

            HashMap<Integer, MetaData> colNamesNTypes = dbInit.getColNamesNTypes(table);
            PreparedStatement statement = statementFactory.buildInsertStatement(table);

            for (int i = 0; i < colNamesNTypes.size(); i++) {
                MetaData data = colNamesNTypes.get(i);
                String rowName = data.getName();
                String type = data.getTypeName();
                char auswahl = 'y';

                if (data.isNullable()) {
                    do {
                        System.out.printf("%s ist Optional wollen Sie diesen angeben? (y/n)%n ", rowName);
                        auswahl = userInput.readChar().charAt(0);
                    } while (auswahl != 'y' && auswahl != 'n');
                }
                if (auswahl == 'n') {
                    statement.setNull(i + 1, data.getTypeNo());
                } else {
                    System.out.println("Bitte geben Sie " + rowName + " ein. ");

                    Method sql = dbInit.getSQLMethodInsert(type);

                    Method userInputMethod = dbInit.getUserInputMethod(type);

                    sql.invoke(statement, i + 1, userInputMethod.invoke(userInput));
                    //statement.setString(3, userInput.scan());

                }
            }
            System.out.println(statement.executeUpdate() == 1 ? "Erfolgreich eingefügt" : "Bitte überprüfe deine Eingabe, da kann etwas nicht stimmmen! (Tabelle wurde nicht verändert)");
        } catch (SQLException e) {
            statementFactory.handleSqlException(e);
        } catch (InvocationTargetException | IllegalAccessException ignored) {

        }
    }

    public void updateTable(String table){
        try{

            HashMap<Integer, MetaData> colNamesNTypes = dbInit.getColNamesNTypes(table);
            PreparedStatement statement = statementFactory.buildUpdateStatement(table);
            ArrayList<MetaData> primaryKeys = new ArrayList<>();
            int statementCount = 1;

            for (int i = 0; i < colNamesNTypes.size(); i++) {
                MetaData data = colNamesNTypes.get(i);
                String rowName = data.getName();
                String type = data.getTypeName();

                char auswahl = 'y';

                if (data.isNullable()) {
                    do {
                        System.out.printf("%s ist Optional wollen Sie diesen angeben? (y/n) %n", rowName);
                        auswahl = UserInput.sc.nextLine().charAt(0);
                    } while (auswahl != 'y' && auswahl != 'n');
                }
                if (auswahl == 'n') {
                    statement.setNull(statementCount++, data.getTypeNo());
                } else {
                    if (data.isPrimaryKey()) {
                        primaryKeys.add(data);
                    } else {
                        System.out.println("Bitte geben Sie " + rowName + " ein. ");
                        Method sql = dbInit.getSQLMethodInsert(type);
                        Method userInputMethod = dbInit.getUserInputMethod(type);
                        Object input = userInputMethod.invoke(userInput);

                        sql.invoke(statement, statementCount++, input);
                    }
                }
            }

            for (int i = 0; i < primaryKeys.size(); i++) {
                System.out.println(primaryKeys.get(i).getName() + " vom zu bearbeitenden " + table + " eingeben: ");
                Method sql = dbInit.getSQLMethodInsert(primaryKeys.get(i).getTypeName());
                Method userInputMethod = dbInit.getUserInputMethod(primaryKeys.get(i).getTypeName());
                Object input = userInputMethod.invoke(userInput);
                sql.invoke(statement, statementCount++, input);
            }

            System.out.println(statement.executeUpdate() == 1 ? "Erfolgreich geupdatet" : "Bitte überprüfe deine Eingabe, da kann etwas nicht stimmmen! (Tabelle wurde nicht verändert)");

        }catch(SQLException e){
            statementFactory.handleSqlException(e);
        } catch (InvocationTargetException | IllegalAccessException ignored) {

        }
    }

    public void countRecusriveFromAccounts() {
        try {
            System.out.println("---RECURSIVE COUNT FROM ACCOUNTS---");
            StringBuilder recursiveOutput = new StringBuilder();
            String statement = "SELECT a.username, COUNT(*) FROM accounts a, accounts b WHERE a.id=b.moderatorid GROUP BY a.username";
            PreparedStatement stmt = dbInit.getCon().prepareStatement(statement);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String result = "Moderator: " + rs.getString(1) +" | ModeratorCount: " + rs.getInt(2);
                recursiveOutput.append(result).append("\n");
            }
            System.out.println(recursiveOutput);
        } catch (SQLException e) {
            statementFactory.handleSqlException(e);
        }
    }

    public void deleteFromTable(String table) {
        try {
            HashMap<Integer, MetaData> colNamesNTypes = dbInit.getColNamesNTypes(table);
            PreparedStatement statement = statementFactory.buildDeleteStatement(table);
            StringBuilder sb = new StringBuilder();
            System.out.println("---LÖSCHE AUS " + table + "---");
            int statementCount = 1;

            for (int i = 0; i < colNamesNTypes.size(); i++) {
                if(colNamesNTypes.get(i).isPrimaryKey()) {
                    String rowName = colNamesNTypes.get(i).getName();
                    String type = colNamesNTypes.get(i).getTypeName();

                    System.out.println("Bitte geben Sie " + rowName + " ein vom zu löschendem " + table);
                    Method sql = dbInit.getSQLMethodInsert(type);
                    Method userInputMethod = dbInit.getUserInputMethod(type);
                    Object input = userInputMethod.invoke(userInput);

                    sql.invoke(statement, statementCount++, input);
                }
            }

            System.out.println(statement.executeUpdate() == 1 ? "Erfolgreich gelöscht" : "Bitte überprüfe deine Eingabe, da kann etwas nicht stimmmen! (Tabelle wurde nicht verändert)");

        } catch (SQLException e) {
            statementFactory.handleSqlException(e);
        } catch (InvocationTargetException | IllegalAccessException ignored) {

        }
    }
}