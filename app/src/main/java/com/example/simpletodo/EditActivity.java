package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText editItem;
    Button btnSave;
    ImageButton editItemDate;

    private String newItemDueDate, currentItemDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editItem = findViewById(R.id.editItem);
        btnSave = findViewById(R.id.btnSave);
        editItemDate = findViewById(R.id.editItemDate);

        getSupportActionBar().setTitle("Edit item");

        editItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        currentItemDueDate = getIntent().getStringExtra(MainActivity.KEY_ITEM_DATE_STRING);
        newItemDueDate = currentItemDueDate;

        // When the user is done editing, they click the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // Create intent which will contain the results
               Intent intent = new Intent();
               // Pass the data (result of editing)
               intent.putExtra(MainActivity.KEY_ITEM_TEXT, editItem.getText().toString());
               intent.putExtra(MainActivity.KEY_ITEM_DATE_STRING, newItemDueDate);
               intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
               // Set the result of the intent
               setResult(RESULT_OK, intent);
               // Finish activity, close the screen and go back
               finish();
           }
        });

        editItemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Integer.parseInt(currentItemDueDate.split("-")[2]),
                Integer.parseInt(currentItemDueDate.split("-")[0]) - 1,
                Integer.parseInt(currentItemDueDate.split("-")[1])
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        newItemDueDate = (month + 1) + "-" + dayOfMonth + "-" + year;
    }
}