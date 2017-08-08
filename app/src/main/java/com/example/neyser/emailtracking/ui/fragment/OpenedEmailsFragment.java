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
import com.example.neyser.emailtracking.io.response.OpenedEmailsResponse;
import com.example.neyser.emailtracking.model.Email;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenedEmailsFragment extends Fragment implements View.OnClickListener {

    private TextView tvResults;
    private EditText etLastName;
    private Button btnFilter;

    private ArrayList<Email> openedEmails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_opened_emails, container, false);

        btnFilter = (Button) view.findViewById(R.id.btnFilterOpenedEmails);
        tvResults = (TextView) view.findViewById(R.id.tvResults);
        etLastName = (EditText) view.findViewById(R.id.etLastName);

        btnFilter.setOnClickListener(this);

        fetchOpenedEmails();
        return view;
    }

    private void fetchOpenedEmails() {
        Call<OpenedEmailsResponse> call = MyApiAdapter.getApiService().getOpenedEmails("");
        call.enqueue(new Callback<OpenedEmailsResponse>() {
            @Override
            public void onResponse(Call<OpenedEmailsResponse> call, Response<OpenedEmailsResponse> response) {
                if (response.isSuccessful()) {
                    OpenedEmailsResponse emailsResponse = response.body();
                    if (emailsResponse != null) {
                        openedEmails = emailsResponse.getEmails();
                        displayEmails(openedEmails);
                    }
                } else {
                    Toast.makeText(getContext(), R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OpenedEmailsResponse> call, Throwable t) {
                Toast.makeText(getContext(), R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayEmails(ArrayList<Email> emails) {
        String s = "";
        for (Email email : emails) {
            s += "Categoria: " + email.getCategory() +"\n"+
                    "Titulo: " +  email.getTitle() +"\n"+
                    "Cliente: " +  email.getClient() +"\n"+
                    "Sist.Opera: " +  email.getSO() +"\n"+
                    "Navegador: " +  email.getBrowser() +"\n"+
                    "IP: " +  email.getIP() +"\n"+
                    "Fecha: " +  email.getDate() +"\n"+
                    "------------------------------------------------------\n";
        }

        tvResults.setText(s);
    }

    private void displayEmails(final String queryClient) {
        final ArrayList<Email> filteredEmails = new ArrayList<>();

        for (Email email : openedEmails)
            if (email.getClient().toLowerCase().contains(queryClient.toLowerCase()))
                filteredEmails.add(email);

        displayEmails(filteredEmails);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFilterOpenedEmails:
                displayEmails(etLastName.getText().toString());
                break;
        }
    }
}











