package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.official.aptoon.R;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText edt_email, edt_password, edt_confirm_password;
    Button register;
    ImageView show_pass;
    boolean hide = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.register_password);
        edt_confirm_password = findViewById(R.id.confirm_password);
        show_pass = (ImageView)findViewById(R.id.show_pass_btn_pwd);

        show_pass.setOnClickListener(this);

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
                    case 2:
                        Toast.makeText(Register.this,"Invalid Email Address", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


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

        }
    }
}
