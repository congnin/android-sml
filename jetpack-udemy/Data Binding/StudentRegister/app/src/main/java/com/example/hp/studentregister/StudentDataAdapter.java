package com.example.hp.studentregister;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.studentregister.databinding.StudentListItemBinding;

import java.util.ArrayList;

public class StudentDataAdapter extends RecyclerView.Adapter<StudentDataAdapter.StudentViewHolder>{

    private ArrayList<Student> students;

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        StudentListItemBinding studentListItemBinding=DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.student_list_item,viewGroup,false);
        return new StudentViewHolder(studentListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder studentViewHolder, int i) {

         Student currentStudent=students.get(i);

         studentViewHolder.studentListItemBinding.setStudent(currentStudent);

    }


    @Override
    public int getItemCount() {

        if(students!=null) {

            return students.size();

        } else{}

        return 0;

            }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
        notifyDataSetChanged();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder{

        private StudentListItemBinding studentListItemBinding;


    public StudentViewHolder(@NonNull StudentListItemBinding studentListItemBinding) {
        super(studentListItemBinding.getRoot());

        this.studentListItemBinding=studentListItemBinding;

    }
}
}
