package com.example.optimus.tezattendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    ArrayList<Users> arrayListUsers;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);

        if(firebaseAuth.getCurrentUser()!= null){
            //profile activity
            finish();
            //startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }
        arrayListUsers = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        editTextPassword= (EditText) findViewById(R.id.editTextPassword);
        buttonSignin= (Button) findViewById(R.id.buttonSignin);
        textViewSignup= (TextView) findViewById(R.id.textViewSignup);

        buttonSignin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);


    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            // email is empty
            Toast.makeText(this,"Please enter Email Address", Toast.LENGTH_SHORT).show();
            return; //stop function from executing further
        }
        if(TextUtils.isEmpty(password)){
            // password is empty
            Toast.makeText(this,"Please enter Password", Toast.LENGTH_SHORT).show();
            return; //stop function from executing further
        }

        //if proper then register :
        progressDialog.setMessage("Logging you in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()){


                    databaseReference = FirebaseDatabase.getInstance().getReference();

                    databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for ( DataSnapshot ds : dataSnapshot.getChildren()){
                                Users users = ds.getValue(Users.class);
                                arrayListUsers.add(users);
                            }

                            for ( Users users : arrayListUsers){
                                if (users.fireUID.equals( FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                    switch (users.type){
                                        case "Parent": startActivity(new Intent(getApplicationContext(),ParentActivity.class));
                                            break;
                                        case "Student": startActivity(new Intent(getApplicationContext(),StudentProfile.class));
                                            break;
                                        case "Teacher": startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    //startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignin){
            userLogin();
        }
        if(view == textViewSignup){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
