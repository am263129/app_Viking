package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.orhanobut.hawk.Hawk;
import com.official.aptoon.Provider.PrefManager;
import com.official.aptoon.R;
import com.official.aptoon.entity.Poster;
import com.official.aptoon.ui.Adapters.PosterAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipe_refresh_layout_list_my_list_search;
    private Button button_try_again;
    private LinearLayout linear_layout_layout_error;
    private RecyclerView recycler_view_activity_my_list_completed,
    recycler_view_activity_my_list_watching,
    recycler_view_activity_my_list_plan,
    recycler_view_activity_my_list_canceled;
    private ImageView image_view_empty_list;
//    private GridLayoutManager gridLayoutManager;
    private PosterAdapter adapter_completed;
    private PosterAdapter adapter_watching;
    private PosterAdapter adapter_plan;
    private PosterAdapter adapter_canceled;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    private Integer page = 0;
    private Integer position = 0;
    private Integer item = 0 ;
    ArrayList<Poster> posterArrayList_completed = new ArrayList<>();
    ArrayList<Poster> posterArrayList_watching = new ArrayList<>();
    ArrayList<Poster> posterArrayList_plan = new ArrayList<>();
    ArrayList<Poster> posterArrayList_canceled = new ArrayList<>();
    LinearLayout area_completed, area_watching, area_plan, area_canceled;
    private RelativeLayout relative_layout_load_more;
    private LinearLayout linear_layout_load_my_list_activity;

    private Integer lines_beetween_ads = 2 ;
    private boolean tabletSize;
    private Boolean native_ads_enabled = false ;
    private int type_ads = 0;
    private PrefManager prefManager;
    String TAG = "MyListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        prefManager= new PrefManager(getApplicationContext());

        initView();
        initAction();
        loadPosters_completed();
        loadPosters_watching();
        loadPosters_plan();
        loadPosters_canceled();
        showAdsBanner();
    }




    private void loadPosters_completed() {
        ArrayList<Poster> tempPosterArrayList = new ArrayList<>();
        try {
            tempPosterArrayList = Hawk.get("my_list_completed");

        }catch (Exception e){
            Log.e(TAG, "no completed list");
        }
        if(tempPosterArrayList == null){
            tempPosterArrayList  = new ArrayList<>();
        }
        if (tempPosterArrayList.size()>0){
            recycler_view_activity_my_list_completed.setVisibility(View.VISIBLE);
            image_view_empty_list.setVisibility(View.GONE);
        }else{
            area_completed.setVisibility(View.GONE);
        }

        for (int i = 0; i < tempPosterArrayList.size(); i++) {
            posterArrayList_completed.add(tempPosterArrayList.get(i));
            if (native_ads_enabled){
                item++;
                if (item == lines_beetween_ads ){
                    item= 0;
                    if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("FACEBOOK")) {
                        posterArrayList_completed.add(new Poster().setTypeView(4));
                    }else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("ADMOB")){
                        posterArrayList_completed.add(new Poster().setTypeView(5));
                    } else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("BOTH")){
                        if (type_ads == 0) {
                            posterArrayList_completed.add(new Poster().setTypeView(4));
                            type_ads = 1;
                        }else if (type_ads == 1){
                            posterArrayList_completed.add(new Poster().setTypeView(5));
                            type_ads = 0;
                        }
                    }
                }
            }
        }

        swipe_refresh_layout_list_my_list_search.setRefreshing(false);
        adapter_completed.notifyDataSetChanged();


    }
    private void loadPosters_watching() {
        List<Poster> tempPosterArrayList = new ArrayList<>();
        try {
            tempPosterArrayList = Hawk.get("my_list_watching");

        }catch (NullPointerException e){
            tempPosterArrayList  = new ArrayList<>();
        }
        if(tempPosterArrayList == null){
            tempPosterArrayList  = new ArrayList<>();
        }
        if (tempPosterArrayList.size()>0){
            recycler_view_activity_my_list_watching.setVisibility(View.VISIBLE);
            image_view_empty_list.setVisibility(View.GONE);
        }else{
            area_watching.setVisibility(View.GONE);
        }

        for (int i = 0; i < tempPosterArrayList.size(); i++) {
            posterArrayList_watching.add(tempPosterArrayList.get(i));
            if (native_ads_enabled){
                item++;
                if (item == lines_beetween_ads ){
                    item= 0;
                    if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("FACEBOOK")) {
                        posterArrayList_watching.add(new Poster().setTypeView(4));
                    }else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("ADMOB")){
                        posterArrayList_watching.add(new Poster().setTypeView(5));
                    } else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("BOTH")){
                        if (type_ads == 0) {
                            posterArrayList_watching.add(new Poster().setTypeView(4));
                            type_ads = 1;
                        }else if (type_ads == 1){
                            posterArrayList_watching.add(new Poster().setTypeView(5));
                            type_ads = 0;
                        }
                    }
                }
            }
        }

        swipe_refresh_layout_list_my_list_search.setRefreshing(false);
        adapter_watching.notifyDataSetChanged();


    }
    private void loadPosters_plan() {
        List<Poster> tempPosterArrayList = new ArrayList<>();
        try {
            tempPosterArrayList = Hawk.get("my_list_plan_to_watch");

        }catch (NullPointerException e){
            tempPosterArrayList  = new ArrayList<>();
        }
        if(tempPosterArrayList == null){
            tempPosterArrayList  = new ArrayList<>();
        }
        if (tempPosterArrayList.size()>0){
            recycler_view_activity_my_list_plan.setVisibility(View.VISIBLE);
            image_view_empty_list.setVisibility(View.GONE);
        }else{
            area_plan.setVisibility(View.GONE);
        }

        for (int i = 0; i < tempPosterArrayList.size(); i++) {
            posterArrayList_plan.add(tempPosterArrayList.get(i));
            if (native_ads_enabled){
                item++;
                if (item == lines_beetween_ads ){
                    item= 0;
                    if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("FACEBOOK")) {
                        posterArrayList_plan.add(new Poster().setTypeView(4));
                    }else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("ADMOB")){
                        posterArrayList_plan.add(new Poster().setTypeView(5));
                    } else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("BOTH")){
                        if (type_ads == 0) {
                            posterArrayList_plan.add(new Poster().setTypeView(4));
                            type_ads = 1;
                        }else if (type_ads == 1){
                            posterArrayList_plan.add(new Poster().setTypeView(5));
                            type_ads = 0;
                        }
                    }
                }
            }
        }

        swipe_refresh_layout_list_my_list_search.setRefreshing(false);
        adapter_plan.notifyDataSetChanged();


    }
    private void loadPosters_canceled() {
        List<Poster> tempPosterArrayList = new ArrayList<>();
        try {
            tempPosterArrayList = Hawk.get("my_list_canceled");

        }catch (NullPointerException e){
            tempPosterArrayList  = new ArrayList<>();
        }
        if(tempPosterArrayList == null){
            tempPosterArrayList  = new ArrayList<>();
        }
        if (tempPosterArrayList.size()>0){
            recycler_view_activity_my_list_canceled.setVisibility(View.VISIBLE);
            image_view_empty_list.setVisibility(View.GONE);
        }else{
            area_canceled.setVisibility(View.GONE);

        }

        for (int i = 0; i < tempPosterArrayList.size(); i++) {
            posterArrayList_canceled.add(tempPosterArrayList.get(i));
            if (native_ads_enabled){
                item++;
                if (item == lines_beetween_ads ){
                    item= 0;
                    if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("FACEBOOK")) {
                        posterArrayList_canceled.add(new Poster().setTypeView(4));
                    }else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("ADMOB")){
                        posterArrayList_canceled.add(new Poster().setTypeView(5));
                    } else if (prefManager.getString("ADMIN_NATIVE_TYPE").equals("BOTH")){
                        if (type_ads == 0) {
                            posterArrayList_canceled.add(new Poster().setTypeView(4));
                            type_ads = 1;
                        }else if (type_ads == 1){
                            posterArrayList_canceled.add(new Poster().setTypeView(5));
                            type_ads = 0;
                        }
                    }
                }
            }
        }

        swipe_refresh_layout_list_my_list_search.setRefreshing(false);
        adapter_canceled.notifyDataSetChanged();


    }

    private void initAction() {



        swipe_refresh_layout_list_my_list_search.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                item = 0;
                page = 0;
                loading = true;
                posterArrayList_completed.clear();
                adapter_completed.notifyDataSetChanged();
                posterArrayList_watching.clear();
                adapter_completed.notifyDataSetChanged();
                posterArrayList_plan.clear();
                adapter_completed.notifyDataSetChanged();
                posterArrayList_canceled.clear();
                adapter_completed.notifyDataSetChanged();
                loadPosters_completed();
                loadPosters_watching();
                loadPosters_plan();
                loadPosters_canceled();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = 0;
                page = 0;
                loading = true;
                posterArrayList_completed.clear();
                adapter_completed.notifyDataSetChanged();
                posterArrayList_watching.clear();
                adapter_completed.notifyDataSetChanged();
                posterArrayList_plan.clear();
                adapter_completed.notifyDataSetChanged();
                posterArrayList_canceled.clear();
                adapter_completed.notifyDataSetChanged();
                loadPosters_completed();
                loadPosters_watching();
                loadPosters_plan();
                loadPosters_canceled();
            }
        });
    }

    private void initView() {

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (!prefManager.getString("ADMIN_NATIVE_TYPE").equals("FALSE")){
            native_ads_enabled=true;
            if (tabletSize) {
                lines_beetween_ads=6*Integer.parseInt(prefManager.getString("ADMIN_NATIVE_LINES"));
            }else{
                lines_beetween_ads=3*Integer.parseInt(prefManager.getString("ADMIN_NATIVE_LINES"));
            }
        }
        if (checkSUBSCRIBED()) {
            native_ads_enabled=false;
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("My list");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.linear_layout_load_my_list_activity=findViewById(R.id.linear_layout_load_my_list_activity);
        this.relative_layout_load_more=findViewById(R.id.relative_layout_load_more);
        this.swipe_refresh_layout_list_my_list_search=findViewById(R.id.swipe_refresh_layout_list_my_list_search);
        button_try_again            = findViewById(R.id.button_try_again);
        image_view_empty_list       = findViewById(R.id.image_view_empty_list);
        linear_layout_layout_error  = findViewById(R.id.linear_layout_layout_error);

        recycler_view_activity_my_list_completed = findViewById(R.id.recycler_view_activity_my_list_completed);
        recycler_view_activity_my_list_watching = findViewById(R.id.recycler_view_activity_my_list_watching);
        recycler_view_activity_my_list_plan = findViewById(R.id.recycler_view_activity_my_list_plan_watch);
        recycler_view_activity_my_list_canceled = findViewById(R.id.recycler_view_activity_my_list_canceled);

        adapter_completed = new PosterAdapter(posterArrayList_completed, this,true);
        adapter_watching = new PosterAdapter(posterArrayList_watching, this,true);
        adapter_plan = new PosterAdapter(posterArrayList_plan, this,true);
        adapter_canceled = new PosterAdapter(posterArrayList_canceled, this,true);

        GridLayoutManager gridLayoutManager;
        if (native_ads_enabled){
            Log.v("MYADS","ENABLED");
            if (tabletSize) {
                gridLayoutManager=  new GridLayoutManager(getApplicationContext(),6,RecyclerView.VERTICAL,false);
                Log.v("MYADS","tabletSize");
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return ((position  + 1) % (lines_beetween_ads  + 1  ) == 0 && position!=0) ? 6 : 1;
                    }
                });
            } else {
                gridLayoutManager=  new GridLayoutManager(getApplicationContext(),3,RecyclerView.VERTICAL,false);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return ((position  + 1) % (lines_beetween_ads + 1 ) == 0  && position!=0)  ? 3 : 1;
                    }
                });
            }
        }else {
            if (tabletSize) {
                gridLayoutManager=  new GridLayoutManager(getApplicationContext(),6,RecyclerView.VERTICAL,false);
            } else {
                gridLayoutManager=  new GridLayoutManager(getApplicationContext(),3,RecyclerView.VERTICAL,false);
            }
        }
        recycler_view_activity_my_list_completed.setHasFixedSize(true);
        recycler_view_activity_my_list_completed.setAdapter(adapter_completed);
        recycler_view_activity_my_list_completed.setLayoutManager(gridLayoutManager);
        recycler_view_activity_my_list_plan.setHasFixedSize(true);
        recycler_view_activity_my_list_plan.setAdapter(adapter_completed);
        recycler_view_activity_my_list_plan.setLayoutManager(gridLayoutManager);
        recycler_view_activity_my_list_watching.setHasFixedSize(true);
        recycler_view_activity_my_list_watching.setAdapter(adapter_completed);
        recycler_view_activity_my_list_watching.setLayoutManager(gridLayoutManager);
        recycler_view_activity_my_list_canceled.setHasFixedSize(true);
        recycler_view_activity_my_list_canceled.setAdapter(adapter_completed);
        recycler_view_activity_my_list_canceled.setLayoutManager(gridLayoutManager);
        area_canceled = findViewById(R.id.area_canceled);
        area_completed = findViewById(R.id.area_completed);
        area_plan = findViewById(R.id.area_plan);
        area_watching = findViewById(R.id.area_watching);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem itemMenu) {
        switch (itemMenu.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
            }
            return true;
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
