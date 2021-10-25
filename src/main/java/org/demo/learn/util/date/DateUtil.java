package org.demo.learn.util.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * @author luwt
 * @date 2021/4/6.
 */
public class DateUtil {

    public static void getNextMonthDay(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate plusMonthDay = localDate.plusMonths(1);
        // 求出下个月的第一天
        System.out.println(plusMonthDay.with(TemporalAdjusters.firstDayOfMonth()));
        // 求出下个月的最后一天
        System.out.println(plusMonthDay.with(TemporalAdjusters.lastDayOfMonth()));
    }

    public static void main(String[] args) {
        getNextMonthDay("2021-02-28");
    }


}
