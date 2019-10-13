package com.ngheizit.myapplication.activity;

import android.location.Location;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.geoanalysis.Analysis;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.internal.jni.CoreAnalysis;
import com.esri.arcgisruntime.internal.jni.CoreRequest;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.layers.LayerContent;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.AnalysisOverlay;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.ngheizit.myapplication.R;
import com.ngheizit.myapplication.ToastUtil;
import com.ngheizit.myapplication.gps.GPSLocationListener;
import com.ngheizit.myapplication.gps.GPSLocationManager;

public class DeviceGpsActivity3D extends AppCompatActivity {

    private SceneView pSceneView;
    private double lat, lon, bearing;
    private GPSLocationManager gpsLocationManager;
    PictureMarkerSymbol symbol;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_gps_3d);

        pSceneView = findViewById(R.id.axSceneView);
        ArcGISScene scene = new ArcGISScene();
        scene.setBasemap(Basemap.createImagery());
        pSceneView.setScene(scene);
        gpsLocationManager = GPSLocationManager.getInstances(DeviceGpsActivity3D.this);
        gpsLocationManager.start(new MyListener());


        symbol = new PictureMarkerSymbol("https://ngheizit.fun/Older/img/joker.png");
        symbol.loadAsync();
        symbol.setHeight(20);
        symbol.setWidth(20);

    }


    class MyListener implements GPSLocationListener {
        String url_china = "https://ngheizit.fun/default-img/joker.png";
        @Override
        public void UpdateLocation(Location location) {
            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                bearing = location.getBearing();
//                 System.out.println(lat + "XXX" + lon + "XXX" + bearing);
                //38.04351294         114.60884694
                //TransformationCatalog.getTransformationsBySuitability()
                try {
                Point point = new Point(lon, lat, SpatialReferences.getWgs84());
                GraphicsOverlay overlay = new GraphicsOverlay();
                pSceneView.getGraphicsOverlays().add(overlay);
                overlay.getGraphics().add(new Graphic(point, symbol));
                }catch (Exception e){

                }

            }
        }

        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) {
            if ("gps" == provider) {
                ToastUtil.showToast("定位类型：" + provider);
            }
        }

        @Override
        public void UpdateGPSProviderStatus(int gpsStatus) {

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // pause SceneView
        pSceneView.pause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // resume SceneView
        pSceneView.resume();
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        // dispose SceneView
        pSceneView.dispose();
    }
}
