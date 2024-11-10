package com.example.quanlysinhvien.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.adapter.ClassesAdapter;
import com.example.quanlysinhvien.adapter.CourseAdapter;
import com.example.quanlysinhvien.model.Classes;
import com.example.quanlysinhvien.model.Course;
import com.example.quanlysinhvien.sqlite.ClassesDAO;
import com.example.quanlysinhvien.sqlite.CourseDAO;

import java.util.List;

public class CourseActivity extends AppCompatActivity {

    Button btnAdd, btnUpdate;
    ListView lvCourse;
    CourseAdapter adapter;
    EditText edtCourseId, edtCourseName, edtCourseDescription, edtCredits;
    private List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.initWidget();

        CourseDAO dao = new CourseDAO(this);
        loadData();
        btnAdd.setOnClickListener(v -> {
            add();
        });

        btnUpdate.setOnClickListener(v -> {

        });

        lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edtCourseId.setText(courses.get(i).getCourseId() + "");
                edtCourseName.setText(courses.get(i).getCourseName());
                edtCourseDescription.setText(courses.get(i).getCourseDescription());
                edtCredits.setText(courses.get(i).getCredits() + "");
            }
        });

        lvCourse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CourseActivity.this);
                alert.setCancelable(true);
                alert.setTitle("Xóa khoá học");
                alert.setMessage("Bạn có chắc chắn muốn xóa khoá học này?");
                alert.setPositiveButton("Yes", (dialogInterface, i1) -> {
                    if (dao.delete(courses.get(i).getCourseId() + "") == -1) {
                        toast("Xoá thất bại!!!");
                    } else {
                        toast("Xoá thành công!!!");
                        loadData();
                        clear();
                    }

                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                return false;
            }
        });

    }

    private void initWidget() {
        lvCourse = findViewById(R.id.lvCourse);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        edtCourseId = findViewById(R.id.edtClassId);
        edtCourseId.setEnabled(false);
        edtCourseName = findViewById(R.id.edtClassName);
        edtCourseDescription = findViewById(R.id.edtCourseDescription);
        edtCredits = findViewById(R.id.edtCredits);
    }

    private void loadData() {
        CourseDAO dao = new CourseDAO(this);
        courses = dao.getAll();
        adapter = new CourseAdapter(this, courses);
        lvCourse.setAdapter(adapter);
    }


    private void toast(String message) {
        Toast.makeText(CourseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void clear(){
        edtCourseId.setText("");
        edtCourseName.setText("");
        edtCourseDescription.setText("");
        edtCredits.setText("");
    }

    private void add() {
        CourseDAO dao = new CourseDAO(this);
        String courseName = edtCourseName.getText().toString();
        String courseDescription = edtCourseDescription.getText().toString();
        String credits = edtCredits.getText().toString();

        if(courseName.isEmpty()) {
            Toast.makeText(this, "Tên khoá học không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        if(credits.isEmpty()) {
            Toast.makeText(this, "Số tín chỉ không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer createInt = Integer.parseInt(credits);

        if(createInt <= 0) {
            Toast.makeText(this, "Số tín chỉ phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Course course = new Course();
        course.setCourseName(courseName);
        course.setCourseDescription(courseDescription);
        course.setCredits(createInt);
        long result = dao.insert(course);
        if (result == -1) {
            toast("Thêm thất bại");
        } else {
            toast("Thêm thành công");
            loadData();
        }
    }

    private void update() {
        CourseDAO dao = new CourseDAO(this);

        String courseName = edtCourseName.getText().toString();
        String credits = edtCredits.getText().toString();

        if(courseName.isEmpty()) {
            Toast.makeText(this, "Tên khoá học không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        if(credits.isEmpty()) {
            Toast.makeText(this, "Số tín chỉ không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer createInt = Integer.parseInt(credits);

        if(createInt <= 0) {
            Toast.makeText(this, "Số tín chỉ phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        Course course = new Course();
        course.setCourseId(Integer.parseInt(edtCourseId.getText().toString()));
        course.setCourseName(courseName);
        course.setCourseDescription(edtCourseDescription.getText().toString());
        course.setCredits(Integer.parseInt(credits));

        long result = dao.update(course);
        if (result == -1) {
            toast("Cập nhật thất bại");
        } else {
            toast("Cập nhật thành công");
            loadData();
            clear();
        }
    }
}