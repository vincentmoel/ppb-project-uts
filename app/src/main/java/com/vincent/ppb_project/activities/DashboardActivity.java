package com.vincent.ppb_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.auth.User;
import com.vincent.ppb_project.R;
import com.vincent.ppb_project.model.UserModel;
import com.vincent.ppb_project.session.SessionManager;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    SessionManager loginSession, rememberMeSession;
    MaterialButton btnLogout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView ivMenu;
    RelativeLayout layoutMatematika, layoutFisika, layoutBiologi, layoutKimia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Hooks
        btnLogout = findViewById(R.id.btn_logout);
        drawerLayout = findViewById(R.id.layout_drawer);
        navigationView = findViewById(R.id.navigation_view);
        ivMenu = findViewById(R.id.btn_menu_dashboard);
        layoutMatematika = findViewById(R.id.layout_matematika);
        layoutFisika = findViewById(R.id.layout_fisika);
        layoutBiologi = findViewById(R.id.layout_biologi);
        layoutKimia = findViewById(R.id.layout_kimia);

        // Session
        loginSession = new SessionManager(this, SessionManager.LOGIN_SESSION);
        rememberMeSession = new SessionManager(this, SessionManager.REMEMBERME_SESSION);

        UserModel dataUser = loginSession.getLoginSessionData();
        Toast.makeText(this, dataUser.getNoHp(), Toast.LENGTH_SHORT).show();

        // Set Navigation Drawer
        setNavDrawer();

        // On Click
        btnLogout.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        layoutMatematika.setOnClickListener(this);
        layoutFisika.setOnClickListener(this);
        layoutBiologi.setOnClickListener(this);
        layoutKimia.setOnClickListener(this);
    }

    private void setNavDrawer() {
        navigationView.bringToFront();
    }

    private void goToPelajaran(String from) {
        Intent intent = new Intent(this, PelajaranActivity.class);
        intent.putExtra("from", from);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();

        if (btnId == R.id.btn_logout) {
            rememberMeSession.clearRememberMeSession();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        if (btnId == R.id.btn_menu_dashboard) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        if (btnId == R.id.layout_matematika) {
            goToPelajaran("Matematika");
        }

        if (btnId == R.id.layout_fisika) {
            goToPelajaran("Fisika");
        }

        if (btnId == R.id.layout_biologi) {
            goToPelajaran("Biologi");
        }

        if (btnId == R.id.layout_kimia) {
            goToPelajaran("Kimia");
        }
    }
}