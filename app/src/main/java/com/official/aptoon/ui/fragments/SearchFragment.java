package com.official.aptoon.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.official.aptoon.Provider.PrefManager;
import com.official.aptoon.R;
import com.official.aptoon.api.apiClient;
import com.official.aptoon.api.apiRest;
import com.official.aptoon.entity.Category;
import com.official.aptoon.entity.Channel;
import com.official.aptoon.entity.Country;
import com.official.aptoon.entity.Poster;
import com.official.aptoon.ui.Adapters.ChannelAdapter;
import com.official.aptoon.ui.Adapters.SearchAdapter;
import com.official.aptoon.ui.activities.HomeActivity;
import com.official.aptoon.ui.activities.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private View view;
    private SearchAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_search, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        adapter = new SearchAdapter(HomeActivity.getInstance().getSupportFragmentManager());
        adapter.addFragment(new Search_ObjectFragment(), "Anime");
        adapter.addFragment(new Search_ObjectFragment(), "Manage");
        adapter.addFragment(new Search_ObjectFragment(), "Chracters");
        adapter.addFragment(new Search_ObjectFragment(), "Genres");
        adapter.addFragment(new Search_ObjectFragment(), "Studios");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        Intent intent = new Intent(HomeActivity.getInstance(), SearchActivity.class);
        intent.putExtra("query","Eskimo");
        startActivity(intent);

        return view;
    }
}