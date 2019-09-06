package com.example.pc_08.myapplication;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.PublicKey;

public class MainActivity extends AppCompatActivity {


    private LinearLayout body;

    private EditText et_username;
    private EditText et_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 设置背景图片透明度 0 - 255 */
        body = (LinearLayout)findViewById(R.id.body);
        body.getBackground().setAlpha(100);


        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_passwold);
        Button btn_signIn = (Button)findViewById(R.id.btn_signin);
        Button btn_jumpOver = (Button)findViewById(R.id.btn_jump);

        // 点击登录按钮事件
        btn_signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String name = et_username.getText().toString();
                final String password = et_password.getText().toString();

                if(name.equals("ngheizit")&&password.equals("041499")){
                    Intent intent = new Intent(getApplicationContext(), DirActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "t o o  b a d ~", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 点击跳过按钮事件
        btn_jumpOver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), DirActivity.class);
                startActivity(intent);
            }
        });


    }
}
