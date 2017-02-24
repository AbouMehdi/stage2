package com.example.mehdi.stage1.Interface;

import com.example.mehdi.stage1.POJO.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mehdi on 03/02/2017.
 */

public interface TheMovieDB
{
    @GET("movie/popular?api_key=74a96a7b828fd2c163ac285b5d56184c")
    Call<Result> popularmovies();


    @GET("movie/top_rated?api_key=74a96a7b828fd2c163ac285b5d56184c")
    Call<Result> topratedmovies();

}
