package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static final int ITEM_ADED = 0;
    protected RecyclerManager recyclerManager;

    protected void onCreate(Bundle savedInstanceState, RecyclerView recyclerView) {
        super.onCreate(savedInstanceState);
        recyclerManager = new RecyclerManager(recyclerView, getApplicationContext());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.list_cat) {
            recyclerManager.setScreen(BookDataExp.Screens.CATHEGORY);
            setTitle("Category");
            recyclerManager.updateBooks();
        } else if (id == R.id.list_title) {
            recyclerManager.setScreen(BookDataExp.Screens.TITLE);
            setTitle("Book");
            recyclerManager.updateBooks();
        } else if (id == R.id.list_author) {
            recyclerManager.setScreen(BookDataExp.Screens.AUTHOR);
            setTitle("Author");
            recyclerManager.updateBooks();
        } else if (id == R.id.about) {
            Intent intent = new Intent(getApplicationContext(), About.class);
            startActivityForResult(intent, ITEM_ADED);
        } else if (id == R.id.help) {
            Intent intent = new Intent(getApplicationContext(), Help.class);
            startActivityForResult(intent, ITEM_ADED);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.principal, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Book> newBook = new ArrayList<>();
                for (Book u : recyclerManager.getBooks()) {
                    if (u.match(newText)) {
                        newBook.add(u);
                    }
                }
                recyclerManager.setAdapter(new BookAdapter(newBook, "author"));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ITEM_ADED) {
            if (resultCode == Activity.RESULT_OK) {
                recyclerManager.open();
                recyclerManager.updateBooks();
            }
        }
    }
}
