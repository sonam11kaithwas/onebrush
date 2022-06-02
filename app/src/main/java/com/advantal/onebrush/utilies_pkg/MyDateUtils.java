package com.advantal.onebrush.utilies_pkg;

/**
 * Created by Sonam on 06-05-2022 15:07.
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyDateUtils {

    public static String convertDateToString(Date date) {
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String strDate = dateFormat.format(date);
            return strDate;
        } else {
            return "";
        }

    }

    public static String longToDate(Long date, String formate) {
        if (date != null) {
            DateFormat simple = new SimpleDateFormat(formate);
            Date result = new Date(date);
            String strDate = simple.format(date);
            return strDate;
        } else {
            return "";
        }

    }

    public static String convertDateToStringSlash(Date date) {
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String strDate = dateFormat.format(date);
            return strDate;
        } else {
            return "";
        }

    }

    public static String convertDateToStringYYYYMMDD(Date date) {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = formatter.format(new Date());
            return strDate;
        } else {
            return "";
        }

    }

    public static String convertDateToStringWithTime(Date date) {
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
            String strDate = dateFormat.format(date);
            return strDate;
        } else {
            return "";
        }

    }

    public static String convertDateToStringWithTimes(Date date) {
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            String strDate = dateFormat.format(date);
            return strDate;
        } else {
            return "";
        }

    }


    public static String convertDateToStringExpiry(Integer duration) {
        if (duration != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy hh:mm:ss");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, duration - 1);
            String newDate = sdf.format(c.getTime());
            return newDate;
        } else {
            return "";
        }

    }

    public static String convertTimeStampToDate(long timeStamp) {
        if (timeStamp != 0L) {
            Date currentDate = new Date(timeStamp);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            String newDate = df.format(currentDate);
            return newDate;
        } else {
            return "";
        }

    }

    public static long getMintue(Date expriyDate) throws ParseException {
        Date date = new Date();

        long timeDiff;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = dateFormat.format(date);
        String expirydate = dateFormat.format(expriyDate);
        System.out.println("strDate=" + strDate);
        System.out.println("expriyDate=" + expirydate);
        Date startDateObj = dateFormat.parse(strDate);
        Date endDateObj = dateFormat.parse(expirydate);
        System.out.println("1:" + strDate.compareTo(expirydate));
        if (strDate.compareTo(expirydate) > 0) {
            timeDiff = startDateObj.getTime() - endDateObj.getTime();
        } else if (strDate.compareTo(expirydate) < 0) {
            timeDiff = startDateObj.getTime() - endDateObj.getTime();
        } else {
            timeDiff = endDateObj.getTime() - startDateObj.getTime();
        }

        long minDiff = timeDiff / (1000 * 60);
        return minDiff;

    }

    public static String convertStringToDateTime(String date) {
        try {
//            date = "2022-05-04T07:03:54.000+00:00";
            if (date != null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.000+00:00");
                Date dateTime = dateFormat.parse(date);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                String newDate = df.format(dateTime);

                return newDate;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Date StringToDate(String date) {
        try {
            if (date != null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dateTime = dateFormat.parse(date);
                return dateTime;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getCurrentDateTimeMinusOne(String formate) {
        SimpleDateFormat gettingfmt = new SimpleDateFormat(formate, Locale.getDefault());
        Calendar currentTimeNow = Calendar.getInstance();
        currentTimeNow.add(Calendar.MINUTE, -1);
        String formated = gettingfmt.format(currentTimeNow.getTime());
        return formated;
    }

    public static String getCurrentDateTimePlusOne(String formate) {
        SimpleDateFormat gettingfmt = new SimpleDateFormat(formate, Locale.getDefault());
        Calendar currentTimeNow = Calendar.getInstance();
        currentTimeNow.add(Calendar.MINUTE, 1);
        String formated = gettingfmt.format(currentTimeNow.getTime());
        return formated;
    }


    public static Date convertStringToDate(String date) {
        try {
            if (date != null) {
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                Date praseDate = simpleDateFormat.parse(date);
                return praseDate;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Long convertStrigDateToLong(String date) {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        long milliseconds = 0;
        try {
            Date d = f.parse(date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public static int dayBetweenTwoDate() {
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date d2 = calendar.getTime();
        long difference = d2.getTime() - d1.getTime();
        float daysBetween = (difference / (1000 * 60 * 60 * 24));
        int days = (int) daysBetween;
        return days;
    }


    public static String getDay(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week abbreviated
            String days = simpleDateformat.format(date);
            return days;
        } else {
            return "";
        }

    }

    public static Date convertStringToDateDDMMYY(String date) {
        try {
            if (date != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                Date dateTime = dateFormat.parse(date);
                return dateTime;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date convertStringToDateMMDDYYYY(String date) {
        try {
            if (date != null) {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
                Date dateTime = dateFormat.parse(date);
                return dateTime;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static String getTime(Date date) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat("hh:mm");
            String newDate = df.format(date);
            return newDate;
        } else {
            return "";
        }

    }

    public static String templatedateFormate(Date date) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd, yyyy");
            String newdate = dateFormat.format(date);
            String month = Month.of(Calendar.MONTH + 1).name();
            return month + " " + newdate;
        } else {
            return "";
        }
    }


    public static void main(String[] args) {
        System.out.println(getTime(new Date()));
    }


}
