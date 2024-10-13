package com.example.quanlysinhvien.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlysinhvien.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private SQLiteDatabase db;

    public CourseDAO(Context context) {
        DbHelper helper = new DbHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public long insert(Course course) {
        ContentValues values = new ContentValues();
        values.put("courseName", course.getCourseName());
        values.put("courseDescription", course.getCourseDescription());
        values.put("credits", course.getCredits());
        return db.insert("courses", null, values);
    }

    @SuppressLint("Range")
    public List<Course> get(String sql, String ...args) {
        List<Course> courses = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, args);

        while (cursor.moveToNext()){
            Course course = new Course();
            course.setCourseId(cursor.getInt(cursor.getColumnIndex("id")));
            course.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
            course.setCourseDescription(cursor.getString(cursor.getColumnIndex("courseDescription")));
            course.setCredits(cursor.getInt(cursor.getColumnIndex("credits")));
            courses.add(course);
        }

        return courses;
    }

    public List<Course> getAll() {
        String sql = "SELECT * FROM courses WHERE active = 1";
        return this.get(sql);
    }

    public long delete(String id){
        ContentValues contentValues = new ContentValues();
        contentValues.put("active", 0);
        return db.update("courses", contentValues, "id=?", new String[]{id});
    }

    public long update(Course course){
        ContentValues contentValues = new ContentValues();
        contentValues.put("courseName", course.getCourseName());
        contentValues.put("courseDescription", course.getCourseDescription());
        contentValues.put("credits", course.getCredits());
        return db.update("courses", contentValues, "id=?", new String[]{course.getCourseId()+""});
    }

    public List<Course> findByName(String name) {
        String sql = "SELECT * FROM courses WHERE courseName LIKE ?";
        return this.get(sql, "%" + name + "%");
    }
}
