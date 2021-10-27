package technician.ifb.com.ifptecnician.alert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import technician.ifb.com.ifptecnician.R;

public class Alertwithicon {

    public void showDialog(Context context, String title, String message,boolean status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setTitle(title);

        if (status){
            builder.setIcon(R.drawable.ic_check_box_black_24dp);


        }
        else {
            builder.setIcon(R.drawable.ic_close_red_240dp);
        }

        builder.setMessage(message);

        String positiveText = context.getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        dialog.dismiss();
                    }
                });

//        String negativeText = context.getString(android.R.string.cancel);
//        builder.setNegativeButton(negativeText,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // negative button logic
//                        dialog.dismiss();
//                    }
//                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


}
