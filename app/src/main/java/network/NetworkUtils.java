package network;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import eu.rodrigocamara.amoviez.BuildConfig;
import utils.Constants;

/**
 * Created by rodrigo.camara on 25/08/2017.
 */

public class NetworkUtils {

    public static URL buildUrl(String sortParameter) {
        Uri builtUri = Uri.parse(Constants.MOVIEDB_BASE_URL).buildUpon().
                appendPath(Constants.API_VERSION).
                appendPath(Constants.QUERY_TYPE).
                appendPath(sortParameter).
                appendQueryParameter(Constants.PARAM_API, BuildConfig.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(Constants.CONNECTION_TIMEOU);
        try {
            if (isConnectionOK(urlConnection.getResponseCode())) {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } else {
                return Constants.HTTP_ERROR;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static boolean isConnectionOK(int responseCode) {
        if (responseCode >= 200 && responseCode <= 299) {
            return true;
        } else {
            return false;
        }
    }
}
