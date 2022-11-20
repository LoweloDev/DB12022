package org.db1.ui;
import org.db1.data.DatabaseManager;
import org.db1.data.StatementFactory;
import org.db1.shared.Helpers;

import java.util.ArrayList;

/**
 * Stellt ein Konsolen Menu bereit um Datenbank mithilfe des DatabaseManager zu verändern
 */
public class Menu {
    private final StatementFactory statementFactory;
    private final DatabaseManager dbManager;

    public Menu(StatementFactory statementFactory, DatabaseManager dbManager) {
        this.statementFactory = statementFactory;
        this.dbManager = dbManager;
    }
    /**
     * Creates a menu frame with all table names and indecies.
     * Then checks for an input and navigates through the submenus
     */
    public void mainMenu() {
        ArrayList<String> menuItems = statementFactory.getDbInit().getTableNames();
        menuItems.add("Beenden");

        int inputNumber;
        do {
            printMenu("Welche Tabelle wollen Sie bearbeiten?", menuItems.toArray(new String[0]));
            inputNumber = statementFactory.getUserInput().readInt();

            if(inputNumber < Helpers.lastIndexOf(menuItems) && inputNumber >= 1) {
                String tabellenName = menuItems.get(inputNumber);
                tableMenuDefault(tabellenName);
            } else if(inputNumber != Helpers.lastIndexOf(menuItems)) {
                System.out.println("Ungültige Eingabe!");
            }
        } while (inputNumber != Helpers.lastIndexOf(menuItems));
        System.out.println("BEENDET");
    }
    /**
     * Printet ein Menu und ruft die Methoden der entsprechnenden tablle an
     * @param tabellenName Tabelle die bearbeitet werden soll.
     *
     */
    public void tableMenuDefault(String tabellenName) {
        String[] menuItems = {"Anzeigen", "Einfügen", "Löschen", "Updaten", "Zurück ins Hauptmenü"};
        int inputNumber;
        do {
            printMenu("Was wollen Sie mit der Tabelle " + tabellenName + " machen?",  menuItems);
            inputNumber = statementFactory.getUserInput().readInt();
            switch (inputNumber) {
                case 0 -> dbManager.showTable(tabellenName);
                case 1 -> dbManager.insertIntoTable(tabellenName);
                case 2 -> dbManager.deleteFromTable(tabellenName);
                case 3 -> dbManager.updateTable(tabellenName);
                case 4 -> System.out.println("... Zurück ins Hauptmenü");
                default -> System.out.println("Ungültige Eingabe!");
            }
        } while (inputNumber != Helpers.lastIndexOf(menuItems));
    }

    private void printMenu(String title, String[] items) {
        System.out.println(title);
        for (int i = 0; i < items.length; i++) {
            System.out.printf("%d) %s \n", i, items[i]);
        }
        System.out.print("Auswahl:  ");
    }

    public void close() {
        statementFactory.close();
    }
}
