package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.official.aptoon.Provider.PrefManager;
import com.official.aptoon.R;
import com.official.aptoon.api.apiClient;
import com.official.aptoon.api.apiRest;
import com.official.aptoon.entity.Actor;
import com.official.aptoon.ui.Adapters.ActorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActorsActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipe_refresh_layout_list_actors_search;
    private Button button_try_again;
    private LinearLayout linear_layout_layout_error;
    private RecyclerView recycler_view_activity_actors;
    private ImageView image_view_empty_list;
    private GridLayoutManager gridLayoutManager;
    private ActorAdapter adapter;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    private Integer page = 0;
    private Integer position = 0;
    private Integer item = 0 ;
    ArrayList<Actor> actorArrayList = new ArrayList<>();
    private RelativeLayout relative_layout_load_more;
    private LinearLayout linear_layout_load_actors_activity;
    private ImageView image_view_activity_actors_search;
    private ImageView image_view_activity_actors_close_search;
    private EditText edit_text_actors_activity_actors;
    private String searchtext = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actors);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initAction();
        loadActors();
        showAdsBanner();
    }

    private void loadActors() {
        if (page==0){
            linear_layout_load_actors_activity.setVisibility(View.VISIBLE);
        }else{
            relative_layout_load_more.setVisibility(View.VISIBLE);
        }
        swipe_refresh_layout_list_actors_search.setRefreshing(false);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Actor>> call = service.getActorsList(page,searchtext);
        call.enqueue(new Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, final Response<List<Actor>> response) {
                if (response.isSuccessful()){
                    if (response.body().size()>0){
                        for (int i = 0; i < response.body().size(); i++) {
                            actorArrayList.add(response.body().get(i));
                        }
                        linear_layout_layout_error.setVisibility(View.GONE);
                        recycler_view_activity_actors.setVisibility(View.VISIBLE);
                        image_view_empty_list.setVisibility(View.GONE);

                        adapter.notifyDataSetChanged();
                        page++;
                        loading=true;
                    }else{
                        if (page==0) {
                            linear_layout_layout_error.setVisibility(View.GONE);
                            recycler_view_activity_actors.setVisibility(View.GONE);
                            image_view_empty_list.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    linear_layout_layout_error.setVisibility(View.VISIBLE);
                    recycler_view_activity_actors.setVisibility(View.GONE);
                    image_view_empty_list.setVisibility(View.GONE);
                }
                relative_layout_load_more.setVisibility(View.GONE);
                swipe_refresh_layout_list_actors_search.setRefreshing(false);
                linear_layout_load_actors_activity.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Actor>> call, Throwable t) {
                linear_layout_layout_error.setVisibility(View.VISIBLE);
                recycler_view_activity_actors.setVisibility(View.GONE);
                image_view_empty_list.setVisibility(View.GONE);
                relative_layout_load_more.setVisibility(View.GONE);
                swipe_refresh_layout_list_actors_search.setVisibility(View.GONE);
                linear_layout_load_actors_activity.setVisibility(View.GONE);

            }
        });
    }

    private void initAction() {
        edit_text_actors_activity_actors.setOnEditorActionListener((v,actionId,event) -> {
            if (edit_text_actors_activity_actors.getText().length()>2){
                item = 0;
                page = 0;
                loading = true;
                actorArrayList.clear();
                adapter.notifyDataSetChanged();
                searchtext = edit_text_actors_activity_actors.getText().toString().trim();
                loadActors();
                image_view_activity_actors_close_search.setVisibility(View.VISIBLE);

            } return false;
        });
        image_view_activity_actors_close_search.setOnClickListener(v->{
            item = 0;
            page = 0;
            loading = true;
            actorArrayList.clear();
            adapter.notifyDataSetChanged();
            this.searchtext = "null";
            edit_text_actors_activity_actors.setText("");
            loadActors();
            image_view_activity_actors_close_search.setVisibility(View.GONE);
        });
        image_view_activity_actors_search.setOnClickListener(v->{
            if (edit_text_actors_activity_actors.getText().length()>2) {
                item = 0;
                page = 0;
                loading = true;
                actorArrayList.clear();
                adapter.notifyDataSetChanged();
                this.searchtext = edit_text_actors_activity_actors.getText().toString().trim();
                loadActors();
                image_view_activity_actors_close_search.setVisibility(View.VISIBLE);
            }
        });
        swipe_refresh_layout_list_actors_search.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                item = 0;
                page = 0;
                loading = true;
                actorArrayList.clear();
                adapter.notifyDataSetChanged();
                loadActors();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = 0;
                page = 0;
                loading = true;
                actorArrayList.clear();
                adapter.notifyDataSetChanged();
                loadActors();
            }
        });
        recycler_view_activity_actors.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {

                    visibleItemCount    = gridLayoutManager.getChildCount();
                    totalItemCount      = gridLayoutManager.getItemCount();
                    pastVisiblesItems   = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            loadActors();
                        }
                    }
                }else{

                }
            }
        });
    }

    private void initView() {
        this.image_view_activity_actors_search=findViewById(R.id.image_view_activity_actors_search);
        this.image_view_activity_actors_close_search=findViewById(R.id.image_view_activity_actors_close_search);
        this.edit_text_actors_activity_actors=findViewById(R.id.edit_text_actors_activity_actors);
        this.linear_layout_load_actors_activity=findViewById(R.id.linear_layout_load_actors_activity);
        this.relative_layout_load_more=findViewById(R.id.relative_layout_load_more);
        this.swipe_refresh_layout_list_actors_search=findViewById(R.id.swipe_refresh_layout_list_actors_search);
        button_try_again            = findViewById(R.id.button_try_again);
        image_view_empty_list       = findViewById(R.id.image_view_empty_list);
        linear_layout_layout_error  = findViewById(R.id.linear_layout_layout_error);
        recycler_view_activity_actors          = findViewById(R.id.recycler_view_activity_actors);
        adapter = new ActorAdapter(actorArrayList, this);
        gridLayoutManager = new GridLayoutManager(this,3);
        recycler_view_activity_actors.setHasFixedSize(true);
        recycler_view_activity_actors.setAdapter(adapter);
        recycler_view_activity_actors.setLayoutManager(gridLayoutManager);

    }

    public boolean checkSUBSCRIBED(){
        PrefManager prefManager= new PrefManager(getApplicationContext());
        if (!prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            return false;
        }
        return true;
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
        }
        return super.onOptionsItemSelected(item);
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
