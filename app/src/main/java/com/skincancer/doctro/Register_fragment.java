package com.skincancer.doctro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class Register_fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    EditText username, email, password,age;
    Button register,login;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    RadioGroup gender;
    ProgressDialog progressDialog;
    String getUsername,getEmail,getPassword;
    String userAge;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    ImageView profile_img;
    RadioButton checkedBtn;
    FirebaseStorage firebaseStorage;
    Uri imagePath;
    Bitmap bitmap;
    String genderValue;
    StorageReference storageReference;

    UserProfile userProfile = new UserProfile();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_register,null);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        progressDialog = new ProgressDialog(getActivity());
        setupUIViews(view);


        List age = new ArrayList<Integer>();
        for (int i = 1; i <= 100; i++) {
            age.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, age);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);
        //StorageReference myref1 = storageReference.child(firebaseAuth.getUid());
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),101);
            }
        });

        ref= firebaseDatabase.getReference("Users");



        firebaseAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validate()) {
                    //upload database
                    getUsername = username.getText().toString().trim();
                    getEmail = email.getText().toString().trim();
                    getPassword = password.getText().toString().trim();
                    checkedBtn = view.findViewById(gender.getCheckedRadioButtonId());
                    genderValue = checkedBtn.getText().toString();


                    /* progressBar.setVisibility(View.VISIBLE);*/
                    progressDialog.setMessage("Loading...Please Wait !!!");
                    progressDialog.show();


                    firebaseAuth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                /*progressBar.setVisibility(View.GONE);*/
                                Toasty.success(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // UserProfile userProfile = new UserProfile(getUsername,getEmail);

                                        ArrayList<UserProfile> list = new ArrayList<>();
                                        userProfile.setUserName(getUsername);
                                        userProfile.setUserEmail(getEmail);
                                        userProfile.setAge(userAge);
                                        userProfile.setGender(genderValue);

                                        ref.child(firebaseAuth.getUid()).setValue(userProfile);
                                        list.add(userProfile);


                                          /*  StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profilepic");
                                            UploadTask uploadTask = imageReference.putFile(imagePath);
                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Register.this, "upload failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Toast.makeText(Register.this, "upload successful", Toast.LENGTH_SHORT).show();
                                                }
                                            });*/


                                        // Toast.makeText(Register.this, "successfully upload", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getActivity(), databaseError.getCode(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                                //
                                if (imagePath != null) {
                                    StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profilepic");
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

                                /*finishAffinity();*/
                                //startActivity(new Intent(getActivity(),MainActivity.class));

                            } else {
                                progressDialog.dismiss();

                                Toasty.error(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();


                            }

                        }
                    });


                }


                //


            }
        });


       /* login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });*/
        return view;
    }

    public void setupUIViews(View view){

        username=view.findViewById(R.id.username);
        email=view.findViewById(R.id.email);
        password=view.findViewById(R.id.password);
        register=view.findViewById(R.id.register);
        //login = view.findViewById(R.id.login);

        progressBar = view.findViewById(R.id.progressBar);
        profile_img = view.findViewById(R.id.profile_img);
        gender = view.findViewById(R.id.gender);
        age = view.findViewById(R.id.age);




    }
    private boolean validate(){

        Boolean result=false;
        String getUsername = username.getText().toString();
        String getEmail = email.getText().toString();
        String getPassword = password.getText().toString();
        /*String getAge = age.getText().toString();*/

        if(getUsername.isEmpty() || (getEmail.isEmpty() || getPassword.isEmpty())){
            Toast.makeText(getActivity(), "Enter all the details", Toast.LENGTH_SHORT).show();

        }
        else{

            if(Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()){

               firebaseAuth.fetchSignInMethodsForEmail(getEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                   @Override
                   public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    boolean check =! task.getResult().getSignInMethods().isEmpty();
                    if(!check){

                    }
                    else{

                        Toasty.error(getActivity(),"Email already exists",Toast.LENGTH_SHORT).show();
                        Toasty.info(getActivity(),"Sign in",Toast.LENGTH_SHORT).show();
                    }


                   }
               });







        return true;

            }
            else{
                Toasty.info(getActivity(),"Invalid Email",Toast.LENGTH_SHORT).show();
            }

            if(getPassword.length()>=6){
                return true;
            }


            else{
                Toasty.error(getActivity(), "Password length should be greater than 6", Toast.LENGTH_SHORT).show();
            }
        }
        return result;

    }
    private void sendUserData(){
        // FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        // UserProfile userProfile = new UserProfile(getUsername,getEmail);
        // myRef.setValue(userProfile);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == RESULT_OK && data.getData()!=null){
            imagePath = data.getData();
            try {
                bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imagePath);
                profile_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



            userAge = String.valueOf(parent.getItemIdAtPosition(position+1));
        //userAge = (int) parent.getItemIdAtPosition(position+1);
           // Toast.makeText(parent.getContext(), "" + userAge, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

