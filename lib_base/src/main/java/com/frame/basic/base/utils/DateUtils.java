package com.frame.basic.base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    /**
     * The tag.
     */
    private static String TAG = "AbDateUtil";

    /**
     * 时间日期格式化到年月日时分秒.
     */
    public static String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间日期格式化到年月日时分秒.
     */
    public static String dateFormatToYMDHMS = "yyyyMMddHHmmss";

    /**
     * 时间日期格式化到年月日.
     */
    public static String dateFormatYMD = "yyyy-MM-dd";
    /**
     * 时间日期格式化到年月日.
     */
    public static String dateFormatToYMD = "yyyyMMdd";
    /**
     * 时间日期格式化到年月日.
     */
    public static String dateFormatToYMD2 = "yyyy年MM月dd日";
    public static String dateFormatToYMD3 = "yyyy-MM-dd";

    /**
     * 时间日期格式化到年月.
     */
    public static String dateFormatYM = "yyyy-MM";
    /**
     * 时间日期格式化到年月.
     */
    public static String dateFormatY_M = "yyyy.MM";

    /**
     * 时间日期格式化到年月日时分.
     */
    public static String dateFormatYMDHM = "yyyy-MM-dd HH:mm";
    /**
     * 时间日期格式化到年月日时分.
     */
    public static String dateFormatToYMDHM = "yyyy年MM月dd日 HH:mm";
    /**
     * 时间日期格式化到年月日时分秒.
     */
    public static String dateFormatToCNYMDHMS = "yyyy年MM月dd日 HH:mm:ss";
    /**
     * 时间日期格式化到年月日时分.
     */
    public static String dateFormatY_M_DHM = "yyyy.MM.dd HH:mm";
    /**
     * 时间日期格式化到年月日时分秒.
     */
    public static String dateFormatY_M_DHMS = "yyyy.MM.dd HH:mm:ss";
    /**
     * 时间日期格式化到年月日.
     */
    public static String dateFormatY_M_D = "yyyy.MM.dd";
    /**
     * 时间日期格式化到月日.
     */
    public static String dateFormatMD = "MM/dd";
    /**
     * 时间日期格式化到月日.
     */
    public static String dateFormatMDtext = "MM月dd日";
    /**
     * 时间日期格式化到月日.
     */
    public static String dateFormatMDs = "MM-dd";
    /**
     * 时间日期格式化到月日时分.
     */
    public static String dateFormatMDHM = "MM-dd HH:mm";
    /**
     * 时分秒.
     */
    public static String dateFormatHMS = "HH:mm:ss";

    /**
     * 时分.
     */
    public static String dateFormatHM = "HH:mm";
    /**
     * 时
     */
    public static String dateFormatH = "HH";

    /**
     * 年.
     */
    public static String dateFormatY = "yyyy";
    /**
     * 年.
     */
    public static String dateFormatYs = "yyyy年";

    /**
     * 月.
     */
    public static String dateFormatM = "MM";

    /**
     * 日.
     */
    public static String dateFormatD = "dd";

    /**
     * 描述：String类型的日期时间转化为Date类型.
     *
     * @param strDate String形式的日期时间
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return Date Date类型日期时间
     */
    public static Date getDateByFormat(String strDate, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = mSimpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 描述：获取偏移之后的Date.
     *
     * @param date          日期时间
     * @param calendarField Calendar属性，对应offset的值，
     *                      如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return Date 偏移之后的日期时间
     */
    public Date getDateByOffset(Date date, int calendarField, int offset) {
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(date);
            c.add(calendarField, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 描述：获取指定日期时间的字符串(可偏移).
     *
     * @param strDate       String形式的日期时间
     * @param format        格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值，
     *                      如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String getStringByOffset(String strDate, String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(mSimpleDateFormat.parse(strDate));
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * 描述：Date类型转化为String类型(可偏移).
     *
     * @param date          the date
     * @param format        the format
     * @param calendarField the calendar field
     * @param offset        the offset
     * @return String String类型日期时间
     */
    public static String getStringByOffset(Date date, String format, int calendarField, int offset) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(date);
            c.add(calendarField, offset);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * 描述：Date类型转化为String类型.
     *
     * @param date   the date
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * 描述：String类型转化为特定String类型日期时间类型
     *
     * @param strDate
     * @param format  the format
     * @return String String类型日期时间
     */
    public static String getStringByFormats(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatToYMD);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * 描述：String类型转化为特定String类型日期时间类型
     *
     * @param strDate
     * @param format  the format
     * @return String String类型日期时间
     */
    public static String getStringByFormatYMD(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMD);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format  输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    public static String getStringByFormat(String strDate, String format) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDHMS);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * 描述：获取milliseconds表示的日期时间的字符串.
     *
     * @param milliseconds the milliseconds
     * @param format       格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 日期时间字符串
     */
    public static String getStringByFormat(long milliseconds, String format) {
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return milliseconds == 0 ? "" : thisDateTime;
    }

    /**
     * 格式化时间
     * @param milliseconds
     * @param format
     * @return
     */
    public static String formatTime(long milliseconds, String format) {
        long newMillSeconds = milliseconds - 28800000;
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(newMillSeconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return milliseconds == 0 ? "" : thisDateTime;
    }

    public static String getFormatTime(long diff) {
        if (diff <= 0) {
            return "";
        }
        
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // long ns = 1000;
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        return ((day * 24) + hour) + "小时" + min + "分" + sec + "秒";
    }

    /**
     * 描述：获取表示当前日期时间的字符串.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    public static String getCurrentDate(String format) {
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;

    }

    /**
     * 描述：获取表示当前日期时间的字符串(可偏移).
     *
     * @param format        格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值，
     *                      如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String getFormatCurrentDateByOffset(String format, int calendarField, int offset) {
        String mDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;

    }
    /**
     * 描述：获取表示当前日期时间的字符串(可偏移).
     *
     * @param calendarField Calendar属性，对应offset的值，
     *                      如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset        偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static long getCurrentDateByOffset(int calendarField, int offset) {
        try {
            Calendar c = new GregorianCalendar();
            c.add(calendarField, offset);
            return c.getTime().getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();

    }

    /**
     * 描述：计算两个日期所差的天数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的天数
     */
    public static int getOffectDay(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        // 先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }

    /**
     * 描述：计算两个日期所差的小时数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    public static int getOffectHour(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = getOffectDay(date1, date2);
        h = h1 - h2 + day * 24;
        return h;
    }

    /**
     * 描述：计算两个日期所差的分钟数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    public static int getOffectMinutes(long date1, long date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = getOffectHour(date1, date2);
        int m = 0;
        m = m1 - m2 + h * 60;
        return m;
    }

    /**
     * 是否同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(long date1, long date2) {
        String preDay = getStringByFormat(date1, DateUtils.dateFormatYMD);
        String nextDay = getStringByFormat(date2, DateUtils.dateFormatYMD);
        return preDay.equals(nextDay);
    }

    /**
     * 是否同一月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMonth(long date1, long date2) {
        String preMonth = getStringByFormat(date1, DateUtils.dateFormatYM);
        String nextMonth = getStringByFormat(date2, DateUtils.dateFormatYM);
        return preMonth.equals(nextMonth);
    }

    /**
     * 是否同一年
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameYear(long date1, long date2) {
        String preYear = getStringByFormat(date1, DateUtils.dateFormatY);
        String nextYear = getStringByFormat(date2, DateUtils.dateFormatY);
        return preYear.equals(nextYear);
    }

    /**
     * 描述：获取本周一.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getFirstDayOfWeek(String format) {
        return getDayOfWeek(format, Calendar.MONDAY);
    }

    /**
     * 描述：获取本周日.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getLastDayOfWeek(String format) {
        return getDayOfWeek(format, Calendar.SUNDAY);
    }

    /**
     * 描述：获取本周的某一天.
     *
     * @param format        the format
     * @param calendarField the calendar field
     * @return String String类型日期时间
     */
    private static String getDayOfWeek(String format, int calendarField) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            int week = c.get(Calendar.DAY_OF_WEEK);
            if (week == calendarField) {
                strDate = mSimpleDateFormat.format(c.getTime());
            } else {
                int offectDay = calendarField - week;
                if (calendarField == Calendar.SUNDAY) {
                    offectDay = 7 - Math.abs(offectDay);
                }
                c.add(Calendar.DATE, offectDay);
                strDate = mSimpleDateFormat.format(c.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * 描述：获取本月第一天.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getFirstDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            // 当前月的第一天
            c.set(GregorianCalendar.DAY_OF_MONTH, 1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;

    }

    /**
     * 描述：获取本月最后一天.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getLastDayOfMonth(String format) {
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            // 当前月的最后一天
            c.set(Calendar.DATE, 1);
            c.roll(Calendar.DATE, -1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * 描述：获取表示当前日期的0点时间毫秒数.
     *
     * @return the first time of day
     */
    public static long getFirstTimeOfDay() {
        Date date = null;
        try {
            String currentDate = getCurrentDate(dateFormatYMD);
            date = getDateByFormat(currentDate + " 00:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 描述：获取表示当前日期24点时间毫秒数.
     *
     * @return the last time of day
     */
    public static long getLastTimeOfDay() {
        Date date = null;
        try {
            String currentDate = getCurrentDate(dateFormatYMD);
            date = getDateByFormat(currentDate + " 23:59:59", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 描述：获取表示所传日期8点时间毫秒数.
     *
     * @return the last time of day
     */
    public static long getEightOfTime(long time) {
        Date date = null;
        try {
            String currentDate = getStringByFormat(time, dateFormatYMD);
            date = getDateByFormat(currentDate + " 08:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 描述：获取表示所传日期0点时间毫秒数.
     *
     * @return the last time of day
     */
    public static long getStartOfTime(long time) {
        Date date = null;
        try {
            String currentDate = getStringByFormat(time, dateFormatYMD);
            date = getDateByFormat(currentDate + " 00:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 描述：获取表示所传日期12点时间毫秒数.
     *
     * @return the last time of day
     */
    public static long getHalfOfTime(long time) {
        Date date = null;
        try {
            String currentDate = getStringByFormat(time, dateFormatYMD);
            date = getDateByFormat(currentDate + " 12:00:00", dateFormatYMDHMS);
            return date.getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 描述：获取表示所传日期月份的第一天.
     *
     * @return the last time of day
     */
    public static long getStartMonthOfTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        return calendar1.getTimeInMillis();
    }

    /**
     * 描述：判断是否是闰年()
     * <p>
     * (year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
     *
     * @param year 年代（如2012）
     * @return boolean 是否为闰年
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 400 != 0) || year % 400 == 0;
    }

    /**
     * 描述：根据时间返回格式化后的时间的描述. 小于1小时显示多少分钟前 大于1小时显示今天＋实际日期，大于今天全部显示实际时间
     *
     * @param strDate   the str date
     * @param outFormat the out format
     * @return the string
     */
    public static String formatDateStr2Desc(String strDate, String outFormat) {

        DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c2.setTime(df.parse(strDate));
            c1.setTime(new Date());
            int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
            if (d == 0) {
                int h = getOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
                if (h > 0) {
                    return "今天" + getStringByFormat(strDate, dateFormatHM);
                    // return h + "小时前";
                } else if (h < 0) {
                    // return Math.abs(h) + "小时后";
                } else if (h == 0) {
                    int m = getOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
                    if (m > 0) {
                        return m + "分钟前";
                    } else if (m < 0) {
                        // return Math.abs(m) + "分钟后";
                    } else {
                        return "刚刚";
                    }
                }

            } else if (d > 0) {
                if (d == 1) {
                    // return "昨天"+getStringByFormat(strDate,outFormat);
                } else if (d == 2) {
                    // return "前天"+getStringByFormat(strDate,outFormat);
                }
            } else if (d < 0) {
                if (d == -1) {
                    // return "明天"+getStringByFormat(strDate,outFormat);
                } else if (d == -2) {
                    // return "后天"+getStringByFormat(strDate,outFormat);
                } else {
                    // return Math.abs(d) +
                    // "天后"+getStringByFormat(strDate,outFormat);
                }
            }

            String out = getStringByFormat(strDate, outFormat);
            if (!"".equals(out)) {
                return out;
            }
        } catch (Exception e) {
        }

        return strDate;
    }

    /**
     * 取指定日期为星期几.
     *
     * @param strDate  指定日期
     * @param inFormat 指定日期格式
     * @return String 星期几
     */
    public static String getWeekNumber(String strDate, String inFormat) {
        String week = "星期日";
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(inFormat);
        try {
            calendar.setTime(df.parse(strDate));
        } catch (Exception e) {
            return "错误";
        }
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (intTemp) {
            case 0:
                week = "星期日";
                break;
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
        }
        return week;
    }

    /**
     * 取指定日期为星期几
     *
     * @param date
     * @return
     */
    public static String getWeekNumber(Date date) {
        String week = "星期日";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (intTemp) {
            case 0:
                week = "星期日";
                break;
            case 1:
                week = "星期一";
                break;
            case 2:
                week = "星期二";
                break;
            case 3:
                week = "星期三";
                break;
            case 4:
                week = "星期四";
                break;
            case 5:
                week = "星期五";
                break;
            case 6:
                week = "星期六";
                break;
        }
        return week;
    }

    /**
     * 判断两个时间相差多久
     *
     * @param date1 需要比较的时间 不能为空(null),需要正确的日期格式
     * @param date2 被比较的时间  为空(null)则为当前时间
     * @param stype 返回值类型   0为多少天，1为多少个月，2为多少年
     * @return
     */
    public static int compareDate(String date1, String date2, int stype) {
        int n = 0;

        String[] u = {"天", "月", "年"};
        String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";

        date2 = date2 == null ? getStringByFormat(System.currentTimeMillis(), formatStyle) : date2;

        DateFormat df = new SimpleDateFormat(formatStyle);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(date1));
            c2.setTime(df.parse(date2));
        } catch (Exception e3) {
            System.out.println("wrong occured");
        }
        //List list = new ArrayList();
        while (!c1.after(c2)) {                     // 循环对比，直到相等，n 就是所要的结果
            //list.add(df.format(c1.getTime()));    // 这里可以把间隔的日期存到数组中 打印出来
            n++;
            if (stype == 1) {
                c1.add(Calendar.MONTH, 1);          // 比较月份，月份+1
            } else {
                c1.add(Calendar.DATE, 1);           // 比较天数，日期+1
            }
        }

        n = n - 1;

        if (stype == 2) {
            n = n / 365;
        }

        return n;
    }

    /**
     * 得到当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        return simple.format(date);

    }

    /**
     * 得到当前年
     *
     * @return
     */
    public static String getCurrentYear() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy");
        return simple.format(date);

    }

    /**
     * 吧时间转化为上午XX:XX
     *
     * @param timestep
     * @return
     */
    public static String getTimeToUpDwon(long timestep) {
        String thisDateTime = null;
        int hour;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatHM);
            thisDateTime = mSimpleDateFormat.format(timestep);
            SimpleDateFormat mhourFormat = new SimpleDateFormat(dateFormatH);
            hour = Integer.valueOf(mhourFormat.format(timestep));
            if (hour >= 0 && hour <= 12) {
                return "上午" + thisDateTime;
            } else {
                return "下午" + thisDateTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    /**
     * 获取date1对应的号数在date2所在月份的时间
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Date getNextSameDay(Date date1, Date date2) {
        int dateNum = Integer.parseInt(getStringByFormat(date1, dateFormatD));
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        aCalendar.setTime(date2);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        if (dateNum <= day) {
            aCalendar.set(aCalendar.get(Calendar.YEAR), aCalendar.get(Calendar.MONTH), dateNum);
        } else {
            aCalendar.set(aCalendar.get(Calendar.YEAR), aCalendar.get(Calendar.MONTH), day);
        }
        return aCalendar.getTime();
    }

    /**
     * 获取时间到小时数
     *
     * @param date
     * @return
     */
    public static Date formTimeToHour(Date date) {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        aCalendar.setTime(date);
        aCalendar.set(aCalendar.get(Calendar.YEAR), aCalendar.get(Calendar.MONTH), aCalendar.get(Calendar.DAY_OF_MONTH), aCalendar.get(Calendar.HOUR_OF_DAY), 0, 0);
        long time = aCalendar.getTimeInMillis();
        Date date1 = new Date(time - time % 1000);
        return date1;
    }

    /**
     * 获取时间长度，小于一天表示X小时，大于一天表示X天X小时;
     *
     * @param time（表示毫秒）
     * @return
     */
    public static String getTimeDayLong(long time) {
        if (time < 24 * 60 * 60 * 1000) {
            return String.valueOf((int) time / 3600000) + "小时";
        } else {
            int day = (int) (time / (24 * 60 * 60 * 1000));
            long de = time - 24 * 60 * 60 * 1000 * day;
            return String.valueOf(day) + "天" + String.valueOf((int) de / 3600000) + "小时";
        }
    }

    /**
     * 获取时间长度，小于一天表示X小时，大于一天表示X天X小时,大于一年表示X年X天X小时;
     *
     * @param time（表示毫秒）
     * @return
     */
    public static String getTimeYearLong(long time) {
        long hourTime = 60 * 60 * 1000;
        long dayTime = 24 * hourTime;
        long yearTime = 365 * dayTime;
        if (time < dayTime) {
            return String.valueOf((int) (time / hourTime)) + "小时";
        } else if (time >= dayTime && time < yearTime) {
            int day = (int) (time / dayTime);
            long de = time - day * dayTime;
            return String.valueOf(day) + "天" + String.valueOf((int) (de / hourTime)) + "小时";
        } else {
            int year = (int) (time / yearTime);
            int day = (int) ((time - year * yearTime) / dayTime);
            long de = time - day * dayTime - year * yearTime;
            return String.valueOf(year) + "年" + String.valueOf(day) + "天" + String.valueOf((int) (de / hourTime)) + "小时";
        }
    }

    /**
     * 将long时间转换成yyyy-mm-dd
     *
     * @param currentTime
     * @return
     * @throws ParseException
     */
    public static String longToString(long currentTime) throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = new SimpleDateFormat(dateFormatToYMD3).format(dateOld); // 把date类型的时间转换为string
        return sDateTime;
    }

    /**
     * 将毫秒时间格式化为00:00:00格式
     *
     * @param currentTime
     * @return
     */
    public static String longToHMS(long currentTime) {
        int hh = (int) (currentTime / (1000 * 60 * 60));
        int mm = (int) ((currentTime - hh * 1000 * 60 * 60) / (1000 * 60));
        int ss = (int) ((currentTime - hh * 1000 * 60 * 60 - mm * 1000 * 60) / 1000);
        StringBuffer sb = new StringBuffer();
        if (hh >= 10) {
            sb.append(hh + ":");
        } else {
            sb.append("0" + hh + ":");
        }

        if (mm >= 10) {
            sb.append(mm + ":");
        } else {
            sb.append("0" + mm + ":");
        }

        if (ss >= 10) {
            sb.append(ss + "");
        } else {
            sb.append("0" + ss);
        }

        return sb.toString();
    }

    /**
     * 获取当前时间偏移23:59:59的时间
     *
     * @param time
     * @return
     */
    public static long getDayLastMinits(long time) {
        long afterTime = time + (24 * 60 * 60 * 1000 - 1000);
        return afterTime;
    }

    /**
     * 获取对应月份的最后一天最后后时间
     *
     * @param time
     * @return
     */
    public static Date getMonthLastDay(long time) {
        int hour = 8;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DATE, 1); //把日期设置为当月第一天
        calendar.roll(Calendar.DATE, -1); //日期回滚一天，也就是最后一天
        Date lastDayOfMonth = calendar.getTime();
        if (calendar.get(Calendar.HOUR_OF_DAY) == hour) {
            calendar.add(Calendar.HOUR_OF_DAY, 15);
            lastDayOfMonth = calendar.getTime();
        }
        return lastDayOfMonth;
    }

    /**
     * 获取倒计时时间
     * 毫秒转HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String getCutdownTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(time);
    }

    /**
     * 获取倒计时时间
     * 毫秒转mm:ss:ms
     *
     * @param remainTime
     * @return
     */
    public static String getCutdownTime2(long remainTime) {
        long minutes = (int) (remainTime / (1000 * 60));
        long mills = (int) ((remainTime - minutes * 1000 * 60) / 1000);
        long millisecond = (int) ((remainTime - minutes * 1000 * 60 - mills * 1000) /10);
        String minutesStr = minutes < 10 ? "0" + minutes : minutes + "";
        String millsStr = mills < 10 ? "0" + mills : mills + "";
        String millisecondStr = millisecond < 10 ? "0" + millisecond : millisecond + "";
        return minutesStr + ":" + millsStr + ":" + millisecondStr;
    }

    /**
     * @param datdString Thu May 18 2017 00:00:00 GMT+0800 (中国标准时间)
     * @return 年月日;
     */
    public static Date parseGMTTime(String datdString) {
        DateFormat gmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        try {
            long lMofifyTime = gmt.parse(datdString).getTime();
            return new Date(lMofifyTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }


    /**
     * 取得当月天数
     * */
    public static int getCurrentMonthLastDay()
    {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
    /**
     * 取得某月天数
     * */
    public static int getMonthDayNum(long time)
    {
        Calendar a = Calendar.getInstance();
        a.setTimeInMillis(time);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
