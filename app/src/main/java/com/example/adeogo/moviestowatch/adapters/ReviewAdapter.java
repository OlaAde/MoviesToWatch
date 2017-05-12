package com.example.adeogo.moviestowatch.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeogo.moviestowatch.R;
import com.example.adeogo.moviestowatch.models.Review;
import com.example.adeogo.moviestowatch.models.Trailer;
import com.example.adeogo.moviestowatch.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by Adeogo on 4/30/2017.
 */

public class ReviewAdapter extends  RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{

    private final ReviewAdapterOnclickHandler mClickHandler;
    Context context;
    private List<Review> mReviewList = null;

    public interface ReviewAdapterOnclickHandler {
        void clickReview(String uriString, String reviewText);
    }

    public ReviewAdapter(ReviewAdapterOnclickHandler onclickHandler){
        mClickHandler = onclickHandler;
    }





    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mAuthor;
        public final TextView mReviewText;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mAuthor = (TextView) itemView.findViewById(R.id.review_author);
            mReviewText = (TextView) itemView.findViewById(R.id.review_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int itemPosition = getAdapterPosition();
            Review review = mReviewList.get(itemPosition);
            String reviewLink = review.getmReviewLink();
            String fullReview = review.getmReviewText();
            mClickHandler.clickReview(reviewLink,fullReview);
        }
    }


    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context =  parent.getContext();
        int layoutForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForListItem, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        Review review = mReviewList.get(position);
        String authorName = review.getmAuthorName();
        holder.mAuthor.setText(authorName);

        String reviewText = review.getmReviewText();
        holder.mReviewText.setText(reviewText);
        holder.mReviewText.setEllipsize(TextUtils.TruncateAt.END);
        holder.mReviewText.setMaxLines(5);
    }

    @Override
    public int getItemCount() {
        if(null == mReviewList){
            return 0;
        }
        return mReviewList.size();
    }

    public void setMovieData(List<Review> reviewList){
        mReviewList = reviewList;
        notifyDataSetChanged();
    }
}
