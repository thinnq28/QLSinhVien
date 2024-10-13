package com.example.quanlysinhvien.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.model.Enrollment;
import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.sqlite.EnrollmentDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GradeAdapter extends BaseAdapter {

    private Context context;
    private List<Student> students;
    private List<Enrollment> enrollments;
    private Integer courseId;

    private final List<String> grades = new ArrayList<>(Arrays.asList("A", "B+", "B", "C+", "C", "D+", "D", "F"));

    public GradeAdapter(Context context, Integer courseId, List<Enrollment> enrollments, List<Student> students) {
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
        view = inflater.inflate(R.layout.layout_grade_item, viewGroup, false);

        TextView tvStudentId = view.findViewById(R.id.tvStudentId);
        TextView tvStudentName = view.findViewById(R.id.tvStudentName);
        EditText edtGrade = view.findViewById(R.id.edtGrade);
        Button btnGrade = view.findViewById(R.id.btnGrade);

        Student student = students.get(i);
        tvStudentId.setText(student.getId() + ": ");
        tvStudentName.setText(student.getName());

        Enrollment enrollment = enrollments.stream().filter(e -> Objects.equals(student.getId(), e.getStudentId())).findFirst().orElse(null);
        if (enrollment != null) {
            edtGrade.setText(enrollment.getGrade() + "");
        }

        EnrollmentDAO enrollmentDAO = new EnrollmentDAO(context);
        btnGrade.setOnClickListener(v -> {
            String grade = edtGrade.getText().toString();
            if (grade.isEmpty()) {
                Toast.makeText(context, "Bạn không được để trống điểm!!!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!grades.contains(grade)) {
                Toast.makeText(context, "Điểm bạn nhập không có trong thang giá trị được quy định!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (enrollment != null) {
                enrollment.setGrade(grade);
                long result = enrollmentDAO.update(enrollment);
                String message = (result != -1) ? "Cập nhật điểm thành công!" : "Cập nhật điểm thất bại!";
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

        });


        return view;
    }
}
