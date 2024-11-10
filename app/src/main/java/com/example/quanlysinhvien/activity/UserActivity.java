package com.example.quanlysinhvien.activity;

import android.content.SharedPreferences;
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
import com.example.quanlysinhvien.adapter.UserAdapter;
import com.example.quanlysinhvien.helper.DateTimeHelper;
import com.example.quanlysinhvien.model.Classes;
import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.model.User;
import com.example.quanlysinhvien.sqlite.StudentDAO;
import com.example.quanlysinhvien.sqlite.UserDAO;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtUsernameFilter, edtUserId;
    Button btnAdd, btnUpdate, btnDelete, btnSearch, btnClear;
    ListView lvUser;
    List<User> users = new ArrayList<>();
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initWidget();
        loadData();
        userAdapter = new UserAdapter(this, users);
        lvUser.setAdapter(userAdapter);

        btnAdd.setOnClickListener(this::add);

        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edtUserId.setText(users.get(i).getId() + "");
                edtUsername.setText(users.get(i).getUsername());
                edtPassword.setText(users.get(i).getPassword());
                edtUsername.setEnabled(false);
            }
        });

        btnUpdate.setOnClickListener(v -> {
            update(v);
        });

        btnDelete.setOnClickListener(v -> {
            delete(v);
        });

        btnClear.setOnClickListener(v -> {
            clear();
        });

    }

    private void initWidget() {
        edtUserId = findViewById(R.id.edtUserId);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnClear = findViewById(R.id.btnClear);
        lvUser = findViewById(R.id.lvUser);
        edtUsernameFilter = findViewById(R.id.edtUsernameFilter);
        btnSearch = findViewById(R.id.btnSearch);
        edtUserId.setEnabled(false);
    }

    private void loadData() {
        UserDAO dao = new UserDAO(this);
        users.clear();
        users = dao.getAll();
        userAdapter = new UserAdapter(this, users);
        lvUser.setAdapter(userAdapter);
    }

    private void clear() {
        edtUsername.setText("");
        edtPassword.setText("");
        edtUserId.setText("");
        edtUsername.setEnabled(true);
    }

    private void add(View v) {
        try {
            if (validate()) return;

            User user = new User();
            user.setUsername(edtUsername.getText().toString());
            user.setPassword(edtPassword.getText().toString());

            UserDAO dao = new UserDAO(this);
            long result = dao.insert(user);
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

    private boolean validate() throws ParseException {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(this, "Tên đăng nhập không được để trống", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            return true;
        }
        UserDAO userDao = new UserDAO(this);
        List<User> users = userDao.findByUsername(username);
        if (!users.isEmpty()) {
            Toast.makeText(this, "Username đã tồn tại!!!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    private void update(View v) {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(this, "Tên đăng nhập không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        UserDAO userDao = new UserDAO(this);
        List<User> users = userDao.findByUsername(username);
        if (users.isEmpty()) {
            Toast.makeText(this, "Username không tồn tại!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = users.get(0);
        user.setPassword(password);

        long result = userDao.changePassword(user);

        if (result == -1) {
            Snackbar snackbar = Snackbar.make(v, "Cập nhật thất bại", Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(v, "Cập nhật thành công", Snackbar.LENGTH_SHORT);
            snackbar.show();
            loadData();
            clear();
        }
    }

    private void delete(View v) {
        String username = readUsername();
        if (!Objects.equals(username, "admin")) {
            Toast.makeText(this, "Bạn không có quyền xoá", Toast.LENGTH_SHORT).show();
            return;
        }

        String usernameAdmin = edtUsername.getText().toString();
        String userId = edtUserId.getText().toString();

        if(usernameAdmin.isEmpty() || userId.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn đối tượng cần xoá", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Objects.equals(usernameAdmin, "admin")) {
            Toast.makeText(this, "Không thể xóa tài khoản admin", Toast.LENGTH_SHORT).show();
            return;
        }


        UserDAO dao = new UserDAO(this);
        AlertDialog.Builder alert = new AlertDialog.Builder(UserActivity.this);
        alert.setCancelable(true);
        alert.setTitle("Xóa người dùng");
        alert.setMessage("Bạn có chắc chắn muốn xóa người dùng này?");
        alert.setPositiveButton("Yes", (dialogInterface, i1) -> {
            if (dao.delete(userId) == -1) {
                Snackbar snackbar = Snackbar.make(v, "Xoá thất bại", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(v, "Xoá thành công", Snackbar.LENGTH_SHORT);
                snackbar.show();
                loadData();
                clear();
            }

        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    private String readUsername() {
        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        return username;
    }
}