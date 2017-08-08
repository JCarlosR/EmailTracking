package com.example.neyser.emailtracking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.common.SpinnerHelper;
import com.example.neyser.emailtracking.io.MyApiAdapter;
import com.example.neyser.emailtracking.io.response.LinkTypesResponse;
import com.example.neyser.emailtracking.io.response.SimpleResponse;
import com.example.neyser.emailtracking.io.response.SubCategoriesByCategoryResponse;
import com.example.neyser.emailtracking.model.Link;
import com.example.neyser.emailtracking.model.LinkType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkFormActivity extends AppCompatActivity {

    private int link_id;
    private Link link;
    private boolean is_new;
    private boolean storing = false;

    private TextInputLayout tilLinkName, tilLinkUrl;
    private EditText etLinkName, etLinkUrl;
    private Spinner spinnerLinkType;

    private ArrayList<LinkType> linkTypes;

    // general views
    private ProgressBar progressBar;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_form);

        Intent intent = getIntent();
        link_id = intent.getIntExtra("link_id", 0);
        is_new = (link_id == 0);

        String title;
        if (is_new)
            title = "Nuevo enlace";
        else {
            title = "Editar enlace";
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

        tilLinkName = (TextInputLayout) findViewById(R.id.tilLinkName);
        tilLinkUrl = (TextInputLayout) findViewById(R.id.tilLinkUrl);

        etLinkName = (EditText) findViewById(R.id.etLinkName);
        etLinkUrl = (EditText) findViewById(R.id.etLinkUrl);

        spinnerLinkType = (Spinner) findViewById(R.id.spinnerLinkType);
    }

    private void loadSpinnerData() {
        Call<LinkTypesResponse> call = MyApiAdapter.getApiService().getLinkTypes();
        call.enqueue(new Callback<LinkTypesResponse>() {
            @Override
            public void onResponse(Call<LinkTypesResponse> call, Response<LinkTypesResponse> response) {
                if (response.isSuccessful()) {
                    LinkTypesResponse typesResponse = response.body();
                    if (typesResponse != null) {
                        linkTypes = typesResponse.getLinkTypes();
                        populateLinkTypesSpinner();
                    }
                } else {
                    Toast.makeText(LinkFormActivity.this, R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LinkTypesResponse> call, Throwable t) {
                Toast.makeText(LinkFormActivity.this, R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateLinkTypesSpinner() {
        final List<String> options = new ArrayList<>();
        for (LinkType type : linkTypes) {
            options.add(type.getName());
        }

        SpinnerHelper.populate(this, spinnerLinkType, options);
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
        if (! validateEditText(etLinkName, tilLinkName, R.string.error_client_first_name)) {
            return;
        }

        if (! validateEditText(etLinkUrl, tilLinkUrl, R.string.error_client_last_name)) {
            return;
        }


        // get edit text values
        final String linkName = etLinkName.getText().toString().trim();
        final String linkUrl = etLinkUrl.getText().toString().trim();

        // get spinner values
        final int linkTypeIndex = SpinnerHelper.getSelectedIndex(spinnerLinkType);
        final LinkType linkType = linkTypes.get(linkTypeIndex);
        final int typeId = linkType.getValue();

        startStoringState();

        link = new Link();
        link.setName(linkName);
        link.setUrl(linkUrl);
        link.setTypeId(typeId);

        // Request to post/store a new link
        Call<SimpleResponse> call = MyApiAdapter.getApiService().postNewLink(linkName, linkUrl, typeId);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    SimpleResponse simpleResponse = response.body();
                    if (simpleResponse != null) {
                        if (simpleResponse.isSuccess()) {
                            Toast.makeText(LinkFormActivity.this, R.string.success_new_link, Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(LinkFormActivity.this, R.string.failure_new_link, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LinkFormActivity.this, R.string.failure_retrofit_response, Toast.LENGTH_SHORT).show();
                }

                stopStoringState();
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(LinkFormActivity.this, R.string.failure_retrofit_callback, Toast.LENGTH_SHORT).show();

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
