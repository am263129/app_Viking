package com.official.aptoon.ui.Adapters;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.official.aptoon.R;
import com.official.aptoon.entity.Category;
import com.official.aptoon.entity.Notification;

import java.util.ArrayList;
import java.util.List;


public class NotificationAdapter extends ArrayAdapter<Notification> implements Filterable {


    ArrayList<Notification> notifications = new ArrayList<Notification>();

    Notification notification;
    public NotificationAdapter(Context context, int textViewResourceId, ArrayList<Notification> objects) {
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
        v = inflater.inflate(R.layout.item_notification, null);

        TextView message = (TextView)v.findViewById(R.id.message_notification);
        ImageView icon = v.findViewById(R.id.icon_notification);

        message.setText(notifications.get(position).getMessage().toString());

        switch (notifications.get(position).getType()){
            case "download":
                icon.setImageResource(R.drawable.ico_notification_download);
                break;
            case "check":
                icon.setImageResource(R.drawable.ico_notification_check_b);
                break;
            case "coin":
                icon.setImageResource(R.drawable.ico_notification_coin);
                break;
        }

        return v;

    }
}