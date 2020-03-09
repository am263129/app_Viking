package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.official.aptoon.Provider.PrefManager;
import com.official.aptoon.R;
import com.official.aptoon.api.apiClient;
import com.official.aptoon.api.apiRest;
import com.official.aptoon.entity.ApiResponse;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_email, edt_password, edt_confirm_password, edt_username, edt_firstname, edt_lastname;
    TextView edt_birthday;
    Button register;
    ImageView show_pass, logo;
    boolean hide = true;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day, birth_year, birth_month, birth_day;
    private DatePickerDialog picker;

    private ProgressDialog register_progress;
    LinearLayout form;


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
        logo = findViewById(R.id.img_logo);
        form = findViewById(R.id.register_form);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screen_height = displayMetrics.heightPixels;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)logo.getLayoutParams();

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.setMargins(0, screen_height/6, 0, 10);
            logo.setLayoutParams(params);
            params = (LinearLayout.LayoutParams)form.getLayoutParams();
            params.height = screen_height*4/7;
        } else {
            params.setMargins(0, screen_height/5, 0, 10);
            logo.setLayoutParams(params);
            params = (LinearLayout.LayoutParams)form.getLayoutParams();
            params.height = screen_height*3/5;
        }


        form.setLayoutParams(params);

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
                        Toast.makeText(RegisterActivity.this,"Password does not match", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(RegisterActivity.this,"Please input password", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(RegisterActivity.this,"Please input Email Address", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(RegisterActivity.this,"Please input User name", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(RegisterActivity.this,"Please input BirthDay", Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(RegisterActivity.this,"Please input First Name and Last Name", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(RegisterActivity.this,"Invalid Email Address", Toast.LENGTH_SHORT).show();
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
        
    register_progress= ProgressDialog.show(this, null,getResources().getString(R.string.operation_progress), true);
    Retrofit retrofit = apiClient.getClient();
    apiRest service = retrofit.create(apiRest.class);
    String name = edt_firstname.getText().toString() + " " + edt_lastname.getText().toString();
    String username = edt_email.getText().toString();
    String password = edt_password.getText().toString();
    String type = "Phone";
    Integer length = username.split("@").length;
    if(username.split("@").length > 1){
        type = username.split("@")[1].split("[.]")[0];
    }
    String image = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg" ;
    Call<ApiResponse> call = service.register(name,username,password,type,image);
        call.enqueue(new Callback<ApiResponse>() {
        @Override
        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
            if(response.body()!=null){
                if (response.body().getCode()==200){

                    String id_user="0";
                    String name_user="x";
                    String username_user="x";
                    String salt_user="0";
                    String token_user="0";
                    String type_user="x";
                    String image_user="x";
                    String enabled="x";
                    for (int i=0;i<response.body().getValues().size();i++){
                        if (response.body().getValues().get(i).getName().equals("salt")){
                            salt_user=response.body().getValues().get(i).getValue();
                        }
                        if (response.body().getValues().get(i).getName().equals("token")){
                            token_user=response.body().getValues().get(i).getValue();
                        }
                        if (response.body().getValues().get(i).getName().equals("id")){
                            id_user=response.body().getValues().get(i).getValue();
                        }
                        if (response.body().getValues().get(i).getName().equals("name")){
                            name_user=response.body().getValues().get(i).getValue();
                        }
                        if (response.body().getValues().get(i).getName().equals("type")){
                            type_user=response.body().getValues().get(i).getValue();
                        }
                        if (response.body().getValues().get(i).getName().equals("username")){
                            username_user=response.body().getValues().get(i).getValue();
                        }
                        if (response.body().getValues().get(i).getName().equals("url")){
                            image_user=response.body().getValues().get(i).getValue();
                        }
                        if (response.body().getValues().get(i).getName().equals("enabled")){
                            enabled=response.body().getValues().get(i).getValue();
                        }
                    }if (enabled.equals("true")){
                        PrefManager prf= new PrefManager(RegisterActivity.this);
                        prf.setString("ID_USER",id_user);
                        prf.setString("SALT_USER",salt_user);
                        prf.setString("TOKEN_USER",token_user);
                        prf.setString("NAME_USER",name_user);
                        prf.setString("TYPE_USER",type_user);
                        prf.setString("USERN_USER",username_user);
                        prf.setString("IMAGE_USER",image_user);
                        prf.setString("LOGGED","TRUE");
                        String  token = FirebaseInstanceId.getInstance().getToken();
//                        if (name_user.equals("null")){
//                            linear_layout_otp_confirm_login_activity.setVisibility(View.GONE);
//                            linear_layout_name_input_login_activity.setVisibility(View.VISIBLE);
//                        }else{
//                            updateToken(Integer.parseInt(id_user),token_user,token,name_user);
//                        }
//
                    }else{
                        Toasty.error(RegisterActivity.this,getResources().getString(R.string.account_disabled), Toast.LENGTH_SHORT, true).show();
                    }
                }
                if (response.body().getCode()==500){
                    Toasty.error(RegisterActivity.this, "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                }
            }else{
                Toasty.error(RegisterActivity.this, "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
            }
            register_progress.dismiss();
        }
        @Override
        public void onFailure(Call<ApiResponse> call, Throwable t) {
            Toasty.error(RegisterActivity.this, "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
            register_progress.dismiss();
        }
    });
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
                picker = new DatePickerDialog(RegisterActivity.this,
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
