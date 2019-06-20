package com.skincancer.doctro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class changepassword_fragments extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    EditText newpassword,conformpassword;
    Button changepassword;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_changepassword,null);
        newpassword = view.findViewById(R.id.new_password);
        conformpassword = view.findViewById(R.id.conform_password);
        changepassword = view.findViewById(R.id.changepasssword);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getNewPassword = newpassword.getText().toString();
                String getConformPassword = conformpassword.getText().toString();
                if(getNewPassword.isEmpty() || getConformPassword.isEmpty()){
                    Toasty.error(getActivity(),"Empty Password",Toasty.LENGTH_SHORT).show();
                }
                else {
                    if (getNewPassword.equals(getConformPassword)) {
                        user.updatePassword(getNewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    Toast.makeText(getActivity(), "Password Changed", Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                } else {

                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                    else{
                        Toasty.error(getActivity(),"Password doesn't match",Toasty.LENGTH_SHORT).show();
                    }
                }






            }
        });
        return view;
    }
}
