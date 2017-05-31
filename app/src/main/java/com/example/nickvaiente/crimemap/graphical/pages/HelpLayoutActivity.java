package com.example.nickvaiente.crimemap.graphical.pages;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.nickvaiente.crimemap.R;

public class HelpLayoutActivity extends AppCompatActivity {

//    Retrieves the Help Page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);

        final FloatingActionButton howToBackButton = (FloatingActionButton) findViewById(R.id.howToBackButton);

        howToBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
