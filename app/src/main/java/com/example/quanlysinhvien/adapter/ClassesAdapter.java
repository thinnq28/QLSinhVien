package com.example.quanlysinhvien.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.model.Classes;

import java.util.List;

public class ClassesAdapter extends BaseAdapter {

    private Context context;
    private List<Classes> classes;

    public ClassesAdapter(List<Classes> classes, Context context) {
        this.classes = classes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return classes.size();
    }

    @Override
    public Object getItem(int i) {
        return classes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        view = inflater.inflate(R.layout.layout_classes_item, viewGroup, false);

        TextView txtClassId = view.findViewById(R.id.tvClassId);
        TextView txtClassName = view.findViewById(R.id.tvClassName);
        Classes c = classes.get(i);

        txtClassName.setText(c.getName());
        txtClassId.setText(c.getId() + "");

        return view;
    }
}
