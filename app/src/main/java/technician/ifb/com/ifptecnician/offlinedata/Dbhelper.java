package technician.ifb.com.ifptecnician.offlinedata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Dbhelper  extends SQLiteOpenHelper {


    SQLiteDatabase myDataBase;
    // Logcat tag
    private static final String LOG = "Dbhelper";
    // Database Version
    private static final int DATABASE_VERSION = 39;
    // Database Name
    private static final String DATABASE_NAME = "ifb_technician_db";

    public Dbhelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

  //  private static final String TABLE_AllTASK= "CREATE TABLE task_data( recordid INTEGER PRIMARY KEY , TicketNo TEXT , Branch TEXT , Franchise TEXT , CallType TEXT , Status TEXT , +Product TEXT , Model TEXT , SerialNo TEXT , MachinStatus TEXT , DOP TEXT ,DOI TEXT , PendingReason TEXT , CallBookDate TEXT , AssignDate TEXT , ClosedDate TEXT ,CancelledDate  TEXT , TechnicianCode  TEXT ,  TechName TEXT ,  CustomerCode  TEXT ,  CustomerName TEXT ,PinCode TEXT ,  TelePhone TEXT ,  RCNNo TEXT ,  MobileNo TEXT , Street TEXT ,City TEXT , State TEXT , Address  TEXT , NO126 TEXT , ServiceType TEXT , Email  TEXT );";

    private  static final String TABLE_AllTASK = "CREATE TABLE task_data( recordid INTEGER PRIMARY KEY, data TEXT, date TEXT );";

    private static final String TABLE_BOMDATA = "CREATE TABLE bom_data( recordid INTEGER PRIMARY KEY, ComponentDescription TEXT, Component TEXT, FGDescription TEXT ,FGProduct TEXT ,FrCode TEXT ,MaterialCategory TEXT ,good_stock TEXT ,refurbished_stock TEXT ,Date TEXT );";

   private static final String TABLE_OFFLINE_SPARE = "CREATE TABLE offline_spare_data( recordid INTEGER PRIMARY KEY, Product TEXT , FGProduct TEXT , FrCode TEXT , Data TEXT , Date TEXT );";

    private static final String TABLE_ESSDATA = "CREATE TABLE ess_data( recordid INTEGER PRIMARY KEY, ComponentDescription TEXT, Component TEXT, ItemType TEXT ,accessories_stock TEXT ,additives_stock TEXT ,ShelfLife TEXT ,Date TEXT );";

    private static final String TABLE_OFFLINE_DASHBOARD = "CREATE TABLE dashboarddata( recordid INTEGER PRIMARY KEY, Name TEXT, Count TEXT );";

    private static final String TABLE_ALLTASK_DATA = "CREATE TABLE alltask( recordid INTEGER PRIMARY KEY, Data TEXT, Time TEXT );";

    private static final String TABLE_ASSIGN_DATA = "CREATE TABLE assigntask( recordid INTEGER PRIMARY KEY, Data TEXT, Time TEXT );";

    private static final String TABLE_PENDING_DATA = "CREATE TABLE pendingtask( recordid INTEGER PRIMARY KEY, Data TEXT, Time TEXT );";

    private static final String TABLE_CANCEL_DATA = "CREATE TABLE canceltask( recordid INTEGER PRIMARY KEY, Data TEXT, Time TEXT );";

    private static final String TABLE_CLOSED_DATA = "CREATE TABLE closetask( recordid INTEGER PRIMARY KEY, Data TEXT, Time TEXT );";

    private static final String TABLE_NR_DATA = "CREATE TABLE nrtask( recordid INTEGER PRIMARY KEY, Data TEXT, Time TEXT );";

    private static final String TABLE_SOFTCLOSE_DATA = "CREATE TABLE softclosetask( recordid INTEGER PRIMARY KEY, Data TEXT, Time TEXT );";

    private static final String TABLE_REQCANCEL_DATA = "CREATE TABLE reqcanceltask( recordid INTEGER PRIMARY KEY, Data TEXT, Time TEXT );";


    private static final String TABLE_LEAD_DATA = "CREATE TABLE leadtask( recordid INTEGER PRIMARY KEY, Data TEXT, Time TEXT );";

    private static final String TABLE_READ_TASK = "CREATE TABLE readtask( reacordid INTEGER PRIMARY KEY, Ticketno TEXT, Date TEXT, ChangeTime TEXT, Status TEXT, C_Status TEXT );";

    private static final String TABLE_OFFLINESERVICE_URL = "CREATE TABLE offlineservice( recordid INTEGER PRIMARY KEY, Ticketno TEXT, Date TEXT, Fileurl TEXT , Type TEXT  );";

   // private static final String TABLE_DELETE_ITEM = "CREATE TABLE DELETE_ITEM( recordid INTEGER PRIMARY KEY, Ticketno TEXT, Sparecode TEXT, C TEXT );";

    private static final String TABLE_SAVE_SPARE = "CREATE TABLE added_spare( recordid INTEGER PRIMARY KEY, Ticketno TEXT, Status TEXT , Sname TEXT, Scode TEXT, Sflag TEXT, Sqty TEXT );";

    private static final String TABLE_SAVE_ESSENTISL = "CREATE TABLE added_ess( recordid INTEGER PRIMARY KEY, Ticketno TEXT, Status TEXT , Ename TEXT, Ecode TEXT, Eqty TEXT , Etype TEXT );";

    private static final String TABLE_SAVE_HOME_DETAILS = "CREATE TABLE added_home_details( recordid INTEGER PRIMARY KEY, Ticketno TEXT, Status TEXT, Product TEXT, Year TEXT, Company TEXT );";

    private static final String TABLE_SAVE_OTHER_DETAILS = "CREATE TABLE added_others_details( recordid INTEGER PRIMARY KEY, Ticketno TEXT, Status TEXT, Serialno TEXT, DOP TEXT , DOI TEXT , Icr_date TEXT, Icr_no TEXT, Feedback TEXT, Member TEXT, House TEXT, Re_date TEXT, Re_time TEXT, Chnge_status TEXT, Pend_res TEXT,Pend_causes TEXT );";

    private static final String TABLE_SAVE_PENDING_REASONE_DETAILS = "CREATE TABLE pending_reasone( recordid INTEGER PRIMARY KEY, Codegroup TEXT, Data TEXT, Datetime TEXT );";

    private  static final String TABLE_OFFLINE_IMAGE_UPLOAD_DATA = "CREATE TABLE ofline_upload_data ( recordid INTEGER PRIMARY KEY, Ticketno TEXT, Serialnoimage BLOB , Oduserialnoimage BLOB , Invioceimage BLOB , Signimage BLOB , Date TEXT , Status TEXT );";

    private  static  final String TABLE_OFFLINE_DELETED_SPARE = "CREATE TABLE offline_upload_deletedspare (recordid INTEGER PRIMARY KEY, Ticketno TEXT, Url TEXT, Date TEXT );";

    private  static  final String TABLE_NPS_SMS = "CREATE TABLE npssms (recordid INTEGER PRIMARY KEY, ticketno TEXT, destination TEXT, status TEXT );";

    private  static  final String TABLE_APPVERSION = "CREATE TABLE appversion (recordid INTEGER PRIMARY KEY, name TEXT, version TEXT, url TEXT );";

    private  static  final String TABLE_SO_HISTORY = "CREATE TABLE so_history (recordid INTEGER PRIMARY KEY, customercode TEXT, ticketno TEXT, problem TEXT , callbookdate TEXT , date TEXT );";

    private  static  final String TABLE_MandatoryCheck = "CREATE TABLE mandatorycheck (recordid INTEGER PRIMARY KEY, data TEXT, product TEXT );";

    private  static  final String TABLE_WaterQuality = "CREATE TABLE waterquality (recordid INTEGER PRIMARY KEY, data TEXT );";

    private  static  final String TABLE_Starttime = "CREATE TABLE starttime (recordid INTEGER PRIMARY KEY, ticketno TEXT , jobstarttime TEXT , mcheck TEXT , water_code TEXT , tds_level TEXT , partnerid TEXT );";

    private static final String TABLE_PUNCHED_AMC = "CREATE TABLE punchedamc (recordid INTEGER PRIMARY KEY , data TEXT , date TEXT );";

    private static final String TABLE_SERIAL_NO = "CREATE TABLE storeserial (recordid INTEGER PRIMARY KEY , serialno TEXT , ticketno TEXT , type TEXT );";

    private static final String TABLE_AMC_READ_TASK = "CREATE TABLE amcreadtask (recordid INTEGER PRIMARY KEY , serialno TEXT , mobile TEXT , alt_mob TEXT , data TEXT );";

    private static final String TABLE_WORK_DETAILS= "CREATE TABLE work_details (recordid INTEGER PRIMARY KEY , ticketno TEXT , serialno TEXT , oduserialno TEXT , dop TEXT , doi TEXT , jobstarttime TEXT , remaintime TEXT , sparedata TEXT , essdata TEXT , feedback TEXT , noofmember TEXT , residence TEXT , customerdetails TEXT , status TEXT , tds TEXT , ctcstatus TEXT );";

    private static final String TABLE_PENDING_SPARE= "CREATE TABLE pending_spare (recordid INTEGER PRIMARY KEY , ticketno TEXT , sparedetails TEXT );";

    private static final String TABLE_OFFLINE_DATA = "CREATE TABLE offline_data (recordid INTEGER PRIMARY KEY , ticketno TEXT , submitdata TEXT , type TEXT , url TEXT );";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(TABLE_BOMDATA);
        db.execSQL(TABLE_ESSDATA);
        db.execSQL(TABLE_AllTASK);
        db.execSQL(TABLE_OFFLINE_DASHBOARD);
        db.execSQL(TABLE_ALLTASK_DATA);
        db.execSQL(TABLE_ASSIGN_DATA);
        db.execSQL(TABLE_PENDING_DATA);
        db.execSQL(TABLE_CANCEL_DATA);
        db.execSQL(TABLE_CLOSED_DATA);
        db.execSQL(TABLE_NR_DATA);
        db.execSQL(TABLE_READ_TASK);
        db.execSQL(TABLE_OFFLINESERVICE_URL);
        db.execSQL(TABLE_SAVE_SPARE);
        db.execSQL(TABLE_SAVE_ESSENTISL);
        db.execSQL(TABLE_SAVE_HOME_DETAILS);
        db.execSQL(TABLE_SAVE_OTHER_DETAILS);
        db.execSQL(TABLE_SAVE_PENDING_REASONE_DETAILS);
        db.execSQL(TABLE_OFFLINE_IMAGE_UPLOAD_DATA);
        db.execSQL(TABLE_OFFLINE_DELETED_SPARE);
        db.execSQL(TABLE_SOFTCLOSE_DATA);
        db.execSQL(TABLE_REQCANCEL_DATA);
        db.execSQL(TABLE_LEAD_DATA);
        db.execSQL(TABLE_NPS_SMS);
        db.execSQL(TABLE_APPVERSION);
        db.execSQL(TABLE_OFFLINE_SPARE);
        db.execSQL(TABLE_SO_HISTORY);
        db.execSQL(TABLE_MandatoryCheck);
        db.execSQL(TABLE_WaterQuality);
        db.execSQL(TABLE_Starttime);
        db.execSQL(TABLE_PUNCHED_AMC);
        db.execSQL(TABLE_SERIAL_NO);
        db.execSQL(TABLE_AMC_READ_TASK);
        db.execSQL(TABLE_WORK_DETAILS);
        db.execSQL(TABLE_PENDING_SPARE);
        db.execSQL(TABLE_OFFLINE_DATA);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS bom_data");
        db.execSQL("DROP TABLE IF EXISTS ess_data");
        db.execSQL("DROP TABLE IF EXISTS task_data");
        db.execSQL("DROP TABLE IF EXISTS dashboarddata");
        db.execSQL("DROP TABLE IF EXISTS alltask");
        db.execSQL("DROP TABLE IF EXISTS assigntask");
        db.execSQL("DROP TABLE IF EXISTS pendingtask");
        db.execSQL("DROP TABLE IF EXISTS canceltask");
        db.execSQL("DROP TABLE IF EXISTS closetask");
        db.execSQL("DROP TABLE IF EXISTS nrtask");
        db.execSQL("DROP TABLE IF EXISTS readtask");
        db.execSQL("DROP TABLE IF EXISTS offlineservice");
        db.execSQL("DROP TABLE IF EXISTS added_spare");
        db.execSQL("DROP TABLE IF EXISTS added_ess");
        db.execSQL("DROP TABLE IF EXISTS added_home_details");
        db.execSQL("DROP TABLE IF EXISTS added_others_details");
        db.execSQL("DROP TABLE IF EXISTS pending_reasone");
        db.execSQL("DROP TABLE IF EXISTS ofline_upload_data");
        db.execSQL("DROP TABLE IF EXISTS offline_upload_deletedspare");
        db.execSQL("DROP TABLE IF EXISTS softclosetask");
        db.execSQL("DROP TABLE IF EXISTS reqcanceltask");
        db.execSQL("DROP TABLE IF EXISTS leadtask");
        db.execSQL("DROP TABLE IF EXISTS npssms");
        db.execSQL("DROP TABLE IF EXISTS appversion");
        db.execSQL("DROP TABLE IF EXISTS offline_spare_data");
        db.execSQL("DROP TABLE IF EXISTS so_history");
        db.execSQL("DROP TABLE IF EXISTS mandatorycheck");
        db.execSQL("DROP TABLE IF EXISTS waterquality");
        db.execSQL("DROP TABLE IF EXISTS starttime");
        db.execSQL("DROP TABLE IF EXISTS punchedamc");
        db.execSQL("DROP TABLE IF EXISTS storeserial");
        db.execSQL("DROP TABLE IF EXISTS amcreadtask");
        db.execSQL("DROP TABLE IF EXISTS work_details");
        db.execSQL("DROP TABLE IF EXISTS pending_spare");
        db.execSQL("DROP TABLE IF EXISTS offline_data");

        // create new tables
        onCreate(db);
    }

    public boolean insert_offline_deletespare_data(String Ticketno,String Url,String Date ){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Ticketno",Ticketno);
        values.put("Url",Url);
        values.put("Date",Date);

        try{
            success=database.insert("offline_upload_deletedspare",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor get_offline_deletespare_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from offline_upload_deletedspare " ,null);
        } catch (Exception e) {
            e.printStackTrace();


        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }

    public Cursor delete_deletespare_data(String Ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from offline_upload_deletedspare where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }

    public boolean insert_offline_image_data(String Ticketno,byte [] Serialnoimage,byte [] Oduserialnoimage, byte [] Invioceimage , byte [] Signimage , String Date, String Status){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Ticketno",Ticketno);
        values.put("Serialnoimage",Serialnoimage);
        values.put("Oduserialnoimage",Oduserialnoimage);
        values.put("Invioceimage",Invioceimage);
        values.put("Signimage",Signimage);
        values.put("Date",Date);
        values.put("Status", Status);

        try{
            success=database.insert("ofline_upload_data",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor get_offline_image_data(String Ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from ofline_upload_data where Ticketno ='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }


    public Cursor get_all_offline_imagedata(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from ofline_upload_data ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }


    public Cursor delete_image_data(String Ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from ofline_upload_data where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }


    public Cursor delete_worktime_image(String Status){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from ofline_upload_data where Status='"+ Status + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }




    public boolean insert_pending_reasone_data(String Codegroup,String Data,String Datetime){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Codegroup",Codegroup);
        values.put("Data",Data);
        values.put("Datetime",Datetime);
        try{
            success=database.insert("pending_reasone",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor fetch_pending_reasone(String Codegroup){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from pending_reasone where Codegroup ='"+ Codegroup + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor fetch_pending_reasone_By_date(String Datetime){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from pending_reasone where Datetime ='"+ Datetime + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public boolean ISDateExsits(String Datetime) {
        Cursor cursor=null;
        SQLiteDatabase database=this.getReadableDatabase();
        try
        {
            cursor=database.rawQuery("select * from pending_reasone where Datetime ='"+ Datetime + "'",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean delete_pending_reasone() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("pending_reasone", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean insert_offlineservice_data(String Ticketno,String Date,String Fileurl,String Type){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Ticketno",Ticketno);
        values.put("Date",Date);
        values.put("Fileurl",Fileurl);
        values.put("Type",Type);
        try{
            success=database.insert("offlineservice",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor get_offlineurl_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  offlineservice ",null);
        } catch (Exception e) {
            e.printStackTrace();


        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }






    public Cursor delete_offlineurl(String Ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from offlineservice where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }

    public boolean insert_read_data(String Ticketno,String Date,String ChangeTime,String Status,String C_Status){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Ticketno",Ticketno);
        values.put("Date",Date);
        values.put("ChangeTime",ChangeTime);
        values.put("Status",Status);
        values.put("C_Status",C_Status);
        try{
            success=database.insert("readtask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public boolean ISreadtaskexits(String Ticketno) {
        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select Ticketno from readtask where Ticketno='"+ Ticketno + "'",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            // cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Cursor fetch_read_data(String Ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from readtask where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor delete_read_data(String Ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from readtask where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }

    public boolean insert_all_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Date",Data);
        values.put("Time",Time);
        try{
            success=database.insert("alltask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    // aqssigned call offline operation

    public boolean insert_assign_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Data",Data);
        values.put("Time",Time);
        try{
            success=database.insert("assigntask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }

        return  false;
    }

    public Cursor get_assign_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  assigntask ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }

        return mCursor;
    }

    public boolean delete_assign_data() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("assigntask", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }


    // soft close call offline operation
    public boolean insert_softclose_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Data",Data);
        values.put("Time",Time);
        try{
            success=database.insert("softclosetask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }

        return  false;
    }

    public Cursor get_softclose_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  softclosetask ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }

        return mCursor;
    }

    public boolean delete_softclose_data() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("softclosetask", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }


  //   pending call offline operation

    public boolean insert_pending_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Data",Data);
        values.put("Time",Time);
        try{
            success=database.insert("pendingtask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor get_pending_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  pendingtask ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }

        return mCursor;
    }

    public boolean delete_pending_data() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("pendingtask", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    //  reqcancel call offline operation

    public boolean insert_reqcancel_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Data",Data);
        values.put("Time",Time);
        try{
            success=database.insert("reqcanceltask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor get_reqcancel_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  reqcanceltask ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }

        return mCursor;
    }

    public boolean delete_reqcancel_data() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("reqcanceltask", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean insert_cancelled_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Data",Data);
        values.put("Time",Time);
        try{
            success=database.insert("canceltask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor get_cancelled_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  canceltask ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return mCursor;
    }

    public boolean delete_cancelled_data() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("canceltask", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }


    public boolean insert_closed_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Data",Data);
        values.put("Time",Time);
        try{
            success=database.insert("closetask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor get_closed_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  closetask ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return mCursor;
    }

    public boolean delete_closed_data() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("closetask", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }


    //  leaddata offline operation

    public boolean insert_lead_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Data",Data);
        values.put("Time",Time);
        try{
            success=database.insert("reqcanceltask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor get_lead_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  reqcanceltask ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }

        return mCursor;
    }

    public boolean delete_lead_data() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("reqcanceltask", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean insert_cancel_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Data",Data);
        values.put("Time",Time);
        try{
            success=database.insert("canceltask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;

    }

    public boolean insert_close_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Data",Data);
        values.put("Time",Time);
        try{
            success=database.insert("closetask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public boolean insert_nr_data(String Data,String Time){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Data",Data);
        values.put("Time",Time);
        try{
            success=database.insert("nrtask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor get_nr_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  nrtask ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }

        return mCursor;
    }

    public boolean delete_nr_data() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("nrtask", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean insert_task_data(String data,String date){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("data",data);
        values.put("date",date);
        try{
            success=database.insert("task_data",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public boolean insert_dashboarddata(String Name,String Count){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Name",Name);
        values.put("Count",Count);
        try {
            success=database.insert("dashboarddata",null,values);
        }
        catch (Exception e){
            success=-1;
            e.printStackTrace();
        }
        return false;
    }

    public boolean insert_bom_data(String componentDescription,String component,String FGDescription,String FGProduct,String FrCode,String MaterialCategory,String good_stock,String refurbished_stock,String date){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("ComponentDescription",componentDescription);
        values.put("Component",component);
        values.put("FGDescription",FGDescription);
        values.put("FGProduct",FGProduct);
        values.put("FrCode",FrCode);
        values.put("MaterialCategory",MaterialCategory);
        values.put("good_stock",good_stock);
        values.put("refurbished_stock",refurbished_stock);
        values.put("Date",date);

        try{
            success=database.insert("bom_data",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;

    }

    public boolean IS_Component_exits(String Component) {
        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select Component from bom_data where Component='"+ Component + "'",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            // cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // ----------------- get singal part details ----------------------

    public Cursor get_partName(String Component){
        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  bom_data where Component='"+ Component + "'",null);
        } catch (Exception e) {
            e.printStackTrace();


        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    // ----------------------- insert essential data ----------------------------

    public boolean insert_ess_data(String componentDescription,String component,String ItemType,String accessories_stock,String additives_stock,String ShelfLife,String date){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("ComponentDescription",componentDescription);
        values.put("Component",component);
        values.put("ItemType",ItemType);
        values.put("accessories_stock",accessories_stock);
        values.put("additives_stock",additives_stock);
        values.put("ShelfLife",ShelfLife);
        values.put("Date",date);
        try{
            success=database.insert("ess_data",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;

    }

    // ----------------  check essential in list ---------------------------

    public boolean IS_ess_Component_exits(String Component) {
        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select Component from ess_data where Component='"+ Component + "'",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            // cursor.close();
            return false;
        }

        return true;
    }

    public Cursor get_ess_Name(String Component){
        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  ess_data where Component='"+ Component + "'",null);
        } catch (Exception e) {
            e.printStackTrace();


        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public boolean deletebom() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("bom_data", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean deleteess() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("ess_data", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean delete_readtask(){


        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("readtask", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean delete_added_spare(){


        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("added_spare", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean delete_added_ess(){


        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("added_ess", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean deletedashboard() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("dashboarddata", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean deletetask() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("task_data", "1", null);
        } catch (Exception e) {
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }

    public boolean istaskdataEmpty(){

        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select * from task_data ",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            // cursor.close();
            return false;
        }

        return true;
    }

    public boolean isbomdataEmpty(){

        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select * from bom_data ",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            // cursor.close();
            return false;
        }

        return true;
    }

    public boolean isessdataEmpty(){

        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select * from ess_data ",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
           //  cursor.close();
            return false;
        }

        cursor.close();

        return true;
    }

    public Cursor getallbomdata(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  bom_data ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor getalldashboard(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  dashboarddata ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor getallessdata(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  ess_data ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor getalltaskdata(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  task_data ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor getesstype(String ComponentDescription) {

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  ess_data where ComponentDescription='"+ ComponentDescription + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor get_Single_Bom_Data(String ComponentDescription){
        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  bom_data where ComponentDescription='"+ ComponentDescription + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;

    }

    public boolean insert_spare(String Ticketno,String Status,String Sname,String Scode,String Sflag,String Sqty){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Ticketno",Ticketno);
        values.put("Status",Status);
        values.put("Sname",Sname);
        values.put("Scode",Scode);
        values.put("Sflag",Sflag);
        values.put("Sqty",Sqty);

        try{
            success=database.insert("added_spare",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;

    }

    public Cursor get_spare_Data(String Ticketno){
        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  added_spare where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor delete_spare_data(String Ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from added_spare where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }

    public boolean is_save_spare_Empty(){

        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select * from added_spare ",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            // cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //added_ess( recordid INTEGER PRIMARY KEY, Ticketno TEXT, Status TEXT , Ename TEXT, Ecode TEXT, Eqty TEXT , Etype TEXT );";

    public boolean insert_save_ess(String Ticketno,String Status,String Ename,String Ecode,String Eqty,String Etype){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Ticketno",Ticketno);
        values.put("Status",Status);
        values.put("Ename",Ename);
        values.put("Ecode",Ecode);
        values.put("Eqty",Eqty);
        values.put("Etype",Etype);

        try{
            success=database.insert("added_ess",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;

    }

    public Cursor get_save_ess_Data(String Ticketno){

        System.out.println("get essential data"+Ticketno);
        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  added_ess where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor delete_save_ess_data(String Ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from added_ess where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();


        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }


    public boolean is_save_ess_Empty(){

        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select * from added_ess ",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            // cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //added_home_details( recordid INTEGER PRIMARY KEY, Ticketno TEXT, Status TEXT, Product TEXT, Year TEXT, Company TEXT );";

    public boolean insert_save_home(String Ticketno,String Status,String Product,String Year,String Company){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Ticketno",Ticketno);
        values.put("Status",Status);
        values.put("Product",Product);
        values.put("Year",Year);
        values.put("Company",Company);


        try{
            success=database.insert("added_home_details",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;

    }

    public Cursor get_home_Data(String Ticketno){
        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  added_home_details where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();


        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor delete_home_data(String Ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from added_home_details where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {

                e2.printStackTrace();
            }
        }
        return mCursor;
    }

    public boolean is_save_home_Empty(){

        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select * from added_home_details ",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            // cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

   //  Ticketno TEXT, Status TEXT, Serialno TEXT, DOP TEXT , DOI TEXT , Icr_date TEXT, Icr_no TEXT, Feedback TEXT, Member TEXT, House TEXT, Re_date TEXT, Re_time TEXT, Chnge_status TEXT, Pend_res TEXT,Pend_causes TEXT );";

    public boolean insert_others_details(String Ticketno,String Status,String Serialno,String DOP,String DOI,String Icr_date,String Icr_no,String Feedback,String Member,String House,String Re_date,String Re_time,String Chnge_status,String Pend_res,String Pend_causes){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Ticketno",Ticketno);
        values.put("Status",Status);
        values.put("Serialno",Serialno);
        values.put("DOP",DOP);
        values.put("DOI",DOI);
        values.put("Icr_date",Icr_date);
        values.put("Icr_no",Icr_no);
        values.put("Feedback",Feedback);
        values.put("Member",Member);
        values.put("House",House);
        values.put("Re_date",Re_date);
        values.put("Re_time",Re_time);
        values.put("Chnge_status",Chnge_status);
        values.put("Pend_res",Pend_res);
        values.put("Pend_causes",Pend_causes);

        try{
            success=database.insert("added_others_details",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;

    }

    public Cursor get_others_details(String Ticketno){
        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  added_others_details where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();


        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor delete_others_details(String Ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from added_others_details where Ticketno='"+ Ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();


        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        mCursor.close();
        return mCursor;
    }

    public boolean is_save_other_Empty(){

        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select * from added_others_details ",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            // cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


//    --------------------------  for nps sms  -------------------------------------------
   public boolean insert_nps_status(String ticketno,String destination,String status){

    long success;
    SQLiteDatabase database=this.getReadableDatabase();
    ContentValues values=new ContentValues();
    values.put("ticketno",ticketno);
    values.put("destination",destination);
    values.put("status",status);


    try{
        success=database.insert("npssms",null,values);
    }catch (Exception e){
        success=-1;
    }
    if (success > -1){
        return true;
    }
    return  false;

}

    public String getnpsstatus(String destination) {
        Cursor cursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        String value = "";
        try {
            cursor = database.rawQuery("select * from npssms where destination= '"+destination +"'",null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                value = cursor.getString(cursor.getColumnIndex("status"));
            }
            return value;
        }finally {
            cursor.close();
        }
    }


//  ----------------------------    insert app version    --------------------------

//    public boolean insert_app_version (String name,String version,String url){
//
//        long success;
//        SQLiteDatabase database=this.getReadableDatabase();
//        ContentValues values=new ContentValues();
//        values.put("name",name);
//        values.put("version",version);
//        values.put("url",url);
//
//        try{
//            success=database.insert("appversion",null,values);
//        }catch (Exception e){
//            success=-1;
//        }
//        if (success > -1){
//            return true;
//        }
//        return  false;
//
//    }

//    public String getappversion() {
//        Cursor cursor = null;
//        SQLiteDatabase database=this.getReadableDatabase();
//        String value = "";
//        try {
//            cursor = database.rawQuery("select * from appversion ",null);
//            if(cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                value = cursor.getString(cursor.getColumnIndex("version"));
//            }
//            return value;
//        }finally {
//            cursor.close();
//        }
//    }

//    public boolean deleteappversion() {
//        long Success;
//        SQLiteDatabase db = this.getWritableDatabase();
//        try {
//            Success = db.delete("appversion", "1", null);
//        } catch (Exception e) {
//            Success = -1;
//        }
//        if (Success > -1) {
//            return true;
//        }
//        return false;
//    }


    // ----------------------    All  offline spare operation --------------------------------------------------

    public boolean insert_offline_spare(String Product ,String FGProduct ,String FrCode ,String Data,String Date ){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("Product",Product);
        values.put("FGProduct",FGProduct);
        values.put("FrCode",FrCode);
        values.put("Data",Data);
        values.put("Date",Data);
        try{
            success=database.insert("offline_spare_data",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }


    public String get_offline_spare_by_fgproduct() {
        Cursor cursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        String value = "";
        try {
            cursor = database.rawQuery("select * from offline_spare_data ",null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                value = cursor.getString(cursor.getColumnIndex("Data"));
            }
            return value;
        }finally {
            cursor.close();
        }
    }

    public Cursor get_offline_spare_data(String FGProduct){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  offline_spare_data where FGProduct ='"+FGProduct+"'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }

        return mCursor;
    }

//    public boolean delete_offline_spare_data(String FGProduct) {
//        long Success;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor mCursor = null;
//        try {
//
//
//            mCursor = db.rawQuery("delete from offline_spare_data where FGProduct='"+ FGProduct + "'",null);
//           // Success = db.delete("delete from offline_spare_data where FGProduct='"+FGProduct+"'", "1", null);
//
//
//        } catch (Exception e) {
//            Success = -1;
//            e.printStackTrace();
//        }
//        if (Success > -1) {
//            return true;
//        }
//        return false;
//    }



    public Cursor delete_offline_spare_data(String FGProduct){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {

            mCursor = database.rawQuery("delete from offline_spare_data where FGProduct='"+ FGProduct + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }


    public boolean insert_sohistory_data(String customercode, String ticketno, String problem ,String callbookdate,String date){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("customercode",customercode);
        values.put("ticketno",ticketno);
        values.put("problem",problem);
        values.put("callbookdate",callbookdate);
        values.put("date",date);
        try{
            success=database.insert("so_history",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }

    public Cursor get_sohistory_data(String customercode){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  so_history where customercode='"+ customercode + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor delete_sohistory(String date){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from so_history where date='"+ date + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {

                e2.printStackTrace();
            }
        }
        return mCursor;
    }


    //    -----------------------  inser water quality ---------------------------

    public boolean insert_waterquality(String data){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("data",data);
        try{
            success=database.insert("waterquality",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }

        return  false;
    }

    public Cursor get_waterquality(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from  waterquality ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }

        return mCursor;
    }

    public boolean delete_waterquality() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("waterquality", "1", null);
        } catch (Exception e) {

            e.printStackTrace();
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }



    //------------------- insert MandatoryCheck -------------------

    public boolean insert_mandatorycheck(String data,String product){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("data",data);
        values.put("product",product);
        try{
            success=database.insert("mandatorycheck",null,values);
        }catch (Exception e){
            e.printStackTrace();
            success=-1;
        }
        if (success > -1){
            return true;
        }

        return  false;
    }

    public Cursor get_mandatorycheck(String product){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from   mandatorycheck where product='"+ product + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }

    public Cursor delete_mandatorycheck(String product){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from mandatorycheck where product='"+ product + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }



    //---------- insert starttime ----------------

   // recordid INTEGER PRIMARY KEY, ticketNo TEXT , jobstarttime TEXT , mcheck TEXT , water_code TEXT , tds_level TEXT , partnerid TEXT );";


    public boolean insert_starttime(String ticketno,String jobstarttime,String mcheck,String water_code,String tds_level,String partnerid){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("ticketno",ticketno);
        values.put("jobstarttime",jobstarttime);
        values.put("mcheck",mcheck);
        values.put("water_code",water_code);
        values.put("tds_level",tds_level);
        values.put("partnerid",partnerid);

        try{
            success=database.insert("starttime",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }

        return  false;
    }

    public Cursor get_starttime(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from starttime ",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }

    public Cursor delete_starttime(String ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from starttime where ticketno ='"+ ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {

                e2.printStackTrace();
            }
        }
        return mCursor;
    }


    // essential lead section


    // punched amc section

    public boolean insert_punched_amc_data(String data,String date){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("data",data);
        values.put("date",date);
        try{

            success=database.insert("punchedamc",null,values);
        }catch (Exception e){

            success=-1;
            e.printStackTrace();
        }

        if (success > -1){

            return true;
        }

        return false;

    }

    public Cursor get_punched_amc(){
        Cursor cursor=null;
        SQLiteDatabase database=this.getReadableDatabase();
        try{

            cursor=database.rawQuery("select * from punchedamc",null);

        }catch (Exception e){

            e.printStackTrace();
        }

        if (cursor !=null){
            try{
               cursor.moveToFirst();

            }catch (Exception ex){

                ex.printStackTrace();
            }
        }
        return cursor;
    }

    public boolean delete_amc_punched_data() {
        long Success;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Success = db.delete("punchedamc", "1", null);
        } catch (Exception e) {

            e.printStackTrace();
            Success = -1;
        }
        if (Success > -1) {
            return true;
        }
        return false;
    }


  // store serial no

    public boolean insert_serial_no_data(String serialno,String ticketno,String type){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("serialno",serialno);
        values.put("ticketno",ticketno);
        values.put("type",type);
        try{

            success=database.insert("storeserial",null,values);
        }catch (Exception e){

            success=-1;
            e.printStackTrace();
        }

        if (success > -1){

            return true;
        }

        return false;

    }


    public String getserialno(String ticketno , String type) {
        Cursor cursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        String value = "";
        try {
            cursor = database.rawQuery("select * from storeserial where ticketno = '"+ ticketno +"' and type = '"+ type +"'" ,null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                value = cursor.getString(cursor.getColumnIndex("serialno"));
                cursor.close();
            }
            return value;
         }catch (Exception e){

            e.printStackTrace();
            cursor.close();
            value="";
            return value;
        }

        finally {
            cursor.close();
        }
    }



    public Cursor delete_serial_no_data(String ticketno,String type){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from storeserial where ticketno ='"+ ticketno + "' and type ='"+type+"'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }



    // insert amc punched data

    public boolean insert_amc_read_data(String serialno,String mobile,String alt_mob,String data){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("serialno",serialno);
        values.put("mobile",mobile);
        values.put("alt_mob",alt_mob);
        values.put("data",data);
        try{
            success=database.insert("amcreadtask",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }





    public boolean ISAMCreadtaskexits(String serialno) {
        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor=null;
        try
        {
            cursor=database.rawQuery("select serialno from amcreadtask where serialno='"+ serialno + "'",null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if (cursor == null || cursor.getCount() <= 0) {
            // cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    public Cursor fetch_amc_read_data(String serialno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("select * from amcreadtask where serialno='"+ serialno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }



    public Cursor delete_amc_read_data(String serialno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from amcreadtask where serialno='"+ serialno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }


    // ------------------------  insert work details ----------------------------

   // private static final String TABLE_WORK_DETAILS= "CREATE TABLE work_details (recordid INTEGER PRIMARY KEY , ticketno TEXT , serialno TEXT , oduserialno TEXT , dop TEXT , doi TEXT , jobstarttime TEXT , remaintime TEXT , sparedata TEXT , essdata TEXT , feedback TEXT , noofmember TEXT , residence TEXT , customerdetails TEXT , status TEXT );";


    public boolean insert_work_data(String ticketno,String serialno,String oduserialno,String dop,String doi,String jobstarttime,String remaintime,
                                    String sparedata,String essdata,String feedback,String noofmember,String residence,String customerdetails,String status,String tds,String ctcstatus){

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("ticketno",ticketno);
        values.put("serialno",serialno);
        values.put("oduserialno",oduserialno);
        values.put("dop",dop);
        values.put("doi",doi);
        values.put("jobstarttime",jobstarttime);
        values.put("remaintime",remaintime);
        values.put("sparedata",sparedata);
        values.put("essdata",essdata);
        values.put("feedback",feedback);
        values.put("noofmember",noofmember);
        values.put("residence",residence);
        values.put("customerdetails",customerdetails);
        values.put("status",status);
        values.put("tds",tds);
        values.put("ctcstatus",ctcstatus);

        try{
            success=database.insert("work_details",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }


    public Cursor get_work_data(String ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {

            mCursor = database.rawQuery("select * from work_details where ticketno ='"+ ticketno + "'",null);

        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
            }
        }
        return mCursor;
    }


    public Cursor delete_work_data(String ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from work_details where ticketno='"+ ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }



    // --------------------   pending spare --------------------


    public boolean insert_pening_spare(String ticketno,String sparedetails){


        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("ticketno",ticketno);
        values.put("sparedetails",sparedetails);

        try{
            success=database.insert("pending_spare",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }


    public Cursor get_pending_spare(String ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {

            mCursor = database.rawQuery("select * from pending_spare where ticketno ='"+ ticketno + "'",null);

        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }



    public Cursor delete_pending_spare(String ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from pending_spare where ticketno='"+ ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }


    public boolean insert_offline_data(String ticketno,String submitdata,String type, String url){
       // ticketno TEXT , submitdata TEXT , type TEXT , url TEXT

        long success;
        SQLiteDatabase database=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("ticketno",ticketno);
        values.put("submitdata",submitdata);
        values.put("type",type);
        values.put("url",url);

        try{
            success=database.insert("offline_data",null,values);
        }catch (Exception e){
            success=-1;
        }
        if (success > -1){
            return true;
        }
        return  false;
    }


    public Cursor get_offline_data(){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {

            mCursor = database.rawQuery("select * from offline_data ",null);

        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }



    public Cursor delete_offline_data(String ticketno){

        Cursor mCursor = null;
        SQLiteDatabase database=this.getReadableDatabase();
        try {
            mCursor = database.rawQuery("delete from offline_data where ticketno='"+ ticketno + "'",null);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (mCursor != null) {
            try {
                mCursor.moveToFirst();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return mCursor;
    }

//  ----------------------------   close db helper   --------------------------

    public synchronized void close() {
        if (this.myDataBase != null) {
            this.myDataBase.close();
        }
        super.close();
    }

}

