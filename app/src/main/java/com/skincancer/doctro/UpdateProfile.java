package com.skincancer.doctro;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class UpdateProfile extends AppCompatActivity {
EditText username,email,age;
Button save;
FirebaseAuth firebaseAuth;
String name,gtemail;
RadioGroup gender;
RadioButton male,female;
DatabaseReference databaseReference;
String gtage;
String userName,userEmail,userAge,userGender;
RadioButton checkedBtn;
String genderValue;

String userId;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.app_name));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateProfile.this,Navdrawer.class));

            }
        });
      /*  Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfile.this,ProfileActivity.class));
            }
        });*/
      /*  setSupportActionBar(toolbar);

        //Add back navigation in the title bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        age = (EditText) findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        final UserProfile userProfile = new UserProfile();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        userId=user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        // DatabaseReference databaseReference = firebaseDatabase.getReference().child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
           /* UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
            username.setText("name"+userProfile.getUserName());
            email.setText("email"+userProfile.g etUserEmail());*/


                /*if(dataSnapshot.exists())*/
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    userName =dataSnapshot.child("userName").getValue().toString();
                    userEmail =dataSnapshot.child("userEmail").getValue().toString();
                    userAge = dataSnapshot.child("age").getValue().toString();
                    userGender =dataSnapshot.child("gender").getValue().toString();

                    username.setText(userName);
                    email.setText(userEmail);
                    age.setText(userAge);
                    if (userGender.equals("Male")) {
                        ((RadioButton) findViewById(R.id.male)).setChecked(true);
                    } else {
                        ((RadioButton) findViewById(R.id.female)).setChecked(true);

                    }
                    Log.i("tag","before");



                   /* if (info.getGender().equals("Male")) {
                        ((RadioButton) findViewById(R.id.male)).setChecked(true);
                    } else {
                        ((RadioButton) findViewById(R.id.female)).setChecked(true);

                    }*/





                }
                findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user != null) {
                            name = username.getText().toString();
                            gtemail = email.getText().toString();
                            gtage =age.getText().toString();
                            checkedBtn = findViewById(gender.getCheckedRadioButtonId());
                            genderValue = checkedBtn.getText().toString();
                            ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("userName", username.getText().toString());
                            result.put("userEmail", email.getText().toString());
                            result.put("age",age.getText().toString());
                            result.put("gender",genderValue);
                            databaseReference.updateChildren(result);

                            list.add(result);


                            Log.i("tag","after");
                            Toasty.success(UpdateProfile.this,"Profile Updated",Toasty.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateProfile.this, Navdrawer.class);
                            startActivity(intent);
                        }
                        else{
                            Toasty.error(UpdateProfile.this,"Error updating",Toasty.LENGTH_SHORT).show();
                        }
                    }

                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                 Toast.makeText(UpdateProfile.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }



        });

      //

                /*DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users").child(userId);
                userProfile.setUserName(name);
                userProfile.setUserEmail(gtemail);
                ref.setValue(userProfile);
                recreate();
                finish();*/






                /*String name = username.getText().toString();
                String gtemail = email.getText().toString();
                userProfile.setUserName(name);
                userProfile.setUserEmail(gtemail);
                ;*/


                // databaseReference.child("userName").setValue(name);
                // databaseReference.child("userEmail").setValue(gtemail);
                //finish();
                // databaseReference.updateChildren((Map<String, Object>) userProfile);
                // finish();
                       /* Map<String,Object> taskMap = new HashMap<String,Object>();
                        taskMap.put("userName", name);
                        taskMap.put("userEmail", gtemail);
                        databaseReference.updateChildren(taskMap);
                        finish();*/








    }




}
