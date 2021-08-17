package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;


    ArrayList<TodoItem> items;

    Button btnAdd;
    ImageButton btnDueDate;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    String currentItemDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnDueDate = findViewById(R.id.btnDueDate);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        // Setting the default item due date
        setDefaultItemDueDate();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete item from the model
                items.remove(position);
                // Notify adapter on which position was deleted
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                // Create new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // Pass the data being edited
                TodoItem itemClicked = items.get(position);
                String itemName = itemClicked.getName();
                i.putExtra(KEY_ITEM_TEXT, itemName);
                i.putExtra(KEY_ITEM_POSITION, position);
                // Display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), currentItemDueDate, Toast.LENGTH_SHORT).show();

                Date newItemDueDate = parseDueDate(currentItemDueDate);
                String newItemName = etItem.getText().toString();
                // Add item to the model
                TodoItem newItem = new TodoItem(newItemName);
                items.add(newItem);
                // Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
                setDefaultItemDueDate();
            }
        });

        btnDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void setDefaultItemDueDate() {
        currentItemDueDate = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);
    }

    private Date parseDueDate(String dateString) {
        int month, day, year;
        month = Integer.parseInt(dateString.split("-")[0]);
        day = Integer.parseInt(dateString.split("-")[1]);
        year = Integer.parseInt(dateString.split("-")[2]);

        Calendar calendar = new GregorianCalendar(year, month, day);
        return calendar.getTime();
    }

    // Handle result of edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve updated text value
            String editedItemName = data.getStringExtra(KEY_ITEM_TEXT);
            // Extract the original position of the edited item from position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            // Update model at the right position with new item text
            TodoItem editedItem = items.get(position);
            editedItem.setName(editedItemName);
            // Notify adapter
            itemsAdapter.notifyItemChanged(position);
            // Persist changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of data.txt
    private void loadItems() {
        try {
            ArrayList<TodoItem> dataFileItems = new ArrayList<>();
            File dataFile = getDataFile();
            Scanner scanner = new Scanner(dataFile);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String itemName = line;
                TodoItem item = new TodoItem(itemName);

                dataFileItems.add(item);
            }
            scanner.close();
            items = dataFileItems;
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // This function saves items by writing into data.txt
    private void saveItems() {
        try {
            File dataFile = getDataFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dataFile));
            for (TodoItem item : items) {
                String itemName = item.getName();

                bufferedWriter.write(itemName);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        currentItemDueDate = (month + 1) + "-" + dayOfMonth + "-" + year;
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}