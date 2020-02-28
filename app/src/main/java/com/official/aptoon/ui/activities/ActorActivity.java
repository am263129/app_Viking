package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jackandphantom.blurimage.BlurImage;
import com.official.aptoon.Provider.PrefManager;
import com.official.aptoon.R;
import com.official.aptoon.api.apiClient;
import com.official.aptoon.api.apiRest;
import com.official.aptoon.entity.Actor;
import com.official.aptoon.entity.Poster;
import com.official.aptoon.ui.Adapters.PosterAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActorActivity extends AppCompatActivity {

    private LinearLayout linear_layout_activity_actor_movies;
    private TextView text_view_activity_actor_height;
    private TextView text_view_activity_actor_born;
    private TextView text_view_activity_actor_full_name;
    private TextView text_view_activity_actor_bio;
    private ImageView image_view_activity_actor_image;
    private ImageView image_view_activity_actor_background;
    private Actor actor;
    private TextView text_view_activity_actor_type;
    private String backable;
    private PosterAdapter posterAdapter;
    private LinearLayoutManager linearLayoutManagerMoreMovies;
    private RecyclerView recycle_view_activity_activity_actor_movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        initView();
        initAction();
        getMovie();
        setActor();

        getRandomMovies();
        showAdsBanner();

    }

    private void setActor() {

        Picasso.with(this).load(actor.getImage()).into(image_view_activity_actor_image);
        final com.squareup.picasso.Target target = new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                BlurImage.with(getApplicationContext()).load(bitmap).intensity(25).Async(true).into(image_view_activity_actor_background);
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) { }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { }
        };
        Picasso.with(getApplicationContext()).load(actor.getImage()).into(target);
        image_view_activity_actor_background.setTag(target);

        ViewCompat.setTransitionName(image_view_activity_actor_image, "imageMain");

        text_view_activity_actor_full_name.setText(actor.getName());
        text_view_activity_actor_bio.setText(actor.getBio());
        text_view_activity_actor_height.setText(actor.getHeight());
        text_view_activity_actor_born.setText(actor.getBorn());
        text_view_activity_actor_type.setText("("+actor.getType()+")");
    }
    private void getMovie() {
        actor = getIntent().getParcelableExtra("actor");
        backable = getIntent().getStringExtra("backable");
    }
    private void initAction() {

    }

    private void initView() {

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.image_view_activity_actor_background= findViewById(R.id.image_view_activity_actor_background);
        this.image_view_activity_actor_image= findViewById(R.id.image_view_activity_actor_image);
        this.text_view_activity_actor_type= findViewById(R.id.text_view_activity_actor_type);
        this.text_view_activity_actor_bio= findViewById(R.id.text_view_activity_actor_bio);
        this.text_view_activity_actor_born= findViewById(R.id.text_view_activity_actor_born);
        this.text_view_activity_actor_full_name= findViewById(R.id.text_view_activity_actor_full_name);
        this.text_view_activity_actor_height= findViewById(R.id.text_view_activity_actor_height);
        this.linear_layout_activity_actor_movies= findViewById(R.id.linear_layout_activity_actor_movies);
        this.recycle_view_activity_activity_actor_movies= findViewById(R.id.recycle_view_activity_activity_actor_movies);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        return;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getRandomMovies() {

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);

        Call<List<Poster>> call = service.getPosterByActor(actor.getId());
        call.enqueue(new Callback<List<Poster>>() {
            @Override
            public void onResponse(Call<List<Poster>> call, Response<List<Poster>> response) {
                if (response.isSuccessful()){
                    if (response.body().size()>0) {
                        linearLayoutManagerMoreMovies = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        posterAdapter  = new PosterAdapter(response.body(), ActorActivity.this);
                        recycle_view_activity_activity_actor_movies.setHasFixedSize(true);
                        recycle_view_activity_activity_actor_movies.setAdapter(posterAdapter);
                        recycle_view_activity_activity_actor_movies.setLayoutManager(linearLayoutManagerMoreMovies);
                        linear_layout_activity_actor_movies.setVisibility(View.VISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Poster>> call, Throwable t) {
            }
        });
    }
    public boolean checkSUBSCRIBED(){
        PrefManager prefManager= new PrefManager(getApplicationContext());
        if (!prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            return false;
        }
        return true;
    }
    public void showAdsBanner() {
        if (getString(R.string.AD_MOB_ENABLED_BANNER).equals("true")) {
            if (!checkSUBSCRIBED()) {
                PrefManager prefManager= new PrefManager(getApplicationContext());
                if (prefManager.getString("ADMIN_BANNER_TYPE").equals("FACEBOOK")){
                    showFbBanner();
                }
                if (prefManager.getString("ADMIN_BANNER_TYPE").equals("ADMOB")){
                    showAdmobBanner();
                }
                if (prefManager.getString("ADMIN_BANNER_TYPE").equals("BOTH")) {
                    if (prefManager.getString("Banner_Ads_display").equals("FACEBOOK")) {
                        prefManager.setString("Banner_Ads_display", "ADMOB");
                        showAdmobBanner();
                    } else {
                        prefManager.setString("Banner_Ads_display", "FACEBOOK");
                        showFbBanner();
                    }
                }
            }
        }
    }
    public void showAdmobBanner(){
        PrefManager prefManager= new PrefManager(getApplicationContext());
        LinearLayout linear_layout_ads =  (LinearLayout) findViewById(R.id.linear_layout_ads);
        final AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(prefManager.getString("ADMIN_BANNER_ADMOB_ID"));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        linear_layout_ads.addView(mAdView);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }
    public void showFbBanner(){
        PrefManager prefManager= new PrefManager(getApplicationContext());
        LinearLayout linear_layout_ads =  (LinearLayout) findViewById(R.id.linear_layout_ads);
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this, prefManager.getString("ADMIN_BANNER_FACEBOOK_ID"), com.facebook.ads.AdSize.BANNER_HEIGHT_90);
        linear_layout_ads.addView(adView);
        adView.loadAd();

    }
}
