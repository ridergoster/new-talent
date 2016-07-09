package net.andoria.newtalent.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.andoria.newtalent.R;
import net.andoria.newtalent.models.SessionData;
import net.andoria.newtalent.views.adapters.VideosAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maxime on 08/07/16.
 */
public class ComicFragment extends Fragment {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_video, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        Picasso.with(getContext()).load("http://www.quizz.biz/uploads/quizz/908405/3_8R7P4.jpg")
                .into(image);

        VideosAdapter adapter = new VideosAdapter(SessionData.getInstance().getVideos(), getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}