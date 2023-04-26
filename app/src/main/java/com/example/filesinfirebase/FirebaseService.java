package com.example.filesinfirebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseService {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference root;

    public FirebaseService() {
        root = storage.getReference();
    }
    public void saveImage(byte[] img){
        Log.i("firebase123", "save image called");
        StorageReference imgRef = root.child("myimage.jpg");
        UploadTask task = imgRef.putBytes(img);  // putStream(s) for larger files
        task.addOnSuccessListener(taskSnapshot -> {
            Log.i("firebase123", "OK uploading " + taskSnapshot.getBytesTransferred());
        });
        task.addOnFailureListener(exception->{
            Log.i("firebase123", "error uploading " + exception);
        });
    }

    public void downloadImage(String name, ImageView imageView){
        StorageReference imgRef = root.child(name);
        imgRef.getBytes(10000000)
                .addOnSuccessListener(bytes -> {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    // how to return the bitmap to the caller
                    imageView.setImageBitmap(bm);
                });
    }
}
