package technician.ifb.com.ifptecnician.barcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import technician.ifb.com.ifptecnician.R;

public class SerialNoScan extends AppCompatActivity implements SerialNoAnalyser.BarcodeResponse{


    private final Executor executor = Executors.newSingleThreadExecutor();
    private final int REQUEST_CODE_PERMISSIONS = 1002;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private boolean flashEnabled = false;
    PreviewView mPreviewView;
    ImageView ivFlashControl;
    SerialNoAnalyser analyzer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_no_scan);  toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Serialno Scanner");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreviewView = findViewById(R.id.previewView);
        ivFlashControl=findViewById(R.id.ivFlashControl);

        analyzer=new SerialNoAnalyser(this,this);

        if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {


        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();



        // ImageAnalysis.Analyzer analyzer = ImageAnalyzer(Scan());



        imageAnalysis.setAnalyzer(executor, analyzer);

        new SerialNoAnalyser(SerialNoScan.this, this);



        // ImageCapture.Builder builder = new ImageCapture.Builder();

        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
        // HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        // Query if extension is available (optional).
//        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
//            // Enable the extension if available.
//            hdrImageCaptureExtender.enableExtension(cameraSelector);
//        }

//        final ImageCapture imageCapture = builder
//                .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
//                .build();

        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());

        // startCamera();

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,  imageAnalysis, preview);

        if (camera.getCameraInfo().hasFlashUnit()) {
            ivFlashControl.setVisibility(View.VISIBLE);

            ivFlashControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (flashEnabled){

                        flashEnabled=false;
                        camera.getCameraControl().enableTorch(false);
                        ivFlashControl.setImageResource(R.drawable.ic_baseline_flashlight_off_24);

                    }
                    else{
                        flashEnabled=true;
                        camera.getCameraControl().enableTorch(true);
                        ivFlashControl.setImageResource(R.drawable.ic_baseline_flashlight_on_24);
                    }


                }
            });
//            binding.ivFlashControl.setOnClickListener {
//                camera.cameraControl.enableTorch(!flashEnabled)

            //camera.getCameraInfo().getTorchState().observe(new BarcodeCamera()){

        }

//            camera.cameraInfo.torchState.observe(this) {
//                it?.let { torchState ->
//                    if (torchState == TorchState.ON) {
//                        flashEnabled = true
//                        binding.ivFlashControl.setImageResource(R.drawable.ic_round_flash_on)
//                    } else {
//                        flashEnabled = false
//                        binding.ivFlashControl.setImageResource(R.drawable.ic_round_flash_off)
//                    }
//                }
//            }
        //   }

    }



    public String getBatchDirectoryName() {

        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {

        }

        return app_folder_path;
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    @Override
    public void brcoderesult(String output) {


        // startCamera();
        System.out.println("output-->"+output);

        Intent intent=new Intent();
        intent.putExtra("barcode", output);
        setResult(RESULT_OK, intent);
        finish();

    }
}