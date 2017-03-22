package com.industries.wow.employees.utils;

import java.util.Calendar;
import java.util.Date;

public class DateParser {

    public static String parseDate(String dateString){
        String year = "";
        String month = "";
        String day = "";
        // TODO: 22.03.2017 iumplement multiple separators
        if(dateString.contains("-")){
            String[] splitted = dateString.split("-");
            if(splitted.length == 3){
                if(splitted[0].length() == 4){
                    year = splitted[0];
                    day = splitted[2];
                } else {
                    year = splitted[2];
                    day = splitted[0];
                }
                month = splitted[1];
            }
        }

        return day + "." + month + "." + year;
    }

    public static String getAge(Date date){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        int last = age % 10;
        String endings = " лет";
        if(last == 1){
            endings = " год";
        } else if(last > 1 && last < 5){
            endings = " года";
        }

        return age +  endings;
    }
}
