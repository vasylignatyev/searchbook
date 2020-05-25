package com.ignatyev.vasyl.booksearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.ignatyev.vasyl.booksearch.models.Book;
import com.ignatyev.vasyl.booksearch.adapters.BookAdapter;
import com.ignatyev.vasyl.booksearch.BookClient;
import com.ignatyev.vasyl.booksearch.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.Headers;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    public static final String  ERROR_MESSAGE = "com.ignatyev.vasyl.booksearch.ERROR_MESSAGE";

    private String searchStr = null;
    private BookAdapter bookAdapter;
    private ProgressBar mProgressBar;
    private ArrayList<Book> aBooks;
    private ListView lvBooks;
    private BookListActivity me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Intent intent = getIntent();
        searchStr = intent.getStringExtra(BookSearchActivity.SEARCH_STRING);


        lvBooks = (ListView) findViewById(R.id.lvBooks);
        lvBooks.setEmptyView(findViewById(R.id.empty_list_item));
        setupListWithFooter();

        aBooks = new ArrayList<Book>();
        bookAdapter = new BookAdapter(this, aBooks);
        lvBooks.setAdapter(bookAdapter);

        me = this;

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchBookDetailsActivity(position);
            }
        });
        showProgress();

        fetchBooks();
    }

    private void fetchBooks() {
        BookClient client = new BookClient();
        String title = getResources().getString(R.string.search_result__for) + ": \"" + searchStr + "\"";
        setTitle(title);

        client.getBooks(searchStr, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON response) {
                JSONArray docs = null;
                try {
                    if(response != null) {
                        JSONObject result = response.jsonObject;
                        docs = result.getJSONArray("docs");
                        final ArrayList<Book> books = Book.fromJson(docs);
                        bookAdapter.clear();
                        for (Book book : books) {
                            bookAdapter.add(book);
                        }
                        bookAdapter.notifyDataSetChanged();
                        hideProgress();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    String errorMessage = e.getMessage();
                    Intent intent;
                    intent = new Intent(me, ErrorActivity.class);
                    intent.putExtra(ERROR_MESSAGE, errorMessage);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }

    public void setupListWithFooter() {
        View footerView = getLayoutInflater().inflate(R.layout.footer_progress, null);
        mProgressBar = (ProgressBar) footerView.findViewById(R.id.pbFooterLoading);
        lvBooks.addFooterView(footerView);
    }

    public void launchBookDetailsActivity(int position) {
        Book book = aBooks.get(position);

        Intent i = new Intent(this, BookDetailActivity.class);
        i.putExtra("book", book);
        startActivity(i);
    }

    void showProgress() {
        Toast.makeText(getApplicationContext(), "Starting progress", Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    void hideProgress() {
        Toast.makeText(getApplicationContext(), "Stopping progress", Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

}
