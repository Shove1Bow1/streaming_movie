package com.example.app_movie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class OTPActivity<Intrinics> extends AppCompatActivity {
    CardView cvBtnConfirmOTP;
    Button btnConfirmOTP;
    Intent intentGetPhoneNo;
    EditText otp_1, otp_2, otp_3, otp_4, otp_5, otp_6;
    String verificationId;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        intentGetPhoneNo = getIntent();
        otp_1 = findViewById(R.id.otp_code_1);
        otp_2 = findViewById(R.id.otp_code_2);
        otp_3 = findViewById(R.id.otp_code_3);
        otp_4 = findViewById(R.id.otp_code_4);
        otp_5 = findViewById(R.id.otp_code_5);
        otp_6 = findViewById(R.id.otp_code_6);
        //cvBtnConfirmOTP=findViewById(R.id.cvBtnConfirmOTP);
        btnConfirmOTP = findViewById(R.id.btnConfirmOTP);
        checkBlockIsEmpty();
        getOTPCode();
        keyPress();
        keyPressDelete();
        btnConfirmOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBlockIsEmpty()) {
                    String otp = otp_1.getText().toString() +
                            otp_2.getText().toString() +
                            otp_3.getText().toString() +
                            otp_4.getText().toString() +
                            otp_5.getText().toString() +
                            otp_6.getText().toString();
                    Log.e("otp", otp_1.getText().toString());
                    verifyCode(otp);
                    if (auth.getCurrentUser() != null) {
                        startActivity(new Intent(OTPActivity.this, Main.class));
                        finish();
                    }
                } else {
                    Toast.makeText(OTPActivity.this, "please fill box with your otp", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Check Block is Empty or Not
    private boolean checkBlockIsEmpty() {
        if (otp_1.getText().toString().isEmpty()) {
            return false;
        }
        if (otp_2.getText().toString().isEmpty()) {
            return false;
        }
        if (otp_3.getText().toString().isEmpty()) {
            return false;
        }
        if (otp_4.getText().toString().isEmpty()) {
            return false;
        }
        if (otp_5.getText().toString().isEmpty()) {
            return false;
        }
        if (otp_6.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void getOTPCode() {
        String phoneNo = intentGetPhoneNo.getStringExtra("phone number");
        Log.d("phone", "getOTPCode: " + phoneNo);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+84" + phoneNo)       // Phone number to verify
                        .setTimeout(90L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(OTPActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPActivity.this, "Can't get OTP", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationId = s;

        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInByCredential(credential);
    }

    private void signInByCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkdocumen(firebaseAuth.getCurrentUser());
                    ;
                }
            }
        });
    }
    //fill number
    private void checkdocumen(FirebaseUser user){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Boolean check=false;
        DocumentReference docIdRef = rootRef.collection("User").document(user.getUid());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        loadUi(1);
                    } else {
                        loadUi(0);
                    }
                } else {
                }
            }
        });
    }
    void loadUi(int i){
        Intent intent;
        if (i==0){
            intent=new Intent(getBaseContext(),AddUserInfo.class);
        }
        else{
            intent =new Intent(getBaseContext(),Main.class);
        }
        startActivity(intent);
        finish();
    }
    private void keyPress(){
        otp_1.addTextChangedListener(new GenericTextWatcher(otp_1));
        otp_2.addTextChangedListener(new GenericTextWatcher(otp_2));
        otp_3.addTextChangedListener(new GenericTextWatcher(otp_3));
        otp_4.addTextChangedListener(new GenericTextWatcher(otp_4));
        otp_5.addTextChangedListener(new GenericTextWatcher(otp_5));
        otp_6.addTextChangedListener(new GenericTextWatcher(otp_6));
    }
    //delete number
    private void keyPressDelete(){
        otp_1.setOnKeyListener(new GenericKeyEvent(otp_1,null));
        otp_2.setOnKeyListener(new GenericKeyEvent(otp_2,otp_1));
        otp_3.setOnKeyListener(new GenericKeyEvent(otp_3,otp_2));
        otp_4.setOnKeyListener(new GenericKeyEvent(otp_4,otp_3));
        otp_5.setOnKeyListener(new GenericKeyEvent(otp_5,otp_4));
        otp_6.setOnKeyListener(new GenericKeyEvent(otp_6,otp_5));
    }
    public class GenericKeyEvent implements View.OnKeyListener{
        EditText currentView,previousView;
        public GenericKeyEvent(EditText currentView,EditText previousView) {
            this.currentView=currentView;
            this.previousView=previousView;
        }

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if(keyEvent.getAction()==KeyEvent.ACTION_DOWN&&i==KeyEvent.KEYCODE_DEL&&currentView.getText().toString().isEmpty()&&currentView.getId()!=R.id.otp_code_1){
                currentView.setText(null);
                previousView.requestFocus();
                return true;
            }
            return false;
        }
    }
    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        public GenericTextWatcher(View view)
        {
            this.view = view;
        }
        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            int KEYCODE_DEL;
            KeyEvent event;
            switch(view.getId())
            {
                case R.id.otp_code_1:
                    if(text.length()==1)
                        otp_2.requestFocus();
                    break;
                case R.id.otp_code_2:
                    if(text.length()==1)
                        otp_3.requestFocus();
                    else if(text.length()==0){
                        otp_1.requestFocus();
                    }

                    break;
                case R.id.otp_code_3:
                    if(text.length()==0 ){
                        otp_2.requestFocus();
                    }

                    else if(text.length()==1)
                        otp_4.requestFocus();
                    break;
                case R.id.otp_code_4:
                    if(text.length()==0)
                    {
                        otp_3.requestFocus();
                    }
                    else
                        otp_5.requestFocus();
                    break;
                case R.id.otp_code_5:
                    if(text.length()==0)
                    {
                        otp_4.requestFocus();
                    }

                    else
                        if(text.length()==1){
                            otp_6.requestFocus();
                        }
                    break;
                case R.id.otp_code_6:
                    if(text.length()==0)
                    {
                        otp_5.requestFocus();
                    }

                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
}