package com.zaf.javatest;

public class MyClass {
    public static void main(String arg[]){
        MyClass test = new MyClass();
        test.subString();
        //test.replace();
    }

    private void subString() {
        String bounds = "[0,1632][1440,1828]";
        String bound2 = "[622,1479][818,1675]";
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

    }
}
