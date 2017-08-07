package com.example.neyser.emailtracking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.common.Global;
import com.example.neyser.emailtracking.io.MyApiAdapter;
import com.example.neyser.emailtracking.io.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<LoginResponse> {
    Button btnLogin;
    EditText etUsername, etPassword;
    TextView tvError;

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

        displayLastLoggedUsername();
    }

    private void displayLastLoggedUsername() {
        final String lastUsername = Global.getStringFromPreferences(this, "username");
        etUsername.setText(lastUsername);
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
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        Call<LoginResponse> call = MyApiAdapter.getApiService().getLogin(username, password);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        if (response.isSuccessful()) {
            LoginResponse loginResponse = response.body();
            if (loginResponse!=null && loginResponse.isSuccess()) {
                Global.saveStringPreference(this, "username", etUsername.getText().toString());

                Intent intent = new Intent(this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                tvError.setText(R.string.error_login_response);
            }
        } else {
            tvError.setText(R.string.failure_retrofit_response);
        }
    }

    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {
        tvError.setText(R.string.failure_retrofit_callback);
    }

}
