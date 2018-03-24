package com.example.optimus.tezattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


    Button buttonGetAttendance;
    ArrayList<String> arrayListSubjects;
    ArrayList<Subject> arrayListAttendLectures,arrayListAllLectures;
    ArrayAdapter<String> arrayAdapterSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        textViewStudentRoll = findViewById(R.id.textViewStudentRoll);
        listViewAttendance = findViewById(R.id.listViewAttendance);

        final Users users = getIntent().getExtras().getParcelable("Users");

        arrayListSubjects = new ArrayList<>();
        arrayListAttendLectures = new ArrayList<>();
        arrayListAllLectures = new ArrayList<>();


        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        subjectRef = FirebaseDatabase.getInstance().getReference();
        buttonGetAttendance = findViewById(R.id.buttonGetAttendance);

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
                    arrayListAttendLectures.add(subject);


                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        subjectRef.child(users.className).child("Subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot ds: dataSnapshot.getChildren()){
                    Subject subject = ds.getValue(Subject.class);
                    arrayListAllLectures.add(subject);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonGetAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAttendance();

            }
        });


    }

    private void calculateAttendance() {
        for(int i=0;i<arrayListAttendLectures.size();i++){
            Double currentAttendance = Double.parseDouble(arrayListAttendLectures.get(i).lectures) * 100
                                        /Double.parseDouble(arrayListAllLectures.get(i).lectures);

            arrayListSubjects.add(arrayListAllLectures.get(i).subjectName + ": " +
                                        Double.toString(currentAttendance));
        }
        arrayAdapterSubjects = new ArrayAdapter<String>(StudentProfile.this, android.R.layout.simple_expandable_list_item_1, arrayListSubjects);
        listViewAttendance.setAdapter(arrayAdapterSubjects);
    }


}
