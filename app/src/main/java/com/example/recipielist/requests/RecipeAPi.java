package com.example.recipielist.requests;

import com.example.recipielist.requests.responses.RecipeSearchResponse;
import com.example.recipielist.requests.responses.RecipieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeAPi {

    //search returns a list
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );

    //retrive a particular recipe
    @GET("api/get")
    Call<RecipieResponse> getRecipe(
          @Query("key") String key,
           @Query("rid") String recipe_id
    );
}
