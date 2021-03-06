package com.example.neyser.emailtracking.ui.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.common.SpinnerHelper;
import com.example.neyser.emailtracking.io.MyApiAdapter;
import com.example.neyser.emailtracking.io.response.ClientsBySellersResponse;
import com.example.neyser.emailtracking.io.response.SellersResponse;
import com.example.neyser.emailtracking.model.Seller;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientsBySellersActivity extends AppCompatActivity {

    private BarChart barChart;
    private TextView tvSellers;

    private Spinner spinnerYear, spinnerMonth;
    private Button btnFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_by_sellers);

        // enable back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Clientes por vendedor");
        }

        barChart = (BarChart) findViewById(R.id.barChartGraph);
        tvSellers = (TextView) findViewById(R.id.tvSellers);

        setupFilters();

        fetchSellers();
        fetchClients();
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
        // final int month = monthIndex -1;
        fetchClients(year, monthIndex);
    }

    private void fetchSellers() {
        Call<SellersResponse> call = MyApiAdapter.getApiService().getSellers();

        call.enqueue(new Callback<SellersResponse>() {
            @Override
            public void onResponse(Call<SellersResponse> call, Response<SellersResponse> response) {
                if (response.isSuccessful()) {
                    SellersResponse sellersResponse = response.body();
                    if (sellersResponse == null) {
                        Toast.makeText(ClientsBySellersActivity.this, R.string.error_sellers_response, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String sellersContent = "";
                    for (Seller seller : sellersResponse.getSellers()) {
                        sellersContent += "Codigo: "+ seller.getId() +"\n"+
                                "Apellido: " + seller.getName() +"\n"+
                                "-----------------------------" +"\n";
                    }
                    tvSellers.setText(sellersContent);
                } else {
                    Toast.makeText(ClientsBySellersActivity.this, R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SellersResponse> call, Throwable t) {
                Toast.makeText(ClientsBySellersActivity.this, R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchClients(int year, int month) {
        Call<ClientsBySellersResponse> call = MyApiAdapter.getApiService().getClientsBySellers(year, month);
        enqueueClientsRequest(call);
    }

    private void fetchClients() {
        Call<ClientsBySellersResponse> call = MyApiAdapter.getApiService().getClientsBySellers();
        enqueueClientsRequest(call);
    }

    private void enqueueClientsRequest(Call<ClientsBySellersResponse> call) {
        call.enqueue(new Callback<ClientsBySellersResponse>() {
            @Override
            public void onResponse(Call<ClientsBySellersResponse> call, Response<ClientsBySellersResponse> response) {
                if (response.isSuccessful()) {
                    ClientsBySellersResponse clientsBySellersResponse = response.body();
                    if (clientsBySellersResponse==null) {
                        Toast.makeText(ClientsBySellersActivity.this, R.string.error_clients_by_sellers_response, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList<ClientsBySellersResponse.ClientCounter> clientCounters = clientsBySellersResponse.getSellers();
                    createBarChart(clientCounters);
                } else {
                    Toast.makeText(ClientsBySellersActivity.this, R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClientsBySellersResponse> call, Throwable t) {
                Toast.makeText(ClientsBySellersActivity.this, R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createBarChart(ArrayList<ClientsBySellersResponse.ClientCounter> counters) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (ClientsBySellersResponse.ClientCounter clientCounter : counters) {
            entries.add(
                new BarEntry(clientCounter.getId(), clientCounter.getQuantity())
            );
        }

        BarDataSet dataSet = new BarDataSet(entries, "Cant clientes por vendedor");
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f); // set custom bar width

        // empty description label
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        barChart.setData(data);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
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
