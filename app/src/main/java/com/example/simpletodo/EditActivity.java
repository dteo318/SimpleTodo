package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editItem;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editItem = findViewById(R.id.editItem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit item");

        editItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // When the user is done editing, they click the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // Create intent which will contain the results
               Intent intent = new Intent();
               // Pass the data (result of editing)
               intent.putExtra(MainActivity.KEY_ITEM_TEXT, editItem.getText().toString());
               intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
               // Set the result of the intent
               setResult(RESULT_OK, intent);
               // Finish activity, close the screen and go back
               finish();
           }
        });
    }
}