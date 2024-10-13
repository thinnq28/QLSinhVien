package com.example.quanlysinhvien.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.adapter.CourseAdapter;
import com.example.quanlysinhvien.model.Course;
import com.example.quanlysinhvien.sqlite.CourseDAO;

import java.util.List;

public class RegisterEnrollmentActivity extends AppCompatActivity {

    ListView lvEnrollment;
    Button btnSearch;
    EditText edtCourseName;
    List<Course> courses;
    CourseAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_enrollment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lvEnrollment = findViewById(R.id.lvEnrollment);
        btnSearch = findViewById(R.id.btnSearch);
        edtCourseName = findViewById(R.id.edtCourseName);

        CourseDAO dao = new CourseDAO(this);
        courses = dao.getAll();
        courseAdapter = new CourseAdapter(this, courses);
        lvEnrollment.setAdapter(courseAdapter);

        lvEnrollment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RegisterEnrollmentActivity.this, RegisterStudentForCourseActivity.class);

                intent.putExtra("courseId", courses.get(i).getCourseId());
                intent.putExtra("courseName", courses.get(i).getCourseName());
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(v -> {
            String courseName = edtCourseName.getText().toString();
            if (courseName.isEmpty()){
                courses = dao.getAll();
                courseAdapter = new CourseAdapter(this, courses);
                lvEnrollment.setAdapter(courseAdapter);
            }else{
                List<Course> courseByName = dao.findByName(courseName);
                courseAdapter = new CourseAdapter(this, courseByName);
                lvEnrollment.setAdapter(courseAdapter);
            }

        });
    }
}