package com.official.aptoon.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.official.aptoon.R;
import com.official.aptoon.ui.Adapters.LiveSearchAdapter;
import com.official.aptoon.ui.activities.HomeActivity;

public class Search_ObjectFragment extends Fragment {

    public static RecyclerView channel_list;
    LinearLayout placehoder_page, data_page;
    static LiveSearchAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_object, container, false);
        channel_list = view.findViewById(R.id.list_search_result);
        channel_list.setLayoutManager(new LinearLayoutManager(HomeActivity.getInstance()));
        placehoder_page = view.findViewById(R.id.linear_layout_placehoder);
        data_page = view.findViewById(R.id.linear_layout_data_search);

        //test
        show_data();
//        adapter = new LiveSearchAdapter(HomeActivity.getInstance(),SearchFragment.result_channel);
//        channel_list.setAdapter(adapter);
        return view;


    }

    public void show_data(){
        placehoder_page.setVisibility(View.GONE);
        data_page.setVisibility(View.VISIBLE);
    }
    public static void refreshAdapter(){
        channel_list.removeAllViews();
//        adapter.notifyDataSetChanged();
        adapter.clear();
        channel_list.setAdapter(adapter);
//        adapter = new LiveSearchAdapter(HomeActivity.getInstance(),SearchFragment.result_channel);
//        channel_list.setAdapter(adapter);
    }
}