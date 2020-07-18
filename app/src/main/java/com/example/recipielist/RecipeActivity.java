package com.example.recipielist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipielist.models.Recipe;
import com.example.recipielist.viewmodels.SingleRecipeViewModel;

import org.w3c.dom.Text;

public class RecipeActivity extends AppCompatActivity {

    private static final String TAG = "RECIPE_ACTIVITY";
    private ImageView recipeImage;
    private TextView recipeTitile,recipeRank;
    private LinearLayout recipeIngContainer;
    private ScrollView scrollView;

    private ProgressBar progressBar;

    private SingleRecipeViewModel singleRecipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipeImage = findViewById(R.id.recipe_image);
        recipeTitile = findViewById(R.id.recipe_title);
        recipeRank = findViewById(R.id.recipe_social_score);
        recipeIngContainer = findViewById(R.id.ingredients_container);
        scrollView = findViewById(R.id.parent);

        progressBar = findViewById(R.id.progress_bar_recipe);

        singleRecipeViewModel = new SingleRecipeViewModel();

        showPorgressBar(true);
        subscribeObserver();
        getIncomingIntent();
    }

    public void showPorgressBar(boolean vis)
    {
        progressBar.setVisibility(vis? View.VISIBLE : View.GONE);
    }

    private void displayErrorScreen(String err){
        recipeTitile.setText("Error retrieving recipe...");
        recipeRank.setText("");
        TextView textView = new TextView(this);
        if(!err.equals("")){
            textView.setText(err);
        }
        else textView.setText("Error");
        textView.setTextSize(15);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        recipeIngContainer.addView(textView);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.ic_launcher_background)
                .into(recipeImage);
        showPorgressBar(false);
        scrollView.setVisibility(View.VISIBLE);
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("recipe")){
            Recipe recipe = getIntent().getParcelableExtra("recipe");
           // Log.d(TAG, "getIncomingIntent: "+recipe.getTitle());
            singleRecipeViewModel.searchRecipeById(recipe.getRecipe_id());
        }
    }

    private void subscribeObserver(){
        singleRecipeViewModel.getRecipe().observe(this, recipe -> {
            if(recipe!=null){
                if(recipe.getRecipe_id().equals(singleRecipeViewModel.getRecipeId())) {
                    setRecipeProp(recipe);
                    singleRecipeViewModel.setRetrieveRecipe(true);
                }
            }
        });

        singleRecipeViewModel.isRecipeRequestTimeout().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean && !singleRecipeViewModel.isRetrieveRecipe()){
                    //Log.d(TAG, "onChanged: Timed out...");
                    displayErrorScreen("Error, Please check your internet connection.");
                }
            }
        });
    }
    private void setRecipeProp(Recipe recipe){
        if(recipe!=null){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.getImage_url())
                    .into(recipeImage);

            Log.d(TAG, "setRecipeProp: "+recipe.getTitle());
            recipeTitile.setText(recipe.getTitle());
            recipeRank.setText(String.valueOf(recipe.getSocial_rank()));

            recipeIngContainer.removeAllViews();
            for(String ingredient: recipe.getIngredients()){
                TextView textView = new TextView(this);
                textView.setText(ingredient);
                textView.setTextSize(15);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                recipeIngContainer.addView(textView);

                showPorgressBar(false);
                scrollView.setVisibility(View.VISIBLE);
            }
        }
    }
}