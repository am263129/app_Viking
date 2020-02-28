package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.official.aptoon.Provider.PrefManager;
import com.official.aptoon.R;
import com.official.aptoon.api.ProgressRequestBody;
import com.official.aptoon.api.apiClient;
import com.official.aptoon.api.apiRest;
import com.official.aptoon.entity.ApiResponse;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditActivity extends AppCompatActivity implements  ProgressRequestBody.UploadCallbacks{

    private PrefManager prf;
    private CircleImageView image_view_edit_activity_user_profile;
    private TextView text_view_edit_activity_name_user;
    private ImageView image_view_edit_activity_name_edit_photo;
    private RelativeLayout relative_layout_edit_activity_save;
    private TextInputLayout text_input_layout_activity_edit_name;
    private TextInputEditText text_input_editor_text_activity_edit_name;
    private int id;
    private String name;
    private String image;
    private int PICK_IMAGE = 1557;
    private String imageUrl;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle bundle = getIntent().getExtras() ;
        this.id =  bundle.getInt("id");
        this.name =  bundle.getString("name");
        this.image =  bundle.getString("image");
        this.prf = new PrefManager(getApplicationContext());
        initView();
        initAction();
        setUser();
    }
    private boolean validatName() {
        if (text_input_editor_text_activity_edit_name.getText().toString().trim().isEmpty() || text_input_editor_text_activity_edit_name.getText().length()  < 3 ) {
            text_input_layout_activity_edit_name.setError(getString(R.string.error_short_value));
            requestFocus(text_input_editor_text_activity_edit_name);
            return false;
        } else {
            text_input_layout_activity_edit_name.setErrorEnabled(false);
        }
        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private void initView() {
        this.image_view_edit_activity_user_profile = (CircleImageView) findViewById(R.id.image_view_edit_activity_user_profile);
        this.image_view_edit_activity_name_edit_photo = (ImageView) findViewById(R.id.image_view_edit_activity_name_edit_photo);
        this.text_view_edit_activity_name_user = (TextView) findViewById(R.id.text_view_edit_activity_name_user);
        this.relative_layout_edit_activity_save= (RelativeLayout) findViewById(R.id.relative_layout_edit_activity_save);
        this.text_input_editor_text_activity_edit_name= (TextInputEditText) findViewById(R.id.text_input_editor_text_activity_edit_name);
        this.text_input_layout_activity_edit_name= (TextInputLayout) findViewById(R.id.text_input_layout_activity_edit_name);
        pd = new ProgressDialog(EditActivity.this);
        pd.setMessage("Updating my user infos");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
    }

    private void initAction() {
        this.relative_layout_edit_activity_save.setOnClickListener( v -> {
            submit();
        });
        this.image_view_edit_activity_name_edit_photo.setOnClickListener(v->{
            SelectImage();
        });
    }

    private void submit() {
        if (!validatName())
            return;
        edit();
    }

    private void setUser() {
        this.text_input_editor_text_activity_edit_name.setText(name);
        this.text_view_edit_activity_name_user.setText(name);
        Picasso.with(this)
                .load(image)
                .error(R.drawable.placeholder_profile)
                .placeholder(R.drawable.placeholder_profile)
                .into(image_view_edit_activity_user_profile);
    }
    private void SelectImage() {
        if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE }, 0);
        }else{
            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            i.setType("image/*");
            startActivityForResult(i, PICK_IMAGE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    SelectImage();
                }
                return;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && null != data) {


            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            imageUrl = picturePath  ;

            File file = new File(imageUrl);
            Picasso.with(this)
                    .load(file)
                    .error(R.drawable.placeholder_profile)
                    .placeholder(R.drawable.placeholder_profile)
                    .into(image_view_edit_activity_user_profile);
        } else {

            Log.i("SonaSys", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.i("SonaSys", "User cancelled");
                    break;
                case -1:
                    break;
            }
        }
    }
    public void edit() {
        pd.show();

        PrefManager prf = new PrefManager(getApplicationContext());

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);

        MultipartBody.Part body = null;
        if (imageUrl != null){
            File file1 = new File(imageUrl);
            int file_size = Integer.parseInt(String.valueOf(file1.length() / 1024 / 1024));
            if (file_size > 20) {
                Toasty.error(getApplicationContext(), "Max file size allowed 20M", Toast.LENGTH_LONG).show();
            }
            Log.v("SIZE", file1.getName() + "");



            final File file = new File(imageUrl);


            ProgressRequestBody requestFile = new ProgressRequestBody(file, EditActivity.this);

            body  = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
        }
        String id_ser=  prf.getString("ID_USER");
        String key_ser=  prf.getString("TOKEN_USER");

        Call<ApiResponse> request = service.editProfile(body, Integer.parseInt(id_ser), key_ser, text_input_editor_text_activity_edit_name.getText().toString().trim());

        request.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()){
                    Toasty.success(getApplication(),getResources().getString(R.string.infos_updated_successfully),Toast.LENGTH_LONG).show();
                    for (int i=0;i<response.body().getValues().size();i++) {
                        if (response.body().getValues().get(i).getName().equals("name")) {
                           String Newname = response.body().getValues().get(i).getValue();
                            if (Newname != null) {
                                if (!Newname.isEmpty()) {
                                    prf.setString("NAME_USER",Newname);

                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("url")) {
                            String NewImage = response.body().getValues().get(i).getValue();
                            if (NewImage != null) {
                                if (!NewImage.isEmpty()) {
                                    prf.setString("IMAGE_USER",NewImage);
                                }
                            }
                        }
                    }
                    finish();
                }else{
                    Toasty.error(getApplication(),getResources().getString(R.string.error_server),Toast.LENGTH_LONG).show();
                }
                pd.dismiss();
                pd.cancel();
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toasty.error(getApplication(),getResources().getString(R.string.error_server),Toast.LENGTH_LONG).show();
                pd.dismiss();
                pd.cancel();
            }
        });
    }

    @Override
    public void onProgressUpdate(int percentage) {
        pd.setProgress(percentage);
    }

    @Override
    public void onError() {
        pd.dismiss();
        pd.cancel();
    }

    @Override
    public void onFinish() {
        pd.dismiss();
        pd.cancel();

    }
}
