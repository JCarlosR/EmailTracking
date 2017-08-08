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
import com.example.neyser.emailtracking.io.response.ClientsResponse;
import com.example.neyser.emailtracking.ui.activity.ClientFormActivity;
import com.example.neyser.emailtracking.ui.adapter.ClientAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ClientsFragment extends Fragment implements View.OnClickListener {


    private ClientAdapter mAdapter;
    private RecyclerView mRecyclerView;


    public ClientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_clients, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        setupRecyclerView(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchClients();
    }

    private void setupRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewClients);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new ClientAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fetchClients() {
        Call<ClientsResponse> call = MyApiAdapter.getApiService().getClients();
        call.enqueue(new Callback<ClientsResponse>() {
            @Override
            public void onResponse(Call<ClientsResponse> call, Response<ClientsResponse> response) {
                if (response.isSuccessful()) {
                    ClientsResponse clientsResponse = response.body();
                    if (clientsResponse != null)
                        mAdapter.setClients(clientsResponse.getClients());
                } else {
                    Toast.makeText(getContext(), R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClientsResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showAddClient();
                break;
        }
    }

    private static final int REGISTER_CLIENT_REQUEST = 1;

    private void showAddClient() {
        Intent intent = new Intent(getContext(), ClientFormActivity.class);
        // intent.putExtra("client", new Gson().toJson());
        startActivityForResult(intent, REGISTER_CLIENT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTER_CLIENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // The client was registered
                fetchClients();
            }
        }
    }
}
