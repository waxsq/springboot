package com.qingge.springboot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: waxsq
 * @date:
 */
public class Utils {

    public static  String getDateTime(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
