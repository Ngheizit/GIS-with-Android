package com.example.xizhemap;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.esri.arcgisruntime.mapping.view.MapView;

public class OAuthActivity extends AppCompatActivity {

    private final String TAG = OAuthActivity.class.getSimpleName();

    private MapView pMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        // get a reference to the map
        pMapView = findViewById(R.id.axMapView);
//
//        try{
//
//        }

    }

}
