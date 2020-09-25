package com.example.hp.studentregister;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

@Entity(tableName ="student_table" )
public class Student extends BaseObservable {

 @PrimaryKey(autoGenerate =true)
 private int studentId;
 private String name;
 private String email;
 private String country;
 private String registeredTime;

    @Ignore
    public Student() {
    }

    public Student(int studentId, String name, String email, String country, String registeredTime) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.country = country;
        this.registeredTime = registeredTime;
    }

    @Bindable
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
        notifyPropertyChanged(BR.studentId);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
        notifyPropertyChanged(BR.name);

    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {

        this.country = country;
        notifyPropertyChanged(BR.country);
    }

    @Bindable
    public String getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(String registeredTime) {

        this.registeredTime = registeredTime;
        notifyPropertyChanged(BR.registeredTime);
    }
}
