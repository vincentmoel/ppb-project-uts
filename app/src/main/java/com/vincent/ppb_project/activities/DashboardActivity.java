package com.vincent.ppb_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.auth.User;
import com.vincent.ppb_project.R;
import com.vincent.ppb_project.model.UserModel;
import com.vincent.ppb_project.session.SessionManager;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    SessionManager loginSession, rememberMeSession;
    MaterialButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Hooks
        btnLogout = findViewById(R.id.btn_logout);

        // Session
        loginSession = new SessionManager(this, SessionManager.LOGIN_SESSION);
        rememberMeSession = new SessionManager(this, SessionManager.REMEMBERME_SESSION);

        UserModel dataUser = loginSession.getLoginSessionData();
        Toast.makeText(this, dataUser.getNoHp(), Toast.LENGTH_SHORT).show();

        // On Click
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();

        if (btnId == R.id.btn_logout) {
            rememberMeSession.clearRememberMeSession();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}