package com.example.xizhemap.displaydevicelaocion;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.xizhemap.R;

import java.util.ArrayList;

// 选择适配器
public class SpinnerAdapter extends ArrayAdapter<ItemData> {

    private final int groupid;
    private final ArrayList<ItemData> list;
    private final LayoutInflater inflater;

    public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<ItemData> list){
        super(context, id, list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
    }

    public View getView(int positon, View convertView, ViewGroup parent){
        View itemView = inflater.inflate(groupid, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(positon).getImageId());
        TextView textView = (TextView) itemView.findViewById(R.id.txt);
        textView.setText(list.get(positon).getText());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position, convertView, parent);
    }

}
