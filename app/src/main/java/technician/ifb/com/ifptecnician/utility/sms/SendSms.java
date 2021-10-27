//package technician.ifb.com.ifptecnician.utility.sms;
//
//import android.app.Activity;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.telephony.SmsManager;
//import android.widget.Toast;
//
//public class SendSms {
//
//
//    private String SimState = "";
//    private String address = ""; // Recipient Phone Number
//    private String message = ""; // Message Body
//
//    private void sendSms(Context context,boolean isSimExists) {
//        if (isSimExists) {
//            try {
//                String SENT = "SMS_SENT";
//
//                PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
//
//                registerReceiver(new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context arg0, Intent arg1) {
//                        int resultCode = getResultCode();
//                        switch (resultCode) {
//                            case Activity.RESULT_OK:
//                                Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_LONG).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                                Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_LONG).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_NO_SERVICE:
//                                Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_LONG).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_NULL_PDU:
//                                Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_LONG).show();
//                                break;
//                            case SmsManager.RESULT_ERROR_RADIO_OFF:
//                                Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_LONG).show();
//                                break;
//                        }
//                    }
//                }, new IntentFilter(SENT));
//
//                SmsManager smsMgr = SmsManager.getDefault();
//                smsMgr.sendTextMessage(address, null, message, sentPI, null);
//            } catch (Exception e) {
//                Toast.makeText(context, e.getMessage() + "!\n" + "Failed to send SMS", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(context, SimState + " " + "Cannot send SMS", Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//
//}
