package com.example.mehdi.stage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mehdi.stage1.POJO.Movie;
import com.example.mehdi.stage1.POJO.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Mehdi on 03/02/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private List<Movie> mMoviesList;
    private Context context;
    private final MoviesAdapterOnClickHandler mClickHandler;

    public MoviesAdapter(MoviesAdapterOnClickHandler onClickHandler, Context c)
    {
        context = c;
        mClickHandler = onClickHandler;
    }


    public interface MoviesAdapterOnClickHandler
    {
        void onClick(Movie movie);
    }



    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public final ImageView mImageView;

        public MoviesAdapterViewHolder(View view)
        {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.movie_image);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view)
        {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMoviesList.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //context = parent.getContext();
        int layoutIdForListItem = R.layout.movies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesAdapterViewHolder holder, int position)
    {
        //String weatherForThisDay = mWeatherData[position];
        //forecastAdapterViewHolder.mWeatherTextView.setText(weatherForThisDay);

        String url_image = Constants.url_image + mMoviesList.get(position).getBackdropPath();
        Log.d(Constants.debug, url_image);
        Picasso.with(context)
                .load(Constants.url_image + mMoviesList.get(position).getBackdropPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.errorplace)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount()
    {
        if (null == mMoviesList) return 0;

        return mMoviesList.size();
    }

    public void setData(List<Movie> movies)
    {
        mMoviesList = movies;
       // Log.d(Constants.debug, mMoviesList.size() + "");
        notifyDataSetChanged();
    }
}