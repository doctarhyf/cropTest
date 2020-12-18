package com.example.croptest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.tbruyelle.rxpermissions3.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private String TAG = "TAG";
    private String perms[] = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private String sourcePath;
    private String destPath;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissions();

        iv = findViewById(R.id.iv);
        sourcePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/crop.jpg";
        destPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath() + "/crop.jpg";


        iv.setImageURI(Uri.fromFile(new File(sourcePath)));

        Log.e(TAG, "dbg -> sourcePath : " + sourcePath );
        Log.e(TAG, "dbg -> destPath : " + destPath );




    }



    private void askPermissions() {
        new RxPermissions(this)
                .request(perms)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M

                        if (granted) {
                            Log.e(TAG, "onCreate: perms granted");
                        }

                    } else {
                        Log.e(TAG, "onCreate: perms refused ");
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            iv.setImageURI(resultUri);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public void onCrop(View view) {

        Uri sourceUri = Uri.fromFile(new File(sourcePath));
        Uri destinationUri = Uri.fromFile(new File(destPath));
        int maxWidth = 420;
        int maxHeight = 420;
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(16, 9)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(this);

    }
}