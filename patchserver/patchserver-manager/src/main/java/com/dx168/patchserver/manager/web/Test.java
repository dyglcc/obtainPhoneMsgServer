package com.dx168.patchserver.manager.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {


    public static void main(String[] args){

        String str = "星巴克】验证码：112111，15分钟内输入有效，立即登录";
        Pattern pattern = Pattern.compile("\\b\\d{4,8}\\b");
        Matcher matcher = pattern.matcher(str);

        if(matcher.find()){
            String foundOne = matcher.group(0);
            System.out.println(foundOne);
        }

//        if(found){
//            String content = matcher.group(0);
//            System.out.println("found" + found +"aa " + content);
//        }else{
//            System.out.println("empty");
//        }

    }
}
