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

//import com.esri.arcgisruntime.mapping.ArcGISMap;
//import com.esri.arcgisruntime.mapping.Basemap;
//import com.esri.arcgisruntime.mapping.view.MapView;

import java.security.PublicKey;

public class DirActivity extends AppCompatActivity {

//    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dir);

        Toast.makeText(getApplicationContext(), "W e l c o m e ~", Toast.LENGTH_SHORT).show();

//        mMapView = (MapView) findViewById(R.id.mapView);
//        setupMap();

    }

//    @Override
//    protected void onPause(){
//        if(mMapView != null){
//            mMapView.pause();
//        }
//    }
//
//    @Override
//    protected  void onResume(){
//        super.onResume();
//        if(mMapView != null){
//            mMapView.dispose();
//        }
//    }
//
//    @Override
//    protected void onDestroy(){
//        if(mMapView != null){
//            mMapView.dispose();
//        }
//        super.onDestroy();
//    }
//
//
//
//    private void setupMap(){
//        if(mMapView !=null){
//            Basemap.Type basemapType = Basemap.Type.STREETS_VECTOR;
//            double latitude = 34.09042;
//            double longitude = -118.71511;
//            int levelOfDetail = 11;
//            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
//            mMapView.setMap(map);
//        }
//    }
}
