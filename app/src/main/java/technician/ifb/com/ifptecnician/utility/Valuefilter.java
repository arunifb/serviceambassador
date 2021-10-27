package technician.ifb.com.ifptecnician.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Valuefilter {

    public static String Enddate(String value,String usges,String qty) {

        String data=value.replace(" ","");
        String numberOnly= data.replaceAll("[^0-9]", "");
       // String firsttwoDigits = data.substring(0,2);
        String notifydate="";
        int runningdays;

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Date newDate;
        System.out.println("Date = "+ cal.getTime());
        int total=0;

        try{

            total=Integer.parseInt(numberOnly)*Integer.parseInt(qty);
        }
        catch (Exception e){

            e.printStackTrace();
        }
        switch (usges){
            case "1/day":
                cal.add(Calendar.DATE, total);
                newDate = cal.getTime();
                notifydate=df.format(newDate);

                break;

            case "2/day":

                runningdays=total/2;
                cal.add(Calendar.DATE, runningdays);
                newDate = cal.getTime();
                notifydate=df.format(newDate);
                break;
            case "3/day":

                runningdays=total/3;
                cal.add(Calendar.DATE, runningdays);
                newDate = cal.getTime();
                notifydate=df.format(newDate);
                break;

            case "1/week":

                runningdays=total*7;
                cal.add(Calendar.DATE, runningdays);
                newDate = cal.getTime();
                notifydate=df.format(newDate);
                break;
            case "2/week":

                runningdays=total/2*7;
                cal.add(Calendar.DATE, runningdays);
                newDate = cal.getTime();
                notifydate=df.format(newDate);
                break;

            case "3/week":
                runningdays=total/3*7;
                cal.add(Calendar.DATE, runningdays);
                newDate = cal.getTime();
                notifydate=df.format(newDate);
                break;

        }
        return notifydate;
    }


    public static String Parsedate(String time){
        String inputPattern = "dd-MMM-yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);

            if( str.equals("19000101")){
                str="00000000";
            }


            // System.out.println(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  str;
    }

    public static String getCallTypes(String ptype){

        String processtype="";

        switch (ptype){

            case "SERVICE CALL":
                processtype="ZSER";
                break;
            case "INSTALLATION CALL":
                processtype="ZINT";
                break;
            case "MANDATORY CALLS":
                processtype="ZMAN";
                break;
            case "REINSTALLLATION ORDER":
                processtype="ZRIN";
                break;
            case "COURTESY VISIT":
                processtype="ZFOC";
                break;

            case "DEALER DEFECTIVE REQUEST":
                processtype="ZDD";
                break;
            case "REWORK ORDER" :
                processtype="ZREW";
                break;
            default:
                break;
        }
        return processtype;
    }

    public static String getdate(){

        String tss="";
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        tss = dfs.format(c);
        return tss;
    }
}
