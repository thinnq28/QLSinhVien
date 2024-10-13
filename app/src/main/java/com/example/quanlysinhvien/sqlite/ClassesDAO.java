package com.example.quanlysinhvien.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlysinhvien.model.Classes;

import java.util.ArrayList;
import java.util.List;

public class ClassesDAO {
    private SQLiteDatabase db;

    public ClassesDAO(Context context) {
        DbHelper helper = new DbHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public long insert(Classes classes) {
        ContentValues values = new ContentValues();
        values.put("name", classes.getName());
        return db.insert("classes", null, values);
    }

    @SuppressLint("Range")
    public List<Classes> get(String sql, String ...args) {
        List<Classes> classes = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, args);

        while (cursor.moveToNext()){
            Classes cls = new Classes();
            cls.setId(cursor.getInt(cursor.getColumnIndex("id")));
            cls.setName(cursor.getString(cursor.getColumnIndex("name")));
            classes.add(cls);
        }

        return classes;
    }

    public List<Classes> getAll() {
        String sql = "SELECT * FROM classes WHERE active = 1";
        return this.get(sql);
    }

    public long delete(String id){
        ContentValues contentValues = new ContentValues();
        contentValues.put("active", 0);
        return db.update("classes", contentValues, "id=?", new String[]{id});
    }

    public long update(Classes classes){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", classes.getName());
        return db.update("classes", contentValues, "id=?", new String[]{classes.getId()+""});
    }
}

