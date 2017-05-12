package com.example.adeogo.moviestowatch;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FullReview extends AppCompatActivity {

    private TextView mLinkTextview;
    private TextView mReviewTextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_review);
        Intent intent = getIntent();
        String reviewLink = intent.getStringExtra("link_review");
        String reviewText = intent.getStringExtra("text_review");

        mLinkTextview = (TextView) findViewById(R.id.link_review);
        mReviewTextview = (TextView) findViewById(R.id.text_review);

        mLinkTextview.setText(reviewLink);

        mReviewTextview.setText(reviewText);

    }
}

