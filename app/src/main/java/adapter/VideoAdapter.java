package adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.amoviez.R;
import pojo.MovieVideos;

/**
 * Created by Ro on 07/10/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private List<MovieVideos> videosList;
    private Context context;

    public VideoAdapter(List<MovieVideos> videosList, Context context) {
        this.videosList = videosList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MovieVideos videos = videosList.get(position);
        holder.tvVideoName.setText(videos.getName());
        holder.cardVideo.setOnClickListener(clickListener(videos));
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    private View.OnClickListener clickListener(final MovieVideos video) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_videoName)
        TextView tvVideoName;

        @BindView(R.id.card_view_video)
        CardView cardVideo;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
