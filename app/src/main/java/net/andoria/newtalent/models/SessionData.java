package net.andoria.newtalent.models;

import android.content.Context;

import net.andoria.newtalent.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxime on 08/07/16.
 */
public class SessionData {
    private static final SessionData INSTANCE = new SessionData();

    private List<Video> videosComics = new ArrayList<>();
    private List<Video> videosArtists= new ArrayList<>();
    private List<Video> videosCrush = new ArrayList<>();
    private User currentUser;

    //Singleton
    private SessionData() {
        //noop
    }

    public static SessionData getInstance() {
        return INSTANCE;
    }

    public List<Video> getVideosCrush() {
        return videosCrush;
    }

    public void clear(Context context) {
        videosArtists.clear();
        videosComics.clear();
        videosCrush.clear();
        currentUser = null;

        PreferenceHelper.getInstance().initPref(context, null);
        PreferenceHelper.getInstance().setToken(null);
    }

    public void setVideosCrush(List<Video> videosCrush) {
        this.videosCrush = videosCrush;
    }

    public List<Video> getVideosComics() {
        return videosComics;
    }

    public void setVideosComics(List<Video> videosComics) {
        this.videosComics = videosComics;
    }

    public List<Video> getVideosArtists() {
        return videosArtists;
    }

    public void setVideosArtists(List<Video> videosArtists) {
        this.videosArtists = videosArtists;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void refreshVideo(Video video) {
        Video tmpVideoToRemove = null;
        for(Video cVideo : videosArtists) {
            if(cVideo.getId().equals(video.getId())) {
                tmpVideoToRemove = cVideo;
                break;
            }
        }
        if(tmpVideoToRemove != null) {
            videosArtists.remove(tmpVideoToRemove);
            videosArtists.add(video);
        } else {
            for(Video cVideo : videosComics) {
                if(cVideo.getId().equals(video.getId())) {
                    tmpVideoToRemove = cVideo;
                    break;
                }
            }

            if(tmpVideoToRemove != null) {
                videosComics.remove(tmpVideoToRemove);
                videosComics.add(video);
            }
        }
    }
}
