package com.official.aptoon.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.official.aptoon.Provider.PrefManager;
import com.official.aptoon.R;
import com.official.aptoon.api.apiClient;
import com.official.aptoon.api.apiRest;
import com.official.aptoon.config.Global;
import com.official.aptoon.entity.Actor;
import com.official.aptoon.entity.ApiResponse;
import com.official.aptoon.entity.BroadcastTime;
import com.official.aptoon.ui.Adapters.BroadcastAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BroadcastTimeActivity extends AppCompatActivity {

    ListView broadcast_list;
    TextView label;
    BroadcastAdapter adapter;
    LinearLayout error_page,list_page, load_page;
    ArrayList<BroadcastTime> time_list;
    Button try_again;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_time);

        init_view();
        init_action();
        load_data();


        time_list = new ArrayList<>();
        //  test
        time_list.add(new BroadcastTime("7:45","My Heart will go"));
        time_list.add(new BroadcastTime("11:23","3 Kids and Advantage of Leon scott kenedy"));
        time_list.add(new BroadcastTime("13:45","Naruto (445)"));
        time_list.add(new BroadcastTime("14:11","Naruto (446)"));
    }

    private void init_view() {
        label = findViewById(R.id.label_time);
        broadcast_list = findViewById(R.id.list_broadcast_time);
        error_page = findViewById(R.id.linear_layout_page_error_broadcast_list);
        list_page = findViewById(R.id.time_list);
        try_again = findViewById(R.id.button_try_again);
        load_page = findViewById(R.id.linear_layout_load_broadcast_time);
        Typeface font = Typeface.createFromAsset(BroadcastTimeActivity.this.getAssets(), "SpartanMB-Regular.otf");
        label.setTypeface(font);
    }

    private void init_action() {
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load_data();
            }
        });
    }



    public void load_data(){
        show_load();

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<BroadcastTime>> call = service.broadcast_time();
        call.enqueue(new Callback<List<BroadcastTime>>() {
            @Override
            public void onResponse(Call<List<BroadcastTime>> call, Response<List<BroadcastTime>> response) {

                if(response.body().size()<0){
                    //exception-no data
                }
                else{
                    for (int i = 0; i < response.body().size(); i ++){
                        time_list.add(response.body().get(i));
                    }
                    adapter = new BroadcastAdapter(BroadcastTimeActivity.this, R.layout.item_broadcast_time, time_list );
                    broadcast_list.setAdapter(adapter);
                    show_data();
                }
            }
            @Override
            public void onFailure(Call<List<BroadcastTime>> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Operation has been cancelled ! ", Toast.LENGTH_SHORT, true).show();
                show_error();
            }
        });
    }

    private void show_load() {
        load_page.setVisibility(View.VISIBLE);
        list_page.setVisibility(View.GONE);
        error_page.setVisibility(View.GONE);
    }

    private void show_error() {
        list_page.setVisibility(View.GONE);
        error_page.setVisibility(View.VISIBLE);
        load_page.setVisibility(View.GONE);
    }
    private void show_data(){
        error_page.setVisibility(View.GONE);
        list_page.setVisibility(View.VISIBLE);
        load_page.setVisibility(View.GONE);
    }
}
