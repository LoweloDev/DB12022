package org.db1.ui;
import org.db1.data.DatabaseManager;
import org.db1.data.StatementFactory;
import org.db1.shared.Helpers;

import java.util.ArrayList;

/**
 * Stellt ein Konsolen Menu bereit um Datenbank mithilfe des DatabaseManager zu verwalten.
 */
public class Menu {
    private final StatementFactory statementFactory;
    private final DatabaseManager databaseManager;

    /**
     * Konsumiert eine Instanz der StatementFactory sowie des DatenbankManagers und instanziert unser Menü.
     * @param statementFactory Instanz der StatementFactory
     * @param databaseManager Instanz des DatabaseManagers
     */
    public Menu(StatementFactory statementFactory, DatabaseManager databaseManager) {
        this.statementFactory = statementFactory;
        this.databaseManager = databaseManager;
    }

    /**
     * Listet die Tabellen auf und gibt die Möglichkeit eine Auszuwählen.
     */
    public void mainMenu() {
        ArrayList<String> menuItems = statementFactory.getMapper().getTableNames();
        menuItems.add("Beenden");

        int inputNumber;
        do {
            printMenu("Welche Tabelle wollen Sie bearbeiten?", menuItems.toArray(new String[0]));
            inputNumber = statementFactory.getInputReader().readInt();

            if(inputNumber < Helpers.lastIndexOf(menuItems) && inputNumber >= 0) {
                String tabellenName = menuItems.get(inputNumber);
                tableMenu(tabellenName);
            } else if(inputNumber != Helpers.lastIndexOf(menuItems)) {
                System.out.println("Ungültige Eingabe!");
            }
        } while (inputNumber != Helpers.lastIndexOf(menuItems));
        System.out.println("BEENDET");
    }

    /**
     * Listet die Verwaltungsoptionen bezüglich einer Tabelle auf und gibt die Möglichkeit diese Auszuwählen.
     * @param tableName Tabellenname
     */
    public void tableMenu(String tableName) {
        String[] menuItems = {"Anzeigen", "Einfügen", "Löschen", "Updaten", "Zurück ins Hauptmenü"};
        int inputNumber;
        do {
            printMenu("Was wollen Sie mit der Tabelle " + tableName + " machen?",  menuItems);
            inputNumber = statementFactory.getInputReader().readInt();
            switch (inputNumber) {
                case 0 -> databaseManager.show(tableName);
                case 1 -> databaseManager.insert(tableName);
                case 2 -> databaseManager.delete(tableName);
                case 3 -> databaseManager.update(tableName);
                case 4 -> System.out.println("... Zurück ins Hauptmenü");
                default -> System.out.println("Ungültige Eingabe!");
            }
        } while (inputNumber != Helpers.lastIndexOf(menuItems));
    }

    /**
     * Druckt das Menü in die Konsole.
     * @param title Menütitel
     * @param items Menüitems
     */
    private void printMenu(String title, String[] items) {
        System.out.println(title);
        for (int i = 0; i < items.length; i++) {
            System.out.printf("%d) %s \n", i, items[i]);
        }
        System.out.print("Auswahl:  \n");
    }

    /**
     * Schließt die StatementFactory (bzw. Ruft dessen close Methode auf die wiederum die des Mappers aufruft sowie die des InputReaders wobei erstere wiederum die der Connection aufruft und diese Datenbankverbindung schließt und
     * letztere die des Scanners aufruft und den Scanner schließt.
     */
    public void close() {
        statementFactory.close();
    }
}
