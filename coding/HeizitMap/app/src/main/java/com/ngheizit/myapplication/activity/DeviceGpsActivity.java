package com.ngheizit.myapplication.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.ngheizit.myapplication.R;
import com.ngheizit.myapplication.ToastUtil;
import com.ngheizit.myapplication.arcgisruntime.ItemData;
import com.ngheizit.myapplication.arcgisruntime.PointClass;
import com.ngheizit.myapplication.arcgisruntime.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DeviceGpsActivity extends AppCompatActivity {

    // 全局变量
    private MapView pMapView;
    private LocationDisplay pLocationDisplay;
    private Spinner pSpinner;
    private int requestCode = 2;
    String[] reqPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, // 精确定位
            Manifest.permission.ACCESS_COARSE_LOCATION // 初略定位
    };
    private TextView tv_distance;
    private TextView tv_time_ms;
    private TextView tv_time_kmh;
    private Button btn_Trajectory;
    private boolean isTrajectory = false;
    private long startTime, nowTime;
    private Date date;
    private LocationManager locationManager; // 位置管理器
    private List<PointClass> pointList = new ArrayList<>(); // 轨迹记录点

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_gps);

        // 控件捆绑
        this.pSpinner = findViewById(R.id.spinner);
        this.pMapView = findViewById(R.id.axMapView);
        this.tv_distance = findViewById(R.id.tv_distance);
        this.tv_time_ms = findViewById(R.id.tv_time);
        this.tv_time_kmh = findViewById(R.id.tv_time2);
        this.btn_Trajectory = findViewById(R.id.btn_trajectory);

        // 生成底图
        ArcGISMap pMap = new ArcGISMap(Basemap.createOpenStreetMap());
        pMap.loadAsync();
        this.pMapView.setMap(pMap);

        // 获得MapView的LocationDisplay，并设置位置数据源状态监听事件
        this.pLocationDisplay = this.pMapView.getLocationDisplay();

        this.pLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {
                // 检查LocationDisplay是否正常开启状态并且没有错误报告，一切正常就中断方法
                if(dataSourceStatusChangedEvent.isStarted() && dataSourceStatusChangedEvent.getError() == null)
                    return;
                else { // 发现错误，处理启动失败的问题
                    // 检查权限
                    boolean permissionCheck1 = ContextCompat.checkSelfPermission(DeviceGpsActivity.this, reqPermissions[0]) == PackageManager.PERMISSION_GRANTED;
                    boolean permissionCheck2 = ContextCompat.checkSelfPermission(DeviceGpsActivity.this, reqPermissions[1]) == PackageManager.PERMISSION_GRANTED;
                    if(!(permissionCheck1 && permissionCheck2)){
                        // 向用户请求权限
                        ActivityCompat.requestPermissions(DeviceGpsActivity.this, reqPermissions, requestCode);
                    }else {
                        // 报告未知故障，并更新用户界面以反映位置显示实际上并未启动
                        String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                        ToastUtil.showToast(message);
                        pSpinner.setSelection(0, true);
                    }
                }
            }
        });

        // 填充Spinner
        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData("Stop", R.drawable.locationdisplaydisabled));
        list.add(new ItemData("On", R.drawable.locationdisplayon));
        list.add(new ItemData("Re-Center", R.drawable.locationdisplayrecenter));
        list.add(new ItemData("Navigation", R.drawable.locationdisplaynavigation));
        list.add(new ItemData("Compass", R.drawable.locationdisplayheading));

        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list);
        this.pSpinner.setAdapter(adapter);
        this.pSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        // 停止位置显示
                        if(pLocationDisplay.isStarted())
                            pLocationDisplay.stop();
                        break;
                    case 1:
                        // 开启位置显示（默认方式）
                        if(!pLocationDisplay.isStarted())
                            pLocationDisplay.startAsync();
                        break;
                    case 2:
                        // 自动居中定位
                        pLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                        if(!pLocationDisplay.isStarted())
                            pLocationDisplay.startAsync();
                        break;
                    case 3:
                        // 导航定位模式
                        pLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
                        if (!pLocationDisplay.isStarted())
                            pLocationDisplay.startAsync();
                        break;
                    case 4:
                        // 罗盘定位模式
                        pLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                        if (!pLocationDisplay.isStarted())
                            pLocationDisplay.startAsync();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btn_Trajectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTrajectory == false){
                    btn_Trajectory.setText("END");
                    isTrajectory = true;
                    date = new Date();
                    startTime = date.getTime();
                    return;
                }
                if(isTrajectory == true){
                    btn_Trajectory.setText("START");
                    isTrajectory = false;
                    return;
                }
            }
        });

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String url_china = "https://ngheizit.fun/Older/img/Woodstock.png";
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(url_china);
        symbol.loadAsync();
        symbol.setHeight(20);
        symbol.setWidth(20);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 5, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    if (!isTrajectory)
                        return;
                    double distance = 0.0; // 距离
                    double lon = location.getLongitude(); // 当前位置经度
                    double lat = location.getLatitude(); // 当前位置纬度
                    pointList.add(new PointClass(lon, lat));
                    PointCollection borderCAtoNV = new PointCollection(SpatialReferences.getWgs84());
                    for(int i = 0; i < pointList.size(); i++){
                        borderCAtoNV.add(pointList.get(i).Lon, pointList.get(i).Lat);
                        if(i > 0){
                            distance += PointClass.GetDistance(pointList.get(i - 1), pointList.get(i));
                        }
                    }
                    date = new Date();
                    nowTime = date.getTime();
                    double second = (nowTime - startTime) / 1000.0;
                    double m = distance * 1000;
                    double ms = Math.round((m/second) * 10) / 10.0;
                    distance = Math.round(distance * 1000) / 1000.0;
                    tv_distance.setText(distance + "km");
                    tv_time_ms.setText(String.format("%s m/s", ms));
                    tv_time_kmh.setText((Math.round(ms * 3.6 * 10) / 10.0 + " km/h"));

//                    Polyline polyline = new Polyline(borderCAtoNV);
                    Point point = new Point(lon, lat, SpatialReferences.getWgs84());
                    GraphicsOverlay overlay = new GraphicsOverlay();
                    pMapView.getGraphicsOverlays().add(overlay);
                    overlay.getGraphics().add(new Graphic(point, symbol));

                }catch (Exception e){ }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        });

    }






    // 其他设置
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Location permission was granted. This would have been triggered in response to failing to start the
            // LocationDisplay, so try starting this again.
            pLocationDisplay.startAsync();
        } else {
            // If permission was denied, show toast to inform user what was chosen. If LocationDisplay is started again,
            // request permission UX will be shown again, option should be shown to allow never showing the UX again.
            // Alternative would be to disable functionality so request is not shown again.
            Toast.makeText(DeviceGpsActivity.this, "something no good", Toast
                    .LENGTH_SHORT).show();

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
}
