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
import pojo.Review;

/**
 * Created by Ro on 07/10/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private List<Review> reviewList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_reviewer)
        TextView tvReviewer;

        @BindView(R.id.tv_review)
        TextView tvReview;

        @BindView(R.id.tv_read_review)
        TextView tvReadReview;

        @BindView(R.id.card_view_video)
        CardView reviewCard;

        public MyViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ReviewAdapter(List<Review> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.MyViewHolder holder, int position) {
        Review reviews = reviewList.get(position);

        holder.tvReviewer.setText(context.getResources().getString(R.string.review_by) + reviews.getAuthor());
        holder.tvReview.setText(reviews.getContent());
        holder.tvReadReview.setOnClickListener(clickListener(reviews));
    }
    private View.OnClickListener clickListener(final Review review) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
            }
        };
    }
    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
