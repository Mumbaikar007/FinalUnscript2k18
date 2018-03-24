package com.example.optimus.tezattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class StudentMain extends AppCompatActivity {

    LinearLayout linearLayoutNews, linearLayoutAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        linearLayoutAttendance = findViewById(R.id.linearLayoutAttendance);
        linearLayoutNews = findViewById( R.id.linearLayoutNews);

        final Users users = getIntent().getExtras().getParcelable("Users");


        linearLayoutNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),StudentNews.class)
                        .putExtra("Users", users));
            }
        });

        linearLayoutAttendance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),StudentProfile.class)
                        .putExtra("Users", users));
            }
        });

    }
}
