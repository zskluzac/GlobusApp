package hu.ait.android.globusapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hu.ait.android.globusapp.EndpointActivity;
import hu.ait.android.globusapp.FileActivity;
import hu.ait.android.globusapp.R;
import hu.ait.android.globusapp.data.EndList;
import hu.ait.android.globusapp.data.Endpoint;
import hu.ait.android.globusapp.network.GlobusAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Zachary on 12/11/2017.
 */

public class EndpointAdapter extends RecyclerView.Adapter<EndpointAdapter.ViewHolder> {

    private int lastPosition = -1;
    private List<Endpoint> endpointList;
    private Context context;

    public EndpointAdapter(Context context, List<Endpoint> endpointList) {
        this.context = context;
        this.endpointList = endpointList;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEndName;
        private TextView tvEndID;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEndName = itemView.findViewById(R.id.tvEndName);
            tvEndID = itemView.findViewById(R.id.tvEndID);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_endpoint, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Endpoint currEnd = endpointList.get(position);
        holder.tvEndName.setText("Temp Text");
        Log.d("HEY! ", "onBindViewHolder: called");
        setAnimation(holder.itemView, position);
        holder.tvEndName.setText(currEnd.getName());
        holder.tvEndID.setText(currEnd.getId());
        holder.tvEndName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EndpointActivity)context).openFileActivity(holder.getAdapterPosition(),
                        currEnd.getName(),
                        currEnd.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return endpointList.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }
}
