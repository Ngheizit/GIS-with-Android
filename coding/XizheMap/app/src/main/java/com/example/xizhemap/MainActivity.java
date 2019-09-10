package com.example.xizhemap;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // license with a license key - 许可应用程序
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud2630151591,none,PM0RJAY3FP20463EM070");


        LinearLayout ll_Entrance = findViewById(R.id.LinearLayout_Main);
        ll_Entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Hello Xizhe Map!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DirActivity.class);
                startActivity(intent);
            }
        });

    }
}
