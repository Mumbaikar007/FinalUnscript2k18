package com.example.optimus.tezattendance;


import android.content.Intent;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class TeacherAddNews extends AppCompatActivity {
     EditText newsName , newsType ,newsDesc;
     private String newsdesc,newsname,newstype;
    private ImageButton newsImage;
    private Button addNews;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference,newsRef;
    private static final int Gallery_Pick= 1;
    private StorageReference newsImagesRef;
    private Uri Imageuri;
    private String downloadUrl,current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_news);
        newsDesc = findViewById(R.id.newsDesc);
        newsName = findViewById(R.id.newsName);
        newsType = findViewById(R.id.newsType);
        addNews =  findViewById(R.id.addNews);

        firebaseAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        newsRef = FirebaseDatabase.getInstance().getReference().child("news");
        newsImagesRef = FirebaseStorage.getInstance().getReference();
        newsImage = findViewById(R.id.newsImage);
        current_user = firebaseAuth.getCurrentUser().getUid().toString();

        if(firebaseAuth.getCurrentUser() == null){
            //user is not logged in
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        newsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
        addNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }
        });

    }

        private void ValidatePostInfo() {
            newsdesc = newsDesc.getText().toString();
            newsname = newsName.getText().toString();
            newstype = newsType.getText().toString();

            if(Imageuri==null){
                Toast.makeText(this, "Please Select One Image...", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(newsdesc)){
                Toast.makeText(this, "Add description to the image", Toast.LENGTH_SHORT).show();
            }      else{

                StoringImageToFirebaseStorage();
            }
        }






    private void StoringImageToFirebaseStorage() {
        StorageReference filePath = newsImagesRef.child("NewsImages").child(Imageuri.getLastPathSegment()+"newsfrcrce.jpg");
        filePath.putFile(Imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    downloadUrl = task.getResult().getDownloadUrl().toString();

                    Toast.makeText(TeacherAddNews.this, "Image Uploaded successfully", Toast.LENGTH_SHORT).show();
                    SavingPostInformationToDatabase();
                }
                else{
                    String message = task.getException().getMessage();
                    Toast.makeText(TeacherAddNews.this, "Error :"+ message, Toast.LENGTH_SHORT).show( );
                }
            }
        });

    }

    private void SavingPostInformationToDatabase() {

        HashMap newsMap = new HashMap();
        newsMap.put("description",newsdesc);
        newsMap.put("name",newsname);
        newsMap.put("type",newstype);
        newsMap.put("newsImage",downloadUrl);
        newsRef.child(current_user+"newsxyz").updateChildren(newsMap)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(TeacherAddNews.this, "Post Update Successfully..", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(TeacherAddNews.this, "Error in Uploading post try again..", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void OpenGallery() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,Gallery_Pick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_Pick && resultCode==RESULT_OK){
            Imageuri = data.getData();
            newsImage.setImageURI(Imageuri);
        }
    }

}
