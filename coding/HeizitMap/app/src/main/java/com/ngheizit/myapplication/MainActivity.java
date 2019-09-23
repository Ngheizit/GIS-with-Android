package com.ngheizit.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.ngheizit.myapplication.activity.TianDiTuActivity;

public class MainActivity extends AppCompatActivity {

    // 全局属性
    private SceneView pSceneView;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 许可应用程序
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud2630151591,none,PM0RJAY3FP20463EM070");


        this.pSceneView = findViewById(R.id.axSceneView);
        this.et_password = findViewById(R.id.axEditText_password);
        setupMap();

        this.et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String word = et_password.getText().toString();
                if(word.equals("爱你一万年")){
                    ToastUtil.showToast("Li Yifu");
                    Intent intent = new Intent(getApplicationContext(), TianDiTuActivity.class);
                    startActivity(intent);
                    ToastUtil.showToast(" ヾ(≧▽≦*)o ");
                }
            }
        });


    }

    private void setupMap(){
        if(pSceneView != null){
            double latitude = 0;    // 纬度
            double lougitude = 0;  // 经度
            double altitude = 3000000;
            double heading = 90;
            double pitch = 90.0;
            double roll = 0.0;

            ArcGISScene scene = new ArcGISScene();
            scene.setBasemap(Basemap.createImagery());
            pSceneView.setScene(scene);
            Camera camera = new Camera(latitude, lougitude, altitude, heading, pitch, roll);
            pSceneView.setViewpointCamera(camera);
        }
    }




    @Override
    protected void onPause() {
        if (pSceneView != null)
            pSceneView.pause();
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (pSceneView != null)
            pSceneView.resume();
    }
    @Override
    protected void onDestroy() {
        if (pSceneView != null)
            pSceneView.dispose();
        super.onDestroy();
    }
}
