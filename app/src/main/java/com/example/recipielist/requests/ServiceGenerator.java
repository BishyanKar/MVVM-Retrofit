package com.example.recipielist.requests;

import com.example.recipielist.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();
    private static RecipeAPi recipeAPi = retrofit.create(RecipeAPi.class);

    public static RecipeAPi getRecipeAPi()
    {
        return recipeAPi;
    }
}
