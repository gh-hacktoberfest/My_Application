package com.example.myapplication.module2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.model.QuestionModel;
import com.example.myapplication.model.QuizListModel;
import com.example.myapplication.viewmodel.QuestionViewModel;

import java.util.HashMap;
import java.util.List;

public class CauDoActivity extends AppCompatActivity {

    QuizListModel quizListModel;

    LinearLayout wrapCaudo;
    Toolbar toolbar;
    TextView timerCountTv, typeQuestion, questionNumber, question, dapan1, dapan2, dapan3, ansFeedbackTv;

    Button nextBtn, resultBtn;

    int count = 1;
    private boolean canAnswer = false;
    private long timer;
    private CountDownTimer countDownTimer;
    private int notAnswerd = 0;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private String answer = "";

    private QuestionViewModel questionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cau_do);
        getSupportActionBar().hide();
        questionViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
//        questionViewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory
//                .getInstance(getApplication())).get(QuestionViewModel.class);

        count = 1;

        wrapCaudo = findViewById(R.id.wrapCauDo);
        wrapCaudo.setVisibility(View.INVISIBLE);

        toolbar = findViewById(R.id.cauDoBack);
        typeQuestion = findViewById(R.id.textViewTitleCaudo);
        questionNumber = findViewById(R.id.questionNumber);
        timerCountTv = findViewById(R.id.quizQuestionsCount);
        question = findViewById(R.id.question);
        dapan1 = findViewById(R.id.dapan1);
        dapan2 = findViewById(R.id.dapan2);
        dapan3 = findViewById(R.id.dapan3);
        nextBtn = findViewById(R.id.nextBtn);
        resultBtn = findViewById(R.id.resultBtn);
        ansFeedbackTv = findViewById(R.id.ansFeedbackTv);

        Intent i = getIntent();
        quizListModel = (QuizListModel) i.getSerializableExtra("quiz");
        typeQuestion.setText(quizListModel.getTitle());

        questionViewModel.setQuizId(quizListModel.getQuizId());
        questionViewModel.getQuestions();

        dapan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAnswer(dapan1);
            }
        });

        dapan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAnswer(dapan2);
            }
        });

        dapan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyAnswer(dapan3);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count ++;
                loadQuestions(count);
                resetOptions();
            }
        });

        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               submitResults();
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DetailQuizActivity.class);
                i.putExtra("quiz", quizListModel);
                startActivity(i);
            }
        });

        ansFeedbackTv.setVisibility(View.INVISIBLE);
        loadData();
    }

    private void loadData(){
        enableOptions();
        loadQuestions(1);
    }

    private void enableOptions(){
        dapan1.setVisibility(View.VISIBLE);
        dapan2.setVisibility(View.VISIBLE);
        dapan3.setVisibility(View.VISIBLE);

        //enable buttons , hide feedback tv , hide nextQuiz btn

        nextBtn.setVisibility(View.GONE);
        resultBtn.setVisibility(View.GONE);
    }

    private void loadQuestions(int i){

        count = i;
        questionViewModel.getQuestionMutableLiveData().observe(this, new Observer<List<QuestionModel>>() {
            @Override
            public void onChanged(List<QuestionModel> questionModels) {
                wrapCaudo.setVisibility(View.VISIBLE);
                questionNumber.setText("Question " + String.valueOf(count) );
                question.setText(questionModels.get(i - 1).getQuestion());
                dapan1.setText(questionModels.get(i - 1).getOption_a());
                dapan2.setText(questionModels.get(i - 1).getOption_b());
                dapan3.setText(questionModels.get(i - 1).getOption_c());
                timer = questionModels.get(i-1).getTimer();
                answer = questionModels.get(i-1).getAnswer();

                //todo set current que no, to que number tv
                startTimer();
            }
        });

        canAnswer = true;
    }

    private void startTimer(){
        timerCountTv.setText(String.valueOf(timer));

        countDownTimer = new CountDownTimer(timer * 1000 , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // update time
                timerCountTv.setText(millisUntilFinished / 1000 + "");

                Long percent = millisUntilFinished/(timer*10);
            }

            @Override
            public void onFinish() {
                canAnswer = false;
                ansFeedbackTv.setVisibility(View.VISIBLE);
                ansFeedbackTv.setText("Times Up !! No answer selected");
                notAnswerd ++;
                showNextBtn();
            }
        }.start();
    }

    private void showNextBtn() {
        if (count == Integer.valueOf(quizListModel.getQuestions())){
            nextBtn.setVisibility(View.GONE);
            resultBtn.setVisibility(View.VISIBLE);
        }else{
            nextBtn.setVisibility(View.VISIBLE);
            resultBtn.setVisibility(View.GONE);
        }
    }

    private void resetOptions(){
        ansFeedbackTv.setVisibility(View.INVISIBLE);
        nextBtn.setVisibility(View.GONE);
        resultBtn.setVisibility(View.GONE);
        dapan1.setBackground(ContextCompat.getDrawable(getApplicationContext() , R.color.light_sky));
        dapan2.setBackground(ContextCompat.getDrawable(getApplicationContext() , R.color.light_sky));
        dapan3.setBackground(ContextCompat.getDrawable(getApplicationContext() , R.color.light_sky));
    }

    private void submitResults() {
        HashMap<String , Object> resultMap = new HashMap<>();
        resultMap.put("correct" , correctAnswer);
        resultMap.put("wrong" , wrongAnswer);
        resultMap.put("notAnswered" , notAnswerd);

        questionViewModel.addResults(resultMap);

        Intent i = new Intent(getApplicationContext(), ResultActivity.class);
        i.putExtra("quizId", quizListModel.getQuizId());
        startActivity(i);

//        QuizragmentDirections.ActionQuizragmentToResultFragment action =
//                QuizragmentDirections.actionQuizragmentToResultFragment();
//        action.setQuizId(quizId);
//        navController.navigate(action);

    }

    private void verifyAnswer(TextView button){
        ansFeedbackTv.setVisibility(View.VISIBLE);
        if (canAnswer){
            if (answer.equals(button.getText())){
                button.setBackground(ContextCompat.getDrawable(getApplicationContext() , R.color.green));
                correctAnswer++;
                ansFeedbackTv.setText("Correct Answer");
            }else{
                button.setBackground(ContextCompat.getDrawable(getApplicationContext() , R.color.red));
                wrongAnswer++;
                ansFeedbackTv.setText("Wrong Answer \nCorrect Answer :" + answer);
            }
        }
        canAnswer=false;
        countDownTimer.cancel();
        showNextBtn();
    }
}