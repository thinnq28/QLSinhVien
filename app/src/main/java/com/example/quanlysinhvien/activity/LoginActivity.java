package com.example.quanlysinhvien.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysinhvien.MainActivity;
import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.model.User;
import com.example.quanlysinhvien.sqlite.UserDAO;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin, btnExit;
    EditText edtUsername, edtPassword;
    CheckBox chkRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initWidget();
        btnLogin.setOnClickListener(this);
        btnExit.setOnClickListener(this);


    }

    private void initWidget() {
        btnLogin = findViewById(R.id.btnLogin);
        btnExit = findViewById(R.id.btnExit);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        chkRemember = findViewById(R.id.chkRemember);
        readAutoLogin();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnLogin) {
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            if (username.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Vui lòng nhập tên đăng nhập!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }
            if (password.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Vui lòng nhập mật khẩu!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }
            // Kiểm tra tên đăng nhập và mật khẩu
            UserDAO userDao = new UserDAO(this);
            List<User> users = userDao.findByUsername(username);
            if (users.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Tên đăng nhập không tồn tại!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }

            if (password.equals(users.get(0).getPassword())) {
                saveUsername();
                if (chkRemember.isChecked()) {
                    saveAutoLogin();
                } else {
                    clearAutoLogin();
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Snackbar snackbar = Snackbar.make(view, "Tên đăng nhập hoặc mật khẩu không đúng!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        } else if (id == R.id.btnExit) {
            finish();
        }
    }

    private void saveUsername() {
        SharedPreferences preferences = getSharedPreferences("username", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", edtUsername.getText().toString());
        editor.apply();
    }

    private void saveAutoLogin() {
        SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("username", edtUsername.getText().toString());
        editor.putString("password", edtPassword.getText().toString());

        editor.apply();
    }

    private boolean readAutoLogin() {
        SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        if (!username.isEmpty()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    private void clearAutoLogin() {
        SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}