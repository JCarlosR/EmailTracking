package com.example.neyser.emailtracking.ui.activity;

import android.graphics.Color;
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
import com.example.neyser.emailtracking.io.response.LinksCounterResponse;
import com.example.neyser.emailtracking.model.LinkCounter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinksPercentActivity extends AppCompatActivity {

    private PieChart chart;

    private Spinner spinnerYear, spinnerMonth;
    private Button btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_by_source);

        // enable back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        chart = (PieChart) findViewById(R.id.chartP);

        setupFilters();
        fetchLinksCounter();
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
        fetchLinksCounter(year, monthIndex);
    }

    private void fetchLinksCounter(final int year, final int month) {
        Call<LinksCounterResponse> call = MyApiAdapter.getApiService().getLinksCounter(year, month);
        enqueueClientsBySource(call);
    }

    private void fetchLinksCounter() {
        fetchLinksCounter(0, 0); // empty filters
    }

    private void enqueueClientsBySource(Call<LinksCounterResponse> call) {
        call.enqueue(new Callback<LinksCounterResponse>() {
            @Override
            public void onResponse(Call<LinksCounterResponse> call, Response<LinksCounterResponse> response) {
                if (response.isSuccessful()) {
                    LinksCounterResponse linksResponse = response.body();
                    if (linksResponse!=null) {
                        ArrayList<LinkCounter> counters = linksResponse.getLinkCounters();
                        createPieChart(counters);
                    }
                } else {
                    Toast.makeText(LinksPercentActivity.this, R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LinksCounterResponse> call, Throwable t) {
                Toast.makeText(LinksPercentActivity.this, R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPieChart(ArrayList<LinkCounter> counters) {
        List<PieEntry> entries = new ArrayList<>();

        for (LinkCounter linkCounter : counters) {
            entries.add(
                new PieEntry(linkCounter.getQuantity(), linkCounter.getName())
            );
        }

        PieDataSet set = new PieDataSet(entries, "Links vistos según contenido");
        set.setColors(ColorTemplate.COLORFUL_COLORS);

        Description description = new Description();
        description.setText("Links según contenido");
        chart.setDescription(description);

        PieData data = new PieData(set);
        data.setValueFormatter(new PercentFormatter());
        chart.setUsePercentValues(true);
        chart.setData(data);
        chart.setHoleRadius(9);
        chart.setTransparentCircleRadius(10);
        chart.setHoleColor(Color.rgb(255, 255, 255));
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
