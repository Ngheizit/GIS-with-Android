package com.example.xizhemap;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class DirActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v){
        int id = v.getId(); // 获取被点击的控件的名字
        Intent intent;
        switch (id){
            case R.id.dir_items_AddFeatures:
                intent = new Intent(getApplicationContext(), AddFeaturesActivity.class);
                startActivity(intent);
                break;
            case R.id.dir_items_DisplayDeviceLocation:
                intent = new Intent(getApplicationContext(), DisplayDeviceLocationActivity.class);
                startActivity(intent);
                break;
            case R.id.dir_items_ChangeSublayerVisibility:
                intent = new Intent(getApplicationContext(), ChangeSublayerVisibilityActivity.class);
                startActivity(intent);
                break;
            case R.id.dir_items_Question:
                intent = new Intent(getApplicationContext(), QuestionActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dir);

        CreateItem(R.id.dir_items_AddFeatures, "Add features", "Add new features to an online feature service");
        CreateItem(R.id.dir_items_DisplayDeviceLocation, "Display Device Location", "Show Location from device");
        CreateItem(R.id.dir_items_ChangeSublayerVisibility, "Change Sublayer Visibility", "Toggle visibility of the map's sublayers");
        CreateItem(R.id.dir_items_Question, "课程知识点汇总", "Questions");



































    }

    private void CreateItem(int id, String title, String description){
        LinearLayout rl = new LinearLayout(this);
        rl.setOnClickListener(this);
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
}
