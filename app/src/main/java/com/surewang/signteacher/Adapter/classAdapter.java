package com.surewang.signteacher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.surewang.signteacher.R;
import com.surewang.signteacher.ViewHolder.ClassItemVH;
import com.surewang.signteacher.entity.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class classAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    List<classes> listItem;

    public classAdapter(Context context,List<classes> listItem){
        this.inflater = LayoutInflater.from(context);
        this.listItem = listItem;

    }
    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassItemVH holder;
        if(convertView==null){
            holder = new ClassItemVH();
            convertView = inflater.inflate(R.layout.item,null);
            holder.classCode =  (TextView) convertView.findViewById(R.id.classCode);
            holder.className = (TextView)convertView.findViewById(R.id.className);
            holder.classNumber = (TextView)convertView.findViewById(R.id.classNumber);
            holder.classId  = (TextView)convertView.findViewById(R.id.classId);
            convertView.setTag(holder);
        }else{
            holder = (ClassItemVH)convertView.getTag();
        }
        holder.classNumber.setText(String.valueOf(listItem.get(position).getClassNumber()));
        holder.className.setText((String)listItem.get(position).getClassName());
        holder.classCode.setText((String)listItem.get(position).getClassKey());
        holder.classId.setText(String.valueOf((int)listItem.get(position).getClassId()));
        return convertView;
    }
}
