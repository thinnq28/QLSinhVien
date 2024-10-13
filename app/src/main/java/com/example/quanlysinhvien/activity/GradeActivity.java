package com.example.quanlysinhvien.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.adapter.CourseAdapter;
import com.example.quanlysinhvien.adapter.GradeAdapter;
import com.example.quanlysinhvien.adapter.StudentAdapter;
import com.example.quanlysinhvien.model.Course;
import com.example.quanlysinhvien.model.Enrollment;
import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.sqlite.CourseDAO;
import com.example.quanlysinhvien.sqlite.EnrollmentDAO;
import com.example.quanlysinhvien.sqlite.StudentDAO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GradeActivity extends AppCompatActivity {


    Spinner spCourse;
    ListView lvStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grade);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        spCourse = findViewById(R.id.spCourse);
        lvStudent = findViewById(R.id.lvStudent);

        StudentAdapter adapter = new StudentAdapter(this, new ArrayList<>());
        lvStudent.setAdapter(adapter);

        CourseDAO courseDAO = new CourseDAO(this);
        List<Course> courses = courseDAO.getAll();
        CourseAdapter courseAdapter = new CourseAdapter(this, courses);
        spCourse.setAdapter(courseAdapter);

        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    loadStudentByEnrollment(courses.get(i).getCourseId());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadStudentByEnrollment(Integer courseId) throws ParseException {
        StudentDAO studentDAO = new StudentDAO(this);
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO(this);
        Course course = (Course) spCourse.getSelectedItem();
        List<Enrollment> enrollments = enrollmentDAO.get("SELECT * FROM enrollments WHERE courseId = ?", course.getCourseId() + "");

        List<String> studentIds = enrollments.stream().map(Enrollment::getStudentId).collect(Collectors.toList());
        List<Student> students = studentDAO.get("SELECT * FROM students WHERE id IN (" + String.join(",", studentIds) + ")");
        GradeAdapter gradeAdapter = new GradeAdapter(this, courseId, enrollments,students);
        lvStudent.setAdapter(gradeAdapter);
    }
}