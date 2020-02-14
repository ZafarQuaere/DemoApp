package com.zaf.javatest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jdk.nashorn.api.scripting.JSObject;

public class MyClass {

//    Be between 8 and 40 characters long
//    Contain at least one digit.
//    Contain at least one lower case character.
//    Contain at least one upper case character.
//    Contain at least on special character from [ @ # $ % ! . ].
    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
    private static Pattern pattern;
    private Matcher matcher;

    public static void main(String arg[]){
        MyClass test = new MyClass();
        //test.subString();
        //test.replace();
        applyLogicForCoordinates();
        pattern = Pattern.compile(PASSWORD_PATTERN);
        boolean valid = test.validatePassword("112233");
        System.out.println("password valid : "+valid);
        boolean valid1 = test.validatePassword("abcdef");
        System.out.println("password valid : "+valid1);
        boolean valid2 = test.validatePassword("Abcdef");
        System.out.println("password valid : "+valid2);
        boolean valid3 = test.validatePassword("Zafar@123");
        System.out.println("password valid : "+valid3);
    }


    public  boolean validatePassword(final String password) {

        return  pattern.matches(password,PASSWORD_PATTERN);

    }

    private static void applyLogicForCoordinates() {
        int selector = 622;
        int x2 = 664;
        int x3 = 816;
        int x4 = 480;
        if ((x3- selector )< 0 || (x3 - selector )> 70){
            System.out.println("This is not the selected item >>>>> ");

        }else{
            System.out.println("This is the selected item  ");
        }
    }

    private void subString() {
        String bounds = "[0,1632][1440,1828]";
        String bound2 = "[622,1479][818,1675]";
        //String note10 = "1080x2280"
        String halfwidth = "540";

        String y2 = bounds.substring(bounds.length()-5,bounds.length()-1);
        String[] split = bound2.split(",");
        for (int i = 0; i < split.length; i++) {
            System.out.println("String at "+split[0].replace("[",""));
        }
       // System.out.println(y2);
    }

    private void replace(){
        String value = "2 OPTIONS";
        String no = value.toLowerCase().replace("options","");
        System.out.println("Value : "+value+" no:"+no.trim());
       // FileUtils.loadJSONFromAsset(getClass(),"mdtl");
    }


}
