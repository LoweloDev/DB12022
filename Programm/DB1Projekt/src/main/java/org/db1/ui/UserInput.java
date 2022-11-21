package org.db1.ui;

import org.db1.shared.Helpers;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * Kann UserInput lesen welcher für Datenbank Values benötigt wird
 * @see java.sql.Types
 */

public class UserInput {

    public static Scanner sc = new Scanner(System.in);

    /**
     * List ein Datum vom Benutzer ein
     * @return Datum was vom Benutzer eingelesen wurde, wurde kein richtiges Datum vom Benutzer eingegeben so wird das heute Datum zurück gegeben
     */
    public Date readDate() {
        System.out.print("Bitte geben Sie ein Datum ein (TT.MM.YYYY): ");
        String date = sc.nextLine();
        // neue Zeile
        System.out.println();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        try {
            java.util.Date date1 = dateFormat.parse(date);
            return new java.sql.Date(date1.getTime());
        } catch (ParseException e) {
            System.out.println("Ungültiges Format angegeben! Nutze aktuelles Datum");
            return new java.sql.Date(System.currentTimeMillis());
        }
    }

    public String readString() {
        return sc.nextLine();
    }
    /**
     * Liste einen long von der Konsole ein, wird so lange wiederholt bis der Nutzer einen gültigen wert eingibt
     * @return Long den ein Nutzer eingegeben hat
     *
     */
    public long readLong(){
        return Long.valueOf((Integer) Helpers.readAndValidateNumber(sc));
    }


    public int readInt() {
        return (int) Helpers.readAndValidateNumber(sc);
    }

    public String readChar() {
        String eingabe;
        do {
            System.out.print("Bitte geben Sie einen character an: ");
            eingabe = sc.nextLine();
        } while (eingabe.length() != 1);
        return String.valueOf(eingabe.charAt(0));
    }

    public void close() {
        sc.close();
    }
}