package com.vincent.ppb_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vincent.ppb_project.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}