package com.hcherndon.dj.framework;


import com.hcherndon.dj.DoubleJumper;

/**
 * Created with IntelliJ IDEA.
 * User: HcHerndon
 * Date: 7/7/13
 * Time: 1:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class Validate {
    public static boolean checkNumberOfArgs(String[] args, int num){
        if(args.length > num)
            return true;
        else
            return false;
    }

    public static boolean isNumberFormat(String in){
        try{
            Double.parseDouble(in);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void checkNullDJP(String s){
        if(DoubleJumper.getInstance().getDJP(s) == null)
            DoubleJumper.getInstance().addDJP(s);
    }
}
