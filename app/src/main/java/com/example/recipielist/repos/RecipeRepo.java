package com.example.recipielist.repos;

import com.airbnb.lottie.LottieAnimationView;
import com.example.recipielist.BgTask;
import com.example.recipielist.models.Recipe;
import com.example.recipielist.requests.RecipeApiClient;

import java.util.List;

import androidx.lifecycle.LiveData;

//connecting sql db and/or online api
public class RecipeRepo {

    private static RecipeRepo instance;
    private RecipeApiClient recipeApiClient;
    private BgTask bgTask;
    private String query;
    private int pageNumber;

    public static RecipeRepo getInstance(){
        if(instance == null){
            instance = new RecipeRepo();
        }
        return instance;
    }

    private RecipeRepo(){
        recipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeApiClient.getRecipes();
    }

    public void setBgTask(BgTask bgTask) {
        this.bgTask = bgTask;
    }

    public void searchRecipesApi(String query, int pageNumber){
        if(pageNumber<1)
            pageNumber = 1;
        this.query = query;
        this.pageNumber = pageNumber;
        recipeApiClient.setBgTask(bgTask);
        recipeApiClient.searchRecipeApi(query,pageNumber);
    }
    public void searchNext(){
        searchRecipesApi(query,pageNumber+1);
    }
    public void cancelRequest(){
        recipeApiClient.cancelReq();
    }
}
