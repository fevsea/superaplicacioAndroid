package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

public class RecyclerManager {
    private RecyclerView recyclerView;
    private BookDataExp mBookData;
    private BookAdapter mAdapter;
    private List<Book> books;
    private Book deleted;
    private Context context;

    public RecyclerManager(RecyclerView recycler, Context context) {
        this.context = context;
        recyclerView = recycler;
        initBookData();
        initDelete();
        initReciclerView();
    }

    public void close() {
        mBookData.close();
    }

    public void open() {
        mBookData.open();
    }
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener lister) {
        recyclerView.addOnItemTouchListener(lister);
    }

    public RecyclerView getRecycler() {
        return recyclerView;
    }

    public void setAdapter(BookAdapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();
    }

    private void initBookData() {
        mBookData = new BookDataExp(context);
        mBookData.open();
        mBookData.setScreen(BookDataExp.Screens.TITLE);
        mBookData.checkFirstInit();
        books = mBookData.generateBooks();
    }

    private void initReciclerView() {
        mAdapter = new BookAdapter(books, "author");
        recyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void updateBooks(){
        books = mBookData.generateBooks();
        mAdapter = new BookAdapter(books, "author");
        recyclerView.setAdapter(mAdapter);
        recyclerView.invalidate();
    }

    public void setScreen(BookDataExp.Screens screen) {
        mBookData.setScreen(screen);
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book getBook(int position) {
        return books.get(position);
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
                Book b = books.get(pos);
                deleted = b;
                mBookData.deleteBook(b);
                mAdapter.bookList.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                mAdapter.notifyItemRangeChanged(pos, mAdapter.bookList.size());

//                Snackbar.make(findViewById(R.id.content_principal), "Book removed!", Snackbar.LENGTH_LONG)
//                        .setAction("UNDO", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Snackbar snackbar1 = Snackbar.make(findViewById(R.id.content_principal), "Book successful restored", Snackbar.LENGTH_SHORT);
//                                snackbar1.show();
//                                mBookData.createBook(deleted.getTitle(), deleted.getAuthor(), deleted.getCategory(),
//                                        String.valueOf(deleted.getYear()), deleted.getPersonal_evaluation());
//                                deleted = null;
//                                updateBooks();
//                            }
//                        }).show();

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
