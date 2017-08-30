package eu.rodrigocamara.amoviez.screens;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.amoviez.R;
import pojo.Movie;
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
}
