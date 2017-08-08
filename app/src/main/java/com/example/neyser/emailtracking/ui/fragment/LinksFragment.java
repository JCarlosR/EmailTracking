package com.example.neyser.emailtracking.ui.fragment;


import android.content.Intent;
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
import com.example.neyser.emailtracking.io.response.LinksResponse;
import com.example.neyser.emailtracking.ui.activity.LinkFormActivity;
import com.example.neyser.emailtracking.ui.adapter.LinkAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class LinksFragment extends Fragment implements View.OnClickListener {

    private LinkAdapter mAdapter;
    private RecyclerView mRecyclerView;


    public LinksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_links, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        setupRecyclerView(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchLinks();
    }

    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewLinks);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new LinkAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fetchLinks() {
        Call<LinksResponse> call = MyApiAdapter.getApiService().getLinks();
        call.enqueue(new Callback<LinksResponse>() {
            @Override
            public void onResponse(Call<LinksResponse> call, Response<LinksResponse> response) {
                if (response.isSuccessful()) {
                    LinksResponse categoriesResponse = response.body();
                    if (categoriesResponse != null)
                        mAdapter.setLinks(categoriesResponse.getLinks());
                } else {
                    Toast.makeText(getContext(), R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LinksResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showAddLink();
                break;
        }
    }

    private static final int REGISTER_LINK_REQUEST = 1;

    private void showAddLink() {
        // Toast.makeText(getContext(), "Add link", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(), LinkFormActivity.class);
        startActivityForResult(intent, REGISTER_LINK_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTER_LINK_REQUEST) {
            if (resultCode == RESULT_OK) {
                // The link was successful registered
                fetchLinks();
            }
        }
    }
}
