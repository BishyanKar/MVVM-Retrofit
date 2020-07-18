package com.example.recipielist.viewmodels;

import android.app.Application;

import com.airbnb.lottie.LottieAnimationView;
import com.example.recipielist.BgTask;
import com.example.recipielist.models.Recipe;
import com.example.recipielist.repos.RecipeRepo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class RecipeViewModel extends ViewModel {

    private RecipeRepo mRecipeRepository;
    private boolean flag; //true when it view recipes else false
    private BgTask bgTask;
    private boolean isPerformingQuery;

    public void setBgTask(BgTask bgTask) {
        this.bgTask = bgTask;
    }

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepo.getInstance();
        flag = false;
        isPerformingQuery = false;
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mRecipeRepository.isQueryExhausted();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query,int pageNumber){
        flag = true;
        isPerformingQuery = true;
        mRecipeRepository.setBgTask(bgTask);
        mRecipeRepository.searchRecipesApi(query,pageNumber);
    }

    public boolean onBackPressed(){
        if(isPerformingQuery){
            mRecipeRepository.cancelRequest();
            isPerformingQuery = false;
        }
        if(flag){
            flag = false;
            return false;
        }
        return true;
    }

    public void searchNext(){
        if(!isPerformingQuery && flag && !isQueryExhausted().getValue()){
            mRecipeRepository.searchNext();
        }
    }

    public boolean isPerformingQuery() {
        return isPerformingQuery;
    }

    public void setPerformingQuery(boolean performingQuery) {
        isPerformingQuery = performingQuery;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
