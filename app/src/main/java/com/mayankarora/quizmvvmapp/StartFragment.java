package com.mayankarora.quizmvvmapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StartFragment extends Fragment {

    private ProgressBar startProgress;
    private TextView startFeedbackText;
    private FirebaseAuth mAuth;
    private NavController navController;
    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startProgress=view.findViewById(R.id.start_progress);
        startFeedbackText=view.findViewById(R.id.start_feedback);
        mAuth = FirebaseAuth.getInstance();
        navController= Navigation.findNavController(view);
        startFeedbackText.setText("Checking User Account..");
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            //create a new account
            startFeedbackText.setText("Creating account..");
            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startFeedbackText.setText("Account created!");
                        navController.navigate(R.id.action_startFragment2_to_listFragment);
                    }
                    else{
                        Log.v("Error",task.getException().toString());
                    }
                }
            });
        }
        else{
            navController.navigate(R.id.action_startFragment2_to_listFragment);
        }

    }
}
