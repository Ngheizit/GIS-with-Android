package com.example.xizhemap;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.SublayerList;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;

public class ChangeSublayerVisibilityActivity extends AppCompatActivity {

    private MapView pMapView;
    private ArcGISMapImageLayer pMapImageLayer;
    private SublayerList pLayers;

    // The layer on/off menu items
    private MenuItem pCities = null;
    private MenuItem pContinent = null;
    private MenuItem pWorld = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changesublayervisivility);

        // inflate Map from layout
        pMapView = findViewById(R.id.axMapView);
        // create a map with the Basemap Type topographic
        ArcGISMap map = new ArcGISMap(Basemap.Type.TOPOGRAPHIC, 48.354406, -99.998267, 2);
        // create a MapImageLayer with dynamically generated map images
        pMapImageLayer = new ArcGISMapImageLayer("https://sampleserver6.arcgisonline.com/arcgis/rest/services/SampleWorldCities/MapServer");
        pMapImageLayer.setOpacity(0.9f);
        // add world cities layers as map operational layer
        map.getOperationalLayers().add(pMapImageLayer);
        // set the map to be displayed in this view
        pMapView.setMap(map);
        // get the layers from the map image layer
        pLayers = pMapImageLayer.getSublayers();

    }

    @Override
    protected void onPause(){
        super.onPause();
        pMapView.pause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        pMapView.resume();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        pMapView.dispose();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        // Inflate the menu; this adds items to the action bar if it is present
            // 填充菜单；将项目添加到操作栏
        getMenuInflater().inflate(R.menu.menu_changesublayervisbility, menu);

        // Get the sub layer switch menu items
        pCities = menu.getItem(0);
        pContinent = menu.getItem(1);
        pWorld = menu.getItem(2);

        // set all layers on by default
        pCities.setChecked(true);
        pContinent.setChecked(true);
        pWorld.setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // handle menu item selection
        // if else is used because this sample is used elsewhere as aLibrary module
        int itemId = item.getItemId();
        if(itemId == R.id.Cities){
            if(pLayers.get(0).isVisible() && pCities.isChecked()){
                // cities layer is on and menu item checked
                pLayers.get(0).setVisible(false);
                pCities.setChecked(false);
            }else if(!pLayers.get(0).isVisible() && !pCities.isChecked()){
                // cities layer is off and menu item unchecked
                pLayers.get(0).setVisible(true);
                Log.d("cities", String.valueOf(pLayers.get(0).getOpacity()));
                pCities.setChecked(true);
            }
            return true;
        } else if (itemId == R.id.Continents) {
            if (pLayers.get(1).isVisible() && pContinent.isChecked()) {
                // continent layer is on and menu item checked
                pLayers.get(1).setVisible(false);
                pContinent.setChecked(false);
            } else if (!pLayers.get(1).isVisible() && !pContinent.isChecked()) {
                // continent layer is off and menu item unchecked
                pLayers.get(1).setVisible(true);
                Log.d("continents", String.valueOf(pLayers.get(1).getOpacity()));
                pContinent.setChecked(true);
            }
            return true;
        } else if (itemId == R.id.World) {
            if (pLayers.get(2).isVisible() && pWorld.isChecked()) {
                // world layer is on and menu item checked
                pLayers.get(2).setVisible(false);
                pWorld.setChecked(false);
            } else if (!pLayers.get(2).isVisible() && !pWorld.isChecked()) {
                // world layer is off and menu item unchecked
                pLayers.get(2).setVisible(true);
                Log.d("world", String.valueOf(pLayers.get(2).getOpacity()));
                pWorld.setChecked(true);
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
