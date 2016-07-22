package net.andoria.newtalent.network.auth;

import android.support.annotation.NonNull;

import net.andoria.newtalent.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by maxime on 22/07/16.
 */
public interface AuthService {

    @POST("/auth/login")
    Call<User> authenticate(@Body @NonNull User user);
}
