package com.example.app_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddUserInfo extends AppCompatActivity {
    EditText name,age,sex;
    FirebaseFirestore db;
    Button click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_info);
        name=findViewById(R.id.name);
        age=findViewById(R.id.age);
        sex=findViewById(R.id.sex);
        db=FirebaseFirestore.getInstance();
        click=findViewById(R.id.button_add_infor);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("age", age.getText().toString());
                user.put("name", name.getText().toString());
                user.put("sex", sex.getText().toString());
                String a= FirebaseAuth.getInstance().getCurrentUser().getUid();
// Add a new document with a generated ID
                db.collection("User").document(a).set(user);
                Intent b=new Intent(getBaseContext(),Main.class);
                startActivity(b);

            }
        });
    }
}