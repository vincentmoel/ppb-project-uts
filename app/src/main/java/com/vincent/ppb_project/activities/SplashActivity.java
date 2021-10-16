package com.vincent.ppb_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vincent.ppb_project.R;
import com.vincent.ppb_project.session.SessionManager;

public class SplashActivity extends AppCompatActivity {

    ImageView ivLogo;
    Animation logoAnim;
    FirebaseAuth mAuth;
    SessionManager rememberMeSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hooks
        ivLogo = findViewById(R.id.iv_logo_splash);
        logoAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
        ivLogo.setAnimation(logoAnim);

        // Set Firebase
        mAuth = FirebaseAuth.getInstance();

        // Set Session
        rememberMeSession = new SessionManager(this, SessionManager.REMEMBERME_SESSION);

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isFirstTimeUser()) {
                    Intent intent = new Intent(getBaseContext(), FirstAuthActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Jika Remember Me
                    if (rememberMeSession.isRememberedMe()) {
                        Intent intent = new Intent(getBaseContext(), DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

            }
        };

        handler.postDelayed(runnable, 4000);

    }

    private boolean isFirstTimeUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            return false;
        } else {
            return true;
        }

    }
}