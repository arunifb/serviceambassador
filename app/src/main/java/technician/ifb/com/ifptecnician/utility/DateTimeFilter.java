package technician.ifb.com.ifptecnician.utility;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeFilter {


    public static String check_notification(String AssignDate){
        String status="";
        System.out.println(AssignDate);
        AssignDate="02/04/2020 12:46:09 PM";

        try {

            Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, -30);
            Date x = now.getTime();

            SimpleDateFormat formats=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a",Locale.ENGLISH);
            Date dts=formats.parse(AssignDate);

//            DateFormat cdf=new SimpleDateFormat("MM/dd/yyyy",Locale.ENGLISH);
//             String currentdate=cdf.format(now.getTime());
//             Calendar calendar=Calendar.getInstance();
//             calendar.setTime(cdf.parse(AssignDate));
//             String assdate=cdf.format(calendar.getTime());


            System.out.println(x +" "+dts);


            if (x.before(dts)) {
                status="true";
            }
            else {
                status="false";
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    public static  String getdopformat(String time){
        String inputPattern = "yyyyMMdd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            assert date != null;
            str = outputFormat.format(date);
             System.out.println("new date"+str);
        } catch (ParseException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return  str;
    }

    public static String get_diffrence(String startdate,String enddate){

        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());

        Date created_date=null,expire_date=null,today_date=null;
        try{
            created_date=format.parse(startdate);
            expire_date=format.parse(enddate);
            Date today=new Date();
            today_date=format.parse(format.format(today));

        }catch (Exception e){

            e.printStackTrace();

        }

        int cyear=0,cmonth=0,cday=0;
        if (created_date.after(today_date)){

        }
        else{

        }
        Calendar ecal=Calendar.getInstance();
        ecal.setTime(expire_date);

        int eday=ecal.get(Calendar.DAY_OF_MONTH);

        Calendar date1=Calendar.getInstance();
        Calendar date2=Calendar.getInstance();

        return "";
    }

    public static boolean isinwarranty(String warrentydate){
        boolean warranty=false;

        try{

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
            Date strDate = sdf.parse(warrentydate);
            assert strDate != null;
            if (System.currentTimeMillis() > strDate.getTime()) {

                warranty=false;
            }
            else {

                warranty=true;
            }
        }catch (Exception e){
            e.printStackTrace();
            warranty=false;
        }

        return warranty;
    }


    public static long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return elapsedDays;
    }

}
