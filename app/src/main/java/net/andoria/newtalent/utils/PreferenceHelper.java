package net.andoria.newtalent.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;

import net.andoria.newtalent.models.Video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by maxime on 08/07/16.
 */
public class PreferenceHelper {

    private static final String FAVOURITE_PREF_KEY = "favouritePrefKey";
    private static final String TOKEN_PREF_KEY = "tokenPrefKey";

    private static final PreferenceHelper INSTANCE = new PreferenceHelper();
    private SharedPreferences sharedPreferences;

    public PreferenceHelper() {
    }

    public void initPref(Context context, String idUser) {
        idUser = idUser == null ? "" : idUser;
        this.sharedPreferences = context.getSharedPreferences(context.getPackageName() + idUser, Context.MODE_PRIVATE);
    }

    public static PreferenceHelper getInstance() {
        return INSTANCE;
    }

    public void setToken(String token) {
        sharedPreferences.edit().putString(TOKEN_PREF_KEY, token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_PREF_KEY, null);
    }
    public List<Video> getFavouriteVideos() {
        Set<String> videoSet = getFavouriteVideoSet();
        List<Video> videos = new ArrayList<>();
        if (!videoSet.isEmpty()) {
            try {
                for (String jsonVideo : videoSet) {
                    Video video = LoganSquare.parse(jsonVideo, Video.class);
                    videos.add(video);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return videos;
    }


    private Set<String> getFavouriteVideoSet() {
        return new HashSet<>(sharedPreferences.getStringSet(FAVOURITE_PREF_KEY, new HashSet<String>()));
    }

    public boolean isFavourite (Video video) {
        for(Video videoFavourite : getFavouriteVideos()) {
            if(videoFavourite.getId().equals(video.getId())) {
                return true;
            }
        }
        return false;
    }
    public void addFavouriteVideo(Video video) {
        try {
            Set<String> videoSet = getFavouriteVideoSet();
            String jsonVideo = LoganSquare.serialize(video);
            videoSet.add(jsonVideo);
            sharedPreferences.edit().putStringSet(FAVOURITE_PREF_KEY, videoSet).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean removeFavouriteVideo(Video video) {
        boolean success = false;
        try {
            Set<String> videoSet = getFavouriteVideoSet();
            Video videoFound = null;
            for (String jsonVideo : videoSet) {
                Video videoFavourite = LoganSquare.parse(jsonVideo, Video.class);
                if(video.getId().equals(videoFavourite.getId())) {
                    videoFound = videoFavourite;
                    break;
                }
            }
            if(videoFound != null) {
                success = videoSet.remove(LoganSquare.serialize(videoFound));
                sharedPreferences.edit().putStringSet(FAVOURITE_PREF_KEY, videoSet).apply();
            }
        } catch (IOException e) {
            Log.e(PreferenceHelper.class.getSimpleName(), e.getLocalizedMessage(), e);
        }
        return success;
    }



}
