package org.db1;

import org.db1.data.DatabaseManager;
import org.db1.data.StatementFactory;
import org.db1.ui.Menu;
// TODO refactor DBManager

/**
 * WICHTIG: Bitte beachten Sie, ggf. 1. dass wir Gradle als build Tool verwendet haben und 2. dass Sie das Installationsskript einmalig auf die Datenbank ausführen müssen, über SQL-Developer oder Equivalenzen, da wir das nicht automatisiert beim Programmstart tun.
 *
 *  @see <a href="https://github.com/LoweloDev/Java-Persistence---but-for-poor-people/tree/main/src/TKKG/DATA">Ebenfalls WICHTIG: Mein (Tobias Barthold) altes DB1 Projekt</a>
 *  Nach Mail-Absprache mit Frau Inga Saatz durften wir den Teil bezüglich der Datenbankkommunikation aus meinem Alten Projekt, den ich im Grunde Solo gebaut habe und somit mindestens mein Intellectual-Property ist wiederverwenden. Dazu sollte ich mein altes Projekt hier auf meinem Github Account
 *  verlinken. Zusätzlich dazu habe ich zur Sicherheit auch einen Screenshot im Root-Ordner des Projektes hinterlegt der diese Absprache mit Frau Saatz abbildet. Die Datei heißt ABSPRACHE_INGA_SAATZ.png
 *
 * Bei unserem Projekt geht es um ein Online-Marktplatz wie bspw. Ebay wir haben uns für einen Online-Marktplatz für Tiere entschieden.<br>
 * Das Programm bereitet hauptsächlich dynamisch SQL Statements vor um so jede beliebige Oracle Datenbank zu verwalten, wenn man den Mapper und Manager noch etwas ausbaut, in dem Fall unsere Tierscout24 Datenbank (der Name unserer hypothetischen Website). <br>
 * @author Tabias Barthold Matrikelnummer: 7209370 <br>
 * @author Mehmet Oezer Matrikelnummer: 7206358 <br>
 * @author Yusuf Bas Matrikelnummer: 7209349 <br>
 * @version 1.0 benutzt wurde IntelliJ und CodeWithMe für Kollaboration
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