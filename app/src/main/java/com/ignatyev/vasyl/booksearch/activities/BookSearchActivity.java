package com.ignatyev.vasyl.booksearch.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ignatyev.vasyl.booksearch.R;

public class BookSearchActivity extends AppCompatActivity {
    public static final String SEARCH_STRING = "com.ignatyev.vasyl.booksearch.EXTRA_SEARCH_STRING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        final Button searchBtn = (Button) findViewById(R.id.doSearch);
        searchBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etSearch = (EditText) findViewById(R.id.etSearch);
                final String query = etSearch.getText().toString();
                openSearchActivity(query);
            }
        }));

    }

    public void openSearchActivity(String query) {
        Intent intent = new Intent(this, BookListActivity.class);
        intent.putExtra(SEARCH_STRING, query);
        startActivity(intent);
    }
}
