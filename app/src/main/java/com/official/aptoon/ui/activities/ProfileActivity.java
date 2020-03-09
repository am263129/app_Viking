package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jackandphantom.blurimage.BlurImage;
import com.official.aptoon.Provider.PrefManager;
import com.official.aptoon.R;
import com.official.aptoon.config.Global;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;

import static com.official.aptoon.ui.activities.HomeActivity.LoadImageFromWebOperations;

public class ProfileActivity extends AppCompatActivity {
    EditText edt_email, edt_birthday, edt_firstname, edt_lastname, edt_cur_pass, edt_new_pass, edt_con_pass;
    ImageView img_user_avatar, btn_edit;
    Bitmap bitmap;
    private Integer PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Typeface font = Typeface.createFromAsset(ProfileActivity.this.getAssets(), "SpartanMB-Regular.otf");
        edt_email = findViewById(R.id.edt_email);
        edt_birthday = findViewById(R.id.edt_birthday);
        edt_firstname = findViewById(R.id.edt_firstname);
        edt_lastname = findViewById(R.id.edt_lastname);
        edt_cur_pass = findViewById(R.id.edt_cur_pass);
        edt_new_pass = findViewById(R.id.edt_new_pass);
        edt_con_pass = findViewById(R.id.edt_con_pass);
        img_user_avatar = findViewById(R.id.img_user_photo);

        edt_email .setTypeface(font);
        edt_birthday .setTypeface(font);
        edt_firstname .setTypeface(font);
        edt_lastname .setTypeface(font);
        edt_cur_pass .setTypeface(font);
        edt_new_pass .setTypeface(font);
        edt_con_pass .setTypeface(font);

        edt_email.setText(Global.user_name);

        if(!Global.user_image.equals("")){
            PrefManager prf= new PrefManager(getApplicationContext());

            Picasso.with(getApplicationContext()).load(prf.getString("IMAGE_USER").toString()).placeholder(R.drawable.placeholder_profile).error(R.drawable.placeholder_profile).resize(200,200).centerCrop().into(img_user_avatar);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    BlurImage.with(getApplicationContext()).load(bitmap).intensity(25).Async(true).into(img_user_avatar);
                }
                @Override
                public void onBitmapFailed(Drawable errorDrawable) { }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) { }
            };
        }


        img_user_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(ProfileActivity.this);
            }
        });



    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
            img_user_avatar.setImageBitmap(bitmap);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
        if (requestCode == PICK_IMAGE) {
            //TODO: action

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                img_user_avatar.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
