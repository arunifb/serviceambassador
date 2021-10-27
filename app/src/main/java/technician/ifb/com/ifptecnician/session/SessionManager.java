package technician.ifb.com.ifptecnician.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

import technician.ifb.com.ifptecnician.LoginActivity;


public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ifb_Pref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    public static final String KEY_PartnerId="PartnerId";
    public static final String KEY_FrCode="FrCode";
    public static final String KEY_EmailId="EmailId";
    public static final String KEY_Name="Name";
    public static final String KEY_passsword="passsword";
    public static final String KEY_mobileno="mobileno";
    public static final String KEY_PROFILE_URL="profileurl";
    public static final String KEY_ALL_DATA="alldata";
    public static final String KEY_subscriptionId="subscriptionId";


    public static final String KEY_IsActive="IsActive";
    public static final String KEY_RoleId="alldata";
    public static final String KEY_RegionCode="RegionCode";
    public static final String KEY_OTPCode="alldata";
    public static final String KEY_result="alldata";
    public static final String KEY_PANNo="PANNo";
    public static final String KEY_AadharNo="AadharNo";
    public static final String KEY_BloodGroup="BloodGroup";





    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(String PartnerId,String FrCode,String EmailId,String Name,String passsword,String mobileno,String url,
                                   String alldata,String RegionCode,
                                   String PANNo,String AadharNo,String BloodGroup){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_PartnerId,PartnerId);
        editor.putString(KEY_FrCode,FrCode);
        editor.putString(KEY_EmailId,EmailId);
        editor.putString(KEY_Name,Name);
        editor.putString(KEY_passsword,passsword);
        editor.putString(KEY_mobileno,mobileno);
        editor.putString(KEY_PROFILE_URL,url);
        editor.putString(KEY_ALL_DATA,alldata);
        editor.putString(KEY_RegionCode,RegionCode);
        editor.putString(KEY_PANNo,PANNo);
        editor.putString(KEY_AadharNo,AadharNo);
        editor.putString(KEY_BloodGroup,BloodGroup);


        editor.commit();
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_PartnerId, pref.getString(KEY_PartnerId, null));
        user.put(KEY_FrCode,pref.getString(KEY_FrCode,null));
        user.put(KEY_EmailId,pref.getString(KEY_EmailId,null));
        user.put(KEY_Name,pref.getString(KEY_Name,null));
        user.put(KEY_passsword,pref.getString(KEY_passsword,null));
        user.put(KEY_mobileno,pref.getString(KEY_mobileno,null));
        user.put(KEY_PROFILE_URL,pref.getString(KEY_PROFILE_URL,null));
        user.put(KEY_ALL_DATA,pref.getString(KEY_ALL_DATA,null));
        user.put(KEY_RegionCode,pref.getString(KEY_RegionCode,null));


        user.put(KEY_PANNo,pref.getString(KEY_PANNo,null));
        user.put(KEY_AadharNo,pref.getString(KEY_AadharNo,null));
        user.put(KEY_BloodGroup,pref.getString(KEY_BloodGroup,null));

        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context,LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
