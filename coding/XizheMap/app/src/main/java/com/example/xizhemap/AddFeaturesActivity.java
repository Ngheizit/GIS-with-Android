package com.example.xizhemap;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AddFeaturesActivity extends AppCompatActivity {

    private static final String TAG = AddFeaturesActivity.class.getSimpleName();

    private MapView axMapView;

    private ServiceFeatureTable pServiceFeatureTable;

    /**
     * Add a new Feature to a ServiceFeatureTable and applies the changes to the server
     *
     * @param mapPoint      location to add feature
     * @param featureTable  service feature table to add feature
     */
    private void addFeature(Point mapPoint, final ServiceFeatureTable featureTable){

        // create default attributes for the feature
        Map<String, Object> attributes = new HashMap<>();
        // 设置字段属性   （字段 → 对应属性）
        attributes.put("typdamage", "Destroyed");
        attributes.put("primcause", "Earthquake");

        // creates a new feature using default attributes and point
        Feature feature = featureTable.createFeature(attributes, mapPoint);

        // check if feature can be added to feature table
        if(featureTable.canAdd()){
            // add the new feature to the feature table and to server
            featureTable.addFeatureAsync(feature).addDoneListener(() -> applyEdits(featureTable));
        }else{
            runOnUiThread(() -> logToUser(true, "Error: Cannot add to feature table"));
        }
    }

    /**
     * Sends any edits on the ServiceFeatureTable to the server
     *
     * @param featureTable  service feature table
     */
    private void applyEdits(ServiceFeatureTable featureTable){

        // apply the changes to the server
        final ListenableFuture<List<FeatureEditResult>> editResult = featureTable.applyEditsAsync();
        editResult.addDoneListener(() ->{
            try{
                List<FeatureEditResult> editResults = editResult.get();
                // check if the server edit was successful
                if(editResults != null && !editResults.isEmpty()){
                    if(!editResults.get(0).hasCompletedWithErrors()){
                        runOnUiThread(() -> logToUser(false, "Feature added"));
                    }else {
                        throw editResults.get(0).getError();
                    }
                }
            }catch (InterruptedException | ExecutionException e){
                runOnUiThread(() -> logToUser(true, "Error: applying edits"));
            }
        });
    }

    private void logToUser(boolean isError, String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if(isError){
            Log.e(TAG, message);
        }else {
            Log.d(TAG, message);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfeatures);

        axMapView = findViewById(R.id.axMapView);

        // create a map with streets basemap
        ArcGISMap pMap = new ArcGISMap(Basemap.Type.STREETS, 30.0, 120.9, 4);

        // create a service feature table from URL
        pServiceFeatureTable = new ServiceFeatureTable("http://sampleserver6.arcgisonline.com/arcgis/rest/services/DamageAssessment/FeatureServer/0");

        // create a feature layer from table
        FeatureLayer pFeatureLayer = new FeatureLayer(pServiceFeatureTable);

        // add the layer to the map
        pMap.getOperationalLayers().add(pFeatureLayer);

        // add a listener to the MapView to detect when a user has performed a single tap to add a new feature to the service feature table
        axMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, axMapView){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent event){
                // create a point from where the user clicked
                android.graphics.Point point = new android.graphics.Point((int)event.getX(), (int)event.getY());

                // create a map point from a point
                Point mapPoint = axMapView.screenToLocation(point);

                // add a new feature to the service feature table
                addFeature(mapPoint, pServiceFeatureTable);
                return super.onSingleTapConfirmed(event);
            }
        });

        // set map to be displayed in map view
        axMapView.setMap(pMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        axMapView.resume();
    }

    @Override
    protected void onPause() {
        axMapView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        axMapView.dispose();
        super.onDestroy();
    }

}
