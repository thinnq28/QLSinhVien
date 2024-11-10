package com.example.quanlysinhvien;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysinhvien.activity.ClassesActivity;
import com.example.quanlysinhvien.activity.CourseActivity;
import com.example.quanlysinhvien.activity.EnrollmentActivity;
import com.example.quanlysinhvien.activity.GradeActivity;
import com.example.quanlysinhvien.activity.LoginActivity;
import com.example.quanlysinhvien.activity.ManageStudentActivity;
import com.example.quanlysinhvien.activity.RegisterEnrollmentActivity;
import com.example.quanlysinhvien.activity.UserActivity;
import com.example.quanlysinhvien.dialog.NewClassDialog;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnCreateClass, btnGetClass, btnManageStudent, btnStatistical;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initWidget();


        btnCreateClass.setOnClickListener(this);
        btnGetClass.setOnClickListener(this);
        btnManageStudent.setOnClickListener(this);
        btnStatistical.setOnClickListener(this);
    }

    private void initWidget() {
        btnCreateClass = findViewById(R.id.btnCreateClass);
        btnGetClass = findViewById(R.id.btnGetClass);
        btnManageStudent = findViewById(R.id.btnManageStudent);
        btnStatistical = findViewById(R.id.btnStatistical);

    }

    @Override
    public void onClick(View view) {
        if (Objects.equals(view.getId(), R.id.btnCreateClass)) {
            NewClassDialog dialog = new NewClassDialog(this);
            dialog.show();
        } else if (Objects.equals(view.getId(), R.id.btnGetClass)) {
            Intent intent = new Intent(this, ClassesActivity.class);
            startActivity(intent);
        } else if (Objects.equals(view.getId(), R.id.btnManageStudent)) {
            Intent intent = new Intent(this, ManageStudentActivity.class);
            startActivity(intent);
        } else if (Objects.equals(view.getId(), R.id.btnStatistical)) {
            Intent intent = new Intent(this, EnrollmentActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        int itemId = item.getItemId();

        if (itemId == R.id.course) {
            intent = new Intent(this, CourseActivity.class);
        } else if (itemId == R.id.registerEnrollment) {
            intent = new Intent(this, RegisterEnrollmentActivity.class);
        } else if (itemId == R.id.grade) {
            intent = new Intent(this, GradeActivity.class);
        } else if (itemId == R.id.user) {
            intent = new Intent(this, UserActivity.class);
        } else {
            clearAutoLogin();
            clearUsername();
            intent = new Intent(this, LoginActivity.class);

        }

        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearAutoLogin() {
        SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private void clearUsername() {
        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}