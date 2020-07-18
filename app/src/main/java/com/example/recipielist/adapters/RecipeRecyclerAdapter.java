package com.example.recipielist.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipielist.R;
import com.example.recipielist.models.Recipe;
import com.example.recipielist.util.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipe> recipes;
    private OnRecipeListener onRecipeListener;
    private Context mContext;
    private static final int RECIPE_TYPE = 1;
    private static final int CATEGORY_TYPE = 2;
    private static final int LOADING_TYPE = 3;

    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener) {
        this.onRecipeListener = onRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = null;
        switch(viewType){
            case RECIPE_TYPE :{
                view = LayoutInflater.from(mContext).inflate(R.layout.recipe_list_item,parent,false);
                return new RecipeViewHolder(view,onRecipeListener);
            }
            case CATEGORY_TYPE: {
                view = LayoutInflater.from(mContext).inflate(R.layout.category_list_item_layout,parent,false);
                return new CategoryViewHolder(view,onRecipeListener);
            }
            case LOADING_TYPE : {
                view = LayoutInflater.from(mContext).inflate(R.layout.loading_item_layout,parent,false);
                return new LoadingViewHolder(view);
            }
            default: {
                view = LayoutInflater.from(mContext).inflate(R.layout.recipe_list_item,parent,false);
                return new RecipeViewHolder(view,onRecipeListener);
            }

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);

        if(itemViewType == RECIPE_TYPE){
            RecipeViewHolder recipeViewHolder = (RecipeViewHolder)holder;
            recipeViewHolder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.slide_down_anim));
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background);
            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipes.get(holder.getAdapterPosition()).getImage_url())
                    .into(recipeViewHolder.imageView);

            recipeViewHolder.title.setText(recipes.get(holder.getAdapterPosition()).getTitle());
            recipeViewHolder.publisher.setText(recipes.get(holder.getAdapterPosition()).getPublisher());
            recipeViewHolder.socialScore.setText(String.valueOf(Math.round(
                    recipes.get(holder.getAdapterPosition()).getSocial_rank()
            )));
        }
        else if(itemViewType == CATEGORY_TYPE){
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            categoryViewHolder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.slide_down_anim));

            Bitmap img = BitmapFactory.decodeResource(mContext.getResources(),Integer.parseInt(
                    recipes.get(holder.getAdapterPosition()).getImage_url()
            ));
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory
                    .create(mContext.getResources(),img);
            roundedBitmapDrawable.setCircular(true);

            categoryViewHolder.imageView.setImageDrawable(roundedBitmapDrawable);

            categoryViewHolder.title.setText(recipes.get(holder.getAdapterPosition()).getTitle());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(recipes == null)
            return CATEGORY_TYPE;
        if(recipes.get(position).getSocial_rank() == -1)
            return CATEGORY_TYPE;
        else if(recipes.get(recipes.size()-1).getTitle().equals("LOADING..."))
        {
            return LOADING_TYPE;
        }
        else if(position == recipes.size() -1 && position!=0 && !recipes.get(position).getTitle().equals("EXHAUSTED..."))
            return LOADING_TYPE;
        else return RECIPE_TYPE;
    }

    public void diplayCategoryType(){
        List<Recipe> categories = new ArrayList<>();
        for(int i =0;i< Constants.DEFAULT_SEARCH_CATEGORIES.length;i++){
            Recipe recipe = new Recipe();
            recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]+"");
            recipe.setSocial_rank(-1);
            categories.add(recipe);
        }
        recipes = categories;
        notifyDataSetChanged();;
    }
    public void displayLoading(){
        if(!isLoading()){
            Recipe recipe = new Recipe();
            recipe.setTitle("LOADING...");
            List<Recipe> loadingList = new ArrayList<>();
            loadingList.add(recipe);
            recipes = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading(){
        if(recipes.size() > 0){
            if(recipes.get(recipes.size() - 1).getTitle().equals("LOADING...")){
                return true;
            }
        }
        return false;
    }

    public void setRecipes(List<Recipe> recipes)
    {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return (recipes!=null)? recipes.size() : 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,publisher,socialScore;
        ImageView imageView;
        OnRecipeListener onRecipeListener;

        public RecipeViewHolder(@NonNull View itemView,OnRecipeListener onRecipeListener) {
            super(itemView);
            this.onRecipeListener = onRecipeListener;
            title = itemView.findViewById(R.id.recipe_title);
            publisher = itemView.findViewById(R.id.recipe_publisher);
            socialScore = itemView.findViewById(R.id.recipe_social_score);
            imageView = itemView.findViewById(R.id.recipe_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRecipeListener.onRecipeClick(getAdapterPosition());
        }
    }
}
