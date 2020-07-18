package com.example.recipielist.viewmodels;

import com.example.recipielist.models.Recipe;
import com.example.recipielist.repos.RecipeRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SingleRecipeViewModel extends ViewModel {
    private RecipeRepo recipeRepo;
    private String recipeId;
    private boolean retrieveRecipe;

    public SingleRecipeViewModel() {
        this.recipeRepo = RecipeRepo.getInstance();
        retrieveRecipe = false;
    }
    public LiveData<Recipe> getRecipe(){
        return recipeRepo.getRecipe();
    }
    public MutableLiveData<Boolean> isRecipeRequestTimeout() {
        return recipeRepo.isRecipeRequestTimeout();
    }
    public void searchRecipeById(String recipeId){
        this.recipeId = recipeId;
        recipeRepo.searchRecipeById(recipeId);
    }

    public String getRecipeId() {
        return recipeId;
    }

    public boolean isRetrieveRecipe() {
        return retrieveRecipe;
    }

    public void setRetrieveRecipe(boolean retrieveRecipe) {
        this.retrieveRecipe = retrieveRecipe;
    }
}
