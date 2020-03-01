package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.official.aptoon.R;

import java.util.Calendar;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText edt_email, edt_password, edt_confirm_password, edt_username, edt_firstname, edt_lastname;
    TextView edt_birthday;
    Button register;
    ImageView show_pass;
    boolean hide = true;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day, birth_year, birth_month, birth_day;
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.register_password);
        edt_confirm_password = findViewById(R.id.confirm_password);
        show_pass = (ImageView)findViewById(R.id.show_pass_btn_pwd);
        edt_birthday = findViewById(R.id.edt_birthday);
        edt_username = findViewById(R.id.edt_username);
        edt_firstname = findViewById(R.id.edt_firstname);
        edt_lastname = findViewById(R.id.edt_lastname);

        show_pass.setOnClickListener(this);
        edt_birthday.setOnClickListener(this);
        register = (Button)findViewById(R.id.btn_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(validate()){
                    case 0:
                        Register();
                        break;
                    case 1:
                        Toast.makeText(Register.this,"Password does not match", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(Register.this,"Please input password", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(Register.this,"Please input Email Address", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(Register.this,"Please input User name", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(Register.this,"Please input BirthDay", Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(Register.this,"Please input First Name and Last Name", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(Register.this,"Invalid Email Address", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


    }
    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public Integer validate(){
        if(edt_email.getText().toString().equals("")){
            return 4;
        }
        else if(edt_confirm_password.getText().toString().equals("") || edt_password.getText().toString().equals("")){
            return 3;
        }
        else if (!edt_confirm_password.getText().toString().equals(edt_password.getText().toString())){
            return 1;
        }
        else if(!isValidEmail(edt_email.getText().toString()) || edt_password.getText().toString().equals("")){
            return 2;
        }
        else if(edt_username.getText().toString().equals("")){
            return 5;
        }
        else if (edt_birthday.getText().toString().equals("")){
            return 6;
        }
        else if (edt_firstname.getText().toString().equals("") || edt_lastname.getText().toString().equals("")){
            return 7;
        }
        else{
            return 0;
        }
    }
    public void Register(){
        Toast.makeText(Register.this,"Welcome to APTOON!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Register.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void ShowHidePass() {
        hide = !hide;
        if (hide){
            edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            edt_confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            show_pass.setImageResource(R.drawable.hide_pass);
        }
        else{
            edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            edt_confirm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            show_pass.setImageResource(R.drawable.show_pass);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_pass_btn_pwd:
                ShowHidePass();
                break;
            case R.id.edt_birthday:
                if(String.valueOf(edt_birthday.getText()).equals(""))
                {
                    birth_year = year;
                    birth_month = month;
                    birth_day = day;
                }
                picker = new DatePickerDialog(Register.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edt_birthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month , day);

                picker.show();
                break;

        }
    }
}
