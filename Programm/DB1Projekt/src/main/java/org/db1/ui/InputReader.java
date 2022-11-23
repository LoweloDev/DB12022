package org.db1.ui;

import org.db1.shared.Helpers;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * Liest Nutzereingaben
 * @see java.sql.Types
 */

public class InputReader {

    public static Scanner scanner = new Scanner(System.in);

    /**
     * List ein Datum von der Konsole ein.
     * @return <code>Date</code> Java Date-Objekt basierend auf der Datumseingabe des Nutzers
     */
    public Date readDate() {
        System.out.print("Bitte geben Sie ein Datum ein (TT.MM.YYYY): ");
        String date = scanner.nextLine();
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

    /**
     * Liest simplen String von der Konsole ein.
     * @return <code>String</code>
     */
    public String readString() {
        return scanner.nextLine();
    }

    /**
     * Liest long von der Konsole ein
     * @return <code>long</code>
     *
     */
    public long readLong(){
        return Long.valueOf((Integer) Helpers.readAndValidateNumber(scanner));
    }

    /**
     * Liest int von der Konsole ein
     * @return <code>int</code>
     */
    public int readInt() {
        return (int) Helpers.readAndValidateNumber(scanner);
    }

    /**
     * Liest char von der Konsole ein
     * @return <code>String</code>
     */
    public String readChar() {
        String eingabe;
        do {
            System.out.print("Bitte geben Sie einen character an: ");
            eingabe = scanner.nextLine();
        } while (eingabe.length() != 1);
        return String.valueOf(eingabe.charAt(0));
    }

    /**
     * Schließt den Scanner
     */
    public void close() {
        scanner.close();
    }
}