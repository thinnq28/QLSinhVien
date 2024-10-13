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
import com.example.quanlysinhvien.model.Classes;
import com.example.quanlysinhvien.sqlite.ClassesDAO;

import java.util.List;

public class ClassesActivity extends AppCompatActivity {

    ListView lvClass;
    EditText edtClassId, edtClassName;
    Button btnUpdate;
    private List<Classes> classes;
    private ClassesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_classes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvClass = findViewById(R.id.lvClass);
        edtClassId = findViewById(R.id.edtClassId);
        edtClassName = findViewById(R.id.edtClassName);
        btnUpdate = findViewById(R.id.btnUpdate);

        ClassesDAO dao = new ClassesDAO(this);
        loadData();

        lvClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edtClassId.setText(classes.get(i).getId() + "");
                edtClassName.setText(classes.get(i).getName());
            }
        });

        lvClass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ClassesActivity.this);
                alert.setCancelable(true);
                alert.setTitle("Xóa lớp");
                alert.setMessage("Bạn có chắc chắn muốn xóa lớp này?");
                alert.setPositiveButton("Yes", (dialogInterface, i1) -> {
                    if (dao.delete(classes.get(i).getId() + "") == -1) {
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

        btnUpdate.setOnClickListener(v -> {
            Classes cls = new Classes();
            cls.setId(Integer.parseInt(edtClassId.getText().toString()));
            cls.setName(edtClassName.getText().toString());
            long result = dao.update(cls);
            if (result == -1) {
                toast("Cập nhật thất bại");
            } else {
                toast("Cập nhật thành công");
                loadData();
                clear();
            }
        });
    }

    private void loadData() {
        ClassesDAO dao = new ClassesDAO(this);
        classes = dao.getAll();
        adapter = new ClassesAdapter(classes, this);
        lvClass.setAdapter(adapter);
    }

    private void toast(String message) {
        Toast.makeText(ClassesActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void clear(){
        edtClassId.setText("");
        edtClassName.setText("");
    }
}