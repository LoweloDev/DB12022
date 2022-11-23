package org.db1;

import org.db1.data.DatabaseManager;
import org.db1.data.StatementFactory;
import org.db1.ui.Menu;


// TODO Klassendiagramm
// TODO TEst ilias
// TODO refactor DBManager

/** Bei Unserem Projekt geht es um ein Online-Marktplatz wie bspw. ebay wir haben uns für einen Online-Marktplatz für Tiere entschieden<br>
 * Das Programm erzeugt dynamisch insert delete und show Querys für eine Datenbank damit sich Kunden für Tierscout24 (der Name unsere Website) anmelden können. <br>
 * @author Tabias Barthold Matrikelnummer: 7209370 <br>
 * @author Mehmet Özer Matrikelnummer: 7206358 <br>
 * @author Yusuf Bas Matrikelnummer: 7209349<br>
 * @version 1.0 benutzt wurde IntelliJ
 */
public class Main {
    /**
     * Datenbankverbindung<br>
     *<code>Url</code> Bitte mit URL der Datenbank ersetzen und ggf. vorher bitte mit Ihrem VPN eine Verbindung zum respektiven Netzwerk herstellen <br>
     *<code>User</code> Bitte Nutzernamen Ihres Datenbank-Logins angeben <br>
     * <code>Pass</code> Bitte Passwort zu Ihrem Login angeben <br>
     */
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@172.22.112.100:1521:fbpool";
        String user = "C##FBPOOL20";
        String pass = "oracle";
        Menu menu = new Menu(new StatementFactory(url,user,pass), new DatabaseManager(url, user, pass));
        menu.mainMenu();
        menu.close();

        // Printed Leerzeile
        System.out.println();
    }
}