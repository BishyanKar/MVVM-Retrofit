package com.example.recipielist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.recipielist.adapters.OnRecipeListener;
import com.example.recipielist.adapters.RecipeRecyclerAdapter;
import com.example.recipielist.models.Recipe;
import com.example.recipielist.requests.RecipeAPi;
import com.example.recipielist.requests.ServiceGenerator;
import com.example.recipielist.requests.responses.RecipeSearchResponse;
import com.example.recipielist.util.Constants;
import com.example.recipielist.viewmodels.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "Main";
    private RecipeViewModel recipeViewModel;
    private RecyclerView recipeView;
    private RecipeRecyclerAdapter recipeRecyclerAdapter;
    private LottieAnimationView lottieAnimationView;
    private SearchView searchView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipeViewModel = new RecipeViewModel();
        recipeView = findViewById(R.id.recipe_view);
        lottieAnimationView = findViewById(R.id.animation_view);
        searchView = findViewById(R.id.search_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initRecyclerView();
        subscribeToRecipes();

        initSearchView();
        //testRetrofitRequest();
        if(!recipeViewModel.isFlag()){
            displaySearchCategories();
        }
    }

    private void initSearchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                searchRecipesApi(s,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void initRecyclerView()
    {
        recipeRecyclerAdapter = new RecipeRecyclerAdapter(this);
        recipeView.setAdapter(recipeRecyclerAdapter);
        recipeView.setLayoutManager(new LinearLayoutManager(this));

        recipeView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recipeView.canScrollVertically(1)){
                    //search newxt page
                    recipeViewModel.searchNext();
                }
            }
        });
    }

    private void subscribeToRecipes(){
        recipeViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes == null)
                    return;
                if(recipeViewModel.isFlag()) {
                    recipeRecyclerAdapter.setRecipes(recipes);
                    recipeViewModel.setPerformingQuery(false);
                }
            }
        });
    }

    private void displaySearchCategories(){
        recipeViewModel.setFlag(false);
        recipeRecyclerAdapter.diplayCategoryType();
    }

    private void searchRecipesApi(String query,int pageNumber){
        lottieAnimationView.setVisibility(View.VISIBLE);
        recipeViewModel.setBgTask(new BgTask(lottieAnimationView));
        recipeViewModel.searchRecipesApi(query,pageNumber);
    }

    private void testRetrofitRequest(){
        searchRecipesApi("chicken breast",1);
    }

    @Override
    public void onRecipeClick(int pos) {
        Intent intent = new Intent(this,RecipeActivity.class);
        intent.putExtra("recipe",recipeRecyclerAdapter.getSelectedRecipie(pos));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        searchView.clearFocus();
        searchRecipesApi(category,1);
    }

    @Override
    public void onBackPressed() {
        if(recipeViewModel.onBackPressed())
            super.onBackPressed();
        else displaySearchCategories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_categories){
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }
}