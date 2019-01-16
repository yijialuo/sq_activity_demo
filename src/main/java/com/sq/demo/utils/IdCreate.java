package com.sq.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yijialuo on 2018/4/13.
 */
//时间戳id
public class IdCreate {
    public static String id(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSS");
        return sdf.format(date);
    }
}
