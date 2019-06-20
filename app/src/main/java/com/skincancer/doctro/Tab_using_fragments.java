package com.skincancer.doctro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Tab_using_fragments extends AppCompatActivity {
TextView tab1,tab2;
ViewPager pager;
FirebaseAuth firebaseAuth;

    SharedPreferences preferences;
String userId;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_using_fragments);
        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        pager = findViewById(R.id.container);
        firebaseAuth = FirebaseAuth.getInstance();
       FirebaseUser user = firebaseAuth.getCurrentUser();

        //userId=user.getUid();
        tab1.setBackgroundResource(R.drawable.line_bg);
        preferences=getSharedPreferences("Userinfo", MODE_PRIVATE);

        if (preferences.getBoolean("rememberme", false)) {
            startActivity(new Intent(Tab_using_fragments.this, Navdrawer.class));
            finish();
        }
        /*preferences = getSharedPreferences("Userinfo",0);
        if(preferences.getBoolean("rememberme",false)){

                startActivity(new Intent(Tab_using_fragments.this, Navdrawer.class));
                finish();

        }*/
      /*  mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!= null){

                        startActivity(new Intent(Tab_using_fragments.this, Navdrawer.class));
                        finish();


                }
            }
        };*/
       /* if (user != null) {
            startActivity(new Intent(Tab_using_fragments.this, Navdrawer.class));
            finish();
        }*/ else {

            pager.setAdapter(new ViewpagerAdapter(getSupportFragmentManager()));
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int position) {
                    tab1.setBackgroundColor(Color.WHITE);
                    tab2.setBackgroundColor(Color.WHITE);
                    if (position == 0) {
                        tab1.setBackgroundResource(R.drawable.line_bg);
                    } else if (position == 1) {
                        tab2.setBackgroundResource(R.drawable.line_bg);


                    }
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
    }
  /*  @Override
    protected void onStart() {
        super.onStart();
        //firebaseAuth.addAuthStateListener(mAuthListener);
        if(userId!=null){
            // startActivity(new Intent(Tab_using_fragments.this,Navdrawer.class));
            Intent intent = new Intent(Tab_using_fragments.this,Navdrawer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            finish();

        }
    }*/

        public void tabClick (View view){
            tab1.setBackgroundColor(Color.WHITE);
            tab2.setBackgroundColor(Color.WHITE);
            view.setBackgroundResource(R.drawable.line_bg);
            int id = view.getId();
            if (id == R.id.tab1) {
                pager.setCurrentItem(0);

            } else if (id == R.id.tab2) {
                pager.setCurrentItem(1);

            }
        }


    public class ViewpagerAdapter extends FragmentPagerAdapter {

        public ViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new Login_fragment();

            } else if (position == 1) {
                return new Register_fragment();
            }

            return new Login_fragment();
        }

        @Override
        public int getCount() {
            return 2;
        }



    }
}
