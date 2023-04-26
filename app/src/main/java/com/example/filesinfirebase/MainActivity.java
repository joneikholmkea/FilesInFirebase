package com.example.filesinfirebase;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> launchGalleryForResult;
    private ActivityResultLauncher<Intent> launchCameraForResult;
    private ImageView imageView;
    private FirebaseService fs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        createGalleryLauncher();
        createCameraLauncher();
        fs = new FirebaseService();
        fs.downloadImage("space.jpg", imageView);
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
                        fs.saveImage(getBytes(bitmap));
                    }
                }
        );
    }
    private void createGalleryLauncher() {
        launchGalleryForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                      Intent intent = result.getData();
                      imageView.setImageURI(intent.getData());
                      Bitmap bm = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                      fs.saveImage(getBytes(bm));
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

    private byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,bs);
        return bs.toByteArray();
    }

}