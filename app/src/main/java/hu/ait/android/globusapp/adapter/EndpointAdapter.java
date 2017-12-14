package hu.ait.android.globusapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import java.util.List;
import hu.ait.android.globusapp.EndpointActivity;
import hu.ait.android.globusapp.R;
import hu.ait.android.globusapp.data.Endpoint;


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
        setAnimation(holder.itemView, position);
        holder.tvEndName.setText(currEnd.getName());
        holder.tvEndID.setText(currEnd.getId());
        setOnEndpointSelect(holder, currEnd);
    }

    private void setOnEndpointSelect(final ViewHolder holder, final Endpoint currEnd) {
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
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }
}
