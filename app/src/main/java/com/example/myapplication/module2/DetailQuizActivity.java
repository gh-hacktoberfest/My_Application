package com.example.myapplication.module2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.myapplication.HomeFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.QuizListModel;

public class DetailQuizActivity extends AppCompatActivity {

    QuizListModel quizListModel;
    ImageView imageView;
    TextView textViewName;
    TextView textViewRank;
    TextView textViewTotal;

    Button startQuizBtn;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_quiz);

        getSupportActionBar().hide();

        Intent i = getIntent();
        quizListModel = (QuizListModel) i.getSerializableExtra("quiz");

        imageView = findViewById(R.id.detailFragmentImage);
        textViewName = findViewById(R.id.detailFragmentTitle);
        textViewRank = findViewById(R.id.detailFragmentDifficulty);
        textViewTotal = findViewById(R.id.detailFragmentQuestions);
        startQuizBtn = findViewById(R.id.startQuizBtn);

        toolbar = findViewById(R.id.detailBack);

        if(quizListModel != null && quizListModel.getImage() != null) {
            Glide.with(imageView).load(quizListModel.getImage()).into(imageView);
        }
        if(quizListModel != null) {
            textViewName.setText(quizListModel.getTitle());
            textViewRank.setText(quizListModel.getDifficulty());
            textViewTotal.setText(quizListModel.getQuestions());
        }

        startQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CauDoActivity.class);
                i.putExtra("quiz", quizListModel);
                startActivity(i);
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();

//                Intent i = new Intent(getApplicationContext(), mainfragment.class);
//                startActivity(i);
            }
        });

    }

    public void back() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        HomeFragment homeFragment = new HomeFragment();
//        fragmentTransaction.add(R.id.frame_layout, homeFragment);
//        fragmentTransaction.hide(homeFragment);
//        fragmentTransaction.add(R.id.frame_layout, new mainfragment());
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}