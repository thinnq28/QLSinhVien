package com.example.quanlysinhvien.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {
    private final static String pattern = "dd/MM/yyyy";

    public static Date toDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.parse(str);
    }

    public static String toString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
