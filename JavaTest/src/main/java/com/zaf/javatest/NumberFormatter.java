package com.zaf.javatest;

public class NumberFormatter {

    public static void main(String arg[]) {
       /* String myNo = "(+91) 7834908329";
        System.out.println("Before my no : "+myNo+" After format my no: "+formatMyNo(myNo));*/

        String name = "98765 +";
        String[] s = name.split(" ");
        System.out.println("Name: " + s[0]);
    }

    private static String formatMyNo(String myNo) {

       // return myNo.replace("[^0-9]".toRegex(),"")
        return myNo.replace("[^0-9]","");
    }
}
