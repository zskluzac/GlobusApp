package hu.ait.android.globusapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hu.ait.android.globusapp.adapter.EndpointAdapter;
import hu.ait.android.globusapp.data.EndList;
import hu.ait.android.globusapp.data.Endpoint;
import hu.ait.android.globusapp.network.GlobusAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EndpointActivity extends AppCompatActivity {

    private EndpointAdapter adapter;
    final static String NAME = "NAME";
    final static String ID = "ID";
    private List<Endpoint> endpointList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endpoint);

        context = this;

        endpointList = new ArrayList<Endpoint>();
        Retrofit retrofit = getRetrofit();
        final GlobusAPI api = retrofit.create(GlobusAPI.class);

        Call<Map<String,List<EndList>>> call = api.getEndpoints();

        call.enqueue(new Callback<Map<String, List<EndList>>>() {
            @Override
            public void onResponse(Call<Map<String, List<EndList>>> call, Response<Map<String, List<EndList>>> response) {
                if(response.isSuccessful()) {
                    for(EndList end : response.body().get("list")) {
                        Endpoint newEnd = new Endpoint(end.getName(), end.getId());
                        endpointList.add(newEnd);
                        Log.d("UPDATE: ", endpointList.get(endpointList.size()-1).getName() + newEnd.getId());
                    }
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);

                    RecyclerView recyclerView = findViewById(R.id.recyclerViewEndpoints);
                    adapter = new EndpointAdapter(context, endpointList);

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<EndList>>> call, Throwable t) {
                Log.e("Could not call API: ", t.getMessage(), t);

            }
        });
    }

    public void openFileActivity(int position, String name, String id) {
        Intent fileIntent = new Intent(this, FileActivity.class);
        fileIntent.putExtra(NAME, name);
        fileIntent.putExtra(ID, id);
        startActivity(fileIntent);
    }


    @NonNull
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
