package eu.rodrigocamara.amoviez.screens;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import adapter.VideoAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.amoviez.R;
import network.NetworkUtils;
import pojo.Movie;
import pojo.Result;
import pojo.Result$$Parcelable;
import pojo.ResultVideos;
import utils.Constants;

public class MovieDetailsActivity extends AppCompatActivity {
    private Movie mMovie;

    @BindView(R.id.iv_details_backdrop)
    ImageView mivBackDrop;
    @BindView(R.id.details_toolbar)
    Toolbar mDetailsToolbar;

    @BindView(R.id.details_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.details_appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.tv_details_movieTitle)
    TextView tvMovieTitle;
    @BindView(R.id.details_movie_overview)
    TextView tvMovieOverview;
    @BindView(R.id.tv_details_release)
    TextView tvMovieRelease;
    @BindView(R.id.iv_details_poster)
    ImageView ivMoviePoster;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.recycler_view_video)
    RecyclerView recyclerView;

    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        mMovie = Parcels.unwrap(getIntent().getParcelableExtra(Constants.PARCEL_MOVIE));
        if (mMovie != null) {
            Picasso.with(this).load(mMovie.getBackdrop_path().toString()).into(mivBackDrop);
            setSupportActionBar(mDetailsToolbar);
            initCollapsingToolbar();
            tvMovieTitle.setText(mMovie.getOriginal_title());
            tvMovieOverview.setText(mMovie.getOverview());
            tvMovieRelease.setText(mMovie.getRelease_date());
            ratingBar.setRating(mMovie.getVote_average() / 2);
            Picasso.with(this).load(mMovie.getPoster_path().toString()).into(ivMoviePoster);
            new BackGroundWorker().execute(NetworkUtils.buildUrl(Constants.REQUEST_VIDEOS_SORT,mMovie.getId()));
        }
    }

    private void initCollapsingToolbar() {
        mAppBarLayout.setExpanded(true);

        mCollapsingToolbar.setTitle(" ");

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mCollapsingToolbar.setTitle(mMovie.getOriginal_title());
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    class BackGroundWorker extends AsyncTask<URL, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(URL... urls) {
            ArrayList<String> httpResult = new ArrayList<>();
            try {
                httpResult.add(NetworkUtils.getResponseFromHttpUrl(urls[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpResult;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    Gson gson = new Gson();

                    ResultVideos results = ResultVideos.create(gson.fromJson(result.get(i), ResultVideos.class).getResults());
                    Log.i("RODRIGO",results.getResults().get(0).getKey());

                    videoAdapter = new VideoAdapter(results.getResults(),getApplicationContext());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(videoAdapter);
                }
            }
        }
    }
}
