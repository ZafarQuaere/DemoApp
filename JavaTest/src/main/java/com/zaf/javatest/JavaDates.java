package com.zaf.javatest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;

public class JavaDates {

    public static void main(String arg[]){
       // currentDate();
        differenceInDate();
    }

    private static void differenceInDate() {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        String loginDate = "31 01 2020";
        //String currentDate = "02 02 2014";
        Date date = new Date();
        String currentDate = myFormat.format(date);
        System.out.println("Current date : "+currentDate+"  loginDate: "+loginDate);
        try {
            Date pLoginDate = myFormat.parse(loginDate);
            long difference = date.getTime()- pLoginDate.getTime();
            float daysBetween = (difference / (1000*60*60*24));
            int days = (int) daysBetween;
            System.out.println("Difference in days : "+days);
        } catch (ParseException e) {
            e.printStackTrace();
        }


       /* try {
            Date dateBefore = myFormat.parse(loginDate);
            Date dateAfter = myFormat.parse(currentDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            float daysBetween = (difference / (1000*60*60*24));
            *//* You can also convert the milliseconds to days using this method
             * float daysBetween =
             *         TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)
             *//*
            System.out.println("Number of Days between dates: "+daysBetween);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private static void currentDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("current dateTime : "+currentTime);

        LocalDate date1 = currentTime.toLocalDate();
        System.out.println("date1: " + date1);
        Month month = currentTime.getMonth();
        int day = currentTime.getDayOfMonth();
        int seconds = currentTime.getSecond();

        System.out.println("Month:" + month +"  day:" + day +"  seconds:" + seconds);

    }
}
