package org.db1.ui;
import org.db1.data.DatabaseManager;
import org.db1.data.StatementFactory;

import java.util.ArrayList;

/**
 * Stellt ein Konsolen Menu bereit um Datenbank mithilfe des DatabaseManager zu verändern
 */

public class Menu {
    private StatementFactory statementFactory;
    private DatabaseManager dbManager;

    public Menu(StatementFactory statementFactory, DatabaseManager dbManager) {
        this.statementFactory = statementFactory;
        this.dbManager = dbManager;
    }


    /**
     * Creates a menu frame with all table names and indecies.
     * Then checks for an input and navigates through the submenus
     */
    public void mainMenu() {
        ArrayList<String> tables = statementFactory.getDbInit().getTableNames();
        String[] menu = new String[tables.size() + 2];
        menu[0] = "Welche Tabelle wollen Sie bearbeiten?";
        for (int i = 0; i < tables.size(); i++) {
            menu[i+1] = tables.get(i);
        }
        menu[menu.length - 1] = "Beenden";

        int eingabe;
        do {
            printMenu(menu);
            eingabe = statementFactory.getUserInput().readInt();
            if(eingabe < menu.length - 1 && eingabe >= 1) {
                String tabellenName = tables.get(eingabe - 1);
                if(!tabellenName.equals("ACCOUNTS"))
                    tableMenuDefault(tabellenName);
                else
                    tableMenuAccounts();
            } else if(eingabe != menu.length - 1) {
                System.out.println("Ungültige Eingabe!");
            }
        } while (eingabe != menu.length - 1);
        System.out.println("BEENDET");
    }


    /**
     * Printet ein Menu und ruft die Methoden der entsprechnenden tablle an
     * @param tabellenName Tabelle die bearbeitet werden soll.
     *
     */
    public void tableMenuDefault(String tabellenName) {
        String[] menu = {"Was wollen Sie mit der Tabelle " + tabellenName + " machen?", "Anzeigen", "Einfügen", "Löschen", "Updaten", "Zurück ins Hauptmenü"};
        int eingabe;
        do {
            printMenu(menu);
            eingabe = statementFactory.getUserInput().readInt();
            switch (eingabe) {
                case 1 -> dbManager.showTable(tabellenName);
                case 2 -> dbManager.insertIntoTable(tabellenName);
                case 3 -> dbManager.deleteFromTable(tabellenName);
                case 4 -> dbManager.updateTable(tabellenName);
                case 5 ->System.out.println("... Zurück ins Hauptmenü");
                default -> System.out.println("Ungültige Eingabe!");
            }
        } while (eingabe != menu.length - 1);
    }

    /**
     * Spezielles Menu für die Tabble Account, die eine rekursive Beziehung enthält, welche nicht dynamisch generiert wird.
     * Deshalb "hard coded" <br>
     * see:
     * {@link DatabaseManager#countRecusriveFromAccounts()}
     */
    public void tableMenuAccounts() {
        String[] menu = {"Was wollen Sie mit der Tabelle ACCOUNTS" + " machen?", "Anzeigen", "Einfügen", "Löschen", "Updaten", "Rekursive Beziehungen zählen","Zurück ins Hauptmenü"};
        int eingabe;
        do {
            printMenu(menu);
            eingabe = statementFactory.getUserInput().readInt();
            switch (eingabe) {
                case 1 -> dbManager.showTable("ACCOUNTS");
                case 2 -> dbManager.insertIntoTable("ACCOUNTS");
                case 3 -> dbManager.deleteFromTable("ACCOUNTS");
                case 4 -> dbManager.updateTable("ACCOUNTS");
                case 5 -> dbManager.countRecusriveFromAccounts();
                case 6 ->System.out.println("... Zurück ins Hauptmenü");
                default -> System.out.println("Ungültige Eingabe!");
            }
        } while (eingabe != menu.length - 1);
    }


    /**
     * Helper Methode die ein Menu mithilfe der übergebene Parameter printet
     * @param options die auf der Konsole zur option stehen sollen
     */
    private void printMenu(String[] options) {
        String placeholder = "-".repeat(23);
        System.out.printf("%s%s%s%n", placeholder, "TKKG-Streaming-Database", placeholder);
        System.out.printf("|   %-64s|%n", options[0]);
        for (int i = 1; i < options.length; i++) {
            System.out.printf("|   (%d) %-60s|%n", i, options[i]);
        }
        System.out.println("-".repeat(69));
        System.out.print("Auswahl:  ");
    }

    /**
     * closes all connections to database and scanners etc.
     */
    public void close() {
        statementFactory.close();
    }
}
