package com.example.recipielist.requests.responses;

import com.example.recipielist.models.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipieResponse {

    @SerializedName("recipe")
    @Expose()
    private Recipe recipe;

    public Recipe getRecipe(){
        return recipe;
    }

    @Override
    public String toString() {
        return "RecipieResponse{" +
                "recipe=" + recipe +
                '}';
    }
}
