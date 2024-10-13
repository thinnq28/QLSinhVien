package com.example.quanlysinhvien.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlysinhvien.helper.DateTimeHelper;
import com.example.quanlysinhvien.model.Classes;
import com.example.quanlysinhvien.model.Enrollment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EnrollmentDAO {
    private SQLiteDatabase db;

    public EnrollmentDAO(Context context) {
        DbHelper helper = new DbHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public long insert(Enrollment enrollment) {
        ContentValues values = new ContentValues();
        values.put("studentId", enrollment.getStudentId());
        values.put("courseId", enrollment.getCourseId());
        values.put("enrollmentDate", DateTimeHelper.toString(new Date()));
        values.put("grade", enrollment.getGrade());
        return db.insert("enrollments", null, values);
    }

    @SuppressLint("Range")
    public List<Enrollment> get(String sql, String ...args) throws ParseException {
        List<Enrollment> enrollments = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, args);

        while (cursor.moveToNext()){
            Enrollment enrollment = new Enrollment();
            enrollment.setId(cursor.getInt(cursor.getColumnIndex("id")));
            enrollment.setStudentId(cursor.getString(cursor.getColumnIndex("studentId")));
            enrollment.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
            enrollment.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
            enrollment.setEnrollmentDate(DateTimeHelper.toDate(cursor.getString(cursor.getColumnIndex("enrollmentDate"))));
            enrollments.add(enrollment);
        }

        return enrollments;
    }

    public List<Enrollment> getAll() throws ParseException {
        String sql = "SELECT * FROM enrollments";
        return this.get(sql);
    }

    public long delete(String studentId, int courseId){
        return db.delete("enrollments", "studentId=? AND courseId=?", new String[]{studentId, courseId+""});
    }

    public long update(Enrollment enrollment){
        ContentValues contentValues = new ContentValues();
        contentValues.put("studentId", enrollment.getStudentId());
        contentValues.put("courseId", enrollment.getCourseId());
        contentValues.put("enrollmentDate", DateTimeHelper.toString(enrollment.getEnrollmentDate()));
        contentValues.put("grade", enrollment.getGrade());
        return db.update("enrollments", contentValues, "id=?", new String[]{enrollment.getId()+""});
    }
}
