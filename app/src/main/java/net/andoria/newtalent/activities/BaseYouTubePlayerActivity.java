package net.andoria.newtalent.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import net.andoria.newtalent.R;
import net.andoria.newtalent.models.Rate;
import net.andoria.newtalent.models.SessionData;
import net.andoria.newtalent.models.Video;
import net.andoria.newtalent.network.APIService;
import net.andoria.newtalent.utils.PreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by maxime on 09/07/16.
 */
public class BaseYouTubePlayerActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    public static final String BASE_YOUTUBE_PLAYER_ACTIVITY_DATA_KEY = "BASE_YOUTUBE_PLAYER_ACTIVITY_DATA_KEY";

    private static final String YOUTUBE_KEY = "AIzaSyB4YJmwUdJQv_rdXu7OzQc0O_iFaPgZFVU";
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.rb_note)
    AppCompatRatingBar rbNote;
    @BindView(R.id.tv_toolbar_label)
    TextView tvToolbarLabel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_rate)
    Button btnRate;
    @BindView(R.id.pb_detail_video)
    RelativeLayout pbDetailVideo;

    private Video video;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_video);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            this.video = extras.getParcelable(BASE_YOUTUBE_PLAYER_ACTIVITY_DATA_KEY);
        }

        initView(false);
    }

    private void initView(boolean isRefresh) {
        if(!isRefresh) {
            initYouTubePlayer();
            initActionBar();
        }
        this.tvTitle.setText(video.getTitle());
        this.tvDescription.setText(video.getDescription());

        if (this.video.isHasVote()) {
            disabledRate();
        }
    }

    private void disabledRate() {
        this.btnRate.setVisibility(View.GONE);
        this.rbNote.setRating(this.video.getRate());
        this.rbNote.setEnabled(false);
    }

    @OnClick(R.id.btn_rate)
    public void onClick() {
        pbDetailVideo.setVisibility(View.VISIBLE);
        Rate rate = new Rate();
        rate.setIdVideo(this.video.getId());
        rate.setRate(rbNote.getRating());

        APIService.getInstance(getBaseContext()).postRate(rate, new APIService.APIResult<Video>() {
            @Override
            public void success(Video res) {
                disabledRate();
                video = res;
                initView(true);
                SessionData.getInstance().refreshVideo(video);
                pbDetailVideo.setVisibility(View.GONE);
            }

            @Override
            public void error(int code, String message) {
                Toast.makeText(getBaseContext(), "FAILED : " + message, Toast.LENGTH_LONG).show();
                pbDetailVideo.setVisibility(View.GONE);
            }
        });
    }

    private void initActionBar() {
        handleToolbarPadding(R.id.toolbar);
        this.setToolbarLabelText(this.video.getTitle());
        this.tvToolbarLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvToolbarLabel.setVisibility(View.GONE);
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        menu.findItem(R.id.action_favorite).setIcon(PreferenceHelper.getInstance().isFavourite(video) ? R.drawable.ic_favourite : R.drawable.ic_favourite_blank);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if (PreferenceHelper.getInstance().isFavourite(video)) {
                    PreferenceHelper.getInstance().removeFavouriteVideo(video);
                    item.setIcon(R.drawable.ic_favourite_blank);
                } else {
                    PreferenceHelper.getInstance().addFavouriteVideo(video);
                    item.setIcon(R.drawable.ic_favourite);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void initYouTubePlayer() {
        YouTubePlayerFragment youTubePlayerFragment =
                (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        youTubePlayerFragment.initialize(YOUTUBE_KEY, this);
    }

    private int getStatusBarHeight() {
        int result = 0;
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setToolbarLabelText(String title) {
        this.tvToolbarLabel.setText(title);
        this.tvToolbarLabel.setVisibility(View.VISIBLE);
    }

    void handleLayoutPadding(int layoutResId) {
        findViewById(layoutResId).setPadding(0, getStatusBarHeight(), 0, 0);
    }

    void handleToolbarPadding(int toolbarResId) {
        // Retrieve the AppCompat Toolbar
        Toolbar toolbar = (Toolbar) findViewById(toolbarResId);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
            ab.setDisplayShowHomeEnabled(false);
            ab.setDisplayShowTitleEnabled(false);
        }
        // Set the padding to match the Status Bar height
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(this.video.getIdYoutube());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(R.string.error_player), youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_KEY, this);
        }
    }

}
