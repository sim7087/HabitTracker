package com.newsapp071997.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.newsapp071997.habittracker.habitdata.HabitContract.HabitEntry;
import com.newsapp071997.habittracker.habitdata.HabitDbHelper;

public class MainActivity extends AppCompatActivity {
    private EditText mHabit;
    private Spinner mWeekday;
    private int weekDayCode;
    Button tableDataButton;
    private TextView mDisplay;
    private HabitDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWeekday = findViewById(R.id.spinner_weekday);
        mHabit = findViewById(R.id.habit);
        tableDataButton = findViewById(R.id.button);
        mDisplay = findViewById(R.id.displayTable);
        tableDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDisplay.setText(" ");
                read();
            }
        });
        dbHelper = new HabitDbHelper(this);
        setUpSpinner();
    }

    private void setUpSpinner() {
        ArrayAdapter weekAdapter = ArrayAdapter.createFromResource(this, R.array.weekDay, android.R.layout.simple_spinner_item);
        weekAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mWeekday.setAdapter(weekAdapter);
        mWeekday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.sunday))) {
                        weekDayCode = HabitEntry.WEEK_DAY_SUNDAY; // Male
                    } else if (selection.equals(getString(R.string.monday))) {
                        weekDayCode = HabitEntry.WEEK_DAY_MONDAY; // Female
                    } else if (selection.equals(getString(R.string.tuesday))) {
                        weekDayCode = HabitEntry.WEEK_DAY_TUESDAY; // Unknown
                    } else if (selection.equals(getString(R.string.wednesday))) {
                        weekDayCode = HabitEntry.WEEK_DAY_WEDNESDAY; // Unknown
                    } else if (selection.equals(getString(R.string.thursday))) {
                        weekDayCode = HabitEntry.WEEK_DAY_THURSDAY; // Unknown
                    } else if (selection.equals(getString(R.string.friday))) {
                        weekDayCode = HabitEntry.WEEK_DAY_FRIDAY; // Unknown
                    } else if (selection.equals(getString(R.string.saturday))) {
                        weekDayCode = HabitEntry.WEEK_DAY_SATURDAY; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                weekDayCode = 0; // Unknown
            }
        });
    }

    private void insert() {
        String habitDescString = mHabit.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_DESC, habitDescString);
        values.put(HabitEntry.COLUMN_WEEK_DAY, weekDayCode);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);
        if (newRowId == -1)
            Toast.makeText(this, "error in saving data", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "habit saved", Toast.LENGTH_SHORT).show();
    }

    private void read() {
        HabitDbHelper dbHelp = new HabitDbHelper(this);
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        String[] projection = {HabitEntry._ID, HabitEntry.COLUMN_WEEK_DAY, HabitEntry.COLUMN_HABIT_DESC};
        Cursor cursor = db.query(HabitEntry.TABLE_NAME, projection, null, null, null, null, null);
        try {
            mDisplay.append(HabitEntry._ID + " - " +
                    HabitEntry.COLUMN_WEEK_DAY + " - " +
                    HabitEntry.COLUMN_HABIT_DESC);
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int weekDayColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_WEEK_DAY);
            int habitDescColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_DESC);
            while (cursor.moveToNext()) {
                int idValue = cursor.getInt(idColumnIndex);
                String weekDayValue = cursor.getString(weekDayColumnIndex);
                String habitValue = cursor.getString(habitDescColumnIndex);
                mDisplay.append("\n" + idValue + " - " + weekDayValue + " - " + habitValue);
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.save:
                insert();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}