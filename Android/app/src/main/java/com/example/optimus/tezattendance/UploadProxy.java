package com.example.optimus.tezattendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UploadProxy extends AppCompatActivity {

    Button buttonCheckAttendance, buttonUpdateAttendance;
    EditText editTextClassName, editTextRollNumber, editTextSubject,
                editTextUpdateAttendance;
    TextView textViewCurrrent;

    String className, rollNumber, subjectName, newAttendance;

    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_proxy);

        buttonCheckAttendance = findViewById(R.id.buttonCheckAttendanceUpload);
        buttonUpdateAttendance = findViewById(R.id.buttonUpdateAttendance);

        editTextClassName = findViewById(R.id.editTextClassNameUpload);
        editTextRollNumber = findViewById(R.id.editTextRollNumberUpload);
        editTextSubject = findViewById(R.id.editTextSubjectNameUpload);
        editTextUpdateAttendance = findViewById(R.id.editTextNewAttendanceUpload);

        textViewCurrrent = findViewById(R.id.textViewCurrentAttendanceUpload);


        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() == null){
            //user is not logged in
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }


        buttonCheckAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                className = editTextClassName.getText().toString();
                rollNumber = editTextRollNumber.getText().toString();
                subjectName = editTextSubject.getText().toString();

                QueryClassStudentAttendance(className, rollNumber, subjectName);

            }
        });


    }


    void QueryClassStudentAttendance(String classNamee, String rollNumberr, String subjectNamee) {

        databaseReference.child(classNamee).child("Students")
                .child(rollNumberr).child("Subjects")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            Subject subject = ds.getValue(Subject.class);
                            //arrayListUID.add(ds.getKey());
                            //arrayListSubjects.add(subject);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
