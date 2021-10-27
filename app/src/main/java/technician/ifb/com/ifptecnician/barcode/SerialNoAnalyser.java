package technician.ifb.com.ifptecnician.barcode;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class SerialNoAnalyser implements ImageAnalysis.Analyzer{

    Context context;
    public BarcodeResponse brcoderesult = null;

    public SerialNoAnalyser(Context context, BarcodeResponse brcoderesult){
        this.context=context;
        this.brcoderesult=brcoderesult;

    }

    @ExperimentalGetImage
    @Override
    public void analyze(ImageProxy imageProxy) {

        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            // Pass image to an ML Kit Vision API
            // ...

            System.out.println("image--> "+image);

            BarcodeScanner scanner = BarcodeScanning.getClient();


            Task<List<Barcode>> result = scanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {

                            System.out.println("barcode--> "+barcodes);

                            if (barcodes.size() == 0)
                            {
                                imageProxy.close();
                            }

                            for (Barcode barcode: barcodes) {
                                Rect bounds = barcode.getBoundingBox();
                                Point[] corners = barcode.getCornerPoints();


                                int valueType = barcode.getValueType();

                                String rawValue = barcodes.get(0).getRawValue();

                                if (rawValue.length()==18){

                                    brcoderesult.brcoderesult(rawValue);

                                }
                                else {
                                 //   Toast.makeText(context,rawValue , Toast.LENGTH_SHORT).show();
                                    imageProxy.close();
                                }


                            }
                            // [END get_barcodes]
                            // [END_EXCLUDE]

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {



                            e.printStackTrace();
                            // Task failed with an exception
                            // ...
                            imageProxy.close();
                        }
                    });


            // [END run_



        }
    }


    public interface BarcodeResponse {
        void brcoderesult(String output);
    }
}
