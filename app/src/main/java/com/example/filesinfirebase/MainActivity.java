package com.example.filesinfirebase;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> launchGalleryForResult;
    private ActivityResultLauncher<Intent> launchCameraForResult;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        createGalleryLauncher();
        createCameraLauncher();
    }
    private void createCameraLauncher() {
        launchCameraForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        // capture image data...
                        Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                        imageView.setImageBitmap(bitmap);
                    }
                }
        );
    }
    private void createGalleryLauncher() { // back at 9.45
        launchGalleryForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                      Intent intent = result.getData();
                      imageView.setImageURI(intent.getData());
                    }
                }
        );
    }
    public void cameraBtnPressed(View view){
        Log.i("imageupload", "clicked camera");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launchCameraForResult.launch(intent);
    }

    public void galleryBtnPressed(View view){
        Log.i("imageupload", "clicked gallery");
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        launchGalleryForResult.launch(intent);
    }
}