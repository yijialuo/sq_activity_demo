package com.sq.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by yijialuo on 2018/4/13.
 */
//时间戳id
public class IdCreate {
    public static String id(){
        return UUID.randomUUID().toString();
    }
}
