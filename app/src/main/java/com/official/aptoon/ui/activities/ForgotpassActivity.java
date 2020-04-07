package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.official.aptoon.R;
import com.official.aptoon.api.apiClient;
import com.official.aptoon.api.apiRest;
import com.official.aptoon.entity.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForgotpassActivity extends AppCompatActivity {

    EditText edt_email,newpass,vcode;
    Button btn_sendSMS,btn_resetpass;
    boolean sent = false;
    ProgressDialog dlg_progress;
    LinearLayout layer_sendvcode, layer_newpass;
    String reset_key = "";
    List<reset_token> keys = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        init_view();
        init_actions();

    }

    private void init_view() {
        edt_email = findViewById(R.id.edt_email);
        btn_sendSMS = findViewById(R.id.btn_sendSMS);

        layer_sendvcode = findViewById(R.id.layer_sendvcode);
        layer_newpass = findViewById(R.id.layer_newpass);
        newpass = findViewById(R.id.edt_newpass);
        btn_resetpass = findViewById(R.id.btn_reset);
        vcode = findViewById(R.id.edt_vcode);
    }
    private void init_actions(){
        btn_sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_email.getText().toString().equals("")){
                    Toasty.error(ForgotpassActivity.this,getResources().getString(R.string.error_empty_email),Toast.LENGTH_SHORT).show();
                }
                else {

                    if(sent){
                        Toasty.normal(ForgotpassActivity.this,getResources().getString(R.string.error_empty_email),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dlg_progress = ProgressDialog.show(ForgotpassActivity.this, null,getResources().getString(R.string.operation_progress), true);
                        Retrofit retrofit = apiClient.getClient();
                        apiRest service = retrofit.create(apiRest.class);
                        String email = edt_email.getText().toString();
                        Call<ApiResponse> call = service.requestresetpassword(email);
                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if(response.body()!=null){

                                    if (response.body().getCode()==200){
                                        for (int i=0;i<response.body().getValues().size();i++){
                                            if (response.body().getValues().get(i).getName().equals("values")){
                                                reset_key=response.body().getValues().get(i).getValue();
                                                vcode.setText(reset_key);
                                            }

                                        }
                                        dlg_progress.dismiss();
                                    }
                                    if (response.body().getCode()==500){
                                        dlg_progress.dismiss();
                                        Toasty.error(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT, true).show();
                                    }

                                }
                                else{
                                    dlg_progress.dismiss();
                                    Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                                dlg_progress.dismiss();
                                Intent intent = new Intent(ForgotpassActivity.this, OuterLoginActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                        sent = true;
                    }
                    btn_sendSMS.setText("Send Again");
                }
            }
        });
        btn_resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vcode.getText().toString().equals("")){
                    Toasty.error(ForgotpassActivity.this,getResources().getString(R.string.error_empty_vcode),Toast.LENGTH_SHORT).show();
                }
                else if(newpass.getText().toString().equals("")){
                    Toasty.error(ForgotpassActivity.this,getResources().getString(R.string.error_empty_password),Toast.LENGTH_SHORT).show();
                }
                else{
                    dlg_progress = ProgressDialog.show(ForgotpassActivity.this, null,getResources().getString(R.string.operation_progress), true);
                    Retrofit retrofit = apiClient.getClient();
                    apiRest service = retrofit.create(apiRest.class);
                    Call<ApiResponse> call = service.getresetcode(edt_email.getText().toString(),reset_key);
                    call.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            if(response.body()!=null){

                                if (response.body().getCode()==200){
                                    for (int i=0;i<response.body().getValues().size();i++){
                                        if (response.body().getValues().get(i).getName().equals("values")){
                                            String test=response.body().getValues().get(i).getValue();
                                        }

                                    }
                                    dlg_progress.dismiss();
                                }
                                if (response.body().getCode()==500){
                                    dlg_progress.dismiss();
                                    Toasty.error(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT, true).show();
                                }

                            }
                            else{
                                dlg_progress.dismiss();
                                Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                            dlg_progress.dismiss();
                            Intent intent = new Intent(ForgotpassActivity.this, OuterLoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });
                }

            }
        });
    }

    private void show_resetlayer(){
        layer_sendvcode.setVisibility(View.GONE);
        layer_newpass.setVisibility(View.VISIBLE);
    }
    private void hide_resetlayer(){
        layer_newpass.setVisibility(View.GONE);
        layer_sendvcode.setVisibility(View.VISIBLE);
    }

    private class reset_token {
        String id;
        String value;
        public reset_token(){

        }
        public reset_token(String id, String value){
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
