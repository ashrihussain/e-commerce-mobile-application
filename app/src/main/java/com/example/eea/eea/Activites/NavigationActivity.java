package com.example.eea.eea.Activites;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.eea.eea.Fragments.HomeFragment;
import com.example.eea.eea.Fragments.ManageAccountFragment;
import com.example.eea.eea.Fragments.PurchaseHistoryFragment;
import com.example.eea.eea.Fragments.ViewCart;
import com.example.eea.eea.R;
import com.github.clans.fab.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//
//import com.example.eea.eea.Fragments.ManageAccountFragment;
//import com.example.eea.eea.Fragments.PurchaseHistoryFragment;
//import com.example.eea.eea.Fragments.ViewCart;
import com.github.clans.fab.FloatingActionMenu;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String uid;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displaySettingsScreen(R.id.nav_home);
        setTitle("Home");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



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

        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void displaySettingsScreen(int id) {
        Fragment fragment = null;

        switch (id) {

            case R.id.nav_home:
                fragment = new HomeFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();


                break;
            case R.id.cart:
                fragment = new ViewCart();
                FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
                fts.replace(R.id.fragment_container, fragment);
                fts.addToBackStack(null);
                fts.commit();
                break;


            case R.id.history:
                 fragment = new PurchaseHistoryFragment();
                FragmentTransaction ftx = getSupportFragmentManager().beginTransaction();
                ftx.replace(R.id.fragment_container, fragment);
                ftx.addToBackStack(null);
                ftx.commit();
                break;

            case R.id.accountsettings:
                fragment = new ManageAccountFragment();
                FragmentTransaction ftz = getSupportFragmentManager().beginTransaction();
                ftz.replace(R.id.fragment_container, fragment);
                ftz.addToBackStack(null);
                ftz.commit();
                break;

            case R.id.nav_logout:
                SharedPreferences sharedPreferences=getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_home) {
            Fragment fragment = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
            setTitle("Home");
            drawer.closeDrawer(GravityCompat.START);


        } else if (id == R.id.cart) {
            Fragment fragment = new ViewCart();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
            setTitle("Shopping Cart");
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.history) {
            Fragment fragment = new PurchaseHistoryFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
            setTitle("Purchase History");
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.accountsettings) {
            Fragment fragment = new ManageAccountFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
            setTitle("Account Settings");
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences=getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();
            Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
            startActivity(intent);
            setTitle("Main Screen");
            drawer.closeDrawer(GravityCompat.START);
        }




            return true;
        }


}
