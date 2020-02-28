package com.official.aptoon.ui.Adapters;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.official.aptoon.R;
import com.official.aptoon.entity.Slide;
import com.official.aptoon.ui.activities.CategoryActivity;
import com.official.aptoon.ui.activities.ChannelActivity;
import com.official.aptoon.ui.activities.GenreActivity;
import com.official.aptoon.ui.activities.MovieActivity;
import com.official.aptoon.ui.activities.SerieActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SlideAdapter extends PagerAdapter {
    private List<Slide> slideList =new ArrayList<Slide>();
    private Activity activity;

    public SlideAdapter(Activity activity, List<Slide> stringList) {
        this.slideList = stringList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return slideList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater)this.activity.getSystemService(this.activity.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.item_slide_one, container, false);

        TextView text_view_item_slide_one_title =  (TextView)  view.findViewById(R.id.text_view_item_slide_one_title);
        ImageView image_view_item_slide_one =  (ImageView)  view.findViewById(R.id.image_view_item_slide_one);

        text_view_item_slide_one_title.setText(slideList.get(position).getTitle());

        CardView card_view_item_slide_one = (CardView) view.findViewById(R.id.card_view_item_slide_one);
        card_view_item_slide_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideList.get(position).getType().equals("1") && slideList.get(position).getUrl()!=null){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(slideList.get(position).getUrl()));
                    activity.startActivity(browserIntent);
                }else if (slideList.get(position).getType().equals("2") && slideList.get(position).getCategory()!=null) {
                    Intent intent  =  new Intent(activity.getApplicationContext(), CategoryActivity.class);
                    intent.putExtra("category",slideList.get(position).getCategory());
                    (activity).startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
                }else if (slideList.get(position).getType().equals("3") && slideList.get(position).getChannel()!=null){
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,image_view_item_slide_one, "imageMain");
                    Intent in = new Intent(activity, ChannelActivity.class);
                    in.putExtra("channel", slideList.get(position).getChannel());
                    activity.startActivity(in, activityOptionsCompat.toBundle());
                }else if (slideList.get(position).getType().equals("4") && slideList.get(position).getPoster()!=null){
                    if (slideList.get(position).getPoster().getType().equals("movie")) {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, image_view_item_slide_one, "imageMain");
                        Intent in = new Intent(activity, MovieActivity.class);
                        in.putExtra("poster", slideList.get(position).getPoster());
                        activity.startActivity(in, activityOptionsCompat.toBundle());
                    } else if (slideList.get(position).getPoster().getType().equals("serie")) {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, image_view_item_slide_one, "imageMain");
                        Intent in = new Intent(activity, SerieActivity.class);
                        in.putExtra("poster", slideList.get(position).getPoster());
                        activity.startActivity(in, activityOptionsCompat.toBundle());
                    }
                }else if (slideList.get(position).getType().equals("5") && slideList.get(position).getGenre()!=null){
                    Intent intent  =  new Intent(activity.getApplicationContext(), GenreActivity.class);
                    intent.putExtra("genre",slideList.get(position).getGenre());
                    (activity).startActivity(intent, ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight()).toBundle());
                }
            }
        });

        Picasso.with(activity).load(slideList.get(position).getImage()).placeholder(R.drawable.placeholder).into(image_view_item_slide_one);

        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public float getPageWidth (int position) {
        return 1f;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);

    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);

    }
    @Override
    public Parcelable saveState() {
        return null;
    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
