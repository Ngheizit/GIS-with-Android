package com.example.pc_08.myapplication;

/**
 * Created by PC-00 on 2019/9/8.
 */

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
