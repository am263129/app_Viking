package com.official.aptoon.ui.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.official.aptoon.R;
import com.official.aptoon.entity.Channel;
import com.official.aptoon.entity.Poster;
import com.official.aptoon.ui.activities.HomeActivity;

import java.util.List;

public class LiveSearchAdapter extends RecyclerView.Adapter<LiveSearchAdapter.PlanetHolder> implements Filterable {

    private static final String TAG = "LivesearchAdapter";
    private Context context;
    private List<Poster> posters;
    boolean like = false;

    public LiveSearchAdapter(Context context, List<Poster> posters) {
        this.context = context;
        this.posters = posters;
    }


    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_livesearch_result, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, final int position) {
        final Poster poster = posters.get(position);
        holder.setDetails(poster);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return posters.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView title, description, rating, channel_type, channel_classification;
        private ImageView movie_image;
        View mView;


        PlanetHolder(View itemView) {
            super(itemView);
            mView = itemView;
            title = itemView.findViewById(R.id.label_title);
            description = itemView.findViewById(R.id.label_description);
            rating = itemView.findViewById(R.id.label_rating);
            movie_image = itemView.findViewById(R.id.movie_image);
            channel_classification = itemView.findViewById(R.id.label_classification);


        }

        void setDetails(Poster poster) {
            try {
                Glide.with(HomeActivity.getInstance()).load(poster.getImage()).into(movie_image);
                rating.setText(String.valueOf(poster.getRating()));
                title.setText(poster.getTitle().toString());
                description.setText(poster.getDescription().toString());
            }
            catch (Exception E){
                Log.e(TAG,E.toString());
            }
        }
    }
    public void clear() {
        int size = posters.size();
        posters.clear();
        notifyItemRangeRemoved(0, size);
    }
}
