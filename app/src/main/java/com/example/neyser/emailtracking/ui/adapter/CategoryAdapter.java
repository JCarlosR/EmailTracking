package com.example.neyser.emailtracking.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<Category> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCategoryName, tvCategoryId;
        public ViewHolder(View view) {
            super(view);

            tvCategoryId = (TextView) view.findViewById(R.id.tvCategoryId);
            tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);
        }
    }

    public CategoryAdapter() {
        mDataSet = new ArrayList<>();
    }

    public void addCategories(ArrayList<Category> myDataSet) {
        mDataSet.addAll(myDataSet);
        notifyDataSetChanged();
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Category category = mDataSet.get(position);
        holder.tvCategoryId.setText("Categoría " + category.getId());
        holder.tvCategoryName.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}