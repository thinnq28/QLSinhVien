package com.example.quanlysinhvien.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.model.Course;

import java.util.List;

public class CourseAdapter extends BaseAdapter {

    private Context context;
    private List<Course> courses;

    public CourseAdapter(Context context, List<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int i) {
        return courses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        view = inflater.inflate(R.layout.layout_course_item, viewGroup, false);

        TextView tvCourseId = view.findViewById(R.id.tvCourseId);
        TextView tvCourseName = view.findViewById(R.id.tvCourseName);

        tvCourseId.setText(courses.get(i).getCourseId() + "");
        tvCourseName.setText(courses.get(i).getCourseName());

        return view;
    }
}
