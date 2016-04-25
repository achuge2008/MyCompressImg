package com.uniquedu.mycompressimg;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 0x23;
    private static final int RESULT_CAMERA_IMAGE = 0x24;
    private Button mButtonZip;
    private Button mButtonGalley;
    private Button mButtonCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonCamera = (Button) findViewById(R.id.button_camera);
        mButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动系统摄像头
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, RESULT_CAMERA_IMAGE);
            }
        });
        mButtonZip = (Button) findViewById(R.id.button_zip);
        mButtonZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUtils.zipImage(Environment.getExternalStorageDirectory() + "/aa.jpg", Environment.getExternalStorageDirectory() + "/bb.jpg");
            }
        });
        mButtonGalley = (Button) findViewById(R.id.button_galley);
        mButtonGalley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        //判断有无该权限
        checkRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE, 0x23);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE) {
            Uri selectedImage = data.getData();
            String picturePath = getImgPath(selectedImage);
            Log.d("MainActivity", "相册中取得的地址：" + picturePath);
        } else if (requestCode == RESULT_CAMERA_IMAGE) {
            Uri selectedImage = data.getData();
            String picturePath = getImgPath(selectedImage);
            Log.d("MainActivity", "相机中取得的地址：" + picturePath);
        }
    }

    private String getImgPath(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    private void checkRequest(String permission, int code) {
        if (checkPermission(permission)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0x23:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("mainactivity", "权限通过");
                }
                break;
        }
    }

    private boolean checkPermission(String permission) {
        boolean isGranted = ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED;
        return isGranted;
    }
}
