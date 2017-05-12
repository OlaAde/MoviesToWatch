package com.example.adeogo.moviestowatch.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeogo.moviestowatch.R;
import com.example.adeogo.moviestowatch.models.Trailer;
import com.example.adeogo.moviestowatch.utilities.NetworkUtils;

import java.util.List;

/**
 * Created by Adeogo on 4/29/2017.
 */

public class TrailerAdapter extends  RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>{

    private final TrailerAdapterOnclickHandler mClickHandler;
    Context context;
    private List<Trailer> mTrailerList = null;

    public interface TrailerAdapterOnclickHandler{
        void clickTrailer(Uri uri);
    }

    public TrailerAdapter(TrailerAdapterOnclickHandler onclickHandler){
        mClickHandler = onclickHandler;
    }





    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mPlayImage;
        public final TextView mTrailerTv;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            mPlayImage = (ImageView) itemView.findViewById(R.id.play_image);
            mTrailerTv = (TextView) itemView.findViewById(R.id.trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int itemPosition = getAdapterPosition();
            Trailer trailer = mTrailerList.get(itemPosition);
            String youtube1 = trailer.getmVideoUrlPart();

            mClickHandler.clickTrailer(NetworkUtils.buildYoutubeUrl(youtube1));
        }
    }


    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context =  parent.getContext();
        int layoutForListItem = R.layout.detail_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForListItem, parent, false);
        return new TrailerAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        Trailer trailer = mTrailerList.get(position);
        String trailerName = trailer.getmTrailerName();
        holder.mTrailerTv.setText(trailerName);
    }

    @Override
    public int getItemCount() {
        if(null == mTrailerList){
            return 0;
        }
        return mTrailerList.size();
    }

    public void setMovieData(List<Trailer> trailerList){
        mTrailerList = trailerList;
        notifyDataSetChanged();
    }
}
