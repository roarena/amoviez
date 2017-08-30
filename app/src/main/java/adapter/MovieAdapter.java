package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.amoviez.R;
import eu.rodrigocamara.amoviez.screens.MovieDetailsActivity;
import pojo.Movie;
import pojo.Movie$$Parcelable;
import utils.Constants;

/**
 * Created by rodrigo.camara on 28/08/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private Context mContext;
    private List<Movie> movieList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_card_moviePoster)
        ImageView ivMovieThumbnail;

        @BindView(R.id.tv_card_movieTitle)
        TextView tvMovieTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.tvMovieTitle.setText(movie.getOriginal_title());

        Picasso.with(mContext).load(movie.getPoster_path().toString()).into(holder.ivMovieThumbnail);
        holder.tvMovieTitle.setOnClickListener(clickListener(movie));
        holder.ivMovieThumbnail.setOnClickListener(clickListener(movie));
    }

    private View.OnClickListener clickListener(final Movie movie) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                intent.putExtra(Constants.PARCEL_MOVIE, new Movie$$Parcelable(movie));
                mContext.startActivity(intent);
            }
        };
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
