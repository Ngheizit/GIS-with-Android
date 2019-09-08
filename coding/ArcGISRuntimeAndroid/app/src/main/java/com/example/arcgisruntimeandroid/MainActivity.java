package com.example.arcgisruntimeandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SceneView;


public class MainActivity extends AppCompatActivity {

    private MapView pMapView;
    private SceneView pSceneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // license with a license key - 许可应用程序
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud2630151591,none,PM0RJAY3FP20463EM070");

        pMapView = findViewById(R.id.axMapView);
        pSceneView = findViewById(R.id.axSceneView);
        setup2DMap();
        setup3DMap();
    }


    private void setup2DMap(){
        if(pMapView != null){
            Basemap.Type basemapType = Basemap.Type.STREETS_VECTOR; // 底图样式
            double latitude = 34; // 纬度
            double longitude = 120; // 经度
            int levelOfDetail = 5; // 缩放系数
            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            pMapView.setMap(map);
        }
    }
    private void setup3DMap(){
        if(pSceneView != null){
            double latitude = 34;
            double longitude = 120;
            double altitude = 44000.0;
            double heading = 0.1;
            double pitch = 30.0;
            double roll = 0.0;
            ArcGISScene scene = new ArcGISScene();
            scene.setBasemap(Basemap.createStreets());
            pSceneView.setScene(scene);
            Camera camera = new Camera(latitude, longitude, altitude, heading, pitch, roll);
            pSceneView.setViewpointCamera(camera);
        }
    }




    // -------------------------------------------------
    // Override the onPause, onResume and onDestroy methods of the MainActivity class

    @Override
    protected void onPause() {
        if (pMapView != null) {
            pMapView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pMapView != null) {
            pMapView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (pMapView != null) {
            pMapView.dispose();
        }
        super.onDestroy();
    }
    // -------------------------------------------------
}
