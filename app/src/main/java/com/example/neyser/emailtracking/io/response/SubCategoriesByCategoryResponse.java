package com.example.neyser.emailtracking.io.response;

import com.example.neyser.emailtracking.model.SubCategory;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubCategoriesByCategoryResponse {

    private ArrayList<SubCategory> subCategories;

    public ArrayList<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
