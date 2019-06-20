package com.skincancer.doctro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {
EditText resetemail;
Button resetpassword;
FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        resetemail = findViewById(R.id.forgotemail);
        firebaseAuth = FirebaseAuth.getInstance();
        findViewById(R.id.resetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = resetemail.getText().toString().trim();
                if(getEmail.equals("")){
                    Toast.makeText(forgotpassword.this, "Please enter email to set new password", Toast.LENGTH_SHORT).show();

                }
                else{
                    firebaseAuth.sendPasswordResetEmail(getEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(forgotpassword.this, "check your email", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(forgotpassword.this,Tab_using_fragments.class));
                                }
                                else{
                                    Toast.makeText(forgotpassword.this, "error", Toast.LENGTH_SHORT).show();


                                }
                        }
                    });

                }
            }
        });
    }
}
