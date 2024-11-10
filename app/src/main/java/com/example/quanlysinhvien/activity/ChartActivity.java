package com.example.quanlysinhvien.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.model.Enrollment;
import com.example.quanlysinhvien.sqlite.EnrollmentDAO;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String courseId = intent.getStringExtra("courseId");

        List<Enrollment> enrollments;
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO(this);
        enrollments = enrollmentDAO.getByCourseId(Integer.parseInt(courseId));
        PieChart chart = findViewById(R.id.chart);
        if(enrollments.isEmpty()) {
            return;
        }
        List<PieEntry> entries = new ArrayList<>();
        float gradeF = enrollments.stream().filter(e -> Objects.equals("F", e.getGrade())).count();
        float gradeD = enrollments.stream().filter(e -> Objects.equals("D", e.getGrade())).count();
        float gradeC = enrollments.stream().filter(e -> Objects.equals("C", e.getGrade())).count();
        float gradeB = enrollments.stream().filter(e -> Objects.equals("B", e.getGrade())).count();
        float gradeA = enrollments.stream().filter(e -> Objects.equals("A", e.getGrade())).count();

        entries.add(new PieEntry(gradeA, "A"));
        entries.add(new PieEntry(gradeB, "B"));
        entries.add(new PieEntry(gradeC, "C"));
        entries.add(new PieEntry(gradeD, "D"));
        entries.add(new PieEntry(gradeF, "F"));

        PieDataSet pieDataSet = new PieDataSet(entries, "Subjects");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(pieDataSet);

        chart.setData(pieData);
        chart.getDescription().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();

    }
}