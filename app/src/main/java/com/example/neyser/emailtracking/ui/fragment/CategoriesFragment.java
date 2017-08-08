package com.example.neyser.emailtracking.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.io.MyApiAdapter;
import com.example.neyser.emailtracking.io.response.CategoriesResponse;
import com.example.neyser.emailtracking.ui.adapter.CategoryAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment implements View.OnClickListener {

    private CategoryAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        setupRecyclerView(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchCategories();
    }

    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewCategories);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new CategoryAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fetchCategories() {
        Call<CategoriesResponse> call = MyApiAdapter.getApiService().getCategories();
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful()) {
                    CategoriesResponse categoriesResponse = response.body();
                    if (categoriesResponse != null)
                        mAdapter.addCategories(categoriesResponse.getCategories());
                } else {
                    Toast.makeText(getContext(), R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showAddCategory();
                break;
        }
    }

    private void showAddCategory() {
        Toast.makeText(getContext(), "Add category", Toast.LENGTH_SHORT).show();
    }
}
