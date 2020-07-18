package com.example.recipielist.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipielist.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    OnRecipeListener onRecipeListener;
    ImageView imageView;
    TextView title;
    public CategoryViewHolder(@NonNull View itemView,OnRecipeListener onRecipeListener)
    {
        super(itemView);
        this.onRecipeListener = onRecipeListener;
        imageView = itemView.findViewById(R.id.category_image);
        title = itemView.findViewById(R.id.category_title);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onRecipeListener.onCategoryClick(title.getText().toString());
    }
}
