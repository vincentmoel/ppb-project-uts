package com.vincent.ppb_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.vincent.ppb_project.R;

public class SplashActivity extends AppCompatActivity {

    ImageView ivLogo;
    Animation logoAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hooks
        ivLogo = findViewById(R.id.iv_logo_splash);
        logoAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
        ivLogo.setAnimation(logoAnim);

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), FirstAuthActivity.class);
                startActivity(intent);
                finish();
            }
        };

        handler.postDelayed(runnable, 4000);

    }
}