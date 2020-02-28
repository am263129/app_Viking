package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.official.aptoon.R;

public class OuterLoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_password, edt_email;
    ImageView img_eye;
    boolean hide = true;
    Button btn_login, btn_facebook, btn_google, btn_livechat;
    TextView btn_forgot, btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outer_login);
        edt_password = (EditText)findViewById(R.id.edit_password);
        img_eye = (ImageView)findViewById(R.id.show_pass_btn);
        edt_email = (EditText)findViewById(R.id.edt_email);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_facebook = (Button)findViewById(R.id.btn_facebook);
        btn_google = (Button)findViewById(R.id.btn_google);
        btn_forgot = (TextView)findViewById(R.id.btn_forget_password);
        btn_signup = (TextView)findViewById(R.id.btn_sign_up);
        btn_livechat = (Button)findViewById(R.id.btn_live_chat);

        btn_login.setOnClickListener(this);
        btn_facebook.setOnClickListener(this);
        btn_google.setOnClickListener(this);
        btn_forgot.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_livechat.setOnClickListener(this);

    }

    public void ShowHidePass(View view) {
        hide = !hide;
        if (hide){
            edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            img_eye.setImageResource(R.drawable.hide_pass);
        }
        else{
            edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            img_eye.setImageResource(R.drawable.show_pass);
        }

    }
    private void login(){

    }

    private void login_with_google(){

    }
    private void login_with_facebook(){

    }



    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:

                if(!isValidEmail(edt_email.getText().toString()) || edt_password.getText().toString().equals("")){
                    Toast.makeText(OuterLoginActivity.this, R.string.Error_Login, Toast.LENGTH_LONG).show();
                }
                else {
                    login();
                }
                break;
            case R.id.btn_facebook:
                login_with_facebook();
                break;
            case R.id.btn_google:
                login_with_google();

                break;
            case R.id.btn_forget_password:
                break;
            case R.id.btn_sign_up:
                break;
            case R.id.btn_live_chat:
                break;
        }
    }
}
