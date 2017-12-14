package hu.ait.android.globusapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import hu.ait.android.globusapp.network.GlobusAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransferActivity extends AppCompatActivity {

    private final int WAIT_LENGTH = 3000;
    private String filename;
    private String endpointID;
    private String endpointName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);
        getIntents();

        TextView tvTaskDescription = findViewById(R.id.tvTaskDescription);
        final LinearLayout transferLayout = findViewById(R.id.transferLayout);
        final TextView tvProgress = findViewById(R.id.tvProgress);
        tvTaskDescription.setText(formatDescription(endpointName, endpointID, filename));

        Retrofit retrofit = getRetrofit();
        final GlobusAPI api = retrofit.create(GlobusAPI.class);

        makeAPICall(transferLayout, tvProgress, api);
    }

    private void makeAPICall(final LinearLayout transferLayout, final TextView tvProgress, GlobusAPI api) {
        Call<String> call = api.getSubState(endpointID, filename);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    handleSuccess(response, tvProgress, transferLayout);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                handleFailure(t);
            }
        });
    }

    private void handleFailure(Throwable t) {
        Log.e(getString(R.string.fail_message), t.getMessage(), t);
        Toast.makeText(TransferActivity.this, getString(R.string.sorry),
                Toast.LENGTH_LONG).show();
        restartApp();
    }

    private void handleSuccess(Response<String> response, TextView tvProgress, LinearLayout transferLayout) {
        setProgressTexts(response, tvProgress, transferLayout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                restartApp();
            }
        }, WAIT_LENGTH);
    }

    private void restartApp() {
        Intent restartIntent = new Intent(TransferActivity.this,
                EndpointActivity.class);
        TransferActivity.this.startActivity(restartIntent);
        TransferActivity.this.finish();
    }

    private void setProgressTexts(Response<String> response, TextView tvProgress, LinearLayout transferLayout) {
        tvProgress.setText(response.body());
        tvProgress.setTextColor(Color.BLACK);
        transferLayout.setBackgroundColor(Color.GREEN);
    }

    private void getIntents() {
        endpointName = getIntent().getStringExtra(EndpointActivity.NAME);
        endpointID = getIntent().getStringExtra(EndpointActivity.ID);
        filename = getIntent().getStringExtra(FileActivity.FILE);
    }

    public String formatDescription(String endpointName, String endpointID, String filename) {
        String output = getString(R.string.transferring) + " ";
        output = output + filename + " ";
        output = output + getString(R.string.to) + " ";
        output = output + endpointName + " (" + endpointID + ").";
        return output;
    }

    @NonNull
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
