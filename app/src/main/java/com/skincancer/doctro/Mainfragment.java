package com.skincancer.doctro;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class Mainfragment extends Fragment {

    ImageView users_skinpic;
    Button select_image, capture_image,process;
    Uri imagePath;
    Bitmap bitmap;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference,next;
    File file;
    Uri fileUri;
    final int RC_TAKE_PHOTO = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, null);
        users_skinpic = view.findViewById(R.id.users_skinpic);
        select_image = view.findViewById(R.id.select_image);
        capture_image = view.findViewById(R.id.capture_image);
        process = view.findViewById(R.id.process);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        next=FirebaseStorage.getInstance().getReference();





        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "nmcc", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 101);
                // startActivityForResult(Intent.createChooser(intent,"Select Image"),101);
            }
        });

        capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 102);




            }
                                         });


process.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.i("tag", "after1");
        if (imagePath != null) {
            Log.i("tag","aftee");
           /* ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
            for (Uri uri : mArrayUri) {*/
           // irebaseStorage.getInstance().getReference().child("Profiles/"+System.currentTimeMillis());
                StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images/"+System.currentTimeMillis());
                UploadTask uploadTask = imageReference.putFile(imagePath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "upload failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      // Toast.makeText(getActivity(), "upload successful", Toast.LENGTH_SHORT).show();
                    }
                });



        }
        else{
            Log.i("tag","hbjcdccccc");

        }
    }
});


        return view;

    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);*/
      /*  if (requestCode == 101 && resultCode == RESULT_OK) {

            bitmap = (Bitmap) data.getExtras().get("data");
            users_skinpic.setImageBitmap(bitmap);

        }*/
/*
        if(requestCode == 101 && resultCode == RESULT_OK && data.getData()!=null){
            imagePath = data.getData();
            try {
                bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imagePath);
                users_skinpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }



        }

    }*/


        @Override
        public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){

            super.onActivityResult(requestCode, resultCode, data);
            Log.i("response", "before");
            if (requestCode == 101 && resultCode == RESULT_OK && data.getData() != null) {
                imagePath = data.getData();
                Log.i("response", "after");
                try {
                    bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagePath);
                    users_skinpic.setImageBitmap(bitmap);
                    users_skinpic.setAdjustViewBounds(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            else if(requestCode ==RC_TAKE_PHOTO && resultCode == RESULT_OK){

                bitmap = (Bitmap) data.getExtras().get("data");
                users_skinpic.setImageBitmap(bitmap);
//                imagePath = getRealPathFromURI(getContext(),bitmap);
                users_skinpic.setImageURI(imagePath);
                users_skinpic.setAdjustViewBounds(true);
                Log.i("tag", "after");


            }















        }




        }
/*
    public Uri getRealPathFromURI(Context context,Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage,
                "Title", null);
        Cursor cursor = context.getContentResolver().query(Uri.parse(path), null, null,
                null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return  Uri.fromFile(new File(cursor.getString(idx)));
    }*/


