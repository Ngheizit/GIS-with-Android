package com.example.pc_08.myapplication;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.PublicKey;



public class CallphoneActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callphone);

        EditText et_number = (EditText)findViewById(R.id.et_phonenumber);
        Button btn_callphone = (Button)findViewById(R.id.btn_callphone);
        btn_callphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_number = et_number.getText().toString().trim();
                if(TextUtils.isEmpty(str_number)){
                    Toast.makeText(getApplicationContext(), "Number isn't null", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.CALL"); // 设置拨打电话功能
                    intent.setData(Uri.parse("tel:"+str_number));
                    startActivity(intent);
                }
            }
        });

    }

}