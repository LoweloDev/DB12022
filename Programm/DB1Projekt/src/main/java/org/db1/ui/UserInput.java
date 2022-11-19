package org.db1.ui;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
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
    public Long readLong(){
        boolean rightinput;
        long i = 0;
        do {
            try{
                rightinput=true;
                i = sc.nextInt();
                sc.nextLine();
            }catch (InputMismatchException e){
                System.out.println("Keine gültige Eingabe");
                rightinput=false;
                sc.nextLine();
            }
        }while (!rightinput);
        return i;
    }


    public int readInt() {
        boolean rightinput;
        int i = 0;

        do {
            try{
                rightinput = true;
                i = sc.nextInt();
                sc.nextLine();
            }catch (InputMismatchException e){
                System.out.println("Keine gültige Eingabe");
                rightinput=false;
                sc.nextLine();
            }
        }while (!rightinput);
        return i;
    }

    public void printMenu(String[] options) {
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
     * Timestamp vom Nutzer einzulesen ist sehr nervig. Deswegen wird einfach die aktuelle Zeit als PLatzhalter an
     * dieser Stelle eingefügt.
     * @return
     */
    public java.sql.Timestamp readTimestamp(){
        System.out.println("Timestamp wird automatisch auf jetzige Zeit gesetzt!");
        return new java.sql.Timestamp(System.currentTimeMillis());
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