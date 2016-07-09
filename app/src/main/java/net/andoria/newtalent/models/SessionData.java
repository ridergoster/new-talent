package net.andoria.newtalent.models;

import java.util.List;

/**
 * Created by maxime on 08/07/16.
 */
public class SessionData {
    private static final SessionData INSTANCE = new SessionData();

    private List<Video> videos;
    //Singleton
    private SessionData() {
        //noop
    }

    public static SessionData getInstance() {
        return INSTANCE;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
