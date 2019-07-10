package com.skincancer.doctro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Base64;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Mainfragment extends Fragment {

    ImageView users_skinpic;
    Button select_image,process;
    Uri imagePath;
    Bitmap bitmap;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;
    DatabaseReference ref;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference,next,filepath;
    File file;
    Uri fileUri,pictureUri;
    final int RC_TAKE_PHOTO = 1;
    String imgString;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, null);
        users_skinpic = view.findViewById(R.id.users_skinpic);
        select_image = view.findViewById(R.id.select_image);
        process = view.findViewById(R.id.process);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        next = FirebaseStorage.getInstance().getReference();

        ref = firebaseDatabase.getReference("Users");


        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Toast.makeText(getActivity(), "nmcc", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 101);*/
                Intent intent = CropImage.activity()
                        .getIntent(getContext());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
/*
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        imageView.setImageURI(resultUri);
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
                    }
                }*/
                // startActivityForResult(Intent.createChooser(intent,"Select Image"),101);
            }
        });

      /*  capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 102);


            }
        });*/


        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tag", "after1");
                    if (imagePath != null) {
                        if(GeneralUtils.isNetworkAvailable(getActivity())) {
                            progressDialog.setMessage("Loading .... Please Wait");
                            progressDialog.show();
                            Log.i("tag", "aftee");
                            // ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                            // for (Uri uri : mArrayUri) {
                            // irebaseStorage.getInstance().getReference().child("Profiles/"+System.currentTimeMillis());
                            StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images/" + System.currentTimeMillis());
                            UploadTask uploadTask = imageReference.putFile(imagePath);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "upload failed", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getActivity(), "upload successful", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });

                        }
                        // }
                         else{
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Internet Connection Alert")
                                    .setMessage("Please check your Internet Connection")
                                    .setPositiveButton("close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();

                        }


                        //startActivity(new Intent(getActivity(),l.class));
                    } else {
                        Log.i("tag", "hbjcdccccc");
                        Toasty.error(getActivity(), "No image selected", Toasty.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }//



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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        imagePath = result.getUri();
                        users_skinpic.setImageURI(imagePath);
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                        Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                    }
                }
    }
    /**/

/*
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
            else if(requestCode ==102 && resultCode == RESULT_OK){
                *//*imagePath = data.getData();
                filepath = storageReference.child("naya").child(imagePath.getLastPathSegment());
                filepath.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                    }
                });*//*
                bitmap = (Bitmap) data.getExtras().get("data");
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 101);



               // users_skinpic.setImageBitmap(bitmap);
                //StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("naya");



//                imagePath = getRealPathFromURI(getContext(),bitmap);
             *//*   users_skinpic.setImageURI(imagePath);
                users_skinpic.setAdjustViewBounds(true);
                Log.i("tag", "after");*//*

               *//* users_skinpic.buildDrawingCache();
                Bitmap bmap = users_skinpic.getDrawingCache();
                String encodedImageData =getEncoded64ImageStringFromBitmap(bmap);
                ref.child(firebaseAuth.getUid()).setValue(encodedImageData);*//*





            }






        }*/

        /**/
/*
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
         imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }*/

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


