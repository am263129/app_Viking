package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.official.aptoon.R;

public class forgotpassActivity extends AppCompatActivity {

    EditText email;
    Button btn_sendSMS;
    boolean sent = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        email = findViewById(R.id.edt_email);
        btn_sendSMS = findViewById(R.id.btn_sendSMS);
        btn_sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().equals("")){
                    Toast.makeText(forgotpassActivity.this, "Please input Email Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(sent){
                        Toast.makeText(forgotpassActivity.this, "We have sent again.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(forgotpassActivity.this, "We have send security code to your Email", Toast.LENGTH_SHORT).show();
                        sent = true;
                    }
                    btn_sendSMS.setText("Send Again");
                }
            }
        });
    }
}
