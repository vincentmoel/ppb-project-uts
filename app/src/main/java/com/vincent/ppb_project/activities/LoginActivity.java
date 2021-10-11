package com.vincent.ppb_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.vincent.ppb_project.R;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener {

    private MaterialButton btnLogin;
    private MaterialButton btnRegisterAtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hooks
        btnLogin = findViewById(R.id.btn_login);
        btnRegisterAtLogin = findViewById(R.id.btn_sudah_punya_akun);

        btnLogin.setOnClickListener(this);
        btnRegisterAtLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();
        if (btnId == R.id.btn_login) {

        }

        if (btnId == R.id.btn_sudah_punya_akun) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}