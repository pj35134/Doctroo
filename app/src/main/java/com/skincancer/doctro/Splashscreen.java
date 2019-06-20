package com.skincancer.doctro;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Splashscreen extends AppCompatActivity {
private static int time = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Animation animation = AnimationUtils.loadAnimation(Splashscreen.this,R.anim.translate);
        findViewById(R.id.doctro_logo).startAnimation(animation);
        findViewById(R.id.doctro_text).startAnimation(animation);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splashscreen.this,Tab_using_fragments.class);
                startActivity(intent);
                finish();
            }
        },time);

    }


}
