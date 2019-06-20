package com.skincancer.doctro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileActivity extends Fragment {
    ImageView profile_img;
    TextView username, email,age,gender;
    Button edit, changepassword;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    DatabaseReference databaseReference;

    String userId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, null);
        profile_img = view.findViewById(R.id.profile_img);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        age = view.findViewById(R.id.age);
        gender = view.findViewById(R.id.gender);
        edit = view.findViewById(R.id.edit);
        progressDialog = new ProgressDialog(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        changepassword = view.findViewById(R.id.changepasssword);
        UserProfile userProfile = new UserProfile();
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        StorageReference storageReference = firebaseStorage.getReference();
        progressDialog.setMessage("Loading");
        progressDialog.show();

        storageReference.child(firebaseAuth.getUid()).child("Images/Profilepic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profile_img);
            }
        });
        userId = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        // DatabaseReference databaseReference = firebaseDatabase.getReference().child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           /* UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
            username.setText("name"+userProfile.getUserName());
            email.setText("email"+userProfile.getUserEmail());*/


                if (dataSnapshot.exists()) {
                    String userName = dataSnapshot.child("userName").getValue().toString();
                    String userEmail = dataSnapshot.child("userEmail").getValue().toString();
                    String userAge =dataSnapshot.child("age").getValue().toString();
                    String userGender=dataSnapshot.child("gender").getValue().toString();

                    username.setText("name" + userName);
                    email.setText("email" + userEmail);
                    age.setText("age"+userAge);
                    gender.setText("gender"+userGender);



                }
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Toast.makeText(ProfileActivity.this,databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateProfile.class));

            }
        });

       /* changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Changepassword.class));
            }
        });*/
    return view;
    }




}
