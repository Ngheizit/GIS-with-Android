package com.ngheizit.myapplication;

public class Question {
    public String content;
    public String right;
    public Question(String content, String right){
        this.content = content;
        this.right = right;
    }
    public boolean check(String reply){
        return reply.equals(this.right);
    }
}
