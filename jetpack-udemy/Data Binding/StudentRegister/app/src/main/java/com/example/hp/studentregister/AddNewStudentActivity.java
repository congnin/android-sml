package com.example.hp.studentregister;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.studentregister.databinding.ActivityAddNewStudentBinding;

public class AddNewStudentActivity extends AppCompatActivity {

//    private Button submitButton;
//    private EditText nameEditText;
//    private EditText emailEditText;
//    private EditText countryEditText;

    private ActivityAddNewStudentBinding activityAddNewStudentBinding;
    Student student;
    private AddNewStudentActivityClickHandlers addNewStudentActivityClickHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student);
        student=new Student();
        activityAddNewStudentBinding=DataBindingUtil.setContentView(this,R.layout.activity_add_new_student);
        activityAddNewStudentBinding.setStudnet(student);

        addNewStudentActivityClickHandlers=new AddNewStudentActivityClickHandlers(this);
        activityAddNewStudentBinding.setClickHandler(addNewStudentActivityClickHandlers);

//        nameEditText=findViewById(R.id.et_name);
//        emailEditText=findViewById(R.id.et_email);
//        countryEditText=findViewById(R.id.et_country);
//        submitButton=findViewById(R.id.btnSubmit);

//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(TextUtils.isEmpty(nameEditText.getText())){
//
//                    Toast.makeText(getApplicationContext(),"Name field cannot be empty",Toast.LENGTH_LONG).show();
//                }else{
//
//                    String name=nameEditText.getText().toString();
//                    String email=emailEditText.getText().toString();
//                    String country=countryEditText.getText().toString();
//
//                    Intent intent=new Intent();
//                    intent.putExtra("NAME",name);
//                    intent.putExtra("EMAIL",email);
//                    intent.putExtra("COUNTRY",country);
//                    setResult(RESULT_OK,intent);
//                    finish();
//
//                }
//
//            }
//        });


    }

    public class AddNewStudentActivityClickHandlers {

        Context context;

        public AddNewStudentActivityClickHandlers(Context context) {
            this.context = context;
        }

        public void onSubmitClicked(View view) {

            if(student.getName()==null){

                Toast.makeText(getApplicationContext(),"Name field cannot be empty",Toast.LENGTH_LONG).show();

            }else{

                Intent intent=new Intent();
                intent.putExtra("NAME",student.getName());
                intent.putExtra("EMAIL",student.getEmail());
                intent.putExtra("COUNTRY",student.getCountry());
                setResult(RESULT_OK,intent);
                finish();

            }

        }
    }
}
