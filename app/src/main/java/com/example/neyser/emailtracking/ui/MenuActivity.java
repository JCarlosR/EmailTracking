package com.example.neyser.emailtracking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.neyser.emailtracking.ClientsBySource;
import com.example.neyser.emailtracking.common.Global;
import com.example.neyser.emailtracking.ui.fragment.CategoriesFragment;
import com.example.neyser.emailtracking.ui.fragment.ClientsFragment;
import com.example.neyser.emailtracking.ui.fragment.DashboardFragment;
import com.example.neyser.emailtracking.ui.fragment.LinksFragment;
import com.example.neyser.emailtracking.ui.fragment.OpenedEmailsFragment;
import com.example.neyser.emailtracking.R;
import com.example.neyser.emailtracking.ClientsBySellers;
import com.example.neyser.emailtracking.ui.fragment.OpenedLinksFragment;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Acceda al menú para ver las opciones", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_right, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Global.saveBooleanPreference(this, "logged_in", false);
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            performTransactionTo(new DashboardFragment());
            setActionBarTitle("Dashboard");

        } else if (id == R.id.nav_opened_emails) {
            performTransactionTo(new OpenedEmailsFragment());
            setActionBarTitle("Emails abiertos por clientes");
        } else if (id == R.id.nav_opened_links) {
            performTransactionTo(new OpenedLinksFragment());
            setActionBarTitle("Enlaces abiertos por clientes");

        } else if (id == R.id.nav_clients_by_sellers) {
            Intent i = new Intent(this, ClientsBySellers.class);
            startActivity(i);

        } else if (id == R.id.nav_clients_by_source) {
            Intent i = new Intent(this, ClientsBySource.class);
            startActivity(i);

        }  else if (id == R.id.nav_clients) {
            performTransactionTo(new ClientsFragment());
        }  else if (id == R.id.nav_categories) {
            performTransactionTo(new CategoriesFragment());
        }  else if (id == R.id.nav_links) {
            performTransactionTo(new LinksFragment());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setActionBarTitle(final String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(title);
    }

    private void performTransactionTo(final Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_menu, fragment).commit();
    }
}
