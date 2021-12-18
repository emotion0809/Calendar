package com.example.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class NewRemindActivity extends AppCompatActivity {
    String year[] = new String[200];

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this, "按下左上角返回鍵", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_remind);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Switch switch_allDay = (Switch) findViewById(R.id.switch_allDay);
        switch_allDay.setChecked(true);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_year);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.array_year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try{
        for (int i = 0; i <= 200; i++) {
            adapter.add(String.format("%d",i + 1900));
            //year[i] = String.format("%d",i + 1900);
        }}catch (Exception ex){
            System.out.println(ex);
        }
        spinner.setAdapter(adapter);
        switch_allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    LinearLayout layout_time = (LinearLayout) findViewById(R.id.layout_time);
                    layout_time.setVisibility(View.INVISIBLE);
                } else {
                }
            }
        });


    }


}