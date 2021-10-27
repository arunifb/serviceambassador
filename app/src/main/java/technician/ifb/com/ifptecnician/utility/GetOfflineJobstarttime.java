package technician.ifb.com.ifptecnician.utility;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetOfflineJobstarttime {

    public String gettime(Context context){

        String datatime="";

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aaa", Locale.getDefault());
        datatime = sdf.format(new Date());


        return datatime;
    }


}
