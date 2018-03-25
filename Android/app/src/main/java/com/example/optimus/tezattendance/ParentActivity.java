package com.example.optimus.tezattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ParentActivity extends AppCompatActivity {

    LinearLayout linearLayoutChat, linearLayoutCheckAttendance,
                    linearLayoutResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        linearLayoutChat = findViewById(R.id.linearLayoutChatter);
        linearLayoutCheckAttendance = findViewById(R.id.linearLayoutAttendanceParent);
        linearLayoutResults = findViewById( R.id.linearLayoutNewsParent);


        linearLayoutResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        linearLayoutChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.adarsh.uchat");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
        });

        linearLayoutCheckAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
