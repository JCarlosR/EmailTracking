package com.example.neyser.emailtracking.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.io.MyApiAdapter;
import com.example.neyser.emailtracking.io.response.OpenedLinksResponse;
import com.example.neyser.emailtracking.model.Email;
import com.example.neyser.emailtracking.model.OpenedLink;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenedLinksFragment extends Fragment implements View.OnClickListener {

    private TextView tvResults;
    private EditText etQuery;
    private Button btnFilter;

    private ArrayList<OpenedLink> openedLinks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_opened_links, container, false);

        btnFilter = (Button) view.findViewById(R.id.btnFilter);
        tvResults = (TextView) view.findViewById(R.id.tvResults);
        etQuery = (EditText) view.findViewById(R.id.etLastName);

        btnFilter.setOnClickListener(this);

        fetchOpenedLinks();
        return view;
    }

    private void fetchOpenedLinks() {
        Call<OpenedLinksResponse> call = MyApiAdapter.getApiService().getOpenedLinks();
        call.enqueue(new Callback<OpenedLinksResponse>() {
            @Override
            public void onResponse(Call<OpenedLinksResponse> call, Response<OpenedLinksResponse> response) {
                if (response.isSuccessful()) {
                    OpenedLinksResponse emailsResponse = response.body();
                    if (emailsResponse != null) {
                        openedLinks = emailsResponse.getOpenedLinks();
                        displayOpenedLinks(openedLinks);
                    }
                } else {
                    Toast.makeText(getContext(), R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OpenedLinksResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayOpenedLinks(ArrayList<OpenedLink> openedLinks) {
        String s = "";
        for (OpenedLink link : openedLinks) {
            s += "Link: " + link.getLink() +"\n"+
                    "Categoria: " + link.getCategory() +"\n"+
                    "Titulo: " +  link.getTitle() +"\n"+
                    "Cliente: " +  link.getClient() +"\n"+
                    "Sist.Opera: " +  link.getSO() +"\n"+
                    "Navegador: " +  link.getBrowser() +"\n"+
                    "IP: " +  link.getIP() +"\n"+
                    "Fecha: " +  link.getDate() +"\n"+
                    "------------------------------------------------------\n";
        }

        tvResults.setText(s);
    }

    private void displayOpenedLinks(final String queryClient) {
        final ArrayList<OpenedLink> filteredList = new ArrayList<>();

        for (OpenedLink openedLink : openedLinks)
            if (openedLink.getClient().toLowerCase().contains(queryClient.toLowerCase()))
                filteredList.add(openedLink);

        displayOpenedLinks(filteredList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFilter:
                displayOpenedLinks(etQuery.getText().toString());
                break;
        }
    }
}











