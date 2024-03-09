package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Utils {
    private static Utils instance;

    private FirebaseServices fbs;
    private String imageStr;

    public Utils()
    {
        fbs = FirebaseServices.getInstance();
    }

    public static Utils getInstance()
    {
        if (instance == null)
            instance = new Utils();

        return instance;
    }
    public void showMessageDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);
        //builder.setMessage(message);

        // Add a button to dismiss the dialog box
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // You can perform additional actions here if needed
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void uploadImage(Context context, Uri selectedImageUri) {
        if (selectedImageUri != null) {
            imageStr = "images/" + UUID.randomUUID() + ".jpg"; //+ selectedImageUri.getLastPathSegment();
            StorageReference imageRef = fbs.getStorage().getReference().child("images/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //selectedImageUri = uri;
                            fbs.setSelectedImageURL(uri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Utils: uploadImage: ", e.getMessage());
                        }
                    });
                    Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Please choose an image first", Toast.LENGTH_SHORT).show();
        }
    }
}
