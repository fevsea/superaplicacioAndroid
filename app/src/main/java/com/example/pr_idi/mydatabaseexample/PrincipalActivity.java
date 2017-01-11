package com.example.pr_idi.mydatabaseexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;


public class PrincipalActivity extends NavigationDrawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_principal);
        setTitle("Book");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        super.onCreate(savedInstanceState, navigationView, recyclerView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        initToolbar(toolbar, drawer);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        initFab(fab);
        initDelete();
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
    protected void onResume() {
        recyclerManager.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        recyclerManager.close();
        super.onPause();
    }

    private void initToolbar(Toolbar toolbar, DrawerLayout drawer) {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

    }

    private void initFab(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddItem.class);
                startActivityForResult(intent, ITEM_ADED);
            }
        });
    }

    private void initDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int pos = viewHolder.getAdapterPosition();
                recyclerManager.deletedSwipe(pos);
                Snackbar.make(findViewById(R.id.content_principal), "Book removed!", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(findViewById(R.id.content_principal), "Book successful restored", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                                recyclerManager.createBookDel();
                            }
                        }).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerManager.getRecycler());
    }

}
