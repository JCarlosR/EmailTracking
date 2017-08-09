package com.example.neyser.emailtracking.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.common.SpinnerHelper;
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

public class ClientsBySourceActivity extends AppCompatActivity {

    private PieChart chart;

    private Spinner spinnerYear, spinnerMonth;
    private Button btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_by_source);

        // enable back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Clientes según medio");
        }

        chart = (PieChart) findViewById(R.id.chartP);

        setupFilters();
        fetchClientsBySource();
    }

    private void setupFilters() {
        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);

        List<String> years = new ArrayList<>();
        years.add("Seleccione año");
        years.add("2016");
        years.add("2017");
        SpinnerHelper.populate(this, spinnerYear, years);

        List<String> months = new ArrayList<>();
        months.add("Seleccione mes");
        months.add("Enero");
        months.add("Febrero");
        months.add("Marzo");
        months.add("Abril");
        months.add("Mayo");
        months.add("Junio");
        months.add("Julio");
        months.add("Agosto");
        months.add("Setiembre");
        months.add("Octubre");
        months.add("Noviembre");
        months.add("Diciembre");
        SpinnerHelper.populate(this, spinnerMonth, months);

        btnFilter = (Button) findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyFilter();
            }
        });
    }

    private void applyFilter() {
        final int yearIndex = SpinnerHelper.getSelectedIndex(spinnerYear);
        final int monthIndex = SpinnerHelper.getSelectedIndex(spinnerMonth);

        if (yearIndex == 0) {
            Toast.makeText(this, R.string.year_missing, Toast.LENGTH_SHORT).show();
            return;
        }

        if (monthIndex == 0) {
            Toast.makeText(this, R.string.month_missing, Toast.LENGTH_SHORT).show();
            return;
        }

        final int year = yearIndex+2015; // 1->2015
        fetchClientsBySource(year, monthIndex);
    }

    private void fetchClientsBySource(final int year, final int month) {
        Call<ClientsBySourceResponse> call = MyApiAdapter.getApiService().getClientsBySource(year, month);
        enqueueClientsBySource(call);
    }

    private void fetchClientsBySource() {
        Call<ClientsBySourceResponse> call = MyApiAdapter.getApiService().getClientsBySource();
        enqueueClientsBySource(call);
    }

    private void enqueueClientsBySource(Call<ClientsBySourceResponse> call) {
        call.enqueue(new Callback<ClientsBySourceResponse>() {
            @Override
            public void onResponse(Call<ClientsBySourceResponse> call, Response<ClientsBySourceResponse> response) {
                if (response.isSuccessful()) {
                    ClientsBySourceResponse clientsBySourceResponse = response.body();
                    if (clientsBySourceResponse==null) {
                        Toast.makeText(ClientsBySourceActivity.this, R.string.error_clients_by_sellers_response, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList<ClientsBySourceResponse.ClientCounter> clientCounters = clientsBySourceResponse.getSources();
                    createPieChart(clientCounters);
                } else {
                    Toast.makeText(ClientsBySourceActivity.this, R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClientsBySourceResponse> call, Throwable t) {
                Toast.makeText(ClientsBySourceActivity.this, R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
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
