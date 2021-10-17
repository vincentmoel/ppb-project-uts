package com.vincent.ppb_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.vincent.ppb_project.R;
import com.vincent.ppb_project.model.UserModel;
import com.vincent.ppb_project.session.SessionManager;

import java.util.concurrent.TimeUnit;

public class VerifyOtpActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvNoHp;
    ProgressBar progressBar;
    PinView pinview;
    MaterialButton btnVerify;
    FirebaseAuth mAuth;
    String noHp, fromWhere, codeBySystem;
    UserModel dataUser;
    FirebaseFirestore firestoreRoot;
    SessionManager loginSession, rememberMeSession;
    boolean isRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        // Hooks
        tvNoHp = findViewById(R.id.tv_noHp_verify);
        progressBar = findViewById(R.id.pb_verify);
        pinview = findViewById(R.id.pinview_verify);
        btnVerify = findViewById(R.id.btn_verify);

        // Set Firebase
        mAuth = FirebaseAuth.getInstance();
        firestoreRoot = FirebaseFirestore.getInstance();

        // Set Session
        loginSession = new SessionManager(this, SessionManager.LOGIN_SESSION);
        rememberMeSession = new SessionManager(this, SessionManager.REMEMBERME_SESSION);

        // Get Intent
        fromWhere = getIntent().getStringExtra("from");
        // Jika dari First Auth
        if (fromWhere.equals("firstAuth")) {
            noHp = getIntent().getStringExtra("noHp");
        }
        // Jika dari Register
        else if (fromWhere.equals("register")) {
            dataUser = getIntent().getParcelableExtra("dataUser");
            noHp = dataUser.getNoHp();
        }
        // Jika dari Login
        else if (fromWhere.equals("login")) {
            dataUser = getIntent().getParcelableExtra("dataUser");
            noHp = dataUser.getNoHp();
            isRememberMe = getIntent().getBooleanExtra("isRememberMe", false);
        }
        // Jika dari Frogot Password
        else if (fromWhere.equals("forgotPassword")) {
            noHp = getIntent().getStringExtra("noHp");
        }

        // On Click
        btnVerify.setOnClickListener(this);

        tvNoHp.setText(noHp);

        sendOTPToUser(noHp);
    }

    private void sendOTPToUser(String noHp) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(noHp)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationFailed(FirebaseException e) {

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            codeBySystem = verificationId;
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String smsCode = phoneAuthCredential.getSmsCode();
            if (smsCode != null) {
                pinview.setText(smsCode);
                verifyCode(smsCode);
            }
        }
    };

    private void verifyCode(String smsCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, smsCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // Jika dari First Auth
                        if (fromWhere.equals("firstAuth")) {
                            Toast.makeText(this, "Authentication Complete!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        // Jika dari register
                        else if (fromWhere.equals("register")) {
                            saveDataUserToDB();
                        }
                        // Jika dari login
                        else if (fromWhere.equals("login")) {
                            Toast.makeText(this, "Berhasil login!", Toast.LENGTH_SHORT).show();
                            loginSession.createLoginSession(dataUser);
                            if (isRememberMe) {
                                rememberMeSession.createRememberMeSession();
                            }
                            Intent intent = new Intent(this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        // Jika dari Forgot Password
                        else if (fromWhere.equals("forgotPassword")) {
                            Intent intent = new Intent(this, NewPasswordActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveDataUserToDB() {

        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            firestoreRoot.document("users/" + uid).set(dataUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getBaseContext(), "Register Complete!", Toast.LENGTH_SHORT).show();
                            loginSession.createLoginSession(dataUser);
                            Intent intent = new Intent(getBaseContext(), DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Register Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();

        if (btnId == R.id.btn_verify) {

            if (pinview.getText() != null) {
                if (!pinview.getText().toString().isEmpty()) {
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        Toast.makeText(this, "Mohon tunggu sebentar", Toast.LENGTH_SHORT).show();
                    } else {
                        String manualCode = pinview.getText().toString();
                        verifyCode(manualCode);
                    }

                } else  {
                    Toast.makeText(this, "Kode OTP tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}