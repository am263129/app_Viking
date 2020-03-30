package com.official.aptoon.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

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
    Button retry;
    public static List<Channel> all_channel;
    public static List<Channel> all_genreData;
    public static List<Channel> result_channel;

    public static RecyclerView channel_list;
    LinearLayout placehoder_page;
    static LiveSearchAdapter search_adapter;
    public static int match_target = 0;
    private static List<Data> dataList=new ArrayList<>();
    private Genre my_genre_list;
    private List<Poster> movieList =  new ArrayList<>();
    ArrayList<FilteredData> total_data =  new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_search, container, false);
        init_view(view);
        init_action();
        loadData();
//        page_load();

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






//        Intent intent = new Intent(HomeActivity.getInstance(), SearchActivity.class);
//        intent.putExtra("query","Eskimo");
//        startActivity(intent);

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
                    dataList.add(new Data().setViewType(0));

                    if (response.body().getChannels().size()>0){
                        Data channelData = new Data();
                        channelData.setChannels(response.body().getChannels());
                        dataList.add(channelData);
                    }

                    if (response.body().getGenres().size()>0){
                        for (int i = 0; i < response.body().getGenres().size(); i++) {
                            Data genreData = new Data();
                            genreData.setGenre(response.body().getGenres().get(i));
                            dataList.add(genreData);
                        }
                    }
                    page_show();
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

//    private void loadMovies() {
//
//        placehoder_page.setVisibility(View.VISIBLE);
//        Retrofit retrofit = apiClient.getClient();
//        apiRest service = retrofit.create(apiRest.class);
//        Call<List<Poster>> call = service.getMoviesByFiltres(genreSelected,orderSelected,page);
//        call.enqueue(new Callback<List<Poster>>() {
//            @Override
//            public void onResponse(Call<List<Poster>> call, final Response<List<Poster>> response) {
//                if (response.isSuccessful()){
//                    if (response.body().size()>0){
//                        for (int i = 0; i < response.body().size(); i++) {
//                            movieList.add(response.body().get(i));
//                            response.body().get(i).get
//                        }
//
//
//                        linear_layout_page_error_movies_fragment.setVisibility(View.GONE);
//                        recycler_view_movies_fragment.setVisibility(View.VISIBLE);
//                        image_view_empty_list.setVisibility(View.GONE);
//
//                        adapter.notifyDataSetChanged();
//                        page++;
//                        loading=true;
//                    }else{
//                        if (page==0) {
//                            linear_layout_page_error_movies_fragment.setVisibility(View.GONE);
//                            recycler_view_movies_fragment.setVisibility(View.GONE);
//                            image_view_empty_list.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }else{
//                    linear_layout_page_error_movies_fragment.setVisibility(View.VISIBLE);
//                    recycler_view_movies_fragment.setVisibility(View.GONE);
//                    image_view_empty_list.setVisibility(View.GONE);
//                }
//                relative_layout_load_more_movies_fragment.setVisibility(View.GONE);
//                swipe_refresh_layout_movies_fragment.setRefreshing(false);
//                linear_layout_load_movies_fragment.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(Call<List<Poster>> call, Throwable t) {
//                linear_layout_page_error_movies_fragment.setVisibility(View.VISIBLE);
//                recycler_view_movies_fragment.setVisibility(View.GONE);
//                image_view_empty_list.setVisibility(View.GONE);
//                relative_layout_load_more_movies_fragment.setVisibility(View.GONE);
//                swipe_refresh_layout_movies_fragment.setVisibility(View.GONE);
//                linear_layout_load_movies_fragment.setVisibility(View.GONE);
//
//            }
//        });
//    }


    public void get_search_result(String index){
//        TabLayout.Tab tab = tabLayout.getTabAt(0);
//        tab.select();
//        loadData();
        result_channel.clear();
        for (int i = 0;i < all_channel.size(); i++){
            String match = "";
            switch (match_target){
                case 0:
                    match = all_channel.get(i).getTitle();
                    break;
                case 1:
                    match = all_channel.get(i).getDescription();
                    break;
                case 2:
                    match = all_channel.get(i).getWebsite();
                    break;
                case 3:
                    match = all_channel.get(i).getClassification();
                    break;
                case 4:
                    match = String.valueOf(all_channel.get(i).getRating());
                    break;
                default:
                    match = all_channel.get(i).getTitle();
                    break;
            }
            if(match.toLowerCase().contains(index.toLowerCase())){
                result_channel.add(all_channel.get(i));
            }
        }

        for(int i = 0; i< dataList.size(); i++){
            List<Channel> channels = dataList.get(i).getChannels();
            for (int j = 0; j< channels.size();j++){
                String image = "";
                Float rating = 0f;
                String title = "";
                String description = "";
                String website = "";
                String classification = "";
                if (channels.get(j).getImage() != null) {
                    image = channels.get(j).getImage();
                }
                if (channels.get(j).getDescription() != null){
                    description = channels.get(j).getDescription();
                }
                if (channels.get(j).getRating() != null){
                    rating = channels.get(j).getRating();
                }
                if (channels.get(j).getWebsite() != null){
                    website = channels.get(j).getWebsite();
                }
                if (channels.get(j).getClassification() !=null){
                    classification = channels.get(j).getClassification();
                }
                FilteredData fdata = new FilteredData(title,description,website,classification,rating, image);
                total_data.add(fdata);
            }
            List<Genre> genres = dataList.get(i).getGenres();

        }
        search_adapter = new LiveSearchAdapter(HomeActivity.getInstance(),SearchFragment.result_channel);
        channel_list.setAdapter(search_adapter);


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

        search_adapter = new LiveSearchAdapter(HomeActivity.getInstance(),SearchFragment.result_channel);
        channel_list.setAdapter(search_adapter);
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
}