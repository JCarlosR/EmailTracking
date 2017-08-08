package com.example.neyser.emailtracking.ui.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.common.SpinnerHelper;
import com.example.neyser.emailtracking.io.MyApiAdapter;
import com.example.neyser.emailtracking.io.response.CategoriesResponse;
import com.example.neyser.emailtracking.io.response.SimpleResponse;
import com.example.neyser.emailtracking.io.response.SubCategoriesByCategoryResponse;
import com.example.neyser.emailtracking.model.Category;
import com.example.neyser.emailtracking.model.Client;
import com.example.neyser.emailtracking.model.SubCategory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientFormActivity extends AppCompatActivity {

    // show full data: https://github.com/JCarlosR/Conciviles/blob/master/app/src/main/res/layout/activity_report.xml
    // register report: https://github.com/JCarlosR/Conciviles/blob/master/app/src/main/java/com/programacionymas/conciviles/ui/activity/ReportFormActivity.java

    private int client_id;
    private Client client;
    private boolean is_new;
    private boolean storing = false;

    private TextInputLayout tilFirstName, tilLastName, tilEmail;
    private EditText etFirstName, etLastName, etEmail;
    private Spinner spinnerCategory, spinnerSubCategory;

    private ArrayList<Category> categories;
    private ArrayList<SubCategory> subCategories;

    // general views
    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_form);

        Intent intent = getIntent();
        client_id = intent.getIntExtra("client_id", 0);
        is_new = (client_id == 0);

        String title;
        if (is_new)
            title = "Nuevo cliente";
        else {
            title = "Editar cliente";
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }

        getViewReferences();
        loadSpinnerData();
    }

    private void getViewReferences() {
        // general views
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tilFirstName = (TextInputLayout) findViewById(R.id.tilFirstName);
        tilLastName = (TextInputLayout) findViewById(R.id.tilLastName);
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);

        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerSubCategory = (Spinner) findViewById(R.id.spinnerSubCategory);
    }

    private void loadSpinnerData() {
        Call<CategoriesResponse> call = MyApiAdapter.getApiService().getCategories();
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                if (response.isSuccessful()) {
                    CategoriesResponse categoryResponse = response.body();
                    if (categoryResponse != null) {
                        categories = categoryResponse.getCategories();
                        populateCategoriesSpinner();
                        loadSubCategories(categories.get(0).getId());
                    }
                } else {
                    Toast.makeText(ClientFormActivity.this, R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Toast.makeText(ClientFormActivity.this, R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSubCategories(final int category_id) {
        Call<SubCategoriesByCategoryResponse> call = MyApiAdapter.getApiService().getSubCategories(category_id);
        call.enqueue(new Callback<SubCategoriesByCategoryResponse>() {
            @Override
            public void onResponse(Call<SubCategoriesByCategoryResponse> call, Response<SubCategoriesByCategoryResponse> response) {
                if (response.isSuccessful()) {
                    SubCategoriesByCategoryResponse requestResponse = response.body();
                    if (requestResponse != null) {
                        subCategories = requestResponse.getSubCategories();
                        populateSubCategoriesSpinner();
                    }
                } else {
                    Toast.makeText(ClientFormActivity.this, R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubCategoriesByCategoryResponse> call, Throwable t) {
                Toast.makeText(ClientFormActivity.this, R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateCategoriesSpinner() {
        final List<String> options = new ArrayList<>();
        for (Category category : categories) {
            options.add(category.getName());
        }

        SpinnerHelper.populate(this, spinnerCategory, options);

        // set listener
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final int i = SpinnerHelper.getSelectedIndex(spinnerCategory);
                loadSubCategories(categories.get(i).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // (?)
            }

        });
    }

    private void populateSubCategoriesSpinner() {
        final List<String> options = new ArrayList<>();
        for (SubCategory subCategory : subCategories) {
            options.add(subCategory.getName());
        }

        SpinnerHelper.populate(this, spinnerSubCategory, options);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            validateForm();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        menu.findItem(R.id.save).setEnabled(!storing);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }


    private boolean validateEditText(EditText editText, TextInputLayout textInputLayout, int errorString) {
        // Log.d("ReportDialogFragment", "Validating an EditText with this value => " + editText.getText().toString());
        if (editText.getText().toString().length() < 1) {
            textInputLayout.setError(getString(errorString));
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void validateForm() {
        if (! validateEditText(etFirstName, tilFirstName, R.string.error_client_first_name)) {
            return;
        }

        if (! validateEditText(etLastName, tilLastName, R.string.error_client_last_name)) {
            return;
        }

        if (! validateEditText(etEmail, tilEmail, R.string.error_client_email)) {
            return;
        }

        // get edit text values
        final String firstName = etFirstName.getText().toString().trim();
        final String lastName = etLastName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();

        // get spinner values
        /*final int categoryIndex = SpinnerHelper.getSelectedIndex(spinnerCategory);
        final Category category = categories.get(categoryIndex);
        final int categoryId = category.getId();*/
        final int subCategoryIndex = SpinnerHelper.getSelectedIndex(spinnerSubCategory);
        final SubCategory subCategory = subCategories.get(subCategoryIndex);
        final int subCategoryId = subCategory.getId();

        // final String potential = spinnerPotential.getSelectedItem().toString();

        startStoringState();

        client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        // client.setInterests(categoryId, subCategoryId);

        // Request to post/store a new client
        Call<SimpleResponse> call = MyApiAdapter.getApiService().postNewClient(firstName, lastName, email, subCategoryId);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    SimpleResponse simpleResponse = response.body();
                    if (simpleResponse != null) {
                        if (simpleResponse.isSuccess()) {
                            Toast.makeText(ClientFormActivity.this, R.string.success_new_client, Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(ClientFormActivity.this, R.string.failure_new_client, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ClientFormActivity.this, R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }

                stopStoringState();
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(ClientFormActivity.this, R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();

                stopStoringState();
            }
        });
        /*
        // If the report ID is ZERO, create a new record
        if (is_new) {

        } else {

            // Request to edit the selected report
            report.setId(report_id);
            Observable<NewReportResponse> observable = report.updateInServer();

            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NewReportResponse>() {

                        @Override
                        public void onNext(NewReportResponse newReportResponse) {
                            // just one item
                            if (newReportResponse.isSuccess()) {
                                Toast.makeText(getApplicationContext(), "El reporte se ha modificado correctamente.", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                stopStoringState();
                                Toast.makeText(getApplicationContext(), newReportResponse.getFirstError(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCompleted() {
                            // Log.d(TAG, "onCompleted" );
                            // caution: finish is called in onNext, be careful with memory leaks at this point
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.d(TAG, "onError Throwable: " + t.toString());
                            // if (t instanceof HttpException) {
                            stopStoringState();
                            Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }*/
    }

    private void startStoringState() {
        storing = true;
        invalidateOptionsMenu();

        nestedScrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopStoringState() {
        storing = false;
        invalidateOptionsMenu();

        nestedScrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
