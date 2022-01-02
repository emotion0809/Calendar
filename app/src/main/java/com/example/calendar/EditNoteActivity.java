package com.example.calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

public class EditNoteActivity extends AppCompatActivity {
    //DataBase
    private static SQLiteDatabase db;
    private DataBase sqlDataBaseHelper;
    //要放入資料庫的資料
    public static String title;
    public static String type;
    public static int color;
    public static String isAllDay = "Y";
    public static int time[][] = new int[2][5];
    //判斷為新增或更新
    public static Boolean isUpdateNote = false;
    public static int updateNoteId;
    //編輯物件
    private EditText et_title;
    private ImageView iv_color;
    private Switch sw_allDay;
    public static Button bt_startTime;
    public static Button bt_endTime;
    private Spinner sp_type;
    private TextView tv_startTime;
    private TextView tv_endTime;
    private TextView tv_color;

    public static LinearLayout lastColor;

    public static int[] colorCircle = {
            R.color.note_blue,
            R.color.note_red,
            R.color.note_green,
            R.color.note_yellow,
            R.color.note_magenta,
            R.color.note_cyan,
            R.color.note_purple,
            R.color.note_orange};

    public static String[] colorString = {
            "藍色","紅色","綠色","黃色","洋紅色","青色","紫色","橘色"
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        //開啟資料庫
        sqlDataBaseHelper = new DataBase(this, "Calendar", null, 1, "Note");
        db = sqlDataBaseHelper.getWritableDatabase();
        //新增返回建
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //取得時間
        MainActivity.getDate();
        //設置物件
        et_title = this.findViewById(R.id.et_Title);
        iv_color = this.findViewById(R.id.iv_color);
        sw_allDay = this.findViewById(R.id.sw_allDay);
        bt_startTime = this.findViewById(R.id.bt_startTime);
        bt_endTime = this.findViewById(R.id.bt_endTime);
        sp_type = this.findViewById(R.id.sp_type);
        tv_startTime = this.findViewById(R.id.tv_startTime);
        tv_endTime = this.findViewById(R.id.tv_endTime);
        tv_color = this.findViewById(R.id.tv_endTime);
        //放入資料
        if (isUpdateNote) {
            String sql = String.format(
                    "SELECT * FROM Note " +
                            "Where id = %d",
                    updateNoteId);
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            actionBar.setTitle("更新記事");
            title = cursor.getString(1);
            type = cursor.getString(2);
            color = cursor.getInt(3);
            isAllDay = cursor.getString(4);
            for (int i = 0; i < time.length; i++) {
                for (int j = 0; j < time[i].length; j++) {
                    time[i][j] = cursor.getInt(i * time[i].length + j + 5);
                }
            }
            et_title.setText(title);
            if (type.matches("Y")) {
                sw_allDay.setChecked(true);
                setTvTime();
            } else {
                sw_allDay.setChecked(false);
                setTvTime();
            }
            iv_color.setColorFilter(colorCircle[color]);
            tv_color.setText(colorString[color]);
        } else {
            actionBar.setTitle("新增記事");
            type = "工作";
            color = 0;
            sw_allDay.setChecked(true);
            isAllDay = "Y";
            for (int i = 0; i < time.length; i++) {
                for (int j = 0; j < time[i].length; j++) {
                    if (i != 1 || j != 3) {
                        time[i][j] = MainActivity.dateTime[j];
                    } else {
                        time[i][j] = MainActivity.dateTime[j] + 1;
                        if (time[i][j] >= 24) {
                            time[i][j] = 0;
                        }
                    }

                }
            }
            setTvTime();
        }
        //switch建觸發(AllDay)
        sw_allDay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                isAllDay = "Y";
                setTvTime();
            } else {
                isAllDay = "N";
                setTvTime();
            }
        });
        //sp_type功能設置
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_type.setAdapter(adapter);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (type = sp_type.getSelectedItem().toString()) {
                    case "工作":
                        tv_startTime.setText("截止時間");
                        tv_endTime.setVisibility(View.INVISIBLE);
                        bt_endTime.setVisibility(View.INVISIBLE);
                        break;
                    case "活動":
                        tv_startTime.setText("開始時間");
                        tv_endTime.setText("結束時間");
                        tv_endTime.setVisibility(View.VISIBLE);
                        bt_endTime.setVisibility(View.VISIBLE);
                        MainActivity.getDate();
                        break;
                    case "提醒":
                        tv_startTime.setText("提醒時間");
                        tv_endTime.setVisibility(View.INVISIBLE);
                        bt_endTime.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplication(), "NothingSelected", Toast.LENGTH_SHORT).show();
            }
        });
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (SelectNoteDialog.moding_Database) {
            getMenuInflater().inflate(R.menu.menu_mod_data, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //返回建觸發
        switch (item.getItemId()) {
            case android.R.id.home:
                SelectNoteDialog.moding_Database = false;
                finish();
                break;
            case R.id.button_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("你確定要刪除這筆資料嗎?");
                builder.setPositiveButton("確定", (dialog, id) -> {
                    db.delete("Note", "id=" + String.format("%s", SelectNoteDialog.id_modifier), null);
                    Intent intent = new Intent();
                    intent.setClass(EditNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                });
                builder.setNegativeButton("取消", (dialogInterface, id) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick_selectTime(View view) {
        switch (view.getId()){
            case R.id.bt_startTime:
                SelectTimeDialog.isStart = 0;
                break;
            case R.id.bt_endTime:
                SelectTimeDialog.isStart = 1;
                break;
        }
        DialogFragment newFragment = new SelectTimeDialog();
        newFragment.show(getSupportFragmentManager(), "selectTime");
    }

    public void onClick_monthChange(View view) {
        switch (view.getId()) {
            case R.id.bt_leftArrow:
                if (SelectTimeDialog.calendarDate[1] != 1) {
                    SelectTimeDialog.calendarDate[1]--;
                } else {
                    SelectTimeDialog.calendarDate[0]--;
                    SelectTimeDialog.monthDays[1] = MainActivity.isLeapYear(SelectTimeDialog.calendarDate[0]);
                    SelectTimeDialog.calendarDate[1] = 12;
                }
                SelectTimeDialog.startDay -= SelectTimeDialog.monthDays[SelectTimeDialog.calendarDate[1] - 1] % 7;
                if (SelectTimeDialog.startDay < 0) {
                    SelectTimeDialog.startDay += 7;
                }
                break;
            case R.id.bt_rightArrow:
                SelectTimeDialog.startDay += SelectTimeDialog.monthDays[SelectTimeDialog.calendarDate[1] - 1] % 7;
                if (SelectTimeDialog.startDay > 6) {
                    SelectTimeDialog.startDay -= 7;
                }
                if (SelectTimeDialog.calendarDate[1] != 12) {
                    SelectTimeDialog.calendarDate[1]++;
                    SelectTimeDialog.monthDays[1] = MainActivity.isLeapYear(SelectTimeDialog.calendarDate[0]);
                } else {
                    SelectTimeDialog.calendarDate[0]++;
                    SelectTimeDialog.calendarDate[1] = 1;
                }
                break;
        }
        SelectTimeDialog.setCalendar();
    }

    public void onClick_selectColor(View view) {
        DialogFragment newFragment = new SelectColorDialog();
        newFragment.show(getSupportFragmentManager(), "selectColor");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick_dialogColor(View v) {
        if (lastColor != null) {
            lastColor.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_color));
        }
        switch (v.getId()) {
            case R.id.linear_blue:
                SelectColorDialog.selected_color = R.color.note_blue;
                color = 0;
                break;
            case R.id.linear_red:
                SelectColorDialog.selected_color = R.color.note_red;
                color = 1;
                break;
            case R.id.linear_green:
                SelectColorDialog.selected_color = R.color.note_green;
                color = 2;
                break;
            case R.id.linear_yellow:
                SelectColorDialog.selected_color = R.color.note_yellow;
                color = 3;
                break;
            case R.id.linear_magenta:
                SelectColorDialog.selected_color = R.color.note_magenta;
                color = 4;
                break;
            case R.id.linear_cyan:
                SelectColorDialog.selected_color = R.color.note_cyan;
                color = 5;
                break;
            case R.id.linear_purple:
                SelectColorDialog.selected_color = R.color.note_purple;
                color = 6;
                break;
            case R.id.linear_orange:
                SelectColorDialog.selected_color = R.color.note_orange;
                color = 7;
                break;
        }
        v.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_color_highlight));
        lastColor = (LinearLayout) v;
    }

    public void onClick_save(View view) {
        //取得Title
        EditText editText_title = (EditText) findViewById(R.id.et_Title);
        title = editText_title.getText().toString();
        System.out.println(editText_title.getText().toString());
        if (!title.matches("")) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", title);
            contentValues.put("type", type);
            contentValues.put("color", color);
            contentValues.put("isAllDay", isAllDay);
            contentValues.put("startYear", time[0][0]);
            contentValues.put("startMonth", time[0][1]);
            contentValues.put("startDate", time[0][2]);
            contentValues.put("startHour", time[0][3]);
            contentValues.put("startMinute", time[0][4]);
            contentValues.put("endYear", time[1][0]);
            contentValues.put("endMonth", time[1][1]);
            contentValues.put("endDate", time[1][2]);
            contentValues.put("endHour", time[1][3]);
            contentValues.put("endMinute", time[1][4]);
            if (isUpdateNote) {
                /////更新資料庫
                db.update("Note", contentValues, "id=" + String.format("%s", SelectNoteDialog.id_modifier), null);
            } else {
                /////新增資料庫
                db.insert("Note", null, contentValues);
            }
            //回到主頁面
            Intent intent = new Intent();
            intent.setClass(EditNoteActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "標題不能空白", Toast.LENGTH_SHORT).show();
        }
    }

    public static void setTvTime() {
        if (isAllDay.matches("Y")) {
            bt_startTime.setText(String.format("%d年%d月%d日", time[0][0], time[0][1], time[0][2]));
            bt_endTime.setText(String.format("%d年%d月%d日", time[1][0], time[1][1], time[1][2]));
        } else {
            bt_startTime.setText(String.format("%d年%d月%d日　　%s:%s", time[0][0], time[0][1], time[0][2], timeFormatter(time[0][3]), timeFormatter(time[0][4])));
            bt_endTime.setText(String.format("%d年%d月%d日　　%s:%s", time[1][0], time[1][1], time[1][2], timeFormatter(time[1][3]), timeFormatter(time[1][4])));
        }
    }

    public static String timeFormatter(int n) {
        String formated = String.format("%d",n);
        if (n < 10) {
            formated = "0" + formated;
        }
        return formated;
    }
}