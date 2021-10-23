package com.vincent.ppb_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vincent.ppb_project.R;

public class FirstAuthActivity extends AppCompatActivity implements View.OnClickListener {

    View parentLayout;
    TextInputLayout tiNoHp;
    MaterialButton btnNext;
    String noHp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

        // Hooks
        tiNoHp = findViewById(R.id.ti_noHp_firstAuth);
        btnNext = findViewById(R.id.btn_next_firstAuth);
        parentLayout = findViewById(android.R.id.content);


        // On Click
        btnNext.setOnClickListener(this);
    }

    private boolean validateNoHp() {

        if (tiNoHp.getEditText() != null) {
            String noHpAwal = tiNoHp.getEditText().getText().toString().trim();
            if (noHpAwal.isEmpty()) {
                tiNoHp.setError("Nomor Handphone tidak boleh kosong");
                return false;
            } else {
                tiNoHp.setError(null);
                String firstDigit = String.valueOf(noHpAwal.charAt(0));
                if (firstDigit.equals("0")) {
                    noHp = "+62" + noHpAwal.substring(1);
                } else {
                    noHp = "+62" + noHpAwal;
                }
                return true;
            }
        } else {
            return false;
        }

    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();
        if (btnId == R.id.btn_next_firstAuth) {
            if (validateNoHp()) {
                Intent intent = new Intent(this, VerifyOtpActivity.class);
                intent.putExtra("noHp", noHp);
                intent.putExtra("from", "firstAuth");
                startActivity(intent);
                finish();
            }
        }
    }
}