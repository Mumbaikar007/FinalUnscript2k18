package com.example.optimus.tezattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AttendanceMiddleware extends AppCompatActivity {

    EditText editTextClassName, editTextSubjectName;
    Button buttonStartScanning, buttonAddAttendance;

    String key, className, subjectName;
    Subject subjectToUpdate;

    ArrayList<String> arrayListUID;
    ArrayList<Subject> arrayListSubjects;

    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_middleware);

        editTextClassName = findViewById(R.id.editTextClassNameScan);
        editTextSubjectName = findViewById(R.id.editTextSubjectNameScan);

        buttonStartScanning = findViewById(R.id.buttonStartScanning);
        buttonAddAttendance = findViewById(R.id.buttonAddAttendance);

        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() == null){
            //user is not logged in
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        arrayListUID = new ArrayList<>();
        arrayListSubjects = new ArrayList<>();
        //arrayListRollNumber = new ArrayList<>();
        //arrayListRollNumberKeys = new ArrayList<>();


        /*
        databaseReference.child("TECMPMB").child("Subjects")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.getChildren()) {

                            Subject subject = ds.getValue(Subject.class);
                            arrayListUID.add(ds.getKey());
                            arrayListSubjects.add(subject);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
           */

        buttonAddAttendance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                className = editTextClassName.getText().toString();
                subjectName =  editTextSubjectName.getText().toString();

                QueryClassName(className);

            }
        });



        buttonStartScanning.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {

                //editTextClassName.setText("Size: " + arrayListUID.size());



                for ( int i = 0; i < arrayListSubjects.size(); i ++){

                    subjectToUpdate = arrayListSubjects.get(i);
                    if ( subjectToUpdate.subjectName.equals(subjectName)){
                        key = arrayListUID.get(i);
                        break;
                    }
                }


                subjectToUpdate.lectures =  Integer.toString( Integer.parseInt(subjectToUpdate.lectures) + 1);

                databaseReference.child(editTextClassName.getText().toString())
                        .child("Subjects").child(key).setValue(subjectToUpdate);



                Intent in=new Intent(AttendanceMiddleware.this,TakeAttendance.class);
                in.putExtra("ClassName", className);
                in.putExtra("SubjectName",subjectName);
                //in.putExtra("Key", key);
                //in.putExtra("SubjectToUpdate", subjectToUpdate);
                startActivity(in);

            }
        });


    }


    void QueryClassName(String classNamee){

        databaseReference.child(classNamee).child("Subjects")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    Subject subject = ds.getValue(Subject.class);
                    arrayListUID.add(ds.getKey());
                    arrayListSubjects.add(subject);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
