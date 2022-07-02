package com.example.cardiacrecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {
    private static int SPLASH_SCREEN=5000;
//Variables
    Animation topAnim,bottomAnim;
    TextView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        logo=findViewById(R.id.dash1);
        logo.setAnimation(topAnim);
   new Handler().postDelayed(new Runnable() {
       @Override
       public void run() {
           Intent intent=new Intent(Dashboard.this,home.class);
           startActivity(intent); finish();
       }
   },SPLASH_SCREEN);
    }
}