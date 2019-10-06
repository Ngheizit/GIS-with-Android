package com.ngheizit.myapplication.api;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;


public class GaodeAPI {
    private static final String _key  = "b61872fc8a151dd3cb56b2d48007b826";


    public static void Search_POI_Around(double lon, double lat, double radius, String keyword, int page, StringCallback callback){
        OkHttpUtils.get()
                .url("https://restapi.amap.com/v3/place/around")
                .addParams("key", _key)
                .addParams("location",  lon + "," + lat)
                .addParams("radius", String.valueOf(radius))
                .addParams("keywords", keyword)
                .addParams("page", String.valueOf(page))
                .build()
                .execute(callback);
    }

    public static void ReGeocoding(double lon, double lat, StringCallback callback){
        OkHttpUtils.get()
                .url("https://restapi.amap.com/v3/geocode/regeo")
                .addParams("key", _key)
                .addParams("location", lon + "," + lat)
                .addParams("extensions", "all")
                .addParams("radius", "500")
                .build()
                .execute(callback);
    }

}
