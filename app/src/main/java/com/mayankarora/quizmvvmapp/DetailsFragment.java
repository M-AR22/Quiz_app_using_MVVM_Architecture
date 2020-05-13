package com.mayankarora.quizmvvmapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class DetailsFragment extends Fragment implements View.OnClickListener {

    private QuizListViewModel quizListViewModel;
    private NavController navController;
    int position;
    private ImageView image;
    private TextView difficulty,totalnoques,title,desc,lastscore;
    private Button startQuiz;
    String quizid,quizname;
    long totalQuestionsToAnswer=0;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public DetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        position =DetailsFragmentArgs.fromBundle(getArguments()).getPosition();
        Log.v("position",Integer.toString(position));
        Log.d("APP_LOG","position :"+position);
        image=view.findViewById(R.id.details_image);
        difficulty=view.findViewById(R.id.details_difficulty_text);
        title=view.findViewById(R.id.details_title);
        desc=view.findViewById(R.id.details_desc);
        totalnoques=view.findViewById(R.id.details_questions_text);
        startQuiz=view.findViewById(R.id.details_start_btn);
        startQuiz.setOnClickListener(this);
        navController= Navigation.findNavController(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quizListViewModel=new ViewModelProvider(getActivity()).get(QuizListViewModel.class);
        quizListViewModel.getQuizListModelData().observe(getViewLifecycleOwner(), new Observer<List<QuizListModel>>() {
            @Override
            public void onChanged(List<QuizListModel> quizListModels) {
                title.setText(quizListModels.get(position).getName());
                difficulty.setText(quizListModels.get(position).getLevel());
                desc.setText(quizListModels.get(position).getDesc());
                totalnoques.setText(quizListModels.get(position).getQuestions()+"");


                quizid=quizListModels.get(position).getQuiz_id();
                Log.v("quizid",quizid);
                quizname=quizListModels.get(position).getName();

                Glide.with(getContext())
                        .load(quizListModels.get(position).getImage())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_image)
                        .into(image);

                totalQuestionsToAnswer=quizListModels.get(position).getQuestions();
                Log.v("totalquestoanswer",Long.toString(totalQuestionsToAnswer));
            }
        });

        //loadResultsData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.details_start_btn:

                DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action =DetailsFragmentDirections.actionDetailsFragmentToQuizFragment();

                action.setQuizid(quizid);
                action.setTotalQuestions(totalQuestionsToAnswer);
                action.setQuizName(quizname);
                Log.v("details",quizid+" "+totalQuestionsToAnswer+" "+quizname);
                navController.navigate(R.id.action_detailsFragment_to_quizFragment);
                break;
        }
    }

    private void loadResultsData() {
        firebaseFirestore.collection("QuizList")
                .document(quizid).collection("Results")
                .document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null && document.exists()){
                        //Get Result
                        Long correct = document.getLong("correct");
                        Long wrong = document.getLong("wrong");
                        Long missed = document.getLong("unanswered");

                        //Calculate Progress
                        Long total = correct + wrong + missed;
                        Long percent = (correct*100)/total;

                        lastscore.setText(percent + "%");
                    } else {
                        //Document Doesn't Exist, and result should stay N/A
                    }
                }
            }
        });
    }
}
