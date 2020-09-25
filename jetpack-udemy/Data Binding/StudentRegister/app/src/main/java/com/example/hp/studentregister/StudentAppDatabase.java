package com.example.hp.studentregister;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Student.class},version = 1)
public abstract class StudentAppDatabase extends RoomDatabase {

    public abstract StudentDao getStudentDao();

}
