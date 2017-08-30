package eu.rodrigocamara.amoviez.screens;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;
import java.util.Random;

import adapter.MovieAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.amoviez.R;
import pojo.Movie;
import pojo.Result;
import utils.Constants;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.iv_main_backdrop)
    ImageView mBackdrop;

    @BindView(R.id.fab_menu)
    FloatingActionMenu mFabMenu;

    @BindView(R.id.fab_toogle)
    FloatingActionButton mFabOption;

    @BindView(R.id.fab_settings)
    FloatingActionButton mFabSettings;

    @BindView(R.id.tv_main_title)
    TextView mtvBarTitle;

    @BindView(R.id.tv_main_subtitle)
    TextView mtvBarSubtitle;

    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovieList;

    private String mQueryFor = Constants.REQUEST_POPULAR_SORT;
    private String mBackdropPath = "";

    private Result queryPopularResult;
    private Result queryTopRatedResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        queryPopularResult = Parcels.unwrap(getIntent().getParcelableExtra(Constants.PARCEL_POPULAR));
        queryTopRatedResult = Parcels.unwrap(getIntent().getParcelableExtra(Constants.PARCEL_TOP_RATED));
        if (queryPopularResult != null && queryTopRatedResult != null) {
            //We always open the app in the Most Popular Filter (Maybe change this later to shared pref?)
            loadMovies(queryPopularResult);

            setSupportActionBar(mToolbar);
            initCollapsingToolbar();


            initFAB();
        } else {
            Toast.makeText(this, R.string.error_request, Toast.LENGTH_LONG).show();
        }
    }

    private void loadMovies(Result result) {
        mMovieList = result.getResults();
        mMovieAdapter = new MovieAdapter(this, mMovieList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mMovieAdapter);
    }

    private void refreshToolbarContent(int title, int subtitle) {
        mtvBarTitle.setText(getString(title));
        mtvBarSubtitle.setText(getString(subtitle));
    }

    private void toggleResultsFilter() {
        if (mQueryFor == Constants.REQUEST_POPULAR_SORT) {
            mQueryFor = Constants.REQUEST_TOP_RATED_SORT;
            loadMovies(queryTopRatedResult);
            refreshToolbarContent(R.string.main_rated_title, R.string.main_rated_subtitle);
            mFabOption.setLabelText(getString(R.string.fab_popular));
        } else {
            mQueryFor = Constants.REQUEST_POPULAR_SORT;
            loadMovies(queryPopularResult);
            refreshToolbarContent(R.string.main_popular_title, R.string.main_popular_subtitle);
            mFabOption.setLabelText(getString(R.string.fab_rated));
        }
    }

    private void initCollapsingToolbar() {
        mAppBarLayout.setExpanded(true);

        mCollapsingToolbar.setTitle(" ");

        //To avoid user data waste, we save this in case user change screen orientation.
        if (mBackdropPath == "") {
            mBackdropPath = mMovieList.get(new Random().nextInt(mMovieList.size())).getBackdrop_path().toString();
        }

        Picasso.with(this).load(mBackdropPath).into(mBackdrop);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mCollapsingToolbar.setTitle(getString(R.string.app_name) + " - " + getString(R.string.splash_subtitle));
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mQueryFor", mQueryFor);
        outState.putString("mBackdropPath", mBackdropPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mQueryFor = savedInstanceState.getString("mQueryFor");
        mBackdropPath = savedInstanceState.getString("mBackdropPath");
    }

    public void initFAB() {
        mFabSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mFabMenu.isOpened()) {
                    mFabMenu.close(true);
                }
            }
        });

        mFabOption.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mFabMenu.isOpened()) {
                    mFabMenu.close(true);
                }
                toggleResultsFilter();
            }
        });
    }
}