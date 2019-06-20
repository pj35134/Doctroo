package com.skincancer.doctro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

public class Login_fragment extends Fragment {
EditText email,password;
    Button login,register;
    FirebaseAuth firebaseAuth;
    SharedPreferences preferences;
    TextView forgotpassword;
    CheckBox rememberme;
    String userId;
    FirebaseUser user;
    ProgressDialog progressDialog;

    int i =1;
    FirebaseAuth.AuthStateListener mAuthListener;
    ProgressBar progressBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
       preferences = getActivity().getSharedPreferences("Userinfo", MODE_PRIVATE);
        user = firebaseAuth.getCurrentUser();
       /* if (preferences.getBoolean("rememberme", false)) {
            startActivity(new Intent(getActivity(), Navdrawer.class));
            getActivity().finish();
        }*/

       /* if(user!=null){
            getActivity().finish();
            startActivity(new Intent(getActivity(),Navdrawer.class));

        }*/
   /*    mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), Navdrawer.class));



                }
            }
        };*/

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,null);
        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);
        forgotpassword = view.findViewById(R.id.forgotpassword);
        rememberme = view.findViewById(R.id.rememberme);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressBar = view.findViewById(R.id.progressBar);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //preferences = this.getActivity().getSharedPreferences("Userinfo",0);

       
        checkConnection();
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),forgotpassword.class));
            }
        });
       /* final ProgressBar progressBar = new ProgressBar(this);
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });*/



        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = email.getText().toString().trim();
                String getPassword = password.getText().toString().trim();
                if(isConnected()) {
                    if (getEmail.isEmpty() || getPassword.isEmpty()) {
                        Toasty.error(getActivity(), "Fill in the details", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.setMessage("Loading.... Please Wait !!!");
                        progressDialog.show();

                        firebaseAuth.signInWithEmailAndPassword(getEmail, getPassword)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            if (rememberme.isChecked()) {
                                                preferences.edit().putBoolean("rememberme", true).apply();
                                            }

                                            progressDialog.dismiss();
                                            // Sign in success, update UI with the signed-in user's information
                                            startActivity(new Intent(getActivity(),Navdrawer.class));
                                        } else {
                                            progressDialog.dismiss();
                                            Toasty.error(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();

                                        }

                                        // ...
                                    }
                                });
                       /* if (rememberme.isChecked()) {
                            preferences.edit().putBoolean("rememberme", true).apply();

                        }*/
                    }
                }
                else{

                    checkConnection();
                }



            }
        });
                    return view;
    }




    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!= null && networkInfo.isConnected();


    }


    public void checkConnection(){

        if(!isConnected()){

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

    }
 /*   @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }*/
    }


