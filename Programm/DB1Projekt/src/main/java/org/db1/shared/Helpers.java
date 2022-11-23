package org.db1.shared;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Einige Helper Methoden die wir für Lesbarkeit angelegt habe.
 */
public class Helpers {

    /**
     * Sollte den input der immer n+1 im vergleich zum Index war (was wir abgeändert haben) zur korrekten Indexvalue ändern und mehr Lesbarkeit bieten als "n-1".
     * @param input Input Integer
     * @return <code>Integer</code>
     */
    public static Integer inputToIndex(int input) {
        return input - 1;
    }

    /**
     * Sollte das letzte Item aus einem Array holen und mehr Lesbarkeit bieten als array.length - 1
     * @param array Ziel Array
     * @return <code>T</code> Result Item
     * @param <T>
     */
    public static <T> T lastItem(T[] array) {
        return array[array.length - 1];
    }

    /**
     * Gibt den letzten Index eines Arrays zurück und soll mehr Lesbarkeit bieten als array.length - 1
     * @param array Ziel-Array von beliebigem Typen
     * @return <code>Integer</code>
     * @param <T>
     */
    public static <T> Integer lastIndexOf(T[] array) {
        return array.length - 1;
    }

    /**
     * Soll den letzten Index einer ArrayList zurückgeben und mehr Lesbarkeiet bieten als array.size() - 1
     * @param arrayList Ziel Array-List von beliebigem Typen
     * @return <code>Integer</code> Index
     * @param <T>
     */
    public static <T> Integer lastIndexOf(ArrayList<T> arrayList) {
        return arrayList.size() - 1;
    }

    /**
     * Sollte Duplicate Code-Fragment vermeiden und den Zahleninput validieren.
     * @param scanner Der Scanner mit dem der Input erfasst werden soll
     * @return <code>Number</code> Long, Integer oder, Double etc.
     */
    public static Number readAndValidateNumber(Scanner scanner) {
        boolean isFalse;
        Number i = 0;
        do {
            try{
                isFalse = false;
                i = scanner.nextInt();
                scanner.nextLine();
            }catch (InputMismatchException e){
                System.out.println("Keine gültige Eingabe");
                isFalse = true;
                scanner.nextLine();
            }
        } while (isFalse);

        return i;
    }
}

