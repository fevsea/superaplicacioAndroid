package com.example.pr_idi.mydatabaseexample;


import java.util.Comparator;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private Evaluation bookData;
    private ListView list;
    private ArrayAdapter<Book> adapterBook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        makeNavigationDraw();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        bookData = new Evaluation(this);
        bookData.open();

        list = (ListView) findViewById(R.id.list);
        List<Book> booksList = bookData.getAllBooks();
        adapterBook = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, booksList);
        list.setAdapter(adapterBook);
    }


    @Override
    protected void onResume() {
        bookData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        bookData.close();
        super.onPause();
    }

    private void makeNavigationDraw() {
        String[] mPlanetTitles = getResources().getStringArray(R.array.listArray);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Book book;
        switch (item.getItemId()) {
            case R.id.action_addNew:
                String[] newBook = new String[] { "Miguel Strogoff", "Jules Verne", "Ulysses", "James Joyce", "Don Quijote", "Miguel de Cervantes", "Metamorphosis", "Kafka" };
                int nextInt = new Random().nextInt(4);
                book = bookData.createBook(newBook[nextInt*2], newBook[nextInt*2 + 1]);
                adapterBook.add(book);

                //FOR NOW ToDo: bad sort has to be changed
                adapterBook.sort(new Comparator<Book>() {
                    @Override
                    public int compare(Book a, Book b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }});
                break;
            case R.id.action_delete:
                if (adapterBook.getCount() > 0) {
                    book = adapterBook.getItem(0);
                    bookData.deleteBook(book);
                    adapterBook.remove(book);
                }
                break;
            default:
                adapterBook.notifyDataSetChanged();
                return super.onOptionsItemSelected(item);

        }
        adapterBook.notifyDataSetChanged();
        return true;

    }


//    ToDo: pass to a own class
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterBook.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}

