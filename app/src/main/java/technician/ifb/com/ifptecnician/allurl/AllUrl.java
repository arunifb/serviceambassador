package technician.ifb.com.ifptecnician.allurl;

import android.Manifest;
import android.content.pm.PackageManager;

import android.telephony.TelephonyManager;
import android.widget.Toast;

public class AllUrl {


    public static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static String KEY_PASSWORD = null;
    public static String USER_NAME="USER_NAME";
    public static String baseUrl;
    public static String loginUrl;
    public static String MOBILE_DETAILS;
    public static String LENGUAGE;
    public static String getotp;
    public static String verifyotp;
    public static String WaterQuality;
    public static String eCatalogue;

    public static String APP_Version;


    static {

         //baseUrl="https://sapsecurity.ifbhub.com/api/";
       //  baseUrl ="https://crm.ifbsupport.com/saapi/api/";
       // baseUrl="http://ifbportal.ifbsupport.com:8081/api/";

        baseUrl="https://crm.ifbsupport.com/technician/api/";
       // baseUrl="https://crm.ifbsupport.com/technician/";
        loginUrl = baseUrl +"validateUser";
        USER_NAME = "user_name";
        KEY_PASSWORD = "password";
        MOBILE_DETAILS ="mobile_details";

        LENGUAGE="lenguage";
        getotp="";
        WaterQuality=baseUrl+"Water/Quality/";
        eCatalogue=baseUrl+"Catalogue/eCatalogue/";

        APP_Version="2.3.4";

    }
}
