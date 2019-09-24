package com.ngheizit.myapplication.arcgisruntime;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngheizit.myapplication.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter {

    // 全局变量
    private final int groupId;
    private final ArrayList<ItemData> list;
    private final LayoutInflater inflater;

    // 构造函数
    public SpinnerAdapter(Activity context, int groupId, int id, ArrayList<ItemData> list){
        super(context, id, list);
        this.list = list;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupId = groupId;
    }

    public View getView(int position, View converView, ViewGroup parent){
        View itemView = inflater.inflate(groupId, parent, false);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());
        TextView textView = (TextView)itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getText());
        return  itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position, convertView, parent);
    }

}
