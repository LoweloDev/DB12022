package org.db1.shared;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Helpers {


    public static Integer inputToIndex(int input) {
        return input - 1;
    }

    public static <T> T lastItem(T[] array) {
        return array[array.length - 1];
    }

    public static <T> T lastItem(ArrayList<T> arrayList) {
        return arrayList.get(arrayList.size() - 1);
    }

    public static <T> Integer lastIndexOf(T[] array) {
        return array.length - 1;
    }

    public static <T> Integer lastIndexOf(ArrayList<T> array) {
        return array.size() - 1;
    }

    public static Number readAndValidateNumber(Scanner sc) {
        boolean isFalse;
        Number i = 0;
        do {
            try{
                isFalse = false;
                i = sc.nextInt();
                sc.nextLine();
            }catch (InputMismatchException e){
                System.out.println("Keine gültige Eingabe");
                isFalse = true;
                sc.nextLine();
            }
        } while (isFalse);

        return i;
    }
}

