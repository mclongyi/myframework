package com.odianyun.search.whale.data.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LyfDateUtilS {

    //===========================db date_format 格式化字段 start============================================
    public static final String DB_FORMAT_Ymd = "%Y-%m-%d";//年月日
    public static final String DB_FORMAT_Ym = "%Y-%m";//年月
    public static final String DB_FORMAT_Ymd_Hi_CN = "%Y年%m月%d日 %H:%i";//年月日

    //===========================db date_format 格式化字段 end============================================

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_PATTERN_POINT = "yyyy.MM.dd HH:mm:ss";
    public static final String PATTERN_ONE = "yyyy年MM月dd日 HH:mm:ss";
    public static final String PATTERN_yMd_Hm_CN = "yyyy年MM月dd日 HH:mm";
    public static final String PATTERN_MD = "MM月dd日";
    public static final String PATTERN_MD_D = "MM.dd";
    public static final String PATTERN_HOUR_MINUTE = "HH:mm";

    public static final String PATTERN_TWO = "yyyy年MM月dd日 HH时mm分";
    public static final String PATTERN_yMd_CN = "yyyy年MM月dd日";
    public static final String PATTERN_yMd_E_CN = "yyyy年MM月dd日 E";

    public static final String PATTERN_YMD = "yyyy-MM-dd";
    public static final String PATTERN_Y = "yyyy";
    public static final String PATTERN_YM = "yyyy-MM";

    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    public static Date addMilliseconds(Date date, int amount) {
        return add(date, 14, amount);
    }


    private static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance(Locale.CHINA);
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为pattern</p>
     *
     * @param millis  毫秒时间戳
     * @param pattern 时间格式
     * @return 时间字符串
     */
    public static String getMillis2String(long millis, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(millis));
        } catch (Exception e) {
            throw new RuntimeException("时间格式错误,格式为" + pattern);
        }
    }
    /**
     * 将日期转为时间字符串
     * <p>格式为pattern</p>
     *
     * @param date
     * @param pattern 时间格式
     * @return 时间字符串
     */
    public static String getDate2String(Date date, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).format(date);
        } catch (Exception e) {
            throw new RuntimeException("时间格式错误,格式为" + pattern);
        }
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为pattern</p>
     *
     * @param millis  毫秒时间戳
     * @return 时间字符串
     */
    public static String getMillis2StringByDefault(long millis) {
        try {
            return new SimpleDateFormat(DEFAULT_PATTERN, Locale.CHINA).format(new Date(millis));
        } catch (Exception e) {
            throw new RuntimeException("时间格式错误,格式为" + DEFAULT_PATTERN);
        }
    }


    /**
     * 将时间字符串转为时间戳
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 毫秒时间戳
     */
    public static long getString2Millis(String time, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).parse(time).getTime();
        } catch (ParseException e) {
            throw new RuntimeException("时间格式错误,格式为" + pattern);
        }
    }


    /**
     * 得到某一天 某个时刻的毫秒值
     *
     * @param millis
     * @param hms    "00:00:00" "23:59:59"
     * @return
     */
    public static long getOnedayZeroMillis(long millis, String hms) {
        String string = getMillis2String(millis, DEFAULT_PATTERN);
        StringBuilder buffer = new StringBuilder(string);
        String s = buffer.replace(string.lastIndexOf(" ") + 1, string.length(), hms).toString();
        return getString2Millis(s, DEFAULT_PATTERN);
    }

    /**
     * 得到某一天 某个时刻的毫秒值
     *
     * @param millis
     * @param hms    "00:00:00" "23:59:59"
     * @return
     */
    public static String getOnedayString(long millis, String hms) {
        String string = getMillis2String(millis, DEFAULT_PATTERN);
        StringBuffer buffer = new StringBuffer(string);
        return buffer.replace(string.lastIndexOf(" ") + 1, string.length(), hms).toString();
    }

    /**
     * 获取星期
     *
     * @param date Date类型时间
     * @return 星期
     */
    public static String getWeek(Date date) {
        return new SimpleDateFormat("EEEE", Locale.CHINA).format(date);
    }

    /**
     * 获取星期
     *
     * @param millis 毫秒时间戳
     * @return 星期
     */
    public static String getWeek(long millis) {
        return getWeek(new Date(millis));
    }


    public static String getWeek(int weekIndex) {
        String weekString;
        switch (weekIndex) {
            case 1:
                weekString = "星期一";
                break;
            case 2:
                weekString = "星期二";
                break;
            case 3:
                weekString = "星期三";
                break;
            case 4:
                weekString = "星期四";
                break;
            case 5:
                weekString = "星期五";
                break;
            case 6:
                weekString = "星期六";
                break;
            case 7:
                weekString = "星期天";
                break;
            default:
                throw new RuntimeException("日期错误");
        }
        return weekString;
    }

    //获取本月的第一天
    public static Calendar getFirstDayOfThisMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月第一天的时间戳
        return c;
    }

    //获取上月的第一天
    public static Calendar getFirstDayOfLastMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月第一天的时间戳
        return c;
    }

    //获取本月的最后一天
    public static long getLastDayOfThisMonth() {
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至0
        ca.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        ca.set(Calendar.MINUTE, 0);
        //将秒至0
        ca.set(Calendar.SECOND, 0);
        //将毫秒至0
        ca.set(Calendar.MILLISECOND, 0);
        // 获取本月最后一天的时间戳
        return ca.getTimeInMillis();
    }

    //由出生日期获得年龄
    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    // 获得本周一0点时间
    public static Calendar getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal;
    }


    /**
     * 获取本周的所有date,从本周一到本周日
     *
     * @return
     */
    public static List<Date> getWeekDateList() {
        List<Date> dates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();//今天
        //将当前时间调到星期一
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        for (int i = 0; i < 7; i++) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    /**
     * 获取本周的所有date,从本周一到本周日
     *
     * @return <'2017-05-16','2017-05-17'>
     */
    public static List<String> getWeekDateStringList() {
        List<String> dates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();//今天
        //将当前时间调到星期一
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }

        for (int i = 0; i < 7; i++) {
            String millis2String = getMillis2String(calendar.getTimeInMillis(), PATTERN_YMD);
            dates.add(millis2String);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }


    //注：下周一零点的时间 00:00:00
    public static Date getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        if (week == 1) {
            cal.add(Calendar.DAY_OF_MONTH, 1);

        } else {
            cal.add(Calendar.DAY_OF_MONTH, (7 - week) + 2);
        }
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return cal.getTime();
    }

    /* 增加上周一获取函数 */
    public static String lastWeekMondayStart() {
        Calendar cal = getTimesWeekmorning();
        cal.add(Calendar.DATE, -7);
        String date = new SimpleDateFormat(LyfDateUtilS.PATTERN_YMD, Locale.CHINA).format(cal.getTime());
        return date + " 00:00:00";
    }

    /**
     * 本周末
     *
     * @return
     */
    public static String nowWeekSundayEnd() {
        Calendar cal = getTimesWeekmorning();
        cal.add(Calendar.DATE, 6);
        String date = new SimpleDateFormat(LyfDateUtilS.PATTERN_YMD, Locale.CHINA).format(cal.getTime());
        return date + " 23:59:59";
    }

    /* 获取上周日获取函数 */
    public static String lastWeekSundayEnd() {
        Calendar cal = getTimesWeekmorning();
        cal.add(Calendar.DATE, -1);
        String date = new SimpleDateFormat(LyfDateUtilS.PATTERN_YMD, Locale.CHINA).format(cal.getTime());
        return date + " 23:59:59";
    }

    /**
     * 本月第一天
     *
     * @return
     */
    public static String nowMonthFirstDate() {
        Calendar cal = getFirstDayOfThisMonth();
        String date = new SimpleDateFormat(LyfDateUtilS.PATTERN_YMD, Locale.CHINA).format(cal.getTime());
        return date + " 00:00:00";
    }

    /**
     * 上月第一天
     *
     * @return
     */
    public static String lastMonthFirstDate() {
        Calendar cal = getFirstDayOfLastMonth();
        String date = new SimpleDateFormat(LyfDateUtilS.PATTERN_YMD, Locale.CHINA).format(cal.getTime());
        return date + " 00:00:00";
    }

    /**
     * 本月最后一天
     *
     * @return
     */
    public static String nowMonthLastDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String date = new SimpleDateFormat(LyfDateUtilS.PATTERN_YMD, Locale.CHINA).format(cal.getTime());
        return date + " 23:59:59";
    }


    /**
     * 上月最后一天
     *
     * @return
     */
    public static String lastMonthLastDate() {
        Calendar cal = getFirstDayOfLastMonth();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String date = new SimpleDateFormat(LyfDateUtilS.PATTERN_YMD, Locale.CHINA).format(cal.getTime());
        return date + " 23:59:59";
    }

    /**
     * 校验日期是否在某个区间内
     * @param startTime 开始时间 2018-06-08 14:00:00
     * @param endTime   结束时间 2018-06-08 14:00:00
     * @return true 现在在闭区间内
     */
    public static boolean checkNowIsInTime(String startTime, String endTime) throws Exception{
        try {
            long startTimeMillis = getString2Millis(startTime, DEFAULT_PATTERN);
            long endTimeMillis = getString2Millis(endTime, DEFAULT_PATTERN);
            long now = System.currentTimeMillis();
            if (now>=startTimeMillis && now<=endTimeMillis){
                return true;
            }
        }catch (Exception e){
            throw new Exception("日期计算异常");
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        boolean nowIsInTime = checkNowIsInTime("2018-10-15 00:00:00", "2018-10-15 02:10:00");
        System.out.println(nowIsInTime);


        String today = LyfDateUtilS.getMillis2String(System.currentTimeMillis(), LyfDateUtilS.PATTERN_YMD);
        if (LyfDateUtilS.checkNowIsInTime(today+" 00:00:00",today+" 02:10:00")){
            System.out.println("-----");
        }
    }
}


