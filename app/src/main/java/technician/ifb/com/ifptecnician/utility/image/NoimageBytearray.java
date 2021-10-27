package technician.ifb.com.ifptecnician.utility.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import technician.ifb.com.ifptecnician.R;

public class NoimageBytearray {

    public byte[] getbytearray(Context context){

        byte[] byteArray=null;
        try{

            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }catch (Exception e){
            e.printStackTrace();

        }

        return byteArray;
    }

}
