package com.skincancer.doctro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class Mainfragment extends Fragment {

    ImageView users_skinpic;
    Button select_image, process;
    Uri imagePath;
    Bitmap bitmap;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    ProgressDialog progressDialog;
    DatabaseReference ref;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference, next, filepath;
    File file;
String okz;
    Uri fileUri;
    String message;
    String path;
    Uri pictureUri;
    String url="http://192.168.1.69:5000";
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
                        .setMinCropResultSize(600,450)
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
                    if (GeneralUtils.isNetworkAvailable(getActivity())) {
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

                        // UploadImage();
                                AsyncTask asyncTask = new AsyncTask() {
                                    @Override
                                    protected Object doInBackground(Object[] objects) {
                                        File file = new File(path);
                                        String content_type = getMimeType(file.getPath());
                                        //  Log.i("hello",content_type);

                                        String file_path = file.getAbsolutePath();

                                        OkHttpClient client = new OkHttpClient();
                                        Log.i("hello",file_path);
                                        RequestBody file_body = RequestBody.create(MediaType.parse(content_type),file);

                                        RequestBody request_body = new MultipartBody.Builder()
                                                .setType(MultipartBody.FORM)
                                                .addFormDataPart("type",content_type)
                                                .addFormDataPart("file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                                                .build();

                                        okhttp3.Request request = new okhttp3.Request.Builder()
                                                .url("http://10.100.20.113:5000")
                                                .post(request_body)
                                                .build();


                                        try {
                                            okhttp3.Response response = client.newCall(request).execute();
                                             okz = response.body().string();
                                           // okz = "10";
                                            Log.i("helloo", okz);
                                            //return okz;


                                            if(!response.isSuccessful()){
                                                throw new IOException("Error : "+response);
                                            }



                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                return okz;
                                    }

                                    @Override
                                    protected void onPostExecute(Object o) {
                                      //  txtvw.setText(o.toString());
                                        message=o.toString();

                                        /*new AlertDialog.Builder(getContext())
                                                .setTitle("Risk factor")
                                                .setMessage(message)
                                                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();*/
                                          Intent intent = new Intent(getContext(), Mainscreen.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("naya", message);
                                intent.putExtras(bundle);
                                startActivity(intent);

                                    }
                                }.execute();

                               /*Thread t = new Thread(new Runnable() {
                                    @Override*/
                                   /* public void run() {
                                        File file  = new File(path);
                                        String content_type  = getMimeType(file.getPath());
                                        //  Log.i("hello",content_type);

                                        String file_path = file.getAbsolutePath();
                                        OkHttpClient client = new OkHttpClient();
                                        Log.i("hello",file_path);
                                        RequestBody file_body = RequestBody.create(MediaType.parse(content_type),file);

                                        RequestBody request_body = new MultipartBody.Builder()
                                                .setType(MultipartBody.FORM)
                                                .addFormDataPart("type",content_type)
                                                .addFormDataPart("file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                                                .build();

                                        okhttp3.Request request = new okhttp3.Request.Builder()
                                                .url("http://10.100.20.113:5000")
                                                .post(request_body)
                                                .build();


                                       try {
                                        okhttp3.Response response = client.newCall(request).execute();
                                        // okz = response.body().toString();
                                        okz = "10";
                                           Log.i("helloo", okz);

                                            if(!response.isSuccessful()){
                                                throw new IOException("Error : "+response);
                                            }



                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }*/
                              //  });

                               // t.start();








                                progressDialog.dismiss();
                                //String message = run();
                               /* new AlertDialog.Builder(getContext())
                                        .setTitle("Risk factor")
                                        .setMessage(message)
                                        .setPositiveButton("close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();*/




                               /* Intent intent = new Intent(getContext(), Mainscreen.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("naya", message);
                                intent.putExtras(bundle);
                                startActivity(intent);*/



                            }
                        });


                    }
                    // }
                    else {
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
                fileUri=data.getData();
               // path = getPathFromURI(getContext(),imagePath);
                path = imagePath.getPath();

                /*try {*/
                try {
                    bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imagePath);
                    // bitmap = (Bitmap) data.getExtras().get("data");
                }catch (IOException e) {
                    e.printStackTrace();
                }

              // bitmap = (Bitmap) data.getExtras().get("data");
               Log.i("tag", String.valueOf(bitmap));
                // bitmap = result.getBitmap();
               /*
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
               /* fileUri = data.getData();

                try{
                    bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);


                }
                catch(Exception ex){

                }*/

                users_skinpic.setImageURI(imagePath);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void UploadImage(){
Log.i("hello","1");

       /* Map<String, String> params = new HashMap();
        String imageData = imageTostring(bitmap);*/
        Log.i("hello","2");
        //Log.i("hello",imageData);
        // byte[] imageData = GeneralUtils.getBlob(bitmap);

      /*  params.put("",imageData);
        Log.i("hello",imageData);*/

        Log.i("hello","3");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

       // JSONObject parameters = new JSONObject(params);

           StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
               @Override
               public void onResponse(String response) {



                               Log.i("hello", response);
                   Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();

                           /* if(response.contains("success")){

                                Toast.makeText(getActivity(), "Image upload", Toast.LENGTH_SHORT).show();
                            }*/
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), ""+error.toString(), Toast.LENGTH_SHORT).show();
                                Log.i("hello", String.valueOf(error));
                            }/*try {
                            i.putExtra("MyData",response.body().string() );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        }

                        )
           {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Log.i("tag","smmm");
                                Map<String, String> params = new HashMap<>();
                               String imageData = imageTostring(bitmap);
                               Log.i("tag",imageData);
                               // byte[] imageData = GeneralUtils.getBlob(bitmap);

                                params.put("images",imageData);
                                return params;

                            }
                        };
        /*requestQueue.getCache().clear();*/
        requestQueue.add(stringRequest);

    }
  /*  private String getPath(Uri uri) {

        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + "=?", new String[]{document_id}, null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }*/

    private String imageTostring(Bitmap bitmap){
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
    byte[] imageBytes = outputStream.toByteArray();
    String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
    return encodedImage;
    }
    /**/
    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public String getPathFromURI(Context context, Uri contentUri) {



            String[] proj = { MediaStore.Images.Media.DATA };
           Cursor cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);

    }


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


    public String run() {
        File file  = new File(path);
        String content_type  = getMimeType(file.getPath());
        //  Log.i("hello",content_type);

        String file_path = file.getAbsolutePath();
        OkHttpClient client = new OkHttpClient();
        Log.i("hello",file_path);
        RequestBody file_body = RequestBody.create(MediaType.parse(content_type),file);

        RequestBody request_body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type",content_type)
                .addFormDataPart("file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("http://10.100.20.113:5000")
                .post(request_body)
                .build();


        try {
            okhttp3.Response response = client.newCall(request).execute();
            // okz = response.body().toString();
            okz = "10";
            Log.i("helloo", okz);

            if(!response.isSuccessful()){
                throw new IOException("Error : "+response);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
        return okz;
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


