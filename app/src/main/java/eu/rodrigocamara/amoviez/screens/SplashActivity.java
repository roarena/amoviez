package eu.rodrigocamara.amoviez.screens;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.amoviez.R;
import network.NetworkUtils;
import pojo.Movie;
import pojo.Result;
import pojo.Result$$Parcelable;
import utils.Constants;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.tv_quotes)
    TextView tvQuotes;

    @BindView(R.id.iv_error)
    ImageView ivError;

    private Handler mQuoteHandler;
    private Runnable mQuoteRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        refreshMovieQuote();

        new BackGroundWorker().execute(NetworkUtils.buildUrl(Constants.REQUEST_POPULAR_SORT), NetworkUtils.buildUrl(Constants.REQUEST_TOP_RATED_SORT));
    }

    private void refreshMovieQuote() {
        setMovieQuote();
        mQuoteHandler = new Handler();
        mQuoteRunnable = new Runnable() {
            public void run() {
                refreshMovieQuote();
            }
        };
        mQuoteHandler.postDelayed(mQuoteRunnable, Constants.TIME_REFRESH_QUOTE);
    }

    private void setMovieQuote() {
        tvQuotes.setText(Html.fromHtml(getRandomMovieQuote()));
    }

    private String getRandomMovieQuote() {
        String linesFromFile;
        try {
            InputStream inputStreams = getAssets().open(Constants.MOVIE_QUOTES_FILE);
            int size = inputStreams.available();
            byte[] bufferReader = new byte[size];
            inputStreams.read(bufferReader);
            inputStreams.close();
            linesFromFile = new String(bufferReader, Constants.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        String[] quotes = linesFromFile.split(System.getProperty(Constants.LINE_SEPARATOR));
        return quotes[new Random().nextInt(quotes.length)];
    }

    class BackGroundWorker extends AsyncTask<URL, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(URL... urls) {
            ArrayList<String> httpResult = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                try {
                    httpResult.add(NetworkUtils.getResponseFromHttpUrl(urls[i]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return httpResult;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i) != Constants.HTTP_ERROR && result.get(i) != null) {
                        Gson gson = new Gson();

                        Result results = Result.create(gson.fromJson(result.get(i), Result.class).getResults());

                        intent.putExtra(Constants.PARCEL_NAME + i, new Result$$Parcelable(results));
                    } else {
                        ivError.setVisibility(View.VISIBLE);
                        tvQuotes.setText(R.string.error_request);
                        mQuoteHandler.removeCallbacks(mQuoteRunnable);
                    }
                }
                startActivity(intent);
            } else {
                ivError.setVisibility(View.VISIBLE);
                tvQuotes.setText(R.string.error_request);
                mQuoteHandler.removeCallbacks(mQuoteRunnable);
            }
        }
    }
}
