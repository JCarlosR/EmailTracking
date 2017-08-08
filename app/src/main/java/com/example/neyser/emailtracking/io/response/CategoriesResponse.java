package com.example.neyser.emailtracking.io.response;

import com.example.neyser.emailtracking.model.Category;

import java.util.ArrayList;
import java.util.Locale;

public class CategoriesResponse {

    private ArrayList<Category> categories;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
