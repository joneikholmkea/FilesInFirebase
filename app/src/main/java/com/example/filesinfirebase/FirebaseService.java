package com.example.filesinfirebase;

import android.util.Log;

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
        StorageReference imgRef = root.child("myimage.jpg");
        UploadTask task = imgRef.putBytes(img);  // putStream(s) for larger files
        task.addOnSuccessListener(taskSnapshot -> {
            Log.i("firebase123", "OK uploading " + taskSnapshot.getBytesTransferred());
        });
        task.addOnFailureListener(exception->{
            Log.i("firebase123", "error uploading " + exception);
        });
    }
}
