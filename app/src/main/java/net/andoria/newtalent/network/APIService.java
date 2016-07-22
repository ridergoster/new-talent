package net.andoria.newtalent.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import net.andoria.newtalent.models.Rate;
import net.andoria.newtalent.models.SessionData;
import net.andoria.newtalent.models.User;
import net.andoria.newtalent.models.Video;
import net.andoria.newtalent.network.auth.AuthService;
import net.andoria.newtalent.network.rate.RateService;
import net.andoria.newtalent.network.user.UserService;
import net.andoria.newtalent.network.video.VideoService;
import net.andoria.newtalent.utils.PreferenceHelper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by maxime on 08/07/16.
 */
public class APIService {
    //    private static final String API_PATH = "https://raw.githubusercontent.com/";
    private static final String API_PATH = "http://176.132.230.172:21203/";
//    private static final String API_PATH = "http://192.168.1.22:3006/";
    private static final String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final int CONNECT_TIMEOUT = 15;
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 60;
    public static final int HTTP_200 = 200;
    //Singleton
    private static final APIService INSTANCE = new APIService();
    private static Context mContext;
    private Retrofit retrofit;
    private CategoriesService categoriesService;
    private UserService userService;
    private AuthService authService;
    private VideoService videoService;
    private RateService rateService;

    private APIService() {
        this.initRetrofitClient();
    }

    public void getVideos(final APIResult<List<Video>> callback) {
        Call<List<Video>> call = this.categoriesService.getVideos();
        call.enqueue(new GenericCallback<List<Video>>(mContext, HTTP_200, "/video", callback));
    }

    public void getComicsMonth(final APIResult<List<Video>> callback) {
        Call<List<Video>> call = this.videoService.getComicsMonth();
        call.enqueue(new GenericCallback<List<Video>>(mContext, HTTP_200, "/videos/showComicsMonth", callback));
    }

    public void getCrushMonth(final APIResult<List<Video>> callback) {
        Call<List<Video>> call = this.videoService.getCrushMonth();
        call.enqueue(new GenericCallback<List<Video>>(mContext, HTTP_200, "/videos/showCrush", callback));
    }

    public void getArtistsMonth(final APIResult<List<Video>> callback) {
        Call<List<Video>> call = this.videoService.getArtistMonth();
        call.enqueue(new GenericCallback<List<Video>>(mContext, HTTP_200, "/videos/showArtistMonth", callback));
    }

    public void postRate(Rate rate, final APIResult<Video> callback) {
        Call<Video> call = this.rateService.postRate(rate);
        call.enqueue(new GenericCallback<Video>(mContext, HTTP_200, "/videos/showArtistMonth", callback));
    }

    public void subscribe(User user, final APIResult<User> callback) {
        Call<User> call = this.userService.subscribe(user);
        call.enqueue(new GenericCallback<User>(mContext, HTTP_200, "/videos/showArtistMonth", callback));
    }

    public void authentication(User user, final APIResult<User> callback) {
        if (verifyConnection()) {
            Call<User> call = this.authService.authenticate(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                    int statusCode = response.code();
                    if (statusCode == HTTP_200) {
                        String authToken = response.headers().get(AUTH_TOKEN_HEADER);
                        if (authToken == null) {
                            callback.error(-1, "No X-AUTH-TOKEN header in response.");
                        } else {
                            Log.d(getClass().getSimpleName(), "token : " + authToken);
                            PreferenceHelper.getInstance().setToken(authToken);
                            initRetrofitClient();
                            User user = response.body();
                            callback.success(user);
                        }
                    } else {
                        callback.error(statusCode, response.message());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(mContext, "Verifier votre connexion", Toast.LENGTH_LONG).show();
        }
    }

    public static APIService getInstance(Context context) {
        mContext = context;
        return INSTANCE;
    }


    public boolean verifyConnection() {
        if (mContext == null) {
            return false;
        } else {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
    }

    private void initRetrofitClient() {
        instancePref(true);
        if (this.retrofit != null) {
            this.retrofit = null;
        }

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(API_PATH)
                .addConverterFactory(LoganSquareConverterFactory.create());

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        String authToken = PreferenceHelper.getInstance().getToken();
        if (authToken != null) {
            httpClientBuilder.addInterceptor(new AuthTokenHeaderInterceptor());
        }
        httpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);

        retrofitBuilder.client(httpClientBuilder.build());

        this.retrofit = retrofitBuilder.build();

        this.categoriesService = this.retrofit.create(CategoriesService.class);
        this.userService = this.retrofit.create(UserService.class);
        this.videoService = this.retrofit.create(VideoService.class);
        this.rateService = this.retrofit.create(RateService.class);
        this.authService = this.retrofit.create(AuthService.class);

        instancePref(false);
    }

    /**
     * Generic API result
     *
     * @param <V>
     */
    public interface APIResult<V> {
        void success(V res);

        void error(int code, String message);
    }

    static class AuthTokenHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .method(original.method(), original.body());
            // Request customization: add request headers
            instancePref(true);
            String token = PreferenceHelper.getInstance().getToken();
            if (token != null) {
                requestBuilder.header(AUTH_TOKEN_HEADER, token);
            }
            instancePref(false);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    }

    private static void instancePref(boolean isGlobal) {
        if(mContext != null) {
            SessionData sessionData = SessionData.getInstance();
            if(!isGlobal && sessionData.getCurrentUser() != null) {
                PreferenceHelper.getInstance().initPref(mContext, sessionData.getCurrentUser().getId());
            } else {
                PreferenceHelper.getInstance().initPref(mContext, null);
            }
        }
    }
}

