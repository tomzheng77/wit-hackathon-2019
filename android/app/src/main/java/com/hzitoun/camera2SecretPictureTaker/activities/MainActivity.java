package com.hzitoun.camera2SecretPictureTaker.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.hzitoun.camera2SecretPictureTaker.R;
import com.hzitoun.camera2SecretPictureTaker.listeners.PictureCapturingListener;
import com.hzitoun.camera2SecretPictureTaker.services.APictureCapturingService;
import com.hzitoun.camera2SecretPictureTaker.services.PictureCapturingServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


/**
 * App's Main Activity showing a simple usage of the picture taking service.
 * @author hzitoun (zitoun.hamed@gmail.com)
 */
public class MainActivity extends AppCompatActivity implements PictureCapturingListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String[] requiredPermissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
    };
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;

    private ImageView uploadBackPhoto;
    
     //The capture service          
    private APictureCapturingService pictureService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        uploadBackPhoto = findViewById(R.id.backIV);
        // getting instance of the Service from PictureCapturingServiceImpl
        pictureService = PictureCapturingServiceImpl.getInstance(this);

        Handler handler = new Handler(getMainLooper());
        final Runnable r = new Runnable() {
            public void run() {
                pictureService.startCapturing(MainActivity.this);
                handler.postDelayed(this, 200);
            }
        };
        handler.postDelayed(r, 200);
    }
    
    private void showToast(final String text) {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show()
        );
    }

    /**
    * Displaying the pictures taken.
    */             
    @Override
    public void onCaptureDone(String pictureUrl, byte[] pictureData) {
        if (pictureData != null && pictureUrl != null) {
            runOnUiThread(() -> {
                final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);

                // rotate the bitmap by 90 degrees
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                // scale the bitmap to fit width
                final int nh = (int) (rotatedBitmap.getHeight() * (512.0 / rotatedBitmap.getWidth()));
                final Bitmap scaled = Bitmap.createScaledBitmap(rotatedBitmap, 512, nh, true);
                uploadBackPhoto.setImageBitmap(scaled);



            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CODE: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();
                }
            }
        }
    }

    /**
     * checking  permissions at Runtime.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        final List<String> neededPermissions = new ArrayList<>();
        for (final String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }
        if (!neededPermissions.isEmpty()) {
            requestPermissions(neededPermissions.toArray(new String[]{}),
                    MY_PERMISSIONS_REQUEST_ACCESS_CODE);
        }
    }
}

