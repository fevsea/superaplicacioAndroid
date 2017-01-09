package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SearchView;
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
    public static final String TAG = PrincipalActivity.class.getSimpleName();
    static final int ITEM_ADED = 0;
    BookData mBookData;
    Queue<Book> deleted;

    private BookAdapter mAdapter;
    private RecyclerView recyclerView;
    List<Book> books;

    Screens actual;
    Stack<Screens> screens;

    public enum Screens {TITLE, AUTHOR, CATHEGORY}

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
        recyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
        generateBooks();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {}
            @Override
            public void onLongItemClick(View view, int position) {
                CustomDialogClass cdd = new CustomDialogClass(PrincipalActivity.this, books.get(position));
                cdd.show();
            }
        }));
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Context context = getApplicationContext();
            Toast.makeText(context, "Settings", Toast.LENGTH_LONG).show();
            String[] newBook = new String[] { "Miguel Strogoff", "Jules Verne", "Ulysses", "James Joyce", "Don Quijote", "Miguel de Cervantes", "Metamorphosis", "Kafka" };
            int nextInt = new Random().nextInt(4);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    @Override
    protected void onPause() {
        mBookData.close();
        super.onPause();
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
                List<Book> newBook = new ArrayList<Book>();
                for (Book u : books) {
                    if (u.match(newText)) {
                        newBook.add(u);
                    }
                }
                recyclerView.setAdapter(new BookAdapter(newBook, "author"));
                recyclerView.invalidate();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
