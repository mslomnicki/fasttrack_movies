package net.slomnicki.udacity.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkUtils {
    public static final String TAG = NetworkUtils.class.getSimpleName();
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.d(TAG, "isOnline: cm" + connectivityManager);

        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
