package com.example.mehdi.stage1;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mehdi.stage1.POJO.Movie;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mehdi on 03/02/2017.
 */

public class DetailMovie extends AppCompatActivity
{


    @BindView(R.id.movie_title) TextView movie_title;;
    @BindView(R.id.movie_date) TextView movie_date;
    @BindView(R.id.movie_score) TextView movie_score;
    @BindView(R.id.movie_synopsis) TextView movie_synopsis;
    @BindView(R.id.movie_thumbnail) ImageView movie_thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie);
        ButterKnife.bind(this);

        Movie movie = EventBus.getDefault().removeStickyEvent(Movie.class);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("MovieDetail");

        PopulateData(movie);

    }
    private void PopulateData(Movie movie)
    {
        Picasso.with(this).load(Constants.url_image_detail + movie.getBackdropPath()).into(movie_thumbnail);
        movie_title.setText(movie.getTitle());
        movie_date.setText(movie.getReleaseDate().substring(0,4));
        movie_score.setText(movie.getVoteAverage() + "/10");
        movie_synopsis.setText(movie.getOverview());
    }

}
