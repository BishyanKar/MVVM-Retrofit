package com.example.recipielist.adapters;

import android.app.ActivityOptions;
import android.util.Pair;
import android.view.View;

public interface OnRecipeListener {
    void onRecipeClick(int pos, Pair<View,String>[] pairs);
    void onCategoryClick(String category);
}
