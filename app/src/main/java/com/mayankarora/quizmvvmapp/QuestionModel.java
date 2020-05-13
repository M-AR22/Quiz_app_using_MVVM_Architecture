package com.mayankarora.quizmvvmapp;

import com.google.firebase.firestore.DocumentId;

public class QuestionModel {

    @DocumentId
    private String questionId;
    private String question, a,b,c, answer;
    private Long timer;

    public QuestionModel() {}

    public QuestionModel(String questionId, String question, String a, String b, String c, String answer, Long timer) {
        this.questionId = questionId;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.answer = answer;
        this.timer = timer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getTimer() {
        return timer;
    }

    public void setTimer(Long timer) {
        this.timer = timer;
    }
}
