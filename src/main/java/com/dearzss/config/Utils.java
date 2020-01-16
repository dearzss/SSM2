package com.dearzss.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class Utils {
    private static String[] parsePatterns = {"yyyy-MM-dd","yyyy年MM月dd日",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyyMMdd"};

    public static boolean isValidDate(String str) {
        boolean convertSuccess=true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
               format.setLenient(false);
               format.parse(str);
            } catch (Exception e) {
               // e.printStackTrace();
                convertSuccess=false;
            }
        return convertSuccess;
    }

    public static boolean isNumeric(String str){
        try {
            Pattern pattern = Pattern.compile("[0-9]*");
            if(str.indexOf(".")>0){//判断是否有小数点
                if(str.indexOf(".")==str.lastIndexOf(".") && str.split("\\.").length==2){ //判断是否只有一个小数点
                    return pattern.matcher(str.replace(".","")).matches();
                }else {
                    return false;
                }
            }else {
                /*return pattern.matcher(str).matches();*/
                return false;
            }
        }catch (Exception e) {
            return false;
        }
    }
}
