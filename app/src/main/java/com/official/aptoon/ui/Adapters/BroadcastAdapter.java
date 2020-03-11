package com.official.aptoon.ui.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.official.aptoon.R;
import com.official.aptoon.entity.BroadcastTime;
import com.official.aptoon.entity.Notification;
import com.official.aptoon.ui.activities.HomeActivity;
import com.official.aptoon.ui.activities.ProfileActivity;

import java.util.ArrayList;


public class BroadcastAdapter extends ArrayAdapter<BroadcastTime> implements Filterable {


    ArrayList<BroadcastTime> notifications = new ArrayList<BroadcastTime>();

    public BroadcastAdapter(Context context, int textViewResourceId, ArrayList<BroadcastTime> objects) {
        super(context, textViewResourceId, objects);
        notifications = objects;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_broadcast_time, null);
        Typeface font = Typeface.createFromAsset(HomeActivity.getInstance().getAssets(), "SpartanMB-Regular.otf");

        TextView time = (TextView)v.findViewById(R.id.time);
        TextView title = (TextView)v.findViewById(R.id.title);
        time.setTypeface(font);
        title.setTypeface(font);
        time.setText(notifications.get(position).getTime().toString());
        title.setText(notifications.get(position).getTitle().toString());
        title.setSelected(true);

        return v;

    }
}