package com.official.aptoon.ui.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.official.aptoon.Provider.PrefManager;
import com.official.aptoon.R;
import com.official.aptoon.entity.DrawerItem;
import com.official.aptoon.ui.activities.BroadcastTimeActivity;
import com.official.aptoon.ui.activities.EditActivity;
import com.official.aptoon.ui.activities.HomeActivity;
import com.official.aptoon.ui.activities.MyListActivity;
import com.official.aptoon.ui.activities.PolicyActivity;
import com.official.aptoon.ui.activities.ProfileActivity;
import com.official.aptoon.ui.activities.SettingsActivity;
import com.official.aptoon.ui.activities.SupportActivity;

import java.util.List;

public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.DrawerItemHolder> {

    // slide menu items
    private List<DrawerItem> items;
    private List<Integer> drawerIcons;
    private Activity activity;
    public DrawerItemAdapter(List<DrawerItem>  items, Activity activity) {
        super();
        this.items = items;
        this.activity = activity;

    }

    @Override
    public DrawerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.menu_drawer_item, parent, false);

        return new DrawerItemAdapter.DrawerItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DrawerItemHolder holder, int position) {
        holder.itemIcon.setImageResource(items.get(position).getIconId());
        holder.itemText.setText(items.get(position).getText());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option_selected(position);
            }
        });
    }

    private void option_selected(int position) {
        Intent intent;
        final PrefManager prf = new PrefManager(activity.getApplicationContext());
        switch (position){
            case 0:
                HomeActivity.getInstance().viewPager.setCurrentItem(0);
                break;
            case 1:
                intent  =  new Intent(activity.getApplicationContext(), EditActivity.class);
                intent.putExtra("id", Integer.parseInt(prf.getString("ID_USER")));
                intent.putExtra("image",prf.getString("IMAGE_USER").toString());
                intent.putExtra("name",prf.getString("NAME_USER").toString());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                break;
            case 2:
                intent = new Intent(activity.getApplicationContext(), MyListActivity.class);
                activity.startActivity(intent);
                HomeActivity.getInstance().overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 3:
                intent  = new Intent(activity, BroadcastTimeActivity.class);
                activity.startActivity(intent);
                break;
            case 4:
                break;
            case 5:
                intent= new Intent(activity, SupportActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

                break;
            case 6:
                intent = new Intent(activity.getApplicationContext(), SettingsActivity.class);
                activity.startActivity(intent);
                HomeActivity.getInstance().overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 7:
                final String appPackageName=activity.getApplication().getPackageName();
                String shareBody = "Download "+activity.getString(R.string.app_name)+" From :  "+"http://play.google.com/store/apps/details?id=" + appPackageName;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,  activity.getString(R.string.app_name));
                activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.app_name)));
                break;
            case 8:
                intent = new Intent(activity.getApplicationContext(), PolicyActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 9:
                HomeActivity.getInstance().logout();
                break;
            case 10:

                if (prf.getString("NOT_RATE_APP").equals("TRUE")) {
                    activity.onBackPressed();
                } else {
                    HomeActivity.getInstance().rateDialog(true);
                }
                break;
        }

        HomeActivity.getInstance().menu_drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class DrawerItemHolder extends RecyclerView.ViewHolder {

        public ImageView itemIcon;
        public TextView itemText;
        public RelativeLayout item;
        public DrawerItemHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.item);
            itemIcon= (ImageView) itemView.findViewById(R.id.drawer_icon);
            itemText= (TextView) itemView.findViewById(R.id.drawer_text);
            Typeface font = Typeface.createFromAsset(activity.getAssets(), "SpartanMB-Regular.otf");
            itemText.setTypeface(font);
        }
    }
}