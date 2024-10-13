package com.example.quanlysinhvien.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlysinhvien.helper.DateTimeHelper;
import com.example.quanlysinhvien.model.Student;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private SQLiteDatabase db;

    public StudentDAO(Context context) {
        DbHelper helper = new DbHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public long insert(Student student) {
        ContentValues values = new ContentValues();
        values.put("id", student.getId());
        values.put("name", student.getName());
        values.put("dateOfBirth", DateTimeHelper.toString(student.getDateOfBirth()));
        values.put("gender", student.getGender());
        values.put("phoneNumber", student.getPhoneNumber());
        values.put("email", student.getEmail());
        values.put("classId", student.getClassId());
        return db.insert("students", null, values);
    }

    @SuppressLint("Range")
    public List<Student> get(String sql, String... args) throws ParseException {
        List<Student> students = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, args);

        while (cursor.moveToNext()) {
            Student student = new Student();
            student.setId(cursor.getString(cursor.getColumnIndex("id")));
            student.setName(cursor.getString(cursor.getColumnIndex("name")));
            student.setDateOfBirth(DateTimeHelper.toDate(cursor.getString(cursor.getColumnIndex("dateOfBirth"))));
            student.setGender(cursor.getString(cursor.getColumnIndex("gender")));
            student.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phoneNumber")));
            student.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            student.setClassId(cursor.getInt(cursor.getColumnIndex("classId")));
            students.add(student);
        }

        return students;
    }

    public List<Student> getAll() {
        String sql = "SELECT * FROM students WHERE active = 1";
        try {
            return this.get(sql);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public long delete(String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("active", 0);
        return db.update("students", contentValues, "id=?", new String[]{id});
    }

    public long update(Student student) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", student.getName());
        contentValues.put("dateOfBirth", DateTimeHelper.toString(student.getDateOfBirth()));
        contentValues.put("gender", student.getGender());
        contentValues.put("phoneNumber", student.getPhoneNumber());
        contentValues.put("email", student.getEmail());
        contentValues.put("classId", student.getClassId());
        return db.update("students", contentValues, "id=?", new String[]{student.getId() + ""});
    }

    public List<Student> findById(String id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        List<Student> students = null;
        try {
            students = this.get(sql, id);
            return students;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
