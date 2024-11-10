package com.example.quanlysinhvien.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlysinhvien.helper.DateTimeHelper;
import com.example.quanlysinhvien.model.Student;
import com.example.quanlysinhvien.model.User;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private SQLiteDatabase db;

    public UserDAO(Context context) {
        DbHelper helper = new DbHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public long insert(User user) {
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        return db.insert("users", null, values);
    }

    @SuppressLint("Range")
    public List<User> get(String sql, String... args) throws ParseException {
        List<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, args);

        while (cursor.moveToNext()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setActive(cursor.getInt(cursor.getColumnIndex("active")) == 1);
            users.add(user);
        }

        return users;
    }

    public List<User> getAll() {
        String sql = "SELECT * FROM users WHERE active = 1";
        try {
            return this.get(sql);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public long delete(String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("active", 0);
        return db.update("users", contentValues, "id=?", new String[]{id});
    }

    public long changePassword(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", user.getPassword());
        return db.update("users", contentValues, "id=?", new String[]{user.getId() + ""});
    }

    public List<User> findByUsername(String id) {
        String sql = "SELECT * FROM users WHERE username = ? AND active = 1";
        List<User> users;
        try {
            users = this.get(sql, id);
            return users;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }




}
