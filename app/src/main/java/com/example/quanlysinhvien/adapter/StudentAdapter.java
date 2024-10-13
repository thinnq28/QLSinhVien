package com.example.quanlysinhvien.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.model.Student;

import org.w3c.dom.Text;

import java.util.List;

public class StudentAdapter extends BaseAdapter {

    private Context context;
    private List<Student> students;

    public StudentAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int i) {
        return students.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        view = inflater.inflate(R.layout.layout_student_item, viewGroup, false);

        TextView tvStudentId = view.findViewById(R.id.tvStudentId);
        TextView tvStudentName = view.findViewById(R.id.tvStudentName);

        Student student = students.get(i);
        tvStudentId.setText(student.getId() + ": ");
        tvStudentName.setText(student.getName());


        return view;
    }
}
