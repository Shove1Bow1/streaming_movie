package com.example.app_movie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditInforActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    EditText edUserName,edUserAge,edUserSex;
    Button btnSave,btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_infor);
        edUserAge=findViewById(R.id.edUserAge);
        edUserName=findViewById(R.id.edUserName);
        edUserSex=findViewById(R.id.edUserSex);
        btnSave=findViewById(R.id.btnSave);
        btnCancel=findViewById(R.id.btnCancel);
        Intent intent=getIntent();
        edUserName.setText(intent.getStringExtra("UserName"));
        edUserSex.setText(intent.getStringExtra("UserSex"));
        edUserAge.setText(intent.getStringExtra("UserAge"));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> user = new HashMap<>();
                user.put("age", edUserAge.getText().toString());
                user.put("name", edUserName.getText().toString());
                user.put("sex", edUserSex.getText().toString());
                firebaseFirestore.collection("User").document(intent.getStringExtra("UserId")).update(user);
                Toast.makeText(EditInforActivity.this,"Save change",Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}