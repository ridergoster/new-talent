package net.andoria.newtalent.network.rate;

import android.support.annotation.NonNull;

import net.andoria.newtalent.models.Rate;
import net.andoria.newtalent.models.Video;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by maxime on 22/07/16.
 */
public interface RateService {

    @POST("/rates")
    Call<Video> postRate(@Body @NonNull Rate rate);

}
