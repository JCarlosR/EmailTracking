package com.example.neyser.emailtracking;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.neyser.emailtracking.io.MyApiAdapter;
import com.example.neyser.emailtracking.io.response.ClientsBySourceResponse;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientsBySource extends AppCompatActivity {
    private PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_by_source);

        // enable back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        chart = (PieChart) findViewById(R.id.chartP);

        fetchClientsBySource();
    }


    private void fetchClientsBySource() {
        Call<ClientsBySourceResponse> call = MyApiAdapter.getApiService().getClientsBySource();
        call.enqueue(new Callback<ClientsBySourceResponse>() {
            @Override
            public void onResponse(Call<ClientsBySourceResponse> call, Response<ClientsBySourceResponse> response) {
                if (response.isSuccessful()) {
                    ClientsBySourceResponse clientsBySourceResponse = response.body();
                    if (clientsBySourceResponse==null) {
                        Toast.makeText(ClientsBySource.this, R.string.error_clients_by_sellers_response, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList<ClientsBySourceResponse.ClientCounter> clientCounters = clientsBySourceResponse.getSources();
                    createPieChart(clientCounters);
                } else {
                    Toast.makeText(ClientsBySource.this, R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClientsBySourceResponse> call, Throwable t) {
                Toast.makeText(ClientsBySource.this, R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPieChart(ArrayList<ClientsBySourceResponse.ClientCounter> clientCounters) {
        List<PieEntry> entries = new ArrayList<>();

        for (ClientsBySourceResponse.ClientCounter clientCounter : clientCounters) {
            entries.add(
                new PieEntry(clientCounter.getQuantity(), clientCounter.getSource())
            );
        }

        PieDataSet set = new PieDataSet(entries, "Medios de captación");
        set.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("Clientes por medio");
        chart.setDescription(description);

        PieData data = new PieData(set);
        chart.setData(data);
        chart.invalidate(); // refresh
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
