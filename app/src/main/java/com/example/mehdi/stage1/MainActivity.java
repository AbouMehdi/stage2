package com.example.mehdi.stage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mehdi.stage1.Interface.TheMovieDB;
import com.example.mehdi.stage1.POJO.Movie;
import com.example.mehdi.stage1.POJO.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler{


    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;
    private ProgressBar pb;
    private TextView mErrorMessageDisplay;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.setTitle(Constants.title_toppopular);


        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
       // GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        GridLayoutManager layoutManager = new GridLayoutManager(this, calculateNoOfColumns(getBaseContext()), GridLayoutManager.VERTICAL, false);


        //new GridLayoutManager()
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        pb = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        // COMPLETED (11) Pass in 'this' as the ForecastAdapterOnClickHandler
        /*
         * The ForecastAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */

        pb.setVisibility(View.VISIBLE);
        mMoviesAdapter = new MoviesAdapter(this, this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMoviesAdapter);

        if (isOnline())
        {
            getmovies(1); // Popular By Default.
        }
        else
        {
            showNoConnectionMessage();
        }

    }

    public static int calculateNoOfColumns(Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    private void ShowDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showNoConnectionMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        pb.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setText(R.string.error_connection);
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private Boolean getmovies(int sort)
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        TheMovieDB service = retrofit.create(TheMovieDB.class);
        // if (mCurrentLocation != null)
        Call<Result> call = null;
        switch (sort)
        {
            case 1 :  call = service.popularmovies();
                break; // Popular movies
            case 2 : call = service.topratedmovies();
                break; // Top Rated
        }
        call.enqueue(new Callback <Result>()
        {
            @Override
            public void onResponse(Call <Result> call, Response<Result> response)
            {
                pb.setVisibility(View.INVISIBLE);
                ShowDataView();
                Log.d(Constants.debug, response.body().getResults().toString());
                mMoviesAdapter.setData(response.body().getResults());
                mRecyclerView.setAdapter(mMoviesAdapter);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t)
            {
                showErrorMessage();
            }
        });
        return true;

    }


    @Override
    public void onClick(Movie movie)
    {
        Intent i = new Intent(this, DetailMovie.class);
        startActivity(i);
        EventBus.getDefault().postSticky(movie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.mainmenu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id)
        {
            case R.id.menuSortPopularity :
                getmovies(1);
                actionBar.setTitle(Constants.title_toppopular);
                return true;

            case R.id.menuSortRating :
                getmovies(2);
                actionBar.setTitle(Constants.title_toprated);
                return true;


        }



        return super.onOptionsItemSelected(item);
    }
}
