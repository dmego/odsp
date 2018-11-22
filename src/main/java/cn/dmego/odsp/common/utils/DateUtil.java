package cn.dmego.odsp.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * class_name: DateUtil
 * package: cn.dmego.odsp.common.utils
 * describe: 日期工具类
 * creat_user: Dmego
 * creat_date: 2018/10/23
 * creat_time: 17:24
 **/
public class DateUtil {


    /**
     * 获取当前时间:String类型 (yyyy-MM-dd HH:mm:ss) 格式
     * @return 当前时间:String类型 (yyyy-MM-dd HH:mm:ss) 格式
     */
    public static String getCurrentDate(){
        return formateDate(new Date());
    }

    /**
     * 获取当前时间
     * @param formate 格式
     * @return 当前时间: String 类型  formate 格式
     */
    public static String getCurrentDate(String formate){
        return formatDate(new Date(),formate);
    }

    /**
     * 获取当前年份字符串
     * @return 当前年份
     */
    public static String getCurrentYear(){
        return formatDate(new Date(),"yyyy");
    }

    /**
     * 获取当前月份字符串
     * @return 当前月份
     */
    public static String getCurrentMonth(){
        return formatDate(new Date(),"MM");
    }

    /**
     * 获取当前天字符串
     * @return 当前天字符串
     */
    public static String getCurrentDay(){
        return formatDate(new Date(),"dd");
    }

    /**
     * 获取当前星期字符串
     * @return 当前星期几
     */
    public static String getCurrentWeek(){
        return formatDate(new Date(),"E");
    }

    /**
     * Date 转化为 String 类型日期
     * @param date 日期
     * @param formate 格式
     * @return String 类型日期
     */
    public static String formatDate(Date date,String formate){
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        return sdf.format(date);
    }

    /**
     * Date 转 String (yyyy-MM-dd HH:mm:ss) 格式
     * @param date 日期
     * @return  String类型 (yyyy-MM-dd HH:mm:ss) 格式
     */
    public static String formateDate(Date date){
        return formatDate(date,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * String 类型日期 转 Date 类型
     * @param date  String 类型日期
     * @param formate 格式
     * @return Date 类型日期
     */
    public static Date parseDate(String date,String formate){
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * String 类型日期 转 Date 类型 yyyy-MM-dd HH:mm:ss 格式
     * @param date  String 类型日期
     * @return Date 类型日期 yyyy-MM-dd HH:mm:ss 格式
     */
    public static Date parseDate(String date){
        return parseDate(date,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 比较时间大小
     * @param first 第一个 1
     * @param second 第二个 2
     * @return : 0 (1=2); -1 (1<2); 1 (1>2)
     */
    public static int compareToDate(String first, String second, String pattern){
        DateFormat df = new SimpleDateFormat(pattern);
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        try {
            cal1.setTime(df.parse(first));
            cal2.setTime(df.parse(second));
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("比较时间出错");
        }
        int result = cal1.compareTo(cal2);
        if(result > 0){return 1;}
        else if(result < 0) {return -1;}
        return 0;
    }

    /**
     * 比较时间大小
     * @param first 第一个 1
     * @param second 第二个 2
     * @return : 0 (1=2); -1 (1<2); 1 (1>2)
     */
    public static int compareToDate(Date first, Date second){
        int result = first.compareTo(second);
        if(result > 0){return 1;}
        else if(result < 0) {return -1;}
        return 0;
    }

    /**
     * 返回给定日期  给定天数后的日期
     * @param date 给定日期
     * @param day 给定天数
     * @return  返回给定日期  给定天数后的日期
     */
    public static Date getAppointDate(Date date,int day){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR,day);
        return cal.getTime();
    }

    /**
     * 获取两个日期之间的天数
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceTwoDate(Date before, Date after){
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        //afterTime - beforeTime 得到毫秒    秒     分   时    天
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 获取给定日期距现在的天数
     * @param date
     * @return
     */
    public static long getPastDays(Date date){
        long nowtime = new Date().getTime();
        long time = date.getTime();
        return (nowtime - time) / (1000 * 60 * 60 * 24);
    }

    /**
     * 获取给定日期距现在的小时数
     * @param date
     * @return
     */
    public static long getPastHours(Date date){
        long nowtime = new Date().getTime();
        long time = date.getTime();
        return (nowtime - time) / (1000 * 60 * 60);
    }

    /**
     * 获取给定日期距现在的分钟数
     * @param date
     * @return
     */
    public static long getPastMinutes(Date date){
        long nowtime = new Date().getTime();
        long time = date.getTime();
        return (nowtime - time) / (1000 * 60);
    }

    /**
     * 获取给定日期距现在的秒数
     * @param date
     * @return
     */
    public static long getPastSeconds(Date date){
        long nowtime = new Date().getTime();
        long time = date.getTime();
        return (nowtime - time) / 1000;
    }

    /**
     * 获取本周的第一天
     * @return
     */
    public static Date getFirstDayOfWeek(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 获取当月的第一天
     * @return
     */
    public static Date getFirstDayOfMonth(){
        Calendar cal = Calendar.getInstance();
        int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        return cal.getTime();
    }

    /**
     * 获取下月的第一天
     * @return
     */
    public static Date getFirstDayOfNextMonth(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,+1);
        int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        return cal.getTime();
    }

    /**
     * 根据生日获取年年龄(周岁)
     * @param birthday
     * @return
     */
    public static int getAgeByBirthDate(Date birthday){
        Calendar cal = Calendar.getInstance();
        //如果出生比当前日期还晚,说明填出生日期错误
        if(cal.before(birthday)){
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        //如果当前月份 小于 出生的月份,说明还没过生日,年龄减一岁
        if(monthNow <= monthBirth){
            //如果当前月份 等于出生的 月份
            if(monthNow == monthBirth){
                //当前的天 小于 出生的天, 说明还没过生日,年龄减一岁
                if(dayOfMonthNow < dayOfMonthBirth){
                    age--;
                }
            }else{
                age--;
            }
        }//除此之外,当前月份 大于出生月份,或当前天大于等于出生天都表名已经过完生日,年龄是年份之差
        return age;
    }


}
