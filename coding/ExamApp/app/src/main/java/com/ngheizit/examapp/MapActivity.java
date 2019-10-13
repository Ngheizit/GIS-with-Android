package com.ngheizit.examapp;

import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.io.RequestConfiguration;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.ViewpointChangedEvent;
import com.esri.arcgisruntime.mapping.view.ViewpointChangedListener;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.BlurEffect;
import com.mingle.sweetpick.RecyclerViewDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.ngheizit.examapp.gps.GPSLocationListener;
import com.ngheizit.examapp.gps.GPSLocationManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MapActivity extends AppCompatActivity {

    private static final String KEY  = "92d4f72edb2b6dcef6e8a6a60cf88d4f";
    private double lon, lat,bearing; // 经度 纬度 方位角
    private SweetSheet axSweetSheet; // 控件：拖拽菜单
    private GPSLocationManager gpsLocationListener; // GPS位置管理对象

    private ArrayList<String> pointlist; //搜索到的点集合
    private ArrayList<String> pointlistuse;//有用的点集合
    private ArrayList<MenuEntity> list;//搜索到的名称集合

    @BindView(R.id.axRelativeLayout_Main) RelativeLayout axRLayout;
    @BindView(R.id.axMapView_Main) MapView axMapView;
    @BindView(R.id.axBtn_ZoomIn) Button axBtn_ZoomIn;
    @BindView(R.id.axBtn_ZoomOut) Button axBtn_ZoomOut;
    @BindView(R.id.axBtn_Locate) Button axBtn_Locate;
    @OnClick({ R.id.axBtn_ZoomIn, R.id.axBtn_ZoomOut, R.id.axBtn_Locate })public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.axBtn_ZoomIn: // 比例放大
                axMapView.setViewpointScaleAsync(axMapView.getMapScale() * 0.5);
                break;
            case R.id.axBtn_ZoomOut: // 比例缩小
                axMapView.setViewpointScaleAsync(axMapView.getMapScale() * 2.0);
                break;
            case R.id.axBtn_Locate: // 定位
                Point point = new Point(lon, lat);
                axMapView.setViewpointCenterAsync(point, 20000);
                break;
        }
    }
    @BindView(R.id.axEt_From) EditText axEt_From;
    @BindView(R.id.axEt_To) EditText axEt_To;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        // 设置位置管理
        gpsLocationListener = GPSLocationManager.getInstances(MapActivity.this);
        gpsLocationListener.start(new Listener());

        // 初始化菜单（菜单绑定）
        axSweetSheet = new SweetSheet(axRLayout);
        pointlistuse = new ArrayList<>();

        // 设置地图（天地图）
        setMap();

        // 添加文字监听事件
        addTextChangeListen(axEt_From);
        addTextChangeListen(axEt_To);

    }

    // 设置地图（天地图）
    private void setMap(){
        if(axMapView == null) return;
        // 设置网络瓦片图层（天地图 矢量地图图层 + 矢量注记图层）
        WebTiledLayer webTiledLayer_Main = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_VECTOR_2000);
        WebTiledLayer webTiledLayer_Anno = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_VECTOR_ANNOTATION_CHINESE_2000);

        // 设置请求配置
        RequestConfiguration requestConfiguration = new RequestConfiguration();
        requestConfiguration.getHeaders()
                .put("referer", "http://www.arcgis.com");
        webTiledLayer_Main.setRequestConfiguration(requestConfiguration);
        webTiledLayer_Anno.setRequestConfiguration(requestConfiguration);

        // 异步加载网络瓦片图层
        webTiledLayer_Main.loadAsync();
        webTiledLayer_Anno.loadAsync();

        // 导入地图
        Basemap basemap = new Basemap(webTiledLayer_Main);
        basemap.getBaseLayers().add(webTiledLayer_Anno);
        ArcGISMap map = new ArcGISMap();
        map.setBasemap(basemap);
        axMapView.setMap(map);
    }

    // 设置文字变化监听
    private void addTextChangeListen(EditText et){
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(!axSweetSheet.isShow()){
                    searchPOI(et);
                }
            }
        });
    }


    private void searchPOI(EditText et){
        String keyword = et.getText().toString();
        JSONObject jsonObject = new JSONObject();
        double lon_viewCenter = axMapView.getVisibleArea().getExtent().getCenter().getX(), // MapView当前视野范围中心点坐标
               lat_viewCenter = axMapView.getVisibleArea().getExtent().getCenter().getY();
        ToastUtil.showToast(lon_viewCenter + ", " + lat_viewCenter);
        try {
            jsonObject.put("keyWord", keyword);
            jsonObject.put("level", "15");
            jsonObject.put("mapBound", "-180,-90,180,90");
            jsonObject.put("queryType", "3"); // 周边搜索
            jsonObject.put("queryRadius", "100000"); // 周边搜索半径 100km
            jsonObject.put("pointLonlat", lon_viewCenter + "," + lat_viewCenter);
            jsonObject.put("count", "20");
            jsonObject.put("start", "0");
//            jsonObject.put("keyWord", keyword);
//            jsonObject.put("level", "11");
//            jsonObject.put("mapBound", "113.51576352656,37.437345659733,115.47631654589,38.763868632776");
//            jsonObject.put("queryType", "1");
//            jsonObject.put("count", "20");
//            jsonObject.put("start", "0");
        } catch (JSONException e) { e.printStackTrace(); }
        OkHttpUtils.get()
                .url("http://api.tianditu.gov.cn/search")
                .addParams("postStr", jsonObject.toString())
                .addParams("type", "query")
                .addParams("tk", KEY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) { e.printStackTrace(); }
                    @Override
                    public void onResponse(String response, int id) {
                        list = new ArrayList<>();
                        pointlist = new ArrayList<>();
                        try {
                            JSONObject jo = new JSONObject(response);
                            JSONArray pois = jo.getJSONArray("pois");
                            for(int i = 0; i < pois.length(); i++){
                                String lonlat = pois.getJSONObject(i).getString("lonlat");
                                pointlist.add(lonlat);
                                String name = pois.getJSONObject(i).getString("name");
                                MenuEntity menuEntity = new MenuEntity();
                                menuEntity.title = name;
                                list.add(menuEntity);
                                System.out.println("%%%%%%%%%%" + lonlat + ": " +  name);
                            }
                            axSweetSheet = new SweetSheet(axRLayout);
                            axSweetSheet.setMenuList(list);
                            axSweetSheet.setDelegate(new RecyclerViewDelegate(true));
                            axSweetSheet.setBackgroundEffect(new BlurEffect(8));
                            axSweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
                                @Override
                                public boolean onItemClick(int position, MenuEntity menuEntity) {
                                    et.setText(menuEntity.title);
                                    pointlistuse.add(pointlist.get(position));
                                    return true;
                                }
                            });
                            // 隐藏输入法
                            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(MapActivity.this.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), 0);
                            // 显示弹窗
                            axSweetSheet.toggle();
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                });
    }

    // GPS位置监听对象
    class Listener implements GPSLocationListener{
        @Override
        public void UpdateLocation(Location location) {
            if(location == null) return;
            lon = location.getLongitude();
            lat = location.getLatitude();
            bearing = location.getBearing();
        }
        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) { }
        @Override
        public void UpdateGPSProviderStatus(int gpsStatus) { }
    }
}
