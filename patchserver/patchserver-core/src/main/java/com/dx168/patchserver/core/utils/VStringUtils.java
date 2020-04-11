package com.dx168.patchserver.core.utils;

public class VStringUtils {

    public static boolean isEmpty(String text){
        if(text == null || text.trim().equals("")){
            return true;
        }
        return false;
    }
}
