package net.andoria.newtalent.network.video;

import net.andoria.newtalent.models.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by maxime on 22/07/16.
 */
public interface VideoService {

    @GET("/videos/showComicsMonth")
    Call<List<Video>> getComicsMonth();

    @GET("/videos/showArtistMonth")
    Call<List<Video>> getArtistMonth();

    @GET("/videos/showCrush")
    Call<List<Video>> getCrushMonth();
}
