package com.example.quanlysinhvien.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "QuanLySinhVien";
    private static final int version = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String classSql = "CREATE TABLE classes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, active INTEGER NOT NULL DEFAULT 1)";

        String studentSql = "CREATE TABLE students (id TEXT PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "dateOfBirth TEXT NOT NULL, " +
                "gender TEXT NOT NULL, " +
                "phoneNumber TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "classId INTEGER NOT NULL, " +
                "active INTEGER NOT NULL DEFAULT 1, " +
                "FOREIGN KEY (classId) REFERENCES classes(id)) ";

        String courseSql = "CREATE TABLE courses (id INTEGER PRIMARY KEY AUTOINCREMENT, courseName TEXT NOT NULL, courseDescription TEXT, credits INTEGER NOT NULL,  active INTEGER NOT NULL DEFAULT 1)"; //so tin chi;

        String enrollmentSql = "CREATE TABLE enrollments (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    studentId TEXT," +
                "    courseId INTEGER," +
                "    enrollmentDate TEXT NOT NULL," +
                "    grade TEXT DEFAULT 'F'," +
                "    FOREIGN KEY (StudentId) REFERENCES Students(id) ON DELETE CASCADE," +
                "    FOREIGN KEY (CourseId) REFERENCES Courses(id) ON DELETE CASCADE)";

        String userSql = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password TEXT NOT NULL, active INTEGER NOT NULL DEFAULT 1)";


        String dataCourse = "INSERT INTO courses (courseName, courseDescription, credits, active)" +
                "VALUES ('Lập trình Java cơ bản', 'Java', 3, 1)";
        String dataClass = "INSERT INTO classes (name) VALUES ('CNTT01'), ('CNTT02')";
        String dataStudent = "INSERT INTO students (id, name, dateOfBirth, gender, phoneNumber, email, classId, active)" +
                " VALUES" +
                "('2023600023','John Doe', '15/01/2000', 'Male', '1234567890', 'john.doe@example.com', 1, 1)," +
                "('2023600024', 'Jane Smith', '10/05/1995', 'Female', '0987654321', 'jane.smith@example.com', 1, 1)," +
                "('2023600025', 'Harry', '10/05/1995', 'Male', '0987654321', 'jane.smith@example.com', 1, 1)," +
                "('2023600026', 'Jamas', '10/05/1995', 'Male', '0987654321', 'jane.smith@example.com', 1, 1)," +
                "('2023600027', 'Barack Obama', '10/05/1995', 'Male', '0987654321', 'jane.smith@example.com', 1, 1)," +
                "('2023600028', 'Donal Trump', '10/05/1995', 'Male', '0987654321', 'jane.smith@example.com', 1, 1)";
        String dataEnrollment = "INSERT INTO enrollments (studentId, courseId, enrollmentDate, grade) VALUES" +
                "                ('2023600023', 1, '01/09/2023', 'F')," +
                "                ('2023600024', 1, '01/09/2023', 'B')," +
                "                ('2023600025', 1, '01/09/2023', 'C')," +
                "                ('2023600026', 1, '01/09/2023', 'D')," +
                "                ('2023600027', 1, '01/09/2023', 'A')," +
                "                ('2023600038', 1, '02/09/2023', 'F')";
        String userSqlData = "INSERT INTO users (username, password, active) VALUES ('admin', 'admin', 1)";



        sqLiteDatabase.execSQL(classSql);
        sqLiteDatabase.execSQL(courseSql);
        sqLiteDatabase.execSQL(studentSql);
        sqLiteDatabase.execSQL(enrollmentSql);
        sqLiteDatabase.execSQL(userSql);


        sqLiteDatabase.execSQL(dataCourse);
        sqLiteDatabase.execSQL(dataClass);
        sqLiteDatabase.execSQL(dataStudent);
        sqLiteDatabase.execSQL(dataEnrollment);
        sqLiteDatabase.execSQL(userSqlData);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String classSql = "DROP TABLE IF EXISTS classes";
        String studentSql = "DROP TABLE IF EXISTS students";
        String coursesSql = "DROP TABLE IF EXISTS courses";

        sqLiteDatabase.execSQL(studentSql);
        sqLiteDatabase.execSQL(classSql);
        sqLiteDatabase.execSQL(coursesSql);

        onCreate(sqLiteDatabase);
    }
}
