package com.example.recipielist.repos;

import com.airbnb.lottie.LottieAnimationView;
import com.example.recipielist.BgTask;
import com.example.recipielist.models.Recipe;
import com.example.recipielist.requests.RecipeApiClient;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

//connecting sql db and/or online api
public class RecipeRepo {

    private static RecipeRepo instance;
    private RecipeApiClient recipeApiClient;
    private BgTask bgTask;
    private String query;
    private int pageNumber;
    private MutableLiveData<Boolean> isQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();

    public static RecipeRepo getInstance(){
        if(instance == null){
            instance = new RecipeRepo();
        }
        return instance;
    }

    private RecipeRepo(){
        recipeApiClient = RecipeApiClient.getInstance();
        initMeidators();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }
    public LiveData<Recipe> getRecipe(){
        return recipeApiClient.getbRecipe();
    }
    public MutableLiveData<Boolean> isRecipeRequestTimeout() {
        return recipeApiClient.isRecipeRequestTimeout();
    }

    public void setBgTask(BgTask bgTask) {
        this.bgTask = bgTask;
    }

    public void searchRecipeById(String recipeId){
        recipeApiClient.getRecipeById(recipeId);
    }

    private void initMeidators(){
        LiveData<List<Recipe>> recieListApiSource = recipeApiClient.getRecipes();
        mRecipes.addSource(recieListApiSource, recipes -> {
            if(recipes!=null){
                mRecipes.setValue(recipes);
                doneQuery(recipes);
            }
            else{
                doneQuery(null);
            }
        });
    }

    public LiveData<Boolean> isQueryExhausted(){
        return isQueryExhausted;
    }

    private void doneQuery(List<Recipe> list){
        if(list!=null){
            if(list.size()%30!=0){
                isQueryExhausted.setValue(true);
            }
        }
        else {
            isQueryExhausted.setValue(true);
        }
    }

    public void searchRecipesApi(String query, int pageNumber){
        if(pageNumber<1)
            pageNumber = 1;
        this.query = query;
        this.pageNumber = pageNumber;
        isQueryExhausted.setValue(false);
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
