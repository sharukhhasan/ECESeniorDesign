package com.hrl.bluetoothlowenergy.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hrl.bluetoothlowenergy.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageCaptureActivity extends AppCompatActivity {
    private static final String TAG = "ImageCaptureActivity";

    private File imageFile;

    private ImageView mImageView;

    private String imagePath = "/storage/emulated/0/Download";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);

        imageFile = Environment.getDataDirectory().getAbsoluteFile();

        File downloadDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

        Log.d(TAG, downloadDir.getAbsolutePath());

        mImageView = (ImageView) findViewById(R.id.capturedImage);

        getFilesFromDir(downloadDir);

        loadImageFromStorage(imagePath);

    }

    public void getFilesFromDir(File filesFromSD) {

        File listAllFiles[] = filesFromSD.listFiles();

        if (listAllFiles != null && listAllFiles.length > 0) {
            for (File currentFile : listAllFiles) {
                if (currentFile.isDirectory()) {
                    getFilesFromDir(currentFile);
                } else {
                    if (currentFile.getName().endsWith("")) {
                        // File absolute path
                        Log.e("File path", currentFile.getAbsolutePath());
                        // File Name
                        Log.e("File path", currentFile.getName());

                    }
                }
            }
        }
    }

    private void loadImageFromStorage(String path) {
        try {
            File f = new File(path, "piImage.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            mImageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
