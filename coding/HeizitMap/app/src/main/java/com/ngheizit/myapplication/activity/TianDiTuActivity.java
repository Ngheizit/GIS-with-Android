package com.ngheizit.myapplication.activity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.esri.arcgisruntime.arcgisservices.TileInfo;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.io.RequestConfiguration;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.ngheizit.myapplication.R;
import com.ngheizit.myapplication.TianDiTuMethodsClass;
import com.ngheizit.myapplication.ToastUtil;
import com.ngheizit.myapplication.gps.GPSLocationListener;
import com.ngheizit.myapplication.gps.GPSLocationManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class TianDiTuActivity extends AppCompatActivity {

    // 全局属性
    private MapView pMapView;
    private GPSLocationManager gpsLocationManager;
    GraphicsOverlay mGraphicsOverlay;
    // private SketchGraphicsOverlay mSketchGraphicsOverlay;
    private Graphic bassRockGraphic;
    private Graphic bassRockGraphic2;
    private double lat, lon, bearing ,area;
    private List<Point> pointList = new ArrayList<>();
    PointCollection pointCollectionsss;

    private TileInfo tileInfo;
    private Envelope fullExtent;
    private WebTiledLayer webTiledLayer;
    private WebTiledLayer webTiledLayer2;


    @BindView(R.id.btn_locate) Button btn_locate;
    @BindView(R.id.btn_zoomIn) Button btn_zoomIn;
    @BindView(R.id.btn_zoomOut) Button btn_zoomOut;
    @OnClick({
            R.id.btn_locate, R.id.btn_zoomIn, R.id.btn_zoomOut
    })public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.btn_zoomIn:
                pMapView.setViewpointScaleAsync(pMapView.getMapScale()*0.5 );
                break;
            case R.id.btn_zoomOut:
                pMapView.setViewpointScaleAsync(pMapView.getMapScale()*2 );
                break;
            case R.id.btn_locate:
                Point point =new Point( lon,lat) ;
                search_poi("博物馆");
                pMapView.setViewpointCenterAsync( point,20329.6);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tian_di_tu);
        ButterKnife.bind(this);


        pMapView = findViewById(R.id.axMapView);
        gpsLocationManager = GPSLocationManager.getInstances(TianDiTuActivity.this);
        gpsLocationManager.start(new MyListener());

        initmap();
    }

    private void initmap() {
        if (pMapView != null) {

            WebTiledLayer webTiledLayer = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_VECTOR_2000);
            WebTiledLayer webTiledLayer1 = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_VECTOR_ANNOTATION_CHINESE_2000);

            //注意：在100.2.0之后要设置RequestConfiguration
            RequestConfiguration requestConfiguration = new RequestConfiguration();
            requestConfiguration.getHeaders().put("referer", "http://www.arcgis.com");
            webTiledLayer.setRequestConfiguration(requestConfiguration);
            webTiledLayer1.setRequestConfiguration(requestConfiguration);
            webTiledLayer.loadAsync();
            webTiledLayer1.loadAsync();
            Basemap basemap = new Basemap(webTiledLayer);
            basemap.getBaseLayers().add(webTiledLayer1);

            ArcGISMap map =new ArcGISMap();
            map.setBasemap(basemap);

            pointCollectionsss = new PointCollection(pMapView.getSpatialReference());
            pMapView.setMap(map);

            mGraphicsOverlay = new GraphicsOverlay();
            pMapView.getGraphicsOverlays().add(mGraphicsOverlay);
        }
    }

    private void search_poi(String keword){
        JSONObject jo = new JSONObject();
        try {
            jo.put("keyWord", keword);
            jo.put("level", "11");
            jo.put("mapBound", "113.51576352656,37.437345659733,115.47631654589,38.763868632776");
            jo.put("queryType", "1");
            jo.put("count", "20");
            jo.put("start", "0");
        }catch (JSONException e){
            e.printStackTrace();
        }
        OkHttpUtils.get()
                .url("http://api.tianditu.gov.cn/search")
                .addParams("postStr", jo.toString())
                .addParams("type", "query")
                .addParams("tk", "92d4f72edb2b6dcef6e8a6a60cf88d4f")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray pois = jsonObject.getJSONArray("pois");
                            for (int i = 0; i < pois.length(); i++){
                                JSONObject jo = pois.getJSONObject(i);
                                String address = jo.getString("address");
                                String name = jo.getString("name");
                                String lonlat = jo.getString("lonlat");
                                System.out.println("&&&&&&&&&&&&  " + address + " " + name + " " + lonlat);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        if (pMapView != null) {
            pMapView.pause();
        }
        gpsLocationManager.stop();
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
        gpsLocationManager.stop();
        super.onDestroy();
    }







    class MyListener implements GPSLocationListener {

        @Override
        public void UpdateLocation(Location location) {
            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                bearing = location.getBearing();

                // System.out.println(lat + "XXX" + lon + "XXX" + bearing);
                //38.04351294         114.60884694
                //TransformationCatalog.getTransformationsBySuitability()
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
}
