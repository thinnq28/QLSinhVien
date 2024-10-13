package com.example.quanlysinhvien.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.adapter.RegisterStudentForCourseAdapter;
import com.example.quanlysinhvien.model.Enrollment;
import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.sqlite.EnrollmentDAO;
import com.example.quanlysinhvien.sqlite.StudentDAO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RegisterStudentForCourseActivity extends AppCompatActivity {

    ListView lvStudent;
    TextView tvCourseName;
    Button btnSearch;
    EditText edtStudentId;
    List<Enrollment> enrollments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_student_for_course);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvStudent = findViewById(R.id.lvStudent);
        tvCourseName = findViewById(R.id.tvCourseName);
        edtStudentId = findViewById(R.id.edtStudentId);
        btnSearch = findViewById(R.id.btnSearch);

        Intent intent = getIntent();
        int courseId = intent.getIntExtra("courseId", 0);
        String courseName = intent.getStringExtra("courseName");
        tvCourseName.setText("Tên khoá học: " + courseName);

        EnrollmentDAO enrollmentDAO = new EnrollmentDAO(this);
        try {
            enrollments = enrollmentDAO.get("SELECT * FROM enrollments WHERE courseId = ?", courseId + "");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<Student> students;
        StudentDAO dao = new StudentDAO(this);

        students = dao.getAll();

        setListView(enrollments, courseId, students);

        StudentDAO studentDAO = new StudentDAO(this);
        btnSearch.setOnClickListener(v -> {
            String studentId = edtStudentId.getText().toString();
            List<Student> studentById = new ArrayList<>();
            if (studentId.isEmpty()) {
                studentById = dao.getAll();
                setListView(enrollments, courseId, studentById);
            } else {

                studentById = studentDAO.findById(studentId);
                if (studentById.isEmpty()) {
                    Toast.makeText(this, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
                } else {
                    setListView(enrollments, courseId, studentById);
                }

            }
        });

    }

    private void setListView(List<Enrollment> enrollments, int courseId, List<Student> students) {
        RegisterStudentForCourseAdapter adapter = new RegisterStudentForCourseAdapter(this, courseId, enrollments, students);
        lvStudent.setAdapter(adapter);
    }
}