package com.official.aptoon.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.official.aptoon.Provider.PrefManager;
import com.official.aptoon.R;
import com.official.aptoon.api.apiClient;
import com.official.aptoon.api.apiRest;
import com.official.aptoon.config.Global;
import com.official.aptoon.entity.ApiResponse;

import org.bouncycastle.asn1.dvcs.TargetEtcChain;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OuterLoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    EditText edt_password, edt_email;
    TextView label_facebook, label_google;
    ImageView img_eye;
    boolean hide = true;
    LoginButton btn_facebook;
    Button btn_login,   btn_livechat;
    RelativeLayout btn_google,btn_view_facebook;
    TextView btn_forgot, btn_signup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    GoogleApiClient mGoogleApiClient;
    private static Button googleSignIn;
    private LoginButton sign_in_button_facebook;


    private ProgressDialog register_progress;
    private PrefManager prf;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outer_login);
        edt_password = (EditText)findViewById(R.id.edit_password);
        img_eye = (ImageView)findViewById(R.id.show_pass_btn);
        edt_email = (EditText)findViewById(R.id.edt_email);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_facebook = (LoginButton)findViewById(R.id.btn_facebook);
        btn_google = (RelativeLayout)findViewById(R.id.btn_google);
        btn_view_facebook = (RelativeLayout)findViewById(R.id.btn_view_facebook);
        btn_forgot = (TextView)findViewById(R.id.btn_forget_password);
        btn_signup = (TextView)findViewById(R.id.btn_sign_up);
        btn_livechat = (Button)findViewById(R.id.btn_live_chat);

        btn_login.setOnClickListener(this);
        btn_facebook.setOnClickListener(this);
        btn_google.setOnClickListener(this);
        btn_forgot.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_livechat.setOnClickListener(this);
        btn_view_facebook.setOnClickListener(this);

        label_facebook = (TextView)findViewById(R.id.label_facebook);
        label_google = (TextView)findViewById(R.id.label_google);




        Typeface font = Typeface.createFromAsset(OuterLoginActivity.this.getAssets(), "SpartanMB-Regular.otf");
        edt_email .setTypeface(font);
        edt_password.setTypeface(font);
        btn_login.setTypeface(font);
        btn_forgot.setTypeface(font);
        btn_signup.setTypeface(font);
        label_facebook.setTypeface(font);
        label_google.setTypeface(font);
        prf= new PrefManager(getApplicationContext());

        GoogleSignIn();
        FaceookSignIn();

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


        register_progress= ProgressDialog.show(OuterLoginActivity.this, null,getResources().getString(R.string.operation_progress), true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        String name = edt_email.getText().toString();
        String password = edt_password.getText().toString();
        Call<ApiResponse> call = service.login(name,password);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.body()!=null){

                    if (response.body().getCode()==200){
                        Toast.makeText(OuterLoginActivity.this,"1",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(OuterLoginActivity.this,"2",Toast.LENGTH_SHORT).show();
                            PrefManager prf= new PrefManager(getApplicationContext());
                            prf.setString("ID_USER",id_user);
                            prf.setString("SALT_USER",salt_user);
                            prf.setString("TOKEN_USER",token_user);
                            prf.setString("NAME_USER",name_user);
                            prf.setString("TYPE_USER",type_user);
                            prf.setString("USERN_USER",username_user);
                            prf.setString("IMAGE_USER",image_user);
                            prf.setString("LOGGED","TRUE");

                            Toast.makeText(OuterLoginActivity.this,"kkk",Toast.LENGTH_SHORT).show();
                            Global.user_image = image_user;
                            String  token = FirebaseInstanceId.getInstance().getToken();
                            if (name_user.equals("null")){
                            }else{
                                updateToken(Integer.parseInt(id_user),token_user,token,name_user);
                            }
                            register_progress.dismiss();
                            Toast.makeText(OuterLoginActivity.this,"kkk bug is here",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OuterLoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();


                        }else{
                            register_progress.dismiss();
                            Toasty.error(getApplicationContext(),getResources().getString(R.string.account_disabled), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                    if (response.body().getCode()==500){
                        register_progress.dismiss();
                        Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                    }

                }else{
                    register_progress.dismiss();
                    Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                register_progress.dismiss();
                //test
                Intent intent = new Intent(OuterLoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void login_with_google(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    private void login_with_facebook(){

    }

    public void GoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    public void FaceookSignIn(){

        // Other app specific specializationsign_in_button_facebook.setReadPermissions(Arrays.asList("public_profile"));
        callbackManager = CallbackManager.Factory.create();

        // Callback registration

        btn_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        getResultFacebook(object);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                set(OuterLoginActivity.this, "Operation has been cancelled ! ");
            }

            @Override
            public void onError(FacebookException exception) {
                set(OuterLoginActivity.this, "Operation has been cancelled ! ");
            }
        });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }
    public static void set(Activity activity, String s){
        Toasty.error(activity,s,Toast.LENGTH_LONG).show();
        activity.finish();
    }

    private void getResultGoogle(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();
            String photo = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg" ;
            if (acct.getPhotoUrl()!=null){
                photo =  acct.getPhotoUrl().toString();
            }

            signUp(acct.getId().toString(),acct.getId(), acct.getDisplayName().toString(),"google",photo);
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        } else {
        }
    }

    private void getResultFacebook(JSONObject object){
        Log.d(TAG, object.toString());
        try {
            signUp(object.getString("id").toString(),object.getString("id").toString(),object.getString("name").toString(),"facebook",object.getJSONObject("picture").getJSONObject("data").getString("url"));
            LoginManager.getInstance().logOut();        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void signUp(String username,String password,String name,String type,String image){
        register_progress= ProgressDialog.show(OuterLoginActivity.this, null,getResources().getString(R.string.operation_progress), true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.register(name,username,password,type,image);
        Global.user_image = image;
        Global.user_name = name;
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
                            PrefManager prf= new PrefManager(getApplicationContext());
                            prf.setString("ID_USER",id_user);
                            prf.setString("SALT_USER",salt_user);
                            prf.setString("TOKEN_USER",token_user);
                            prf.setString("NAME_USER",name_user);
                            prf.setString("TYPE_USER",type_user);
                            prf.setString("USERN_USER",username_user);
                            prf.setString("IMAGE_USER",image_user);
                            Global.user_image = image_user;
                            prf.setString("LOGGED","TRUE");
                            String  token = FirebaseInstanceId.getInstance().getToken();
                            if (name_user.equals("null")){
                            }else{
                                updateToken(Integer.parseInt(id_user),token_user,token,name_user);
                            }


                        }else{
                            Toasty.error(getApplicationContext(),getResources().getString(R.string.account_disabled), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                    if (response.body().getCode()==500){
                        Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                    }
                }else{
                    Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                }
                register_progress.dismiss();
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                register_progress.dismiss();
            }
        });
    }

    public void updateToken(Integer id,String key,String token,String name){
        register_progress= ProgressDialog.show(OuterLoginActivity.this, null,getResources().getString(R.string.operation_progress), true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.editToken(id,key,token,name);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    prf.setString("NAME_USER",name );

                    Toasty.success(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    register_progress.dismiss();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                register_progress.dismiss();
            }
        });
    }


    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){

            case R.id.btn_login:

                if(!isValidEmail(edt_email.getText().toString()) || edt_password.getText().toString().equals("")){
                    Toast.makeText(OuterLoginActivity.this, R.string.Error_Login, Toast.LENGTH_LONG).show();
                }
                else {
                    login();
                }
                break;
            case R.id.btn_view_facebook:
                btn_facebook.callOnClick();
                break;
            case R.id.btn_google:
                login_with_google();

                break;
            case R.id.btn_forget_password:
                intent =  new Intent(OuterLoginActivity.this, forgotpassActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sign_up:
                intent = new Intent(OuterLoginActivity.this, Register.class);
                startActivity(intent);
                break;
            case R.id.btn_live_chat:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            getResultGoogle(result);
        }
    }
}
