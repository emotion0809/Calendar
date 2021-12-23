package com.example.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendar.ui.calendar.CalendarFragment;

public class NewRemindActivity extends AppCompatActivity {

    private static final String DataBaseName = "Calendar";
    private static final int DataBaseVersion = 1;
    private static String DataBaseTable = "Remind";
    private static SQLiteDatabase db;
    private SqlDataBaseHelper sqlDataBaseHelper;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //返回建觸發
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_remind);
        //新增返回建
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //switch建觸發(AllDay)
        Switch switch_allDay = (Switch) findViewById(R.id.switch_allDay);
        switch_allDay.setChecked(true);
        switch_allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                } else {
                }
            }
        });
        try {
            Spinner sp = this.findViewById(R.id.spinner_type);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            sp.setAdapter(adapter);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    TextView tv = findViewById(R.id.text_startTime);
                    TextView ta = findViewById(R.id.text_endTime);
                    Button btn = findViewById(R.id.button_endTime);
                    switch (sp.getSelectedItem().toString()) {
                        case "工作":
                            //????
                            tv.setText("截止時間");
                            ta.setText("");
                            btn.setClickable(false);
                            btn.setText("");
                            break;
                        case "活動":
                            //????
                            tv.setText("開始時間");
                            ta.setText("結束時間");
                            btn.setClickable(true);
                            btn.setText("WWW");
                            break;
                        case "提醒":
                            //????
                            tv.setText("提醒時間");
                            ta.setText("");
                            btn.setClickable(false);
                            btn.setText("");
                            break;
                    }
                    //Toast.makeText(getApplicationContext(), String.format("%s", selectedItemView), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                    Toast.makeText(getApplicationContext(), "Fuck plz", Toast.LENGTH_SHORT).show();
                }

            });
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), String.format("%s", ex), Toast.LENGTH_LONG).show();
        }


    }

    public void onClick_selectTime(View view) {
        DialogFragment newFragment = new SelectTimeDialog();
        newFragment.show(getSupportFragmentManager(), "selectTime");
    }

    public void onClick_selectColor(View view) {
        DialogFragment newFragment = new SelectColorDialog();
        newFragment.show(getSupportFragmentManager(), "selectColor");
    }

    public void onClick_dialogAro(View view) {
        try {
            switch (view.getId()) {
                case R.id.dialog_la:
                    break;
                case R.id.dialog_ra:
                    if (MainActivity.isLeapYear(SelectTimeDialog.year) == 29 && SelectTimeDialog.month - 1 == 1) {
                        SelectTimeDialog.startdate += 29 % 7;
                    } else {
                        SelectTimeDialog.startdate += CalendarFragment.month_days[SelectTimeDialog.month - 1] % 7;
                    }

                    if (SelectTimeDialog.startdate > 7) {
                        SelectTimeDialog.startdate -= 7;
                    }
                    if (SelectTimeDialog.month != 12) {
                        SelectTimeDialog.month++;
                    } else {
                        SelectTimeDialog.year++;
                        SelectTimeDialog.month = 1;
                    }
                    SelectTimeDialog.recount();
                    break;

            }
        } catch (Exception ex) {
            Toast.makeText(this, String.format("%s", ex), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick_save(View view){
        sqlDataBaseHelper = new SqlDataBaseHelper(this,DataBaseName,null,DataBaseVersion,DataBaseTable);
        db = sqlDataBaseHelper.getWritableDatabase();
        long id;
        ContentValues contentValues = new ContentValues();
        contentValues.put("title","A");
        id = db.insert(DataBaseTable,null,contentValues);
        Cursor c = db.rawQuery("SELECT * FROM " + DataBaseTable,null);
        String titleArray[] = new String[c.getCount()];
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            titleArray[i] = c.getString(1);
            c.moveToNext();
        }

        Toast.makeText(this,titleArray[0],Toast.LENGTH_LONG).show();
    }

}