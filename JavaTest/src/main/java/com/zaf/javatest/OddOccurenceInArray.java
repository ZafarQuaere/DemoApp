package com.zaf.javatest;

import java.util.Arrays;
import java.util.Scanner;

public class OddOccurenceInArray {
    public static void main(String [] arg){
        System.out.println("Enter no of elements you want in array: ");
        int n;
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        int array [] = new int[n];
        for (int i = 0; i < array.length; i++)
        {
            System.out.println("Please enter number");
            array[i] = scanner.nextInt();
        }

        System.out.println("arrays is :"+ Arrays.asList(array).toString());
    }
}
