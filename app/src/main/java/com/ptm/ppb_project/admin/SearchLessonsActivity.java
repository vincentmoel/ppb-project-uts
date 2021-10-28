package com.ptm.ppb_project.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ptm.ppb_project.R;

public class SearchLessonsActivity extends AppCompatActivity {

    TextInputLayout tiSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lessons);

        // Hooks
        tiSearch = findViewById(R.id.ti_searchLessons);

        tiSearch.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Toast.makeText(getApplicationContext(), tiSearch.getEditText().getText(), Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });


    }
}