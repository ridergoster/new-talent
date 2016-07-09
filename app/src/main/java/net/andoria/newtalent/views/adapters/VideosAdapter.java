package net.andoria.newtalent.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.andoria.newtalent.R;
import net.andoria.newtalent.models.Video;
import net.andoria.newtalent.views.viewholders.VideosViewHolder;

import java.util.List;

/**
 * Created by maxime on 09/07/16.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosViewHolder> {

    private List<Video> videos;
    private Context mContext;

    public VideosAdapter(List<Video> videos, Context context) {
        this.videos = videos;
        this.mContext = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideosViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_rl_video_items, parent, false), mContext);
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {
        holder.bindView(videos.get(position));

    }
}