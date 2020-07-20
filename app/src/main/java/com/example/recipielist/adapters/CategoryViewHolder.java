package com.example.recipielist.adapters;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipielist.R;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

public class CategoryViewHolder extends RecyclerView.ViewHolder{
    OnRecipeListener onRecipeListener;
    ImageView imageView;
    TextView title;
    CompositeDisposable compositeDisposable;
    public CategoryViewHolder(@NonNull View itemView,OnRecipeListener onRecipeListener)
    {
        super(itemView);
        this.onRecipeListener = onRecipeListener;
        imageView = itemView.findViewById(R.id.category_image);
        title = itemView.findViewById(R.id.category_title);

        compositeDisposable = new CompositeDisposable();

        //itemView.setOnClickListener(this);
        preventSpamAndListen(itemView);
    }

    public void preventSpamAndListen(View view) {
        RxView.clicks(view)
                .throttleFirst(5000, TimeUnit.MILLISECONDS)  //thw window duration is set such that it allows the network request to complete
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        Log.d("ADAPTER", "onNext: itemView click handled : " + System.currentTimeMillis());
                        onRecipeListener.onCategoryClick(title.getText().toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
        }
}
