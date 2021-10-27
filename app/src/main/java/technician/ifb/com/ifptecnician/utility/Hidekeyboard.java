package technician.ifb.com.ifptecnician.utility;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class Hidekeyboard {


    public static void hideSoftKeyboard(Activity activity) {

        try {

            if (activity != null) {
                InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (manager != null) {
                    manager.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0);
                }
            }
        }
        catch (Exception e){

            e.printStackTrace();


        }

    }
}
