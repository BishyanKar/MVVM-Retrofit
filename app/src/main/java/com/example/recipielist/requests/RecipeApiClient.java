package com.example.recipielist.requests;

import android.util.Log;

import com.example.recipielist.AppExecutors;
import com.example.recipielist.BgTask;
import com.example.recipielist.models.Recipe;
import com.example.recipielist.requests.responses.RecipeSearchResponse;
import com.example.recipielist.requests.responses.RecipieResponse;
import com.example.recipielist.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

//for api interactions
public class RecipeApiClient {
    private static final String TAG = "RAC";
    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private MutableLiveData<Recipe> bRecipe;
    private MutableLiveData<Boolean> bRecipeRequestTimeout;
    private GetRecipeRunnable getRecipeRunnable;
    private RetrieveRecipeRunnable retrieveRecipeRunnable;
    public static final int DECODE_STATE_COMPLETED = 123;
    private BgTask bgTask;
    //private static BgTask bgTask = new BgTask();

    public static RecipeApiClient getInstance(){
        if(instance == null){
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
        bRecipe = new MutableLiveData<>();
        bRecipeRequestTimeout = new MutableLiveData<>();
    }

    public void setBgTask(BgTask bgTask) {
        this.bgTask = bgTask;
    }

    public MutableLiveData<Recipe> getbRecipe() {
        return bRecipe;
    }
    public void getRecipeById(String resipeId)
    {
        if(retrieveRecipeRunnable!=null){
            retrieveRecipeRunnable = null;
        }
        retrieveRecipeRunnable = new RetrieveRecipeRunnable(resipeId);
        final Future handler = AppExecutors.getInstance().getNetworkIO().submit(retrieveRecipeRunnable);

        bRecipeRequestTimeout.setValue(false);
        AppExecutors.getInstance().getNetworkIO().schedule(() -> {
            bRecipeRequestTimeout.postValue(true);
            handler.cancel(true);
        },Constants.NETWORK_TIMEOUT,TimeUnit.MILLISECONDS);
    }

    public MutableLiveData<Boolean> isRecipeRequestTimeout() {
        return bRecipeRequestTimeout;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    public void searchRecipeApi(String query,int pageNumber){
        if(getRecipeRunnable != null){
            getRecipeRunnable = null;
        }
        getRecipeRunnable = new GetRecipeRunnable(query,pageNumber);
        final Future handler = AppExecutors.getInstance().getNetworkIO().submit(getRecipeRunnable);
        AppExecutors.getInstance().getNetworkIO().schedule(() -> {
            //network timed out
            handler.cancel(true);
        }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveRecipeRunnable implements Runnable{
        private String recipeId;
        boolean cancelRequest;

        public RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipe(recipeId).execute();
                if (cancelRequest)
                    return;
                if(response.code() == 200){
                    Recipe recipe = ((RecipieResponse)response.body()).getRecipe();
                    bRecipe.postValue(recipe);
                   // Log.d(TAG, "run: 200OK"+recipe.getTitle());
                }
                else {
                    Log.d(TAG, "run: "+ response.errorBody().toString());
                    bRecipe.postValue(null);
                }
                //animationView.setVisibility(View.INVISIBLE);
                //bgTask.handleDecodeState(DECODE_STATE_COMPLETED);
            }
            catch (Exception e){
                e.printStackTrace();
                bRecipe.postValue(null);
                //animationView.setVisibility(View.INVISIBLE);
                //bgTask.handleDecodeState(DECODE_STATE_COMPLETED);
            }
        }
        private Call<RecipieResponse> getRecipe(String recipeId){
            return ServiceGenerator.getRecipeAPi().getRecipe(Constants.API_KEY,
                    recipeId);
        }
        private void cancelReq()
        {
            Log.d(TAG, "cancelReq: cancelling search req");
            cancelRequest  = true;
        }
    }

    private class GetRecipeRunnable implements Runnable{
        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public GetRecipeRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNumber).execute();
                if (cancelRequest)
                    return;
                if(response.code() == 200){
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                    if(pageNumber == 1){
                        mRecipes.postValue(list); //we use post value to live data when we send data from background thread else setValue() is used
                    }
                    else {
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }
                }
                else {
                    Log.d(TAG, "run: "+ response.errorBody().toString());
                    mRecipes.postValue(null);
                }
                //animationView.setVisibility(View.INVISIBLE);
                bgTask.handleDecodeState(DECODE_STATE_COMPLETED);
            }
            catch (Exception e){
                e.printStackTrace();
                mRecipes.postValue(null);
                //animationView.setVisibility(View.INVISIBLE);
                bgTask.handleDecodeState(DECODE_STATE_COMPLETED);
            }
        }
        private Call<RecipeSearchResponse> getRecipes(String query , int pageNumber){
            return ServiceGenerator.getRecipeAPi().searchRecipe(Constants.API_KEY,
                    query,String.valueOf(pageNumber));
        }
        private void cancelReq()
        {
            Log.d(TAG, "cancelReq: cancelling search req");
            cancelRequest  = true;
        }
    }
    public void cancelReq(){
        if(getRecipeRunnable!=null)
        {
            Log.d(TAG, "Retrofit request cancelled");
            getRecipeRunnable.cancelReq();
            bgTask.handleDecodeState(DECODE_STATE_COMPLETED);
        }
        if(retrieveRecipeRunnable!=null)
        {
            Log.d(TAG, "Retrofit request cancelled");
            retrieveRecipeRunnable.cancelReq();
            bgTask.handleDecodeState(DECODE_STATE_COMPLETED);
        }
    }
}
