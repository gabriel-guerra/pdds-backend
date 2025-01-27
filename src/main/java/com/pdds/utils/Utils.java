package com.pdds.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static Date stringToDate(String string){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.parse(string);
        }catch (Exception e){
            System.out.println("Error to convert " + string + "to Date java.");
        }

        return null;
    }


}
