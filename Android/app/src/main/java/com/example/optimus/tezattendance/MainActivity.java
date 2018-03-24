package com.example.optimus.tezattendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    ArrayList<Users> arrayListUsers;
    DatabaseReference databaseReference;

    public Spinner spinnerType;
    public EditText editTextIDD, editTextPasswordReg, editTextClassNameReg, editTextType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextIDD = findViewById(R.id.editTextIDD);
        editTextClassNameReg = findViewById(R.id.editTextClassNameReg);
        editTextPasswordReg = findViewById(R.id.editTextPasswordReg);
        spinnerType = findViewById(R.id.spinnerType);



        databaseReference = FirebaseDatabase.getInstance().getReference();
        if(firebaseAuth.getCurrentUser()!= null){

            progressDialog.setMessage("Please Wait...");
            progressDialog.show();

            //profile activity
            //finish();
            //startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

            databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for ( DataSnapshot ds : dataSnapshot.getChildren()){
                        Users users = ds.getValue(Users.class);
                        //arrayListUsers.add(users);
                        if (users.fireUID.equals( FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            progressDialog.dismiss();
                            switch (users.type){

                                case "Parent":
                                    startActivity(new Intent(getApplicationContext(),ParentActivity.class));
                                    finish();
                                    break;

                                case "Student": startActivity(new Intent(getApplicationContext(),StudentProfile.class)
                                .putExtra("Users", users));

                                 break;
                                case "Teacher":
                                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                                    finish();
                            }
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewLogin);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

    }

    private void registerUser(){
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
        progressDialog.setMessage("Creating your account...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //this method is executed when the reistration is done successfully
                if(task.isSuccessful()){
                    // actually open LoginActivity
                    //for now we use TOAST only

                    Toast.makeText(MainActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser userProfile = FirebaseAuth.getInstance().getCurrentUser();
                    if (userProfile != null) {
                        String uid = userProfile.getUid();
                        Users user = new Users(spinnerType.getSelectedItem().toString(),
                                                editTextIDD.getText().toString(),
                                                editTextPassword.getText().toString(),
                                                editTextClassNameReg.getText().toString(),
                                                userProfile.getUid());
                        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("users");
                        userDatabase.push().setValue(user);
                    }





                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));



                    Toast.makeText(MainActivity.this,"Account created successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Could not register OR Already registered", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            registerUser();
        }
        if(view == textViewSignin){
            Log.d("Auth", "CLicked Text Login");
            //open LoginActivity
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}
