package com.example.recipielist.util;

import com.example.recipielist.R;

public class Constants {
    public static final String BASE_URL = "https://recipesapi.herokuapp.com/";
    public static final String API_KEY = "";
    public static final int NETWORK_TIMEOUT = 10000;

    public static final String[] DEFAULT_SEARCH_CATEGORIES =
            {"Barbecue", "Breakfast", "Chicken", "Beef", "Brunch", "Dinner", "Wine", "Italian"};

    public static final Integer[] DEFAULT_SEARCH_CATEGORY_IMAGES =
            {
                    R.drawable.barbecue,
                    R.drawable.breakfast,
                    R.drawable.chicken,
                    R.drawable.beef,
                    R.drawable.brunch,
                    R.drawable.dinner,
                    R.drawable.wine,
                    R.drawable.italian
            };
}
