package net.andoria.newtalent.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.andoria.newtalent.R;
import net.andoria.newtalent.models.SessionData;
import net.andoria.newtalent.models.Video;
import net.andoria.newtalent.network.APIService;
import net.andoria.newtalent.views.adapters.VideosAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maxime on 08/07/16.
 */
public class MusicFragment extends Fragment {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pb_load)
    RelativeLayout pbLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_video, container, false);
        ButterKnife.bind(this, rootView);
        loadArtistMonth();
        return rootView;
    }

    private void initView() {
        Picasso.with(getContext()).load("http://www.alternativenation.net/wp-content/uploads/2016/04/nirvana93.jpg")
                .into(image);

        VideosAdapter adapter = new VideosAdapter(SessionData.getInstance().getVideosArtists(), getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadArtistMonth() {
        pbLoad.setVisibility(View.VISIBLE);
        APIService.getInstance(getContext()).getArtistsMonth(new APIService.APIResult<List<Video>>() {
            @Override
            public void success(List<Video> res) {
                SessionData.getInstance().setVideosArtists(res);
                initView();
                pbLoad.setVisibility(View.GONE);
            }

            @Override
            public void error(int code, String message) {
                Toast.makeText(getContext(), "FAILED : " + message, Toast.LENGTH_LONG).show();
                pbLoad.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadArtistMonth();
    }
}
