package com.example.neyser.emailtracking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.common.Global;
import com.example.neyser.emailtracking.io.MyApiAdapter;
import com.example.neyser.emailtracking.io.response.SimpleResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<SimpleResponse> {

    // views
    Button btnLogin;
    EditText etUsername, etPassword;
    TextView tvError;

    // input data
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvError = (TextView) findViewById(R.id.txtError);
        etUsername = (EditText) findViewById(R.id.txtUsuario);
        etPassword = (EditText) findViewById(R.id.txtClave);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        redirectIfAlreadyAuthenticated();
        displayLastLoggedUsername();
    }

    private void redirectIfAlreadyAuthenticated() {
        final boolean loggedIn = Global.getBooleanFromPreferences(this, "logged_in");
        if (loggedIn)
            goToMenuActivity();
    }

    private void displayLastLoggedUsername() {
        final String lastUsername = Global.getStringFromPreferences(this, "username");
        etUsername.setText(lastUsername);
    }

    private void goToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                performLogin();
                break;
        }

    }

    private void performLogin() {
        username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        Call<SimpleResponse> call = MyApiAdapter.getApiService().getLogin(username, password);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
        if (response.isSuccessful()) {
            SimpleResponse loginResponse = response.body();
            if (loginResponse!=null && loginResponse.isSuccess()) {
                Global.saveStringPreference(this, "username", username);
                Global.saveBooleanPreference(this, "logged_in", true);

                goToMenuActivity();
            } else {
                tvError.setText(R.string.error_login_response);
            }
        } else {
            tvError.setText(R.string.failure_retrofit_response);
        }
    }

    @Override
    public void onFailure(Call<SimpleResponse> call, Throwable t) {
        tvError.setText(R.string.failure_retrofit_callback);
    }

}
