package com.ngheizit.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ngheizit.myapplication.R;

public class LinksActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        int id = v.getId(); // 获取被点击的控件的名字
        Intent intent;
        switch (id){
            case R.id.links_tian_di_tu:
                intent = new Intent(getApplicationContext(), TianDiTuActivity.class);
                startActivity(intent);
                break;
            case R.id.links_question:
                intent = new Intent(getApplicationContext(), QuestionActivity.class);
                startActivity(intent);
                break;
            case R.id.links_device_gps:
                intent = new Intent(getApplicationContext(), DeviceGpsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        CreateItem(R.id.links_question, "解题达人", "迎接审判吧！");
        CreateItem(R.id.links_tian_di_tu, "天地图", "调用天地图API");
        CreateItem(R.id.links_device_gps, "GPS轨迹", "设备位置测试");

    }



    // 创建链接方法
    private void CreateItem(int id, String title, String description){
        LinearLayout rl = new LinearLayout(this);
        rl.setOnClickListener(this);
        rl.setBackgroundResource(R.drawable.corsers_bg);
        rl.setPadding(15,10,15,10);
        rl.setOrientation(LinearLayout.VERTICAL);

        LinearLayout ll_dirList = (LinearLayout)findViewById(R.id.axLinearLayout_linksList);
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
}
