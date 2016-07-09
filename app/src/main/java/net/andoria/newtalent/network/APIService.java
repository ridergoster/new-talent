package net.andoria.newtalent.network;

import android.content.Context;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import net.andoria.newtalent.models.Video;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by maxime on 08/07/16.
 */
public class APIService {
    private static final String API_PATH = "https://raw.githubusercontent.com/";
    public static final int CONNECT_TIMEOUT = 15;
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 60;
    public static final int HTTP_200 = 200;
    //Singleton
    private static final APIService INSTANCE = new APIService();
    private static Context mContext;
    private Retrofit retrofit;
    private CategoriesService categoriesService;

    private APIService() {
        this.initRetrofitClient();
    }

    public void getVideos(final APIResult<List<Video>> callback) {
        Call<List<Video>> call = this.categoriesService.getVideos();
        call.enqueue(new GenericCallback<List<Video>>(mContext, HTTP_200, "/video", callback));
    }

    public static APIService getInstance(Context context) {
        mContext = context;
        return INSTANCE;
    }

    private void initRetrofitClient() {
        if(this.retrofit != null) {
            this.retrofit = null;
        }

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(API_PATH)
                .addConverterFactory(LoganSquareConverterFactory.create());

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);

        this.retrofit = retrofitBuilder.build();

        this.categoriesService = this.retrofit.create(CategoriesService.class);
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

}

