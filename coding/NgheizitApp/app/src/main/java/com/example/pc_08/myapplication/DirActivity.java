package com.example.pc_08.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.esri.arcgisruntime.mapping.ArcGISMap;
//import com.esri.arcgisruntime.mapping.Basemap;
//import com.esri.arcgisruntime.mapping.view.MapView;

import org.w3c.dom.Text;

import java.security.PublicKey;
import java.util.function.Function;

public class DirActivity extends AppCompatActivity {

//    private MapView mMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dir);

        Toast.makeText(getApplicationContext(), "W e l c o m e ~", Toast.LENGTH_SHORT).show();

        this.CreateItem(R.id.dir_item_getInternet, "网络请求", "第三周");
        LinearLayout ll_getInternet = (LinearLayout)findViewById(R.id.dir_item_getInternet);
        ll_getInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GetInternetActivity.class);
                startActivity(intent);
            }
        });

        this.CreateItem(R.id.dir_item_callphone, "Call Phone", "拨号");
        LinearLayout ll_callphone = (LinearLayout)findViewById(R.id.dir_item_callphone);
        ll_callphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CallphoneActivity.class);
                startActivity(intent);
            }
        });


        this.CreateItem(R.id.dir_item_mgWebsite, "Personal Website", "个人网站：ngheizit.fun");
        LinearLayout ll_myWebsite = (LinearLayout)findViewById(R.id.dir_item_mgWebsite);
        ll_myWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://ngheizit.fun");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });



        this.CreateItem(R.id.dir_item_myGithub, "Personal Github", "Github账户：Ngheizit");
        LinearLayout ll_myGithub = (LinearLayout)findViewById(R.id.dir_item_myGithub);
        ll_myGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/Ngheizit");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });





//        mMapView = (MapView) findViewById(R.id.mapView);
//        setupMap();
    }


    private void CreateItem(int id, String title, String description){
        LinearLayout rl = new LinearLayout(this);
        rl.setBackgroundResource(R.drawable.corsers_bg);
        rl.setPadding(10,5,10,5);
        rl.setOrientation(LinearLayout.VERTICAL);

        LinearLayout ll_dirList = (LinearLayout)findViewById(R.id.ll_dirList);
        ll_dirList.addView(rl);


        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rl.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.topMargin = 15;
        rl.setLayoutParams(params);

        TextView tv_title = new TextView(this);
        // tv_title.setHeight(50);
        tv_title.setPadding(0, 0, 0, 10);
        tv_title.setText(title);
        tv_title.setTextAppearance(this, R.style.DirListItem_title);
        rl.addView(tv_title);

        TextView tv_description = new TextView(this);
        tv_description.setText(description);
        tv_description.setTextAppearance(this, R.style.DirListItem_description);
        tv_description.setGravity(Gravity.RIGHT);
        rl.addView(tv_description);
        rl.setId(id);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(getApplicationContext(), "!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onTouchEvent(event);
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
