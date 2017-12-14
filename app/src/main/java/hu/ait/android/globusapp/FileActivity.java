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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import hu.ait.android.globusapp.adapter.FileListAdapter;
import hu.ait.android.globusapp.data.File;
import hu.ait.android.globusapp.data.FileObject;
import hu.ait.android.globusapp.network.GlobusAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FileActivity extends AppCompatActivity {

    private FileListAdapter adapter;
    private List<FileObject> fileList;
    private String intentID;
    private String intentName;
    public static String FILE = "FILE";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);


        setStartingVariables();
        Retrofit retrofit = getRetrofit();
        final GlobusAPI api = retrofit.create(GlobusAPI.class);

        makeAPICall(api);


        setupToolbar();
    }

    private void makeAPICall(GlobusAPI api) {
        Call<Map<String, List<File>>> call = api.getFiles();

        call.enqueue(new Callback<Map<String, List<File>>>() {
            @Override
            public void onResponse(Call<Map<String, List<File>>> call, Response<Map<String,
                    List<File>>> response) {
                if(response.isSuccessful()) {
                    populateFileList(response);
                    setupRecyclerViewAndAdapter(context, fileList, new LinearLayoutManager(context), adapter);
                }
            }

            @Override
            public void onFailure(Call<Map<String, List<File>>> call, Throwable t) {
                Log.e(getString(R.string.fail_message), t.getMessage(), t);
            }
        });
    }

    private void setupRecyclerViewAndAdapter(Context context, List<FileObject> fileList, LinearLayoutManager layout, FileListAdapter adapter) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFiles);
        adapter = new FileListAdapter(context, fileList);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarFiles);
        setSupportActionBar(toolbar);
    }

    private void populateFileList(Response<Map<String, List<File>>> response) {
        for(File list : response.body().get("files")) {
            FileObject newFile = new FileObject(list.getName(), list.getSize());
            fileList.add(newFile);
        }
    }

    private void setStartingVariables() {
        intentName = getIntent().getStringExtra(EndpointActivity.NAME);
        intentID = getIntent().getStringExtra(EndpointActivity.ID);
        context = this;
        fileList = new ArrayList<>();
    }

    public void commitFile(int position, String filename) {
        Intent transferIntent = new Intent(this, TransferActivity.class);
        transferIntent.putExtra(EndpointActivity.NAME, intentName);
        transferIntent.putExtra(EndpointActivity.ID, intentID);
        transferIntent.putExtra(FILE, filename);
        startActivity(transferIntent);
    }

    @NonNull
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
