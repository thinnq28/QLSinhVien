package com.example.quanlysinhvien.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.model.Enrollment;
import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.sqlite.EnrollmentDAO;

import java.util.List;

public class RegisterStudentForCourseAdapter extends BaseAdapter {
    private Context context;
    private List<Student> students;
    private List<Enrollment> enrollments;
    private int courseId;

    public RegisterStudentForCourseAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
    }

    public RegisterStudentForCourseAdapter(Context context, List<Student> students, List<Enrollment> enrollments) {
        this.context = context;
        this.students = students;
        this.enrollments = enrollments;
    }

    public RegisterStudentForCourseAdapter(Context context, int courseId, List<Enrollment> enrollments, List<Student> students) {
        this.context = context;
        this.courseId = courseId;
        this.enrollments = enrollments;
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
        view = inflater.inflate(R.layout.layout_register_student_for_course, viewGroup, false);

        TextView tvStudentId = view.findViewById(R.id.tvStudentId);
        TextView tvStudentName = view.findViewById(R.id.tvStudentName);
        CheckBox chkStudent = view.findViewById(R.id.chkStudent);

        Student student = students.get(i);
        tvStudentId.setText(student.getId() + ": ");
        tvStudentName.setText(student.getName());

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId().equals(student.getId())) {
                chkStudent.setChecked(true);
                break;
            }
        }

        EnrollmentDAO enrollmentDAO = new EnrollmentDAO(context);
        // Set CheckBox click listener
        chkStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle CheckBox click
                if(chkStudent.isChecked()){
                    // Thực hiện các thao tác đăng ký ở đây
                    Enrollment enrollment = new Enrollment();
                    enrollment.setStudentId(student.getId());
                    enrollment.setCourseId(courseId);
                    enrollment.setGrade("F");
                    enrollmentDAO.insert(enrollment);

                    Toast.makeText(context, "Đã đăng ký", Toast.LENGTH_SHORT).show();
                }else{
                    // Thực hiện các thao tác hủy đăng ký ở đây
                    enrollmentDAO.delete(student.getId(), courseId);

                    Toast.makeText(context, "Đã hủy đăng ký", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
