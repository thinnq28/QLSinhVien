package com.example.quanlysinhvien.activity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ManageStudentActivity extends AppCompatActivity {

    Button btnSave, btnUpdate, btnClear, btnDelete, btnDatePicker;
    Spinner spClass;
    EditText edtStudentId, edtStudentName, edtDateOfBirth, edtPhoneNumber, edtEmail;
    RadioGroup rdoGroupGender;
    ListView lvStudent;
    List<Classes> classes;
    List<Student> students = new ArrayList<>();
    StudentAdapter studentAdapter;


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        doWork();
        fillClasses();
        try {
            loadData();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        btnSave.setOnClickListener(v -> {
            add(v);
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
            update(v);
        });

        btnDelete.setOnClickListener(v -> {
            delete();
        });

        btnClear.setOnClickListener(v -> {
            clear();
            edtStudentId.setEnabled(true);
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doWork() {
        btnDatePicker.setOnClickListener(v -> {
            DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    edtDateOfBirth.setText(i2 + "/" + (i1 + 1) + "/" + i);
                }
            };
            LocalDate localDate = LocalDate.now();
            DatePickerDialog pic = new DatePickerDialog(ManageStudentActivity.this, onDateSetListener, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
            pic.setTitle("Date time picker");
            pic.show();
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
        edtDateOfBirth.setEnabled(false);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtEmail = findViewById(R.id.edtEmail);
        rdoGroupGender = findViewById(R.id.rdoGroupGender);
        lvStudent = findViewById(R.id.lvStudent);
        btnDatePicker = findViewById(R.id.btnDatePicker);
    }

    private void loadData() throws ParseException {
        StudentDAO dao = new StudentDAO(this);
        students.clear();
        students = dao.getAll();
        studentAdapter = new StudentAdapter(this, students);
        lvStudent.setAdapter(studentAdapter);
    }

    private void loadStudentByClass() throws ParseException {
        StudentDAO studentDAO = new StudentDAO(this);
        Classes cls = (Classes) spClass.getSelectedItem();
        students = studentDAO.get("SELECT * FROM students WHERE classId = ? and active = 1", cls.getId() + "");
        studentAdapter = new StudentAdapter(this, students);
        lvStudent.setAdapter(studentAdapter);
    }

    private void clear() {
        edtStudentId.setText("");
        edtStudentName.setText("");
        edtDateOfBirth.setText("");
        edtPhoneNumber.setText("");
        edtEmail.setText("");
        edtStudentId.setEnabled(false);
        rdoGroupGender.check(R.id.rdoMale);
    }

    private void toast(String message) {
        Toast.makeText(ManageStudentActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void add(View v) {
        try {

            if (validate(true)) {
                return;
            }

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
                Snackbar snackbar = Snackbar.make(v, "Thêm thành công", Snackbar.LENGTH_SHORT);
                snackbar.show();
                loadData();
                clear();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(View v) {
        try {

            if(validate(false)) {
                return;
            }

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
    }

    private void delete() {
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
    }

    private boolean validate(boolean isInsert) throws ParseException {
        String id = edtStudentId.getText().toString();
        String name = edtStudentName.getText().toString();
        String dateOfBirth = edtDateOfBirth.getText().toString();
        String phoneNumber = edtPhoneNumber.getText().toString();
        String email = edtEmail.getText().toString();

        if (id.isEmpty()) {
            Toast.makeText(this, "Mã sinh viên không được để trống", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (name.isEmpty()) {
            Toast.makeText(this, "Tên sinh viên không được để trống", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (dateOfBirth.isEmpty()) {
            Toast.makeText(this, "Ngày sinh không được để trống", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (rdoGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Giới tính không được để trống", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email không được để trống", Toast.LENGTH_SHORT).show();
            return true;
        }

        String patternOfPhoneNumber = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
        if (!phoneNumber.matches(patternOfPhoneNumber)) {
            Toast.makeText(this, "Số điện thoại không đúng định dạng", Toast.LENGTH_SHORT).show();
            return true;
        }

        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (!matcher.matches()) {
            Toast.makeText(this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (isInsert) {
            StudentDAO studentDAO = new StudentDAO(this);

            List<Student> students = studentDAO.findById(id);
            if (!students.isEmpty()) {
                Toast.makeText(this, "Mã sinh viên đã tồn tại", Toast.LENGTH_SHORT).show();
                return true;
            }

            students = studentDAO.get("SELECT * FROM students WHERE email = ? and active = 1", email);
            if (!students.isEmpty()) {
                Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                return true;
            }

            students = studentDAO.get("SELECT * FROM students WHERE phoneNumber = ? and active = 1", phoneNumber);
            if (!students.isEmpty() && isInsert) {
                Toast.makeText(this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                return true;
            }

        } else {

            StudentDAO studentDAO = new StudentDAO(this);

            List<Student> students = studentDAO.findById(id);

            String emailOfStudent = students.get(0).getEmail();
            String phoneNumberOfStudent = students.get(0).getPhoneNumber();

            students = studentDAO.get("SELECT * FROM students WHERE email = ? and active = 1", email);
            students = students.stream().filter(e -> !Objects.equals(e.getEmail(), emailOfStudent)).collect(Collectors.toList());
            if (!students.isEmpty()) {
                Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                return true;
            }

            students = studentDAO.get("SELECT * FROM students WHERE phoneNumber = ? and active = 1", phoneNumber);
            students = students.stream().filter(e -> !Objects.equals(e.getPhoneNumber(), phoneNumberOfStudent)).collect(Collectors.toList());
            if (!students.isEmpty()) {
                Toast.makeText(this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return false;
    }

}