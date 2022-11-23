package org.db1;

import org.db1.data.DatabaseManager;
import org.db1.data.StatementFactory;
import org.db1.ui.Menu;

// TODO Test ilias
// TODO refactor DBManager

/**
 * WICHTIG: Bitte beachten Sie, dass wir Gradle als BuildTool verwendet haben und eine kleine Library verwendet haben die für die Aufgabe NICHT relevant war, um bei jedem Start unseres Programms unser Installationsskript neu auszuführen!
 * Sie können diese in der build.gradle finden. Wenn Sie Gradle verwenden sollte es ohne Änderungen abgesehen von der Datenbankverbindung selber keine Probleme bei der Ausführung geben.
 *
 *  @see <a href="https://github.com/LoweloDev/Java-Persistence---but-for-poor-people/tree/main/src/TKKG/DATA">Ebenfalls WICHTIG: Mein altes DB1 Projekt</a>
 *  Nach Mail-Absprache mit Frau Inga Saatz durften wir den Teil bezüglich der Datenbankkommunikation aus meinem Alten Projekt, den ich im Grunde Solo gebaut habe und somit mein Intellectual-Property ist wiederverwenden. Dazu sollte ich mein altes Projekt hier auf meinem Github Account
 *  verlinken. Grund ist der, dass diese Lösung dynamisch genug ist um mit minimaler Anpassung mit jeder beliebigen Datenbank von Oracle zu funktionieren. Ich habe durchaus weitere Ideen das ganze noch besser umzusetzen und mich dabei zwangsläufig an Dinge wie Repository Design Pattern
 *  und Spring Boot anzunähern, jedoch würde dies den Rahmen dieses Projekts meiner Meinung nach sprengen.
 *
 * Bei unserem Projekt geht es um ein Online-Marktplatz wie bspw. Ebay wir haben uns für einen Online-Marktplatz für Tiere entschieden.<br>
 * Das Programm bereitet dynamisch SQL Statements vor um so (im Endeffekt wie, in dem Fall ich (Tobias Barthold), auch schon im letzten Projekt gesagt habe) jede beliebige Oracle Datenbank zu verwalten, wenn man den Mapper und Manager noch etwas ausbaut, in dem Fall unsere Tierscout24 Datenbank (der Name unserer hypothetischen Website). <br>
 * @author Tabias Barthold Matrikelnummer: 7209370 <br>
 * @author Mehmet Oezer Matrikelnummer: 7206358 <br>
 * @author Yusuf Bas Matrikelnummer: 7209349 <br>
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