package com.ignatyev.vasyl.booksearch.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.ignatyev.vasyl.booksearch.R;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        String errorMessage = getIntent().getExtras().getString(BookListActivity.ERROR_MESSAGE);
        TextView errorText = (TextView) findViewById(R.id.tvError);
        errorText.setText(errorMessage);
    }
}
