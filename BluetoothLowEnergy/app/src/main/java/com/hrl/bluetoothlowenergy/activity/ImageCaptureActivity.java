package com.hrl.bluetoothlowenergy.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hrl.bluetoothlowenergy.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

public class ImageCaptureActivity extends AppCompatActivity {
    private static final String TAG = "ImageCaptureActivity";

    private File imageFile;

    private ImageView mImageView;

    private String imagePath = "/storage/emulated/0/Download";
    private int counter = 1;
    private String imageName;
    private File downloadDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);

        imageFile = Environment.getDataDirectory().getAbsoluteFile();

        downloadDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

        Log.d(TAG, downloadDir.getAbsolutePath());

        mImageView = (ImageView) findViewById(R.id.capturedImage);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
            }
        }, 5000);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getFilesFromDir(downloadDir);

        loadImageFromStorage(imagePath);
    }

    public void getFilesFromDir(File filesFromSD) {
        File listAllFiles[] = filesFromSD.listFiles();

        if (listAllFiles != null && listAllFiles.length > 0) {
            for (File currentFile : listAllFiles) {
                if (currentFile.isDirectory()) {
                    getFilesFromDir(currentFile);
                    Log.d(TAG, currentFile.getAbsolutePath());
                } else {
                    if (currentFile.getName().endsWith("")) {
                        Log.e("File path", currentFile.getAbsolutePath());
                        Log.e("File path", currentFile.getName());
                    }
                }
            }
        }
    }

    private void loadImageFromStorage(String path) {
        imageName = "piImage-" + counter + ".jpg";
        try {
            if(counter == 0) {
                File f = new File(path, "piImage.jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                mImageView.setImageBitmap(b);
            } else {
                File f = new File(path, imageName);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                mImageView.setImageBitmap(b);
            }
            counter++;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
