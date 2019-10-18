package com.ngheizit.examapp;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PolylineBuilder;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.io.RequestConfiguration;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;
import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.DimEffect;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MapActivity extends AppCompatActivity {

    private static final String KEY  = "82336205e2fbc3838263aeb80c78112a"; // 天地图密钥
    private double lon, lat,bearing; // 经度 纬度 方位角
    private SweetSheet axSweetSheet; // 控件：拖拽菜单
    private GPSLocationManager gpsLocationListener; // GPS位置管理对象
    private String pointStart, pointEnd; // 导航起点和终点
    private boolean isTrajectory = false; // 轨迹记录开启关闭控制事件
    private String imageUrl = "https://ngheizit.fun/Older/img/Woodstock.png"; // 轨迹样式图片
    private PictureMarkerSymbol pictureMarkerSymbol; // 轨迹样式
    private long startTime, nowTime; // 轨迹记录开始时间和实时时间
    private List<Point> pointList = new ArrayList<>(); // 轨迹记录点


    @BindView(R.id.axRelativeLayout_Main) RelativeLayout axRLayout;
    @BindView(R.id.axMapView_Main) MapView axMapView;
    @BindView(R.id.axBtn_ZoomIn) Button axBtn_ZoomIn;
    @BindView(R.id.axBtn_ZoomOut) Button axBtn_ZoomOut;
    @BindView(R.id.axBtn_Locate) Button axBtn_Locate;
    @BindView(R.id.axBtn_Trajectory) Button axBtn_Trajectory;
    @BindView(R.id.axBtn_Navigate) Button axBtn_Navigate;
    @BindView(R.id.axBtn_Clear) Button axBtn_Clear;
    @OnClick({
            R.id.axBtn_ZoomIn, R.id.axBtn_ZoomOut, R.id.axBtn_Locate,
            R.id.axBtn_Trajectory,
            R.id.axBtn_Navigate, R.id.axBtn_Clear
    })public void onViewClicked(View view){
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
            case R.id.axBtn_Trajectory: // 记录轨迹
                isTrajectory = !isTrajectory;
                BitmapDrawable closeBitmap = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.close);
                BitmapDrawable traBitmap = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.trajectory);
                if(isTrajectory) {
                    pointList.clear();
                    axBtn_Trajectory.setBackground(closeBitmap);
                    startTime = new Date().getTime();
                }
                else { axBtn_Trajectory.setBackground(traBitmap); }
                break;
            case R.id.axBtn_Navigate: // 获取导航路径
                try {  navigate(); }
                catch (Exception e){ ToastUtil.showToast("还未设置起终点"); }
                break;
            case R.id.axBtn_Clear: // 清理导航路径
                axEt_From.setText("");
                axEt_To.setText("");
                axMapView.getGraphicsOverlays().clear();
                break;
        }
    }
    @BindView(R.id.axEt_From) EditText axEt_From;
    @BindView(R.id.axEt_To) EditText axEt_To;
    @BindView(R.id.axTv_info) TextView axTv_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        // 设置位置管理
        gpsLocationListener = GPSLocationManager.getInstances(MapActivity.this);
        gpsLocationListener.start(new Listener());
        gpsLocationListener.setMinDistance(10);

        // 初始化菜单（菜单绑定）
        axSweetSheet = new SweetSheet(axRLayout);

        // 设置地图（天地图）
        setMap();

        // 添加文字监听事件
        addTextChangeListen(axEt_From);
        addTextChangeListen(axEt_To);

        // 符号加载
        pictureMarkerSymbol = new PictureMarkerSymbol(imageUrl);
        pictureMarkerSymbol.loadAsync();
        pictureMarkerSymbol.setHeight(20);
        pictureMarkerSymbol.setWidth(20);
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

    // 抓取POI
    private void searchPOI(EditText et){

        String keyword = et.getText().toString();
        JSONObject jsonObject = new JSONObject();
        double lon_viewCenter = axMapView.getVisibleArea().getExtent().getCenter().getX(), // MapView当前视野范围中心点坐标
               lat_viewCenter = axMapView.getVisibleArea().getExtent().getCenter().getY();
        try {
            jsonObject.put("keyWord", keyword);
            jsonObject.put("level", "15");
            jsonObject.put("mapBound", "-180,-90,180,90");
            jsonObject.put("queryType", "3"); // 周边搜索
            jsonObject.put("queryRadius", "100000"); // 周边搜索半径 100km
            jsonObject.put("pointLonlat", lon_viewCenter + "," + lat_viewCenter);
            jsonObject.put("count", "20");
            jsonObject.put("start", "0");
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
                        ArrayList<String> pointList = new ArrayList<>(); //搜索到的POI点集合
                        ArrayList<MenuEntity> list = new ArrayList<>();//搜索到的POI名称集合
                        try {
                            JSONObject jo = new JSONObject(response);
                            JSONArray pois = jo.getJSONArray("pois");
                            for(int i = 0; i < pois.length(); i++){
                                String lonlat = pois.getJSONObject(i).getString("lonlat");
                                pointList.add(lonlat);
                                String name = pois.getJSONObject(i).getString("name");
                                MenuEntity menuEntity = new MenuEntity();
                                menuEntity.title = name;
                                list.add(menuEntity);
                            }
                            axSweetSheet = new SweetSheet(axRLayout);
                            axSweetSheet.setMenuList(list);
                            axSweetSheet.setDelegate(new RecyclerViewDelegate(true));
                            axSweetSheet.setBackgroundEffect(new DimEffect(8));
                            axSweetSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
                                @Override
                                public boolean onItemClick(int position, MenuEntity menuEntity) {
                                    et.setText(menuEntity.title);
                                    if(et == axEt_From) pointStart = pointList.get(position);
                                    if(et == axEt_To) pointEnd = pointList.get(position);
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

    // 获取导航线路
    private void navigate(){
        // 导航起点
        String s_lon = pointStart.split(" ")[0];
        String s_lat = pointStart.split(" ")[1];
        // 导航终点
        String e_lon = pointEnd.split(" ")[0];
        String e_lat = pointEnd.split(" ")[1];
        // 导航接口
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orig", s_lon + "," + s_lat);
            jsonObject.put("dest", e_lon + "," + e_lat);
        }catch (JSONException e){ e.printStackTrace(); }
        OkHttpUtils.get()
                .url("http://api.tianditu.gov.cn/drive")
                .addParams("postStr", jsonObject.toString())
                .addParams("type", "search")
                .addParams("tk", KEY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) { e.printStackTrace(); }
                    @Override
                    public void onResponse(String response, int id) {
                        HashMap<?, ?> m = XmlUtil.UnPackageXml(response);
                        String routelatlon = m.get("routelatlon").toString();
                        String[] points = routelatlon.split(";");
                        drawLine(points);
                    }
                });
    }

    // 绘制导航路径 包含起终点符号样式 并且更新地图显示范围
    private void drawLine(String[] points){
        // 点击绘线
        PolylineBuilder lineBuilder = new PolylineBuilder(SpatialReferences.getWgs84());
        for (int i = 0; i < points.length; i ++){
            String[] lonlat = points[i].split(",");
            lineBuilder.addPoint(Double.valueOf(lonlat[0]), Double.valueOf(lonlat[1]));
        }
        Graphic lineGraphic = new Graphic(lineBuilder.toGeometry());
        GraphicsOverlay overlay = new GraphicsOverlay();
        overlay.getGraphics().add(lineGraphic);
        // 设置线样式
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 5);
        SimpleRenderer lineRenderer = new SimpleRenderer(lineSymbol);
        overlay.setRenderer(lineRenderer);
        // 添加到地图
        axMapView.getGraphicsOverlays().clear();
        axMapView.getGraphicsOverlays().add(overlay);

        // 设置起终点样式
        BitmapDrawable sPointBitmap = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.startpoint);
        BitmapDrawable ePointBitmap = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.endpoint);
        drawTrajectory(Double.valueOf(points[0].split(",")[0]), Double.valueOf(points[0].split(",")[1]), sPointBitmap);
        drawTrajectory(Double.valueOf(points[points.length - 1].split(",")[0]), Double.valueOf(points[points.length - 1].split(",")[1]), ePointBitmap);

        // 设置地图视野范围
        axMapView.setViewpointGeometryAsync(overlay.getExtent());
        axMapView.setViewpointScaleAsync(axMapView.getMapScale() * 1.5);
    }

    // GPS位置监听对象
    class Listener implements GPSLocationListener{
        @Override
        public void UpdateLocation(Location location) {
            if(location == null) return;
            lon = location.getLongitude();
            lat = location.getLatitude();
            bearing = location.getBearing();
            if(isTrajectory) {
                drawTrajectory(lon, lat);
                showInfo();
            }
        }
        @Override
        public void UpdateStatus(String provider, int status, Bundle extras) { }
        @Override
        public void UpdateGPSProviderStatus(int gpsStatus) { }
    }

    // 显示轨迹信息（路程，速度，配速）
    private void showInfo(){
        pointList.add(new Point(lon, lat, SpatialReferences.getWgs84()));
        double distance = 0.0;
        for(int i = 0; i < pointList.size(); i++) {
            if (i > 0) {
                distance += getDistance(pointList.get(i - 1).getX(), pointList.get(i - 1).getY(), pointList.get(i).getX(), pointList.get(i).getY());
            }
        }
        if(distance != 0) {
            nowTime = new Date().getTime();
            double second = (nowTime - startTime) / 1000.0;
            double m = distance * 1000;
            double ms = Math.round((m / second) * 10) / 10.0;
            distance = Math.round(distance * 1000) / 1000.0;
            String str_distance = distance + " km";
            String str_ms = ms + " m/s";
            String str_kmh = Math.round(ms * 3.6 * 10) / 10.0 + "  km/h";
            String str_speed = Math.round(second / 60.0 / distance * 10) / 10.0 + " min/km";
            axTv_info.setText(String.format("路程：%1$s；配速：%2$s", str_distance, str_speed));
        }
    }

    // 根据经纬度计算距离
    private double rad(double d){ return d * Math.PI / 180.0; }
    private double getDistance(double lon1, double lat1, double lon2, double lat2){
        double EARTH_RADIUS  = 6378.137;
        double a = rad(lat1) - rad(lat2);
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad(lat1)) * Math.cos(rad(lat2)) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }

    // 绘制点
    private void drawTrajectory(double lon, double lat){
        Point point = new Point(lon, lat, SpatialReferences.getWgs84());
        GraphicsOverlay overlay = new GraphicsOverlay();
        axMapView.getGraphicsOverlays().add(overlay);
        Graphic graphic = new Graphic(point, pictureMarkerSymbol);
        overlay.getGraphics().add(new Graphic(point, pictureMarkerSymbol));
    }
    private void drawTrajectory(double lon, double lat, BitmapDrawable bitmapDrawable){
        final PictureMarkerSymbol symbol = new PictureMarkerSymbol(bitmapDrawable);
        symbol.setWidth(40);
        symbol.setHeight(40);
        symbol.setOffsetY(10);
        symbol.loadAsync();
        Point point = new Point(lon, lat, SpatialReferences.getWgs84());
        symbol.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                GraphicsOverlay overlay = new GraphicsOverlay();
                overlay.getGraphics().add(new Graphic(point, symbol));
                axMapView.getGraphicsOverlays().add(overlay);
            }
        });
    }
}
