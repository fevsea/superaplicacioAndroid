package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;


public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // for Log.x(TAG, "Error x");
    public static final String TAG = PrincipalActivity.class.getSimpleName();
    static final int ITEM_ADED = 0;
    BookData mBookData;
    Queue<Book> deleted;


    private BookAdapter mAdapter;
    private RecyclerView recyclerView;
    List<Book> books;

    Screens actual;
    Stack<Screens> screens;

    public enum Screens {
        TITLE, AUTHOR, CATHEGORY
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Book");
        mBookData = new BookData(getApplicationContext());
        deleted = new LinkedList<Book>();

        actual = Screens.TITLE;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddItem.class);
                startActivityForResult(intent, ITEM_ADED);
            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //setContentView(R.layout.content_principal);
        recyclerView = (RecyclerView) findViewById(R.id.rv_numbers);

        generateBooks();
        mAdapter = new BookAdapter(books, "author");


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                mBookData.open();
                Integer pos = viewHolder.getAdapterPosition();
                Log.d(TAG, "Pos: " + pos);
                Book b = books.get(pos);
                deleted.add(b);
                mBookData.deleteBook(b);
                mBookData.close();

                mAdapter.notifyItemRemoved(pos);
            }
        };



        /*
         * A LinearLayoutManager is responsible for measuring and positioning item views within a
         * RecyclerView into a linear list. This means that it can produce either a horizontal or
         * vertical list depending on which parameter you pass in to the LinearLayoutManager
         * constructor. By default, if you don't specify an orientation, you get a vertical list.
         * In our case, we want a vertical list, so we don't need to pass in an orientation flag to
         * the LinearLayoutManager constructor.
         *
         * There are other LayoutManagers available to display your data in uniform grids,
         * staggered grids, and more! See the developer documentation for more details.
         */
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */

        recyclerView.setHasFixedSize(true);

        /*
         * The GreenAdapter is responsible for displaying each item in the list.
         */

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter);
    }

    public void updateBooks(){
        generateBooks();
        recyclerView.setAdapter(new BookAdapter(books, "author"));
        recyclerView.invalidate();
    }


    public class compareByTitle implements Comparator<Book> {
        @Override
        public int compare(Book a, Book b) {
            return a.getTitle().toLowerCase().compareTo(b.getTitle().toLowerCase());
        }
    }
    public class compareByAuthor implements Comparator<Book> {
        @Override
        public int compare(Book a, Book b) {
            return a.getAuthor().toLowerCase().compareTo(b.getAuthor().toLowerCase());
        }
    }
    public class compareByCathegory implements Comparator<Book> {
        @Override
        public int compare(Book a, Book b) {
            return a.getCategory().toLowerCase().compareTo(b.getCategory().toLowerCase());
        }
    }



    protected void generateBooks() {
        mBookData.open();
        books = mBookData.getAllBooks();
        mBookData.close();
        if (actual == Screens.CATHEGORY){
            Collections.sort(books, new compareByCathegory() );
        } else if (actual == Screens.AUTHOR){
            Collections.sort(books, new compareByAuthor() );
        } else if (actual == Screens.TITLE){
            Collections.sort(books, new compareByTitle() );
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Context context = getApplicationContext();
            Toast.makeText(context, "Settings", Toast.LENGTH_LONG).show();
            String[] newBook = new String[] { "Miguel Strogoff", "Jules Verne", "Ulysses", "James Joyce", "Don Quijote", "Miguel de Cervantes", "Metamorphosis", "Kafka" };
            int nextInt = new Random().nextInt(4);
            // save the new book to the database
            mBookData.open();
            mBookData.createBook(newBook[nextInt*2], newBook[nextInt*2 + 1], null, null, null);
            mBookData.close();
            updateBooks();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Context context = getApplicationContext();
        if (id == R.id.list_cat) {
            actual = Screens.CATHEGORY;
            setTitle("Category");
            updateBooks();
        } else if (id == R.id.list_title) {
            actual = Screens.TITLE;
            setTitle("Book");
            updateBooks();
        } else if (id == R.id.list_author) {
            actual = Screens.AUTHOR;
            setTitle("Author");
            updateBooks();
        } else if (id == R.id.about) {
            Toast.makeText(context, "About", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.help) {
            Toast.makeText(context, "Help", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == ITEM_ADED) {
            Log.d(TAG, "DETECTED");
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "EXECUTED");
                updateBooks();
            }
        }
    }

    @Override
    protected void onResume() {
        mBookData.open();
        super.onResume();
    }

    // Life cycle methods. Check whether it is necessary to reimplement them

    @Override
    protected void onPause() {
        mBookData.close();
        super.onPause();
    }

}
