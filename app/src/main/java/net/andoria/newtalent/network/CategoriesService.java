package net.andoria.newtalent.network;

import net.andoria.newtalent.models.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by maxime on 08/07/16.
 */
public interface CategoriesService {

    @GET("florent37/MyYoutube/master/myyoutube.json")
    Call<List<Video>> getVideos();
}
