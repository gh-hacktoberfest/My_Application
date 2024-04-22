package com.example.myapplication.model;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class QuizListModel implements Serializable {

    @DocumentId
    private String quizId;
    private String title,image, difficulty, questions;


    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public QuizListModel(){}

    public QuizListModel(String quizId, String title, String image, String difficulty, String questions) {
        this.quizId = quizId;
        this.title = title;
        this.image = image;
        this.difficulty = difficulty;
        this.questions = questions;
    }
}
