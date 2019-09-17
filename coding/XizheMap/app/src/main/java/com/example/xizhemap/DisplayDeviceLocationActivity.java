package com.example.xizhemap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Item;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.example.xizhemap.displaydevicelaocion.ItemData;
import com.example.xizhemap.displaydevicelaocion.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DisplayDeviceLocationActivity extends AppCompatActivity {

    private TextView tv_distance;

    private MapView pMapView;
    private LocationDisplay pLocationDisplay;
    private Spinner pSpinner;


    private int requestCode = 2;
    String[] reqPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    private LocationManager lm;
    private List<PointClass> pointLists = new ArrayList<>();
    private List<PointClass> pointLists2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaydevicelocation);

        tv_distance = findViewById(R.id.tv_distance);

        // Get the Spinner from layout
        pSpinner = (Spinner) findViewById(R.id.spinner);

        // Get the Map from layout and set a map with the BasemapType Imagery
        pMapView = findViewById(R.id.axMapView);
        ArcGISMap pMap = new ArcGISMap(Basemap.createImagery());
        pMapView.setMap(pMap);

        // get the MapView's LocationDisplay - 位置显示
        pLocationDisplay = pMapView.getLocationDisplay();

        // Listen to changes in the status of the location data source
        pLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                // If LocationDisplay started OK, then continue
                if (dataSourceStatusChangedEvent.isStarted())
                    return;

                // No error is reported, then continue
                if (dataSourceStatusChangedEvent.getError() == null)
                    return;

                // if an error is found, handle the failure to start
                // Check permissions to see if failure may be due to lack of permissions
                boolean permissionCheck1 = ContextCompat.checkSelfPermission(DisplayDeviceLocationActivity.this,
                        reqPermissions[0]) == PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(DisplayDeviceLocationActivity.this,
                        reqPermissions[1]) == PackageManager.PERMISSION_GRANTED;

                if(!(permissionCheck1 && permissionCheck2)){
                    // If permissions are not already granted, request permission from the user
                    ActivityCompat.requestPermissions(DisplayDeviceLocationActivity.this, reqPermissions, requestCode);
                }else {
                    // Report other unknown failure types to the user - for example, location services may not
                    // be enabled on the device
                    String message = String.format("Error in DataSourceStatusChangedListener: %s",
                            dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(DisplayDeviceLocationActivity.this, message, Toast.LENGTH_LONG).show();

                    // Update UI to reflect that the location display did not actually start
                    pSpinner.setSelection(0, true);
                }
            }
        });

        // Populate the list for the Location display options for the spinner's Adapter
        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData("Stop", R.drawable.locationdisplaydisabled));
        list.add(new ItemData("On", R.drawable.locationdisplayon));
        list.add(new ItemData("Re-Center", R.drawable.locationdisplayrecenter));
        list.add(new ItemData("Navigation", R.drawable.locationdisplaynavigation));
        list.add(new ItemData("Compass", R.drawable.locationdisplayheading));

        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.sppinner_layout, R.id.txt, list);
        pSpinner.setAdapter(adapter);
        pSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positon, long id) {
                switch (positon){
                    // Stop Location Display
                    case 0:
                        if(pLocationDisplay.isStarted())
                            pLocationDisplay.stop();
                        break;
                    case 1:
                        // Start Location Display
                        if(!pLocationDisplay.isStarted())
                            pLocationDisplay.startAsync();
                        break;
                    case 2:
                        // Re-Center MapView on Location
                        // AutoPanMode - Default: In this mode, the MapView attempts to keep the location symbol on-screen by
                        // re-centering the location symbol when the symbol moves outside a "wander extent". The location symbol
                        // may move freely within the wander extent, but as soon as the symbol exits the wander wxtent, the MapView
                        // re-center the map on the symbol
                        pLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                        if(!pLocationDisplay.isStarted())
                            pLocationDisplay.startAsync();
                        break;
                    case 3:
                        // StartNavigation Mode
                        // This mode is best suited for in-vehicle navigation
                        pLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
                        if(!pLocationDisplay.isStarted())
                            pLocationDisplay.startAsync();
                        break;
                    case 4:
                        // Start Compass Mode
                        // This mode is better suited for waypoint navigation when the user is walking
                        pLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                        if(!pLocationDisplay.isStarted())
                            pLocationDisplay.startAsync();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {

                    double distance = 0.0;

                    double lon = pLocationDisplay.getLocation().getPosition().getX();
                    double lat = pLocationDisplay.getLocation().getPosition().getY();
                    pointLists.add(new PointClass(lon, lat));
                    PointCollection borderCAtoNV = new PointCollection(SpatialReferences.getWgs84());
                    for(int i = 0; i < pointLists.size(); i++){
                        borderCAtoNV.add(pointLists.get(i).Lon, pointLists.get(i).Lat);

                    }
                    Polyline polyline = new Polyline(borderCAtoNV);
                    SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3);
                    GraphicsOverlay overlay = new GraphicsOverlay();
                    pMapView.getGraphicsOverlays().add(overlay);
                    overlay.getGraphics().add(new Graphic(polyline, lineSymbol));

                    // ---------------------------------

                    lon = location.getLongitude();
                    lat = location.getLatitude();
                    pointLists2.add(new PointClass(lon, lat));
                    borderCAtoNV = new PointCollection(SpatialReferences.getWgs84());
                    for(int i = 0; i < pointLists2.size(); i++){
                        borderCAtoNV.add(pointLists2.get(i).Lon, pointLists2.get(i).Lat);
                        if(i > 0){
                            distance += PointClass.GetDistance(pointLists2.get(i - 1), pointLists2.get(i));
                            Toast.makeText(getApplicationContext(), Double.toString(distance), Toast.LENGTH_SHORT).show();
                        }
                    }
                    distance = Math.round(distance * 1000) / 1000;
                    tv_distance.setText(Double.toString(distance));
                    polyline = new Polyline(borderCAtoNV);

                    lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.YELLOW, 2);
                    overlay = new GraphicsOverlay();
                    pMapView.getGraphicsOverlays().add(overlay);
                    overlay.getGraphics().add(new Graphic(polyline, lineSymbol));

                    // ------------------------------------


                }catch(Exception e) {

                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        // If request is cancelled, the result arrays are empty
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // Location permission was granted. This would have been triggered in response to failing to start the
            // Location Display, so try start this again
            pLocationDisplay.startAsync();
        }else{
            // If permission was denied, how to inform user what was chosen. If LocationDisplay is started again,
            // request permission UX will be shown again, option should be shown to allow never showing the UX again.
            // Alternative would be to disable functionality so request is not shown again.
            Toast.makeText(DisplayDeviceLocationActivity.this,
                    "LocationDisplayManager cannot run because location permission was denied",
                    Toast.LENGTH_SHORT).show();
            // Update UI to reflect that the location display did not actually start
            pSpinner.setSelection(0, true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pMapView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pMapView.dispose();
    }

    // ---------------------------------------------


}
