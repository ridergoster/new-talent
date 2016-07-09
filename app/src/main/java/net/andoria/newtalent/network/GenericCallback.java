package net.andoria.newtalent.network;

import android.content.Context;
import android.util.Log;

import net.andoria.newtalent.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by maxime on 08/07/16.
 */
public class GenericCallback<V> implements Callback<V> {
    private static final String TAG_STATUS = " status: ";

    private final APIService.APIResult<V> callback;
    private final Context mContext;
    private final int statusCodeOK;
    private final String methodName;

    public GenericCallback(Context context, int statusCodeOk, String methodName, APIService.APIResult<V> callback) {
        this.mContext = context;
        this.statusCodeOK = statusCodeOk;
        this.methodName = methodName;
        this.callback = callback;
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Log.e(getClass().getSimpleName(), "Error while calling the '" + methodName + "' method! " + t.getLocalizedMessage(), t);
        callback.error(-1, t.getMessage());
    }

    @Override
    public void onResponse(Call<V> call, Response<V> response) {
        int statusCode = response.code();
        V resp = response.body();
        if(resp == null) {
            Log.e(getClass().getSimpleName(), "Empty response body while calling the '" + methodName + "' method!");
            callback.error(statusCode, mContext.getResources().getString(R.string.cb_empty_response_error));
        } else {
            Log.d(getClass().getSimpleName(), methodName + TAG_STATUS + statusCode);
            if(statusCode == statusCodeOK) {
                Log.d(getClass().getSimpleName(), "Successful call for the '" + methodName + "' method!");
                callback.success(resp);
            }
        }
    }
}
