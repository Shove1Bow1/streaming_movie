package com.example.app_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class PhoneLoginActivity extends AppCompatActivity {
    EditText ipPhoneNo,ipOTP;
    Button btnGetCode,btnSendCode;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String verificationId;
    CardView cvUserLogin_PhoneLoginActivity, cvGoogleLogin_PhoneLoginActivity, cvBtnGetCode_PhoneLoginActivity;
    GoogleSignInClient mGoogleSignInClient;
    TextView tvSignUp;
    int RC_SIGN_IN=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        ipPhoneNo = findViewById(R.id.ipPhoneNo);
        tvSignUp = findViewById(R.id.tvSignUp);
        btnGetCode = findViewById(R.id.btnGetOTP);
        cvUserLogin_PhoneLoginActivity = findViewById(R.id.cvUser_PhoneLoginActivity);
        cvGoogleLogin_PhoneLoginActivity = findViewById(R.id.cvGoogle_PhoneLoginActivity);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SignUpActivity.class));
            }
        });
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ipPhoneNo.getText().toString().length()==9)
                {
                    Toast.makeText(getBaseContext(),"Get OTP",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(PhoneLoginActivity.this,OTPActivity.class);
                    intent.putExtra("phone number",ipPhoneNo.getText().toString());
                    startActivity(intent);
                }
               else{
                   Toast.makeText(PhoneLoginActivity.this,"Please Fill Phone Number with 9 letter",Toast.LENGTH_LONG).show();
                }
            }
        });
        cvUserLogin_PhoneLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneLoginActivity.this, UserLoginActivity.class));
            }
        });

        cvGoogleLogin_PhoneLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRequest();
                signIn();
            }
        });
}

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("573880327975-ago46m5ln4s95elvb2524c4n7i1v8t2s.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void loadui(String uid) {
        Intent intent=new Intent(getBaseContext(),Main.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("check", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("check", "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("checl", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            loadui(auth.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("check", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}