package com.example.pc_08.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by PC-00 on 2019/9/16.
 */

public class GetInternetActivity extends AppCompatActivity {

    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getinternet);

        tv_show = (TextView)findViewById(R.id.tv_show);

        OkHttpUtils.post()
                .url("http://api.tianditu.gov.cn/drive?postStr={\"orig\":\"116.35506,39.92277\",\"dest\":\"116.39751,39.90854\",\"mid\":\"116.36506,39.91277;116.37506,39.92077\",\"style\":\"0\"}&type=search&tk=f3f6ac205f23ada9a2692e8ce57d9fc7")
//                .addParams("type", "yuantong")
//                .addParams("postid", "887206386359524048")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        HashMap<?,?> m = XmlUtil.UnPackageXml(s);
                        Object routelatlon = m.get("routelatlon");
                        tv_show.setText(routelatlon.toString());
                    }
                });

    }

}
