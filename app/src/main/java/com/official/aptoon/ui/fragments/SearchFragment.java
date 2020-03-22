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
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.official.aptoon.entity.Data;
import com.official.aptoon.entity.Genre;
import com.official.aptoon.entity.SearchedChannel;
import com.official.aptoon.entity.Country;
import com.official.aptoon.entity.Poster;
import com.official.aptoon.ui.Adapters.LiveSearchAdapter;
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
    private static TabLayout tabLayout;
    private ViewPager viewPager;
    LinearLayout  error_page, data_page;
    Button retry;
    public static List<Channel> all_channel;
    public static List<Channel> result_channel;

    public static RecyclerView channel_list;
    LinearLayout placehoder_page;
    static LiveSearchAdapter search_adapter;
    public static int match_target = 0;
    private List<Data> dataList=new ArrayList<>();
    private Genre my_genre_list;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_search, container, false);
        init_test_data();
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
                        all_channel = response.body().getChannels();
                        channelData.setChannels(response.body().getChannels());
                        dataList.add(channelData);
                    }

                    if (response.body().getGenres().size()>0){
                        if (my_genre_list!=null){
                            Data genreDataMyList = new Data();
                            genreDataMyList.setGenre(my_genre_list);
                            dataList.add(genreDataMyList);
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


    private static void init_test_data() {
        all_channel = new ArrayList<>();
        result_channel = new ArrayList<>();
//        all_channel.add(new SearchedChannel("Naruto 290","With the promise of granting any wish the omnipotent holy grail triggered three wars in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)7.93, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fdownload%20(2).jpeg?alt=media&token=53a5070a-b09e-44b2-aef2-7d6385a577a8"));
//        all_channel.add(new SearchedChannel("Dengen ","As the Fourth Holy Grail War rages on with no clear victor in sight, the remaining Servants and their Masters are called upon by Church Supervisor Risei Kotomine in order to band together and confront an im...",
//                "www.movie.naruto.com", "Episodes:13", (float)8.49, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2FSmartSelect_20200229-002050_Samsung%20Internet.jpg?alt=media&token=f5133bed-e9cf-4338-994f-67e99ec61e60"));
//        all_channel.add(new SearchedChannel("Rikimaru ","Fate/Prototype is digest version of Kingko Nasu's original version of Fate/stay night.",
//                "www.movie.naruto.com", "Episodes:13", (float)8.39, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fimages%20(1).jpeg?alt=media&token=83c56019-fe9d-40f0-8200-4afcc32e9ec6"));
//        all_channel.add(new SearchedChannel("Rogue Knight 290","With the promise of granting any wish the omnipotent holy grail triggered three wars in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.42, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fdownload.jpeg?alt=media&token=de325547-77a2-46e5-a3e4-9505ee649ed9"));
//        all_channel.add(new SearchedChannel("Advantage of Sasske 290","With the promise of granting any wish the omnipotent holy grail triggered three wars in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.41, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fdownload%20(1).jpeg?alt=media&token=2c4801c8-ac89-491f-acad-6402bd7ddafd"));
//        all_channel.add(new SearchedChannel("Naruto 391","As the Fourth Holy Grail War rages on with no clear victor in sight, the remaining Servants ed three wars in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.67, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fdownload%20(3).jpeg?alt=media&token=29eb9584-fb2f-43a3-aa41-a0b09f73304c"));
//        all_channel.add(new SearchedChannel("Naruto 392(Kakashi vs Orochimaru)","With the promise of granting any wish the omnipotent holy grail triggered three wars in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.17, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fdownload%20(3).jpeg?alt=media&token=29eb9584-fb2f-43a3-aa41-a0b09f73304c"));
//        all_channel.add(new SearchedChannel("Sakura VS Hinata","Fate/Prototype is digest version of Kingko Nasu's original version of Fate/stay night.past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.89, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fdownload%20(2).jpeg?alt=media&token=53a5070a-b09e-44b2-aef2-7d6385a577a8"));
//        all_channel.add(new SearchedChannel("Pig Family","With the promise of granting any wish the omnipotent holy grail triggered three wars in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.23, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fdownload.jpeg?alt=media&token=de325547-77a2-46e5-a3e4-9505ee649ed9"));
//        all_channel.add(new SearchedChannel("Kid kid kid","With the promise of granting any wish the omnipotent holy grail triggered three wars in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.19, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fdownload.jpeg?alt=media&token=de325547-77a2-46e5-a3e4-9505ee649ed9"));
//        all_channel.add(new SearchedChannel("Arana Token","With the promise of granting any wish the omnipotent holy grail triggered three wars in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.12, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fbackground%20(2).jpg?alt=media&token=c8defdd0-7768-49ee-8e55-fbb059ad1bf0"));
//        all_channel.add(new SearchedChannel("Dengen Drive Tournament","With the promise of granting any wish the omnipotent holy grail triggered three wars in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.43, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fimages%20(1).jpeg?alt=media&token=83c56019-fe9d-40f0-8200-4afcc32e9ec6"));
//        all_channel.add(new SearchedChannel("Arena Event","As the Fourth Holy Grail War rages on with no clear victor in sight, the remaining Servants  in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.22, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fimages.jpeg?alt=media&token=ef5f1971-0c15-4475-8f63-1dea3b5cb00d"));
//        all_channel.add(new SearchedChannel("Galex and Athel","As the Fourth Holy Grail War rages on with no clear victor in sight, the remaining Servants s in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.54, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fdownload%20(2).jpeg?alt=media&token=53a5070a-b09e-44b2-aef2-7d6385a577a8"));
//        all_channel.add(new SearchedChannel("Charlie","With the promise of granting any wish the omnipotent holy grail triggered three wars in the past, each too cruel and fierce to leave a victor, in spite of that, the wealthy Einzbern family is confident that the Fourth Holy Gra..",
//                "www.movie.naruto.com", "Episodes:13", (float)8.29, "https://firebasestorage.googleapis.com/v0/b/lancul-10966.appspot.com/o/Restaurant%2Fimages.jpeg?alt=media&token=ef5f1971-0c15-4475-8f63-1dea3b5cb00d"));
        for (int i = 0; i< all_channel.size(); i++){
            result_channel.add(i,all_channel.get(i));
        }

    }

    public static void get_search_result(String index){
//        TabLayout.Tab tab = tabLayout.getTabAt(0);
//        tab.select();

        init_test_data();
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