package com.ngheizit.examapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.SceneView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String KEY = "runtimelite,1000,rud2630151591,none,PM0RJAY3FP20463EM070";

    @BindView(R.id.axSceneView_HelloWorld)
    SceneView axSceneView;
    @BindView(R.id.axEditText_Usename)
    EditText axEditText_Usename;
    @BindView(R.id.axEditText_Password)
    EditText axEditText_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // ArcGIS Rumtime 许可
        ArcGISRuntimeEnvironment.setLicense(KEY);

        // 设置场景
        setScene();

        // 设置登录监听
        setSignIn();

    }

    // 设置场景
    private void setScene(){
        if(axSceneView != null){
            double latitude = 0,
                    longitude = 0,
                    altitude = 25000000.0,
                    heading = 0.1,
                    pitch = 3.0,
                    roll = 0.0;
            ArcGISScene scene = new ArcGISScene();
            scene.setBasemap(Basemap.createImagery());
            axSceneView.setScene(scene);
            Camera camera = new Camera(latitude, longitude, altitude, heading, pitch, roll);
            axSceneView.setViewpointCamera(camera);
        }
    }

    // 设置登录监听
    private void setSignIn(){
        axEditText_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String word = s.toString();
                if(word.equals("爱你一万年")){
                    ToastUtil.showToast("LLY");
                    startActivity(new Intent(getApplicationContext(), MapActivity.class));
                    ToastUtil.showToast("BINGO");
                }
            }
        });
    }


    @Override
    protected void onPause() {
        if (axSceneView != null) {
            axSceneView.pause();
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (axSceneView != null) {
            axSceneView.resume();
        }
    }
    @Override
    protected void onDestroy() {
        if (axSceneView != null) {
            axSceneView.dispose();
        }
        super.onDestroy();
    }
}
