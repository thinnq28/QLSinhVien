package com.example.quanlysinhvien.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.model.Classes;
import com.example.quanlysinhvien.sqlite.ClassesDAO;

import java.util.Objects;

public class NewClassDialog extends Dialog implements View.OnClickListener {

    private Button btnCreate, btnCancel;
    private EditText edtClassName;
    private Context context;

    public NewClassDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_new_class);

        edtClassName = findViewById(R.id.edtClassName);
        btnCreate = findViewById(R.id.btnCreate);
        btnCancel = findViewById(R.id.btnCancel);
        btnCreate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (Objects.equals(view.getId(), R.id.btnCreate)) {
            if (edtClassName.getText().toString().isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập tên lớp", Toast.LENGTH_SHORT).show();
            } else {
                Classes classes = new Classes();
                classes.setName(edtClassName.getText().toString());
                ClassesDAO dao = new ClassesDAO(context);
                dao.insert(classes);

                Toast.makeText(context, "Tạo lớp thành công", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        } else if (Objects.equals(view.getId(), R.id.btnCancel)) {
            dismiss();
        }
    }
}
