package com.example.quanlysinhvien.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.adapter.ClassesAdapter;
import com.example.quanlysinhvien.adapter.StudentAdapter;
import com.example.quanlysinhvien.helper.DateTimeHelper;
import com.example.quanlysinhvien.model.Classes;
import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.sqlite.ClassesDAO;
import com.example.quanlysinhvien.sqlite.StudentDAO;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.List;

public class ManageStudentActivity extends AppCompatActivity {

    Button btnSave, btnUpdate, btnClear, btnDelete;
    Spinner spClass;
    EditText edtStudentId, edtStudentName, edtDateOfBirth, edtPhoneNumber, edtEmail;
    RadioGroup rdoGroupGender;
    ListView lvStudent;
    List<Classes> classes;
    List<Student> students;
    StudentAdapter studentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.initWidget();
        fillClasses();
        try {
            loadData();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        btnSave.setOnClickListener(v -> {
            try {
                Student student = new Student();
                student.setId(edtStudentId.getText().toString());
                student.setName(edtStudentName.getText().toString());
                student.setDateOfBirth(DateTimeHelper.toDate(edtDateOfBirth.getText().toString()));
                student.setGender(rdoGroupGender.getCheckedRadioButtonId() == R.id.rdoMale ? "Nam" : "Nữ");
                student.setPhoneNumber(edtPhoneNumber.getText().toString());
                student.setEmail(edtEmail.getText().toString());
                Classes cls = (Classes) spClass.getSelectedItem();
                student.setClassId(cls.getId());

                StudentDAO dao = new StudentDAO(this);
                long result = dao.insert(student);
                if (result == -1) {
                    Snackbar snackbar = Snackbar.make(v, "Thêm thất bại", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(v, "Thêm thành bại", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    loadData();
                    clear();
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        });

        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    loadStudentByClass();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edtStudentId.setText(students.get(i).getId());
                edtStudentName.setText(students.get(i).getName());
                edtDateOfBirth.setText(DateTimeHelper.toString(students.get(i).getDateOfBirth()));
                edtPhoneNumber.setText(students.get(i).getPhoneNumber());
                edtEmail.setText(students.get(i).getEmail());
                rdoGroupGender.check(students.get(i).getGender().equals("Nam") ? R.id.rdoMale : R.id.rdoFemale);
                edtStudentId.setEnabled(false);
            }
        });

        btnUpdate.setOnClickListener(v -> {
            try {
                Student student = new Student();
                student.setId(edtStudentId.getText().toString());
                student.setName(edtStudentName.getText().toString());

                student.setDateOfBirth(DateTimeHelper.toDate(edtDateOfBirth.getText().toString()));

                student.setGender(rdoGroupGender.getCheckedRadioButtonId() == R.id.rdoMale ? "Nam" : "Nữ");
                student.setPhoneNumber(edtPhoneNumber.getText().toString());
                student.setEmail(edtEmail.getText().toString());
                Classes cls = (Classes) spClass.getSelectedItem();
                student.setClassId(cls.getId());
                StudentDAO dao = new StudentDAO(this);
                long result = dao.update(student);

                if (result == -1) {
                    Snackbar snackbar = Snackbar.make(v, "Cập nhật thất bại", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(v, "Cập nhật thành công", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    loadData();
                    clear();
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        btnDelete.setOnClickListener(v -> {
            String studentId = edtStudentId.getText().toString();
            StudentDAO dao = new StudentDAO(this);
            AlertDialog.Builder alert = new AlertDialog.Builder(ManageStudentActivity.this);
            alert.setCancelable(true);
            alert.setTitle("Xóa sinh viên");
            alert.setMessage("Bạn có chắc chắn muốn xóa sinh viên này?");
            alert.setPositiveButton("Yes", (dialogInterface, i1) -> {
                if (dao.delete(studentId) == -1) {
                    toast("Xoá thất bại!!!");
                } else {
                    toast("Xoá thành công!!!");
                    try {
                        loadData();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    clear();
                }

            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        });

        btnClear.setOnClickListener(v -> {
            clear();
            edtStudentId.setEnabled(true);
        });
    }

    private void fillClasses() {
        ClassesDAO dao = new ClassesDAO(this);
        classes = dao.getAll();
        ClassesAdapter adapter = new ClassesAdapter(classes, this);
        spClass.setAdapter(adapter);
    }

    private void initWidget() {
        btnSave = findViewById(R.id.btnSave);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnClear = findViewById(R.id.btnClear);
        btnDelete = findViewById(R.id.btnDelete);
        spClass = findViewById(R.id.spClass);
        edtStudentId = findViewById(R.id.edtStudentId);
        edtStudentName = findViewById(R.id.edtStudentName);
        edtDateOfBirth = findViewById(R.id.edtDateOfBirth);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtEmail = findViewById(R.id.edtEmail);
        rdoGroupGender = findViewById(R.id.rdoGroupGender);
        lvStudent = findViewById(R.id.lvStudent);
    }

    private void loadData() throws ParseException {
        StudentDAO dao = new StudentDAO(this);
        students = dao.getAll();
        studentAdapter = new StudentAdapter(this, students);
        lvStudent.setAdapter(studentAdapter);
    }

    private void loadStudentByClass() throws ParseException {
        StudentDAO studentDAO = new StudentDAO(this);
        Classes cls = (Classes) spClass.getSelectedItem();
        students = studentDAO.get("SELECT * FROM students WHERE classId = ?", cls.getId() + "");
        studentAdapter = new StudentAdapter(this, students);
        lvStudent.setAdapter(studentAdapter);
    }

    private void clear() {
        edtStudentId.setText("");
        edtStudentName.setText("");
        edtDateOfBirth.setText("");
        edtPhoneNumber.setText("");
        edtEmail.setText("");
        rdoGroupGender.check(R.id.rdoMale);
    }

    private void toast(String message) {
        Toast.makeText(ManageStudentActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}