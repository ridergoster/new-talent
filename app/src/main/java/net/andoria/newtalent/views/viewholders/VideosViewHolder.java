package net.andoria.newtalent.views.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.andoria.newtalent.R;
import net.andoria.newtalent.activities.MainActivity;
import net.andoria.newtalent.activities.BaseYouTubePlayerActivity;
import net.andoria.newtalent.models.Video;

/**
 * Created by maxime on 09/07/16.
 */
public class VideosViewHolder extends RecyclerView.ViewHolder{

    private final Context mContext;
    private final ImageView ivMusic;
    private final TextView tvTitle;
    private final TextView tvDescription;

    public VideosViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        ivMusic = (ImageView) itemView.findViewById(R.id.iv_music);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title_music);
        tvDescription = (TextView) itemView.findViewById(R.id.tv_description_music);
    }

    public void bindView(final Video video) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BaseYouTubePlayerActivity.class);
                intent.putExtra(BaseYouTubePlayerActivity.BASE_YOUTUBE_PLAYER_ACTIVITY_DATA_KEY, video);
                ((MainActivity) mContext).startActivity(intent);
            }
        });
        Picasso.with(mContext).load(video.getImageUrl()).into(ivMusic);
        tvTitle.setText(video.getName());
        tvDescription.setText(video.getDescription());
    }
}
