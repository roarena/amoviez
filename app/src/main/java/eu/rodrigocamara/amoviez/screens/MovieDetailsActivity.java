package eu.rodrigocamara.amoviez.screens;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import adapter.ReviewAdapter;
import adapter.VideoAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import data.DatabaseUtils;
import data.FavoritesContract;
import eu.rodrigocamara.amoviez.R;
import network.NetworkUtils;
import pojo.Movie;
import pojo.ResultReviews;
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

    @BindView(R.id.tb_favorite)
    ToggleButton tbFavorite;

    @BindView(R.id.recycler_view_video)
    RecyclerView recyclerViewVideo;

    private VideoAdapter videoAdapter;

    @BindView(R.id.recycler_view_review)
    RecyclerView recyclerViewReview;

    private ReviewAdapter reviewAdapter;

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
            if (checkFavorite()) {
                tbFavorite.setChecked(true);
            }
            tbFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkFavorite()) {
                        removeFromFavorite();
                    }else{
                        addToFavorite();
                    }
                }
            });
            new BackGroundWorker().execute(NetworkUtils.buildUrl(Constants.REQUEST_VIDEOS_SORT, mMovie.getId()), NetworkUtils.buildUrl(Constants.REQUEST_REVIEWS_SORT, mMovie.getId()));
        }
    }

    private void addToFavorite() {
        ContentValues cv = new ContentValues();

        cv.put(FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID, mMovie.getId());
        cv.put(FavoritesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE, mMovie.getOriginal_title());
        cv.put(FavoritesContract.FavoriteMovies.COLUMN_POSTER_PATH, mMovie.getOriginalPosterPath());
        cv.put(FavoritesContract.FavoriteMovies.COLUMN_OVERVIEW, mMovie.getOverview());
        cv.put(FavoritesContract.FavoriteMovies.COLUMN_VOTE_AVERAGE, mMovie.getVote_average());
        cv.put(FavoritesContract.FavoriteMovies.COLUMN_RELEASE_DATE, mMovie.getOriginalReleaseDate());
        cv.put(FavoritesContract.FavoriteMovies.COLUMN_BACKDROP_PATH, mMovie.getOriginalBackdropPath());

        DatabaseUtils.getDB(getApplicationContext()).insert(FavoritesContract.FavoriteMovies.TABLE_NAME, null, cv);
    }

    private void removeFromFavorite() {
        DatabaseUtils.getDB(this).delete(FavoritesContract.FavoriteMovies.TABLE_NAME, FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID + "=" + mMovie.getId(), null);
    }

    private boolean checkFavorite() {
        Cursor cursor = DatabaseUtils.getDB(this).query(FavoritesContract.FavoriteMovies.TABLE_NAME,
                new String[]{FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID},
                FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID + "=" + mMovie.getId(),
                null, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
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
            if (result.size() > 0) {
                Gson gson = new Gson();
                RecyclerView.LayoutManager mLayoutManagerVideo = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                RecyclerView.LayoutManager mLayoutManagerReview = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                // Get videos
                ResultVideos resultVideos = ResultVideos.create(gson.fromJson(result.get(0), ResultVideos.class).getResults());

                if (resultVideos.getResults().size() > 0) {
                    videoAdapter = new VideoAdapter(resultVideos.getResults(), getApplicationContext());
                    recyclerViewVideo.setLayoutManager(mLayoutManagerVideo);
                    recyclerViewVideo.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewVideo.setAdapter(videoAdapter);
                }

                // Get Reviews
                ResultReviews resultReviews = ResultReviews.create(gson.fromJson(result.get(1), ResultReviews.class).getResults());
                if (resultReviews.getResults().size() > 0) {
                    reviewAdapter = new ReviewAdapter(resultReviews.getResults(), getApplicationContext());
                    recyclerViewReview.setLayoutManager(mLayoutManagerReview);
                    recyclerViewReview.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewReview.setAdapter(reviewAdapter);
                }
            }
        }
    }
}
