package com.example.recipielist.viewmodels;

import com.example.recipielist.models.Recipe;
import com.example.recipielist.repos.RecipeRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class SingleRecipeViewModel extends ViewModel {
    private RecipeRepo recipeRepo;
    private String recipeId;

    public SingleRecipeViewModel() {
        this.recipeRepo = RecipeRepo.getInstance();
    }
    public LiveData<Recipe> getRecipe(){
        return recipeRepo.getRecipe();
    }
    public void searchRecipeById(String recipeId){
        this.recipeId = recipeId;
        recipeRepo.searchRecipeById(recipeId);
    }

    public String getRecipeId() {
        return recipeId;
    }
}
