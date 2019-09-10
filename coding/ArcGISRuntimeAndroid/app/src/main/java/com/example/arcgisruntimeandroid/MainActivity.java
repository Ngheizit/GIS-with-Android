package com.example.arcgisruntimeandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;


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
        setupWebMap();
        addLayerFormUrl("https://services9.arcgis.com/8vu5jgpRPi7NCKmE/ArcGIS/rest/services/中国省级行政单元/FeatureServer/0");
        addLayerFormUrl("https://services9.arcgis.com/8vu5jgpRPi7NCKmE/ArcGIS/rest/services/中国行政界线/FeatureServer/0");
        addLayerFromPortalItem("bfdd8f9b62154bd7a0e4df5063fa87d0");
//        addLayerFormUrl("https://services9.arcgis.com/8vu5jgpRPi7NCKmE/ArcGIS/rest/services/中国省级行政中心/FeatureServer/0");
    }

    // 二维底图
    private void setup2DMap(){
        if(pMapView != null){
            Basemap.Type basemapType = Basemap.Type.OPEN_STREET_MAP; // 底图样式
            double latitude = 34; // 纬度
            double longitude = 120; // 经度
            int levelOfDetail = 5; // 缩放系数
            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            pMapView.setMap(map);
        }
    }
    // 三维场景底图
    private void setup3DMap(){
        if(pSceneView != null){
            double latitude = 34;
            double longitude = 120;
            double altitude = 44000.0;
            double heading = 0.1;
            double pitch = 30.0;
            double roll = 0.0;
            ArcGISScene scene = new ArcGISScene();
            scene.setBasemap(Basemap.createOpenStreetMap());
            pSceneView.setScene(scene);
            Camera camera = new Camera(latitude, longitude, altitude, heading, pitch, roll);
            pSceneView.setViewpointCamera(camera);
        }
    }

    // Add a layer to the map from url
    private void addLayerFormUrl(String url){
        String str_url = url;
        ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(str_url);
        FeatureLayer featureLayer = new FeatureLayer(serviceFeatureTable);
        ArcGISMap map = pMapView.getMap();
        map.getOperationalLayers().add(featureLayer);
    }
    // Add a layer to map from portal item
    private void addLayerFromPortalItem(String id){
        String itemID = id; // Load the portal item
        Portal portal = new Portal("http://www.arcgis.com");
        final PortalItem portalItem = new PortalItem(portal, itemID);
        FeatureLayer pFeatureLayer = new FeatureLayer(portalItem, 0);
        ArcGISMap map = pMapView.getMap();
        map.getOperationalLayers().add(pFeatureLayer);
    }
    // Add a web map
    private void setupWebMap(){
        if(pMapView != null){
            String itemId = "055b0aa25f54493cb207663829585eb2";
            Portal portal = new Portal("http://www.arcgis.com", false);
            PortalItem portalItem = new PortalItem(portal, itemId);
            ArcGISMap map = new ArcGISMap(portalItem);
            pMapView.setMap(map);
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
