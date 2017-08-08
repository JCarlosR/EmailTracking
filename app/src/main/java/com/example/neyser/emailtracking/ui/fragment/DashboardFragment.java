package com.example.neyser.emailtracking.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.io.MyApiAdapter;
import com.example.neyser.emailtracking.io.response.QuantityResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private TextView tvViewed, tvSent, tvClicked, tvClients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


        tvViewed = (TextView) view.findViewById(R.id.txtVistos);
        tvSent = (TextView) view.findViewById(R.id.txtEnviados);
        tvClicked = (TextView) view.findViewById(R.id.txtClick);
        tvClients = (TextView) view.findViewById(R.id.txtCliente);

        fetchViewedEmails();
        fetchSentEmails();
        fetchClickedEmails();
        fetchClientsCount();
        return view;
    }

    private void fetchViewedEmails() {
        Call<QuantityResponse> call = MyApiAdapter.getApiService().getViewedEmails();
        call.enqueue(new Callback<QuantityResponse>() {
            @Override
            public void onResponse(Call<QuantityResponse> call, Response<QuantityResponse> response) {
                if (response.isSuccessful()) {
                    QuantityResponse quantityResponse = response.body();
                    if (quantityResponse!=null && quantityResponse.isSuccess()) {
                        tvViewed.setText(String.valueOf(quantityResponse.getQuantity()));
                    }
                }
            }

            @Override
            public void onFailure(Call<QuantityResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSentEmails() {
        Call<QuantityResponse> call = MyApiAdapter.getApiService().getSentEmails();
        call.enqueue(new Callback<QuantityResponse>() {
            @Override
            public void onResponse(Call<QuantityResponse> call, Response<QuantityResponse> response) {
                if (response.isSuccessful()) {
                    QuantityResponse quantityResponse = response.body();
                    if (quantityResponse!=null && quantityResponse.isSuccess()) {
                        tvSent.setText(String.valueOf(quantityResponse.getQuantity()));
                    }
                }
            }

            @Override
            public void onFailure(Call<QuantityResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchClickedEmails() {
        Call<QuantityResponse> call = MyApiAdapter.getApiService().getClickedEmails();
        call.enqueue(new Callback<QuantityResponse>() {
            @Override
            public void onResponse(Call<QuantityResponse> call, Response<QuantityResponse> response) {
                if (response.isSuccessful()) {
                    QuantityResponse quantityResponse = response.body();
                    if (quantityResponse!=null && quantityResponse.isSuccess()) {
                        tvClicked.setText(String.valueOf(quantityResponse.getQuantity()));
                    }
                }
            }

            @Override
            public void onFailure(Call<QuantityResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchClientsCount() {
        Call<QuantityResponse> call = MyApiAdapter.getApiService().getClientsCount();
        call.enqueue(new Callback<QuantityResponse>() {
            @Override
            public void onResponse(Call<QuantityResponse> call, Response<QuantityResponse> response) {
                if (response.isSuccessful()) {
                    QuantityResponse quantityResponse = response.body();
                    if (quantityResponse!=null && quantityResponse.isSuccess()) {
                        tvClients.setText(String.valueOf(quantityResponse.getQuantity()));
                    }
                }
            }

            @Override
            public void onFailure(Call<QuantityResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
