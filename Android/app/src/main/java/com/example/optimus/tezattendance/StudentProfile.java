package com.example.optimus.tezattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentProfile extends AppCompatActivity {

    private TextView textViewStudentRoll;
    private ListView listViewAttendance;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,subjectRef;

    ArrayList<String> arrayListSubjects;
    ArrayList<String> arrayListLectures;
    ArrayAdapter<String> arrayAdapterSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        textViewStudentRoll = findViewById(R.id.textViewStudentRoll);
        listViewAttendance = findViewById(R.id.listViewAttendance);

        Users users = getIntent().getExtras().getParcelable("Users");

        arrayListSubjects = new ArrayList<>();
        arrayListLectures = new ArrayList<>();


        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        subjectRef = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() == null){
            //user is not logged in
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        databaseReference.child(users.className).child("Students").child(users.idd)
                .child("Subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot ds: dataSnapshot.getChildren()){
                    Subject subject = ds.getValue(Subject.class);
                    arrayListSubjects.add(subject.subjectName);
                    arrayListLectures.add(subject.lectures);

                }

                arrayAdapterSubjects = new ArrayAdapter<String>(StudentProfile.this, android.R.layout.simple_expandable_list_item_1, arrayListSubjects);
                listViewAttendance.setAdapter(arrayAdapterSubjects);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
