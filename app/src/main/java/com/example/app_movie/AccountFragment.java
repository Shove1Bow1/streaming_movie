package com.example.app_movie;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.preference.PreferenceFragmentCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class AccountFragment extends PreferenceFragmentCompat{
    Button btnSignOut,btnEditInfo;
    TextView tvUserName,tvUserAge,tvUserSex;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore;
    SwipeRefreshLayout pullToRefreshInfo;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pullToRefreshInfo=view.findViewById(R.id.pullToRefreshInfo);
        btnSignOut=view.findViewById(R.id.btnSignOut);
        tvUserSex=view.findViewById(R.id.tvUserSex);
        tvUserAge=view.findViewById(R.id.tvUserAge);
        tvUserName=view.findViewById(R.id.tvUserName);
        btnEditInfo=view.findViewById(R.id.btnEditInfo);
        firebaseFirestore=FirebaseFirestore.getInstance();
        fetchUserData();
        pullToRefreshInfo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchUserData();
                pullToRefreshInfo.setRefreshing(false);
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity(), UserLoginActivity.class));
                getActivity().finish();
            }
        });
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),EditInforActivity.class);
                intent.putExtra("UserName",tvUserName.getText().toString());
                intent.putExtra("UserAge",tvUserAge.getText().toString());
                intent.putExtra("UserSex",tvUserSex.getText().toString());
                intent.putExtra("UserId",auth.getCurrentUser().getUid());
                startActivity(intent);
            }
        });
        getChildFragmentManager().beginTransaction().add(R.id.frameSetting,new SettingFragment()).commit();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
    private void fetchUserData(){
        String a= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference=firebaseFirestore.collection("User").document(a);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot= task.getResult();
                    tvUserAge.setText(documentSnapshot.getString("age"));
                    tvUserName.setText(documentSnapshot.getString("name"));
                    tvUserSex.setText(documentSnapshot.getString("sex"));
                }
            }
        });
    }
}