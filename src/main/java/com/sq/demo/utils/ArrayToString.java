package com.sq.demo.utils;

import java.util.List;

public class ArrayToString {
    public static String array(String[] params){
        if(params==null||params.length==0)
            return "";
        else {
            String res=params[0];
            for(int i=1;i<params.length;i++){
                res=res+","+params[i];
            }
            return res;
        }
    }

    public static String list(List<String> params){
        if(params==null||params.size()==0)
            return "";
        else {
            String res=params.get(0);
            for (int i=1;i<params.size();i++){
                res=res+","+params.get(i);
            }
            return res;
        }
    }
}
