package org.db1.data;
import org.db1.ui.InputReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Erlaubt die Verwaltung unserer Datenbank.
 */

public class DatabaseManager {
    private final InputReader inputReader;
    private final Mapper dbMapper;
    private final StatementFactory statementFactory;

    /**
     * @param url Verbindungs-Url
     * @param user Nutzername
     * @param pass Passwort
     * Es wird eine InputReader Instanz angelegt, um die Nutzereingaben zu verarbeiten, in Kombination mit den Hashmaps bei denen die Methoden dazu auf bestimmte Datentypen von SQL gemapped wurden.
     * Es wird eine Instanz von unserem Mapper aufgerufen um die korrekte InputReader methode basierend auf dem Datentypen auswählen können.
     * Es wird eine Instanz der StatementFactory angelegt damit wir unsere SQL Queries zusammenbauen lassen können.
     */
    public DatabaseManager(String url, String user, String pass) {
        inputReader = new InputReader();
        dbMapper = Mapper.getInstance(url, user, pass);
        statementFactory = new StatementFactory(url, user, pass);
    }

    /**
     *
     * Konsumiert einen Tabellennamen, holt sich dann über einen SQL query die Tabelle und deren Daten und printed diese in die Konsole.
     * @param tableName Tabellenname
     */

    public void show(String tableName) {
        try {
            PreparedStatement statement = statementFactory.buildShowAllStatement(tableName);
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
                // Printed Leerzeile für Formatting
                System.out.println();
            }
        } catch (SQLException e) {
            statementFactory.handleSqlException(e);
        }
    }

    /**
     * Die Methode konsumiert einen Tabellennamen und holt sich damit alle Datenttypen und Namen von Spalten der Tabelle und baut die Vorbereitung des Insert Statement fertig.
     * Dann werden einmal über alle Spalten iteriert und die Nutzereingabe gefordert.
     * Mittels der Nutzereingabe werden dann dynamisch die Methoden ausgeführt, die wir über den Datenttyp der jeweiligen Spalte durch die Vorbereitung im Mapper bekommen haben.
     * @param tableName Tabellenname
     */
    public void insert(String tableName) {
        try {

            HashMap<Integer, MetaData> columnMetaData = dbMapper.getColumnMetaData(tableName);
            PreparedStatement statement = statementFactory.buildInsertStatement(tableName);

            for (int i = 0; i < columnMetaData.size(); i++) {
                MetaData data = columnMetaData.get(i);
                String rowName = data.getName();
                String type = data.getTypeName();
                char auswahl = 'y';

                if (data.isNullable()) {
                    do {
                        System.out.printf("%s ist Optional wollen Sie diesen angeben? (y/n)%n ", rowName);
                        auswahl = inputReader.readChar().charAt(0);
                    } while (auswahl != 'y' && auswahl != 'n');
                }
                if (auswahl == 'n') {
                    statement.setNull(i + 1, data.getTypeNo());
                } else {
                    System.out.println("Bitte geben Sie " + rowName + " ein. ");

                    Method setParameterInStatement = dbMapper.getStatementSetMethod(type);

                    Method readMethod = dbMapper.getInputReadMethod(type);

                    setParameterInStatement.invoke(statement, i + 1, readMethod.invoke(inputReader));
                }
            }
            System.out.println(statement.executeUpdate() == 1 ? "Erfolgreich eingefügt" : "Bitte überprüfe deine Eingabe!");
        } catch (SQLException e) {
            statementFactory.handleSqlException(e);
        } catch (InvocationTargetException | IllegalAccessException ignored) {}
    }

    /**
     * Konsumiert Tabellennamen und holt sich damit alle Spalten und deren Datenttypen und bereitet das Update-Statement über die StatementFactory vor.
     * Mittels diese Datenttypen werden dann die korrekten SET Methoden für das Statement sowie READ Methoden für die Nutzereingabe ausgewählt. Möglich durch die Vorbereitung im Mapper.
     * Iteriert über alle Spalten und fragt für diese die Nutzereingabe an.
     * @param tableName Tabellenname
     */
    public void update(String tableName){
        try{

            HashMap<Integer, MetaData> columnMetaData = dbMapper.getColumnMetaData(tableName);
            PreparedStatement statement = statementFactory.buildUpdateStatement(tableName);

            ArrayList<MetaData> primaryKeys = new ArrayList<>();
            int statementCount = 1;

            for (int i = 0; i < columnMetaData.size(); i++) {
                MetaData data = columnMetaData.get(i);
                String rowName = data.getName();
                String type = data.getTypeName();

                char auswahl = 'y';

                if (data.isNullable()) {
                    do {
                        System.out.printf("%s ist Optional wollen Sie diesen angeben? (y/n) %n", rowName);
                        auswahl = InputReader.scanner.nextLine().charAt(0);
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
                        Object input = userInputMethod.invoke(inputReader);

                        sql.invoke(statement, statementCount++, input);
                    }
                }
            }

            for (MetaData primaryKey : primaryKeys) {
                System.out.println(primaryKey.getName() + " vom zu bearbeitenden " + tableName + " eingeben: ");
                Method sql = dbMapper.getStatementSetMethod(primaryKey.getTypeName());
                Method userInputMethod = dbMapper.getInputReadMethod(primaryKey.getTypeName());
                Object input = userInputMethod.invoke(inputReader);
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
     * @param tableName Tabellenname
     */
    public void delete(String tableName) {
        try {
            HashMap<Integer, MetaData> columnMetaData = dbMapper.getColumnMetaData(tableName);
            PreparedStatement statement = statementFactory.buildDeleteStatement(tableName);
            System.out.println("---LÖSCHE AUS " + tableName + "---");
            int statementCount = 1;

            for (int i = 0; i < columnMetaData.size(); i++) {
                if(columnMetaData.get(i).isPrimaryKey()) {
                    String rowName = columnMetaData.get(i).getName();
                    String type = columnMetaData.get(i).getTypeName();

                    System.out.println("Bitte geben Sie " + rowName + " ein vom zu löschendem " + tableName);
                    Method sql = dbMapper.getStatementSetMethod(type);
                    Method userInputMethod = dbMapper.getInputReadMethod(type);
                    Object input = userInputMethod.invoke(inputReader);

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