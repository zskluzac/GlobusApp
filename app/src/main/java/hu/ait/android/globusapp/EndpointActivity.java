package hu.ait.android.globusapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
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
        getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);

        context = this;
        endpointList = new ArrayList<>();
        Retrofit retrofit = getRetrofit();
        final GlobusAPI api = retrofit.create(GlobusAPI.class);

        makeAPICall(api);
        setupToolbar();
    }

    private void makeAPICall(GlobusAPI api) {
        Call<Map<String,List<EndList>>> call = api.getEndpoints();

        call.enqueue(new Callback<Map<String, List<EndList>>>() {
            @Override
            public void onResponse(Call<Map<String, List<EndList>>> call, Response<Map<String, List<EndList>>> response) {
                if(response.isSuccessful()) {
                    populateEndpointList(response);
                    setRecyclerViewandAdapter();
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<EndList>>> call, Throwable t) {
                Log.e(getString(R.string.fail_message), t.getMessage(), t);

            }
        });
    }

    private void populateEndpointList(Response<Map<String, List<EndList>>> response) {
        for(EndList end : response.body().get("list")) {
            Endpoint newEnd = new Endpoint(end.getName(), end.getId());
            endpointList.add(newEnd);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setRecyclerViewandAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEndpoints);
        adapter = new EndpointAdapter(context, endpointList);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.mHelp) {
            launchHelp();
        } else if(id == R.id.mAbout) {
            showAbout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchHelp() {
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }

    private void showAbout() {
        Toast.makeText(this,
                getString(R.string.first_about) + " " +
                        getString(R.string.second_about),
                Toast.LENGTH_LONG).show();
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
