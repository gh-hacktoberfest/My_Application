package com.example.myapplication.module2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.QuizListModel;
import com.example.myapplication.viewmodel.QuestionViewModel;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {
    private QuestionViewModel viewModel;
    private TextView correctAnswer , wrongAnswer , notAnswered;
    private TextView percentTv;
    private ProgressBar scoreProgressbar;
    private String quizId;
    private Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        getSupportActionBar().hide();

        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
                .getInstance(getApplication())).get(QuestionViewModel.class);

        correctAnswer = findViewById(R.id.correctAnswerTv);
        wrongAnswer = findViewById(R.id.wrongAnswersTv);
        notAnswered = findViewById(R.id.notAnsweredTv);
        percentTv = findViewById(R.id.resultPercentageTv);
        scoreProgressbar = findViewById(R.id.resultCoutProgressBar);
        homeBtn = findViewById(R.id.home_btn);


        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        Intent i = getIntent();
        quizId = i.getStringExtra("quizId");

        viewModel.setQuizId(quizId);
        viewModel.getResults();
        viewModel.getResultMutableLiveData().observe( this, new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {

                Long correct = stringLongHashMap.get("correct");
                Long wrong = stringLongHashMap.get("wrong");
                Long noAnswer = stringLongHashMap.get("notAnswered");

                correctAnswer.setText(correct.toString());
                wrongAnswer.setText(wrong.toString());
                notAnswered.setText(noAnswer.toString());

                Long total = correct + wrong + noAnswer;
                Long percent = (correct*100)/total;

                percentTv.setText(String.valueOf(percent));
                scoreProgressbar.setProgress(percent.intValue());

            }
        });

    }
}