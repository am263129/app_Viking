package com.official.aptoon.ui.Adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.official.aptoon.R;
import com.official.aptoon.entity.Country;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CountryAdapter extends  RecyclerView.Adapter<CountryAdapter.CountryHolder>{
    private List<Country> countriesList;
    private Activity activity;
    public CountryAdapter(List<Country> countriesList, Activity activity) {
        this.countriesList = countriesList;
        this.activity = activity;
    }
    @Override
    public CountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country , null);
        CountryHolder mh = new CountryHolder(v);
        return mh;
    }
    @Override
    public void onBindViewHolder(CountryHolder holder, final int position) {
        holder.text_view_item_country_title.setText(countriesList.get(position).getTitle());
    }
    @Override
    public int getItemCount() {
        return countriesList.size();
    }
    public class CountryHolder extends RecyclerView.ViewHolder {
        private final TextView text_view_item_country_title;
        public CountryHolder(View itemView) {
            super(itemView);
            this.text_view_item_country_title =  (TextView) itemView.findViewById(R.id.text_view_item_country_title);
        }
    }
}
