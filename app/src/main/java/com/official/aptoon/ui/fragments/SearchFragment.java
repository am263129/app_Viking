package com.official.aptoon.ui.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.official.aptoon.R;
import com.official.aptoon.api.apiClient;
import com.official.aptoon.api.apiRest;
import com.official.aptoon.entity.Channel;
import com.official.aptoon.entity.Data;
import com.official.aptoon.entity.FilteredData;
import com.official.aptoon.entity.Genre;
import com.official.aptoon.entity.Poster;
import com.official.aptoon.ui.Adapters.LiveSearchAdapter;
import com.official.aptoon.ui.Adapters.SearchAdapter;
import com.official.aptoon.ui.activities.HomeActivity;

import java.net.NoRouteToHostException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private View view;
    private SearchAdapter adapter;
    private static TabLayout tabLayout;
    private ViewPager viewPager;
    LinearLayout  error_page, data_page;
    private Button retry;
    private EditText search_index;
    private ImageView btn_search;
    public static RecyclerView channel_list;
    LinearLayout placehoder_page;
    public static int match_target = 0;
    private static List<Data> dataList=new ArrayList<>();
    private List<Poster> search_data_list = new ArrayList<>();
    LiveSearchAdapter search_adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_search, container, false);
        init_view(view);
        init_action();
        loadData();

        adapter = new SearchAdapter(HomeActivity.getInstance().getSupportFragmentManager());
        adapter.addFragment(new Search_ObjectFragment(), "Anime");
        adapter.addFragment(new Search_ObjectFragment(), "Manage");
        adapter.addFragment(new Search_ObjectFragment(), "Chracters");
        adapter.addFragment(new Search_ObjectFragment(), "Genres");
        adapter.addFragment(new Search_ObjectFragment(), "Studios");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                match_target = tab.getPosition();
                refresh_list();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return view;
    }

    private void refresh_list() {

    }

    private void loadData() {


        //video streaming.

        page_load();
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Data> call = service.homeData();
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                apiClient.FormatData(getActivity(),response);
                if (response.isSuccessful()){
                    dataList.clear();

                    if (response.body().getChannels().size()>0){
                        Data channelData = new Data();
                        channelData.setChannels(response.body().getChannels());
                        dataList.add(channelData);
                    }

                    if (response.body().getGenres().size()>0){
                        for (int i = 0; i < response.body().getGenres().size(); i++) {
                            if(response.body().getGenres().get(i).getId() ==0){
                                search_data_list = response.body().getGenres().get(i).getPosters();
                            }
                            Data genreData = new Data();
                            genreData.setGenre(response.body().getGenres().get(i));
                            dataList.add(genreData);
                        }
                    }

                    page_show();
                    search_adapter = new LiveSearchAdapter(HomeActivity.getInstance(),search_data_list);
                    channel_list.setAdapter(search_adapter);
                    search_adapter.notifyDataSetChanged();
                }else{
                    page_error();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                page_error();
            }
        });
    }

    public void get_search_result(String index){
        if(index.equals("")){
            search_adapter = new LiveSearchAdapter(HomeActivity.getInstance(),search_data_list);
            channel_list.setAdapter(search_adapter);
            search_adapter.notifyDataSetChanged();
        }
        else
        {
            List<Poster> result_data_list = new ArrayList<>();
            for (int i = 0; i < search_data_list.size(); i++) {
                if (search_data_list.get(i).getTitle().toLowerCase().contains(index.toLowerCase())) {
                    result_data_list.add(search_data_list.get(i));
                }
            }
            search_adapter = new LiveSearchAdapter(HomeActivity.getInstance(),result_data_list);
            channel_list.setAdapter(search_adapter);
            search_adapter.notifyDataSetChanged();
        }


    }

    private void init_view(View view) {

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        error_page = view.findViewById(R.id.linear_layout_page_error_search);
        data_page = view.findViewById(R.id.linear_layout_show_search);
        retry = (Button)view.findViewById(R.id.button_try_again);
        channel_list = view.findViewById(R.id.list_search_result);
        channel_list.setLayoutManager(new LinearLayoutManager(HomeActivity.getInstance()));
        placehoder_page = view.findViewById(R.id.linear_layout_placehoder);
        data_page = view.findViewById(R.id.linear_layout_data_search);
        search_index = view.findViewById(R.id.edt_search_index);
        btn_search = view.findViewById(R.id.btn_search);
        search_index.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                get_search_result(search_index.getText().toString());
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_search_result(search_index.getText().toString());
            }
        });

    }
    private void init_action() {

    }
    private void page_load() {
        error_page.setVisibility(View.GONE);
        data_page.setVisibility(View.GONE);
    }
    private void page_error() {
        error_page.setVisibility(View.VISIBLE);
        data_page.setVisibility(View.GONE);
    }
    private void page_show() {
        error_page.setVisibility(View.GONE);
        data_page.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}