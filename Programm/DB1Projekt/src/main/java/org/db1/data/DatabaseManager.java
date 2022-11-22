package org.db1.data;
import org.db1.ui.UserInput;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Erlaubt die Verwaltung unserer Datenbank.
 */

public class DatabaseManager {
    private final UserInput userInput;
    private final Mapper dbMapper;
    private final StatementFactory statementFactory;

    /**
     * @param url der Connection
     * @param user der Conected
     * @param pass um zu Conection
     * Es wird eine UserInput Instanz angelegt um den UserInput zu verarbeiten in Kombination mit den Hashmaps bei denen die Methoden dazu auf bestimmte Datentypen von SQL gemapped wurden.
     * Es wird eine Instanz von unserem Mapper aufgerufen um die korrekte UserInput methode basierend auf dem Datentypen auswählen können.
     * Es wird eine Instanz der StatementFactory angelegt damit wir unsere SQL Queries zusammenbauen lassen können.
     */

    public DatabaseManager(String url, String user, String pass) {
        userInput = new UserInput();
        dbMapper = Mapper.getInstance(url, user, pass);
        statementFactory = new StatementFactory(url, user, pass);
    }


    /**
     *
     * @param table
     * Konsumiert einen Tabellennamen, holt sich dann über einen SQL query die Tabelle und deren Daten und printed diese in die Konsole.
     */

    public void showTable(String table) {
        try {
            PreparedStatement statement = statementFactory.buildShowAllStatement(table);
            ResultSet resultSet = statement.executeQuery();


            int columns = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columns; i++) {
                System.out.printf("%-20s", resultSet.getMetaData().getColumnLabel(i));
}
            System.out.println();

            for (int i = 0; i < columns; i++) {
                System.out.print("--------------------");
            }

            System.out.println();

            while (resultSet.next()) {
                for (int i = 1; i <= columns; i++) {
                    System.out.printf("%-20s", resultSet.getString(i));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            statementFactory.handleSqlException(e);
        }
    }

    /**
     * Die Methode konsumiert einen Tabellennamen und holt sich damit alle Datenttypen und Namen von Spalten der Tabelle und baut die Vorbereitung des Insert Statement fertig.
     * Dann werden einmal über alle Spalten iteriert und die Nutzereingabe gefordert.
     * Mittels der Nutzereingabe werden dann dynamisch die Methoden ausgeführt die wir über den Datenttyp der jeweiligen Spalte durch die Vorbereitung im Mapper bekommen haben.
     * @param table
     */
    public void insertIntoTable(String table) {
        try {

            HashMap<Integer, MetaData> colNamesNTypes = dbMapper.getColNamesNTypes(table);
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

                    Method setParameterInStatement = dbMapper.getStatementSetMethod(type);

                    Method readMethod = dbMapper.getInputReadMethod(type);

                    setParameterInStatement.invoke(statement, i + 1, readMethod.invoke(userInput));
                }
            }
            System.out.println(statement.executeUpdate() == 1 ? "Erfolgreich eingefügt" : "Bitte überprüfe deine Eingabe, da kann etwas nicht stimmmen! (Tabelle wurde nicht verändert)");
        } catch (SQLException e) {
            statementFactory.handleSqlException(e);
        } catch (InvocationTargetException | IllegalAccessException ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * Konsumiert Tabellennamen und holt sich damit alle Spalten und deren Datenttypen und bereitet das Update-Statement über die StatementFactory vor.
     * Mittels diese Datenttypen werden dann die korrekten SET Methoden für das Statement sowie READ Methoden für die Nutzereingabe ausgewählt. Möglich durch die Vorbereitung im Mapper.
     * Iteriert über alle Spalten und fragt für diese die Nutzereingabe an.
     * @param table der Tabellenname aus den etwas Gelöscht werden soll
     */
    public void updateTable(String table){
        try{

            HashMap<Integer, MetaData> colNamesNTypes = dbMapper.getColNamesNTypes(table);
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
                        Method sql = dbMapper.getStatementSetMethod(type);
                        Method userInputMethod = dbMapper.getInputReadMethod(type);
                        Object input = userInputMethod.invoke(userInput);

                        sql.invoke(statement, statementCount++, input);
                    }
                }
            }

            for (int i = 0; i < primaryKeys.size(); i++) {
                System.out.println(primaryKeys.get(i).getName() + " vom zu bearbeitenden " + table + " eingeben: ");
                Method sql = dbMapper.getStatementSetMethod(primaryKeys.get(i).getTypeName());
                Method userInputMethod = dbMapper.getInputReadMethod(primaryKeys.get(i).getTypeName());
                Object input = userInputMethod.invoke(userInput);
                sql.invoke(statement, statementCount++, input);
            }

            System.out.println(statement.executeUpdate() == 1 ? "Erfolgreich geupdatet" : "Bitte überprüfe deine Eingabe, da kann etwas nicht stimmmen! (Tabelle wurde nicht verändert)");

        }catch(SQLException e){
            statementFactory.handleSqlException(e);
        } catch (InvocationTargetException | IllegalAccessException ignored) {

        }
    }

    /**
     * Löscht einen Eintrag aus einer Tabelle.
     * Dazu wird einmal über die Spalten und Datentypen iteriert und für Primary-Keys eine Nutzereingabe gefordert.
     * @param table
     */
    public void deleteFromTable(String table) {
        try {
            HashMap<Integer, MetaData> colNamesNTypes = dbMapper.getColNamesNTypes(table);
            PreparedStatement statement = statementFactory.buildDeleteStatement(table);
            System.out.println("---LÖSCHE AUS " + table + "---");
            int statementCount = 1;

            for (int i = 0; i < colNamesNTypes.size(); i++) {
                if(colNamesNTypes.get(i).isPrimaryKey()) {
                    String rowName = colNamesNTypes.get(i).getName();
                    String type = colNamesNTypes.get(i).getTypeName();

                    System.out.println("Bitte geben Sie " + rowName + " ein vom zu löschendem " + table);
                    Method sql = dbMapper.getStatementSetMethod(type);
                    Method userInputMethod = dbMapper.getInputReadMethod(type);
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