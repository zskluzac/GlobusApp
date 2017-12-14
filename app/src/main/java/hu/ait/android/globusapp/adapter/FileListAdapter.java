package hu.ait.android.globusapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import java.util.List;
import hu.ait.android.globusapp.FileActivity;
import hu.ait.android.globusapp.R;
import hu.ait.android.globusapp.data.FileObject;

/**
 * Created by Zachary on 12/12/2017.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private int lastPosition = -1;
    private List<FileObject> fileList;
    private Context context;

    public FileListAdapter(Context context, List<FileObject> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvFileName;
        private TextView tvFileSize;
        private CardView fileCard;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            tvFileSize = itemView.findViewById(R.id.tvFileSize);
            fileCard = itemView.findViewById(R.id.fileCard);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_file, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FileObject currFile = fileList.get(position);
        setAnimation(holder.itemView, position);
        holder.tvFileName.setText(currFile.getName());
        String fileSizeFormatted = formatFileSize(currFile);
        holder.tvFileSize.setText(fileSizeFormatted);
        setCardSelected(holder, currFile);
    }

    private void setCardSelected(final ViewHolder holder, final FileObject currFile) {
        holder.fileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FileActivity)context).commitFile(holder.getAdapterPosition(),
                        currFile.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

    }

    private String formatFileSize(FileObject file) {
        Long size = Long.valueOf(file.getSize());
        if(size <= 1000 && size >= 1) {
            return String.valueOf(size) + " " + "B";
        } else if (size <= 1000*1000 && size >=1) {
            return String.valueOf(size/1000) + " " + "KB";
        } else if(size <= 1000*1000*1000 && size >=1) {
            return String.valueOf(size /(1000*1000)) + " " + "MB";
        } else if(file.getSize().length() > 9 && size >=1) {
            return String.valueOf((size/(1000))/(1000*1000)) + " " + "GB";
        } else if(size == 0) {
            return "Folder";
        } else {
            return "File size too large to show";
        }
    }
}
