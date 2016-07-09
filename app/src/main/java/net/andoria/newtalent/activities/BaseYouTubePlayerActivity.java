package net.andoria.newtalent.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import net.andoria.newtalent.R;
import net.andoria.newtalent.models.Video;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maxime on 09/07/16.
 */
public class BaseYouTubePlayerActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    public static final String BASE_YOUTUBE_PLAYER_ACTIVITY_DATA_KEY = "BASE_YOUTUBE_PLAYER_ACTIVITY_DATA_KEY";

    private static final String YOUTUBE_KEY = "AIzaSyB4YJmwUdJQv_rdXu7OzQc0O_iFaPgZFVU";
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private Video video;

    @BindView(R.id.tv_toolbar_label)
    TextView tvToolbarLabel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_video);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            this.video = extras.getParcelable(BASE_YOUTUBE_PLAYER_ACTIVITY_DATA_KEY);
        }

        handleToolbarPadding(R.id.toolbar);
        initYouTubePlayer();
        this.setToolbarLabelText(this.video.getName());
        this.tvToolbarLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvToolbarLabel.setVisibility(View.GONE);
                onBackPressed();
            }
        });
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
            Uri uri=Uri.parse(this.video.getVideoUrl());
            String cueVideo = uri.getQueryParameter("v");
            youTubePlayer.cueVideo(cueVideo);
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
