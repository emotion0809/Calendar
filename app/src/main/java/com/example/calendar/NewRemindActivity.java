package com.example.calendar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendar.ui.calendar.CalendarFragment;
import com.facebook.stetho.Stetho;

import java.time.LocalDateTime;

public class NewRemindActivity extends AppCompatActivity {

    //DataBase
    private static final String DataBaseName = "Calendar";
    private static final int DataBaseVersion = 1;
    private static final String DataBaseTable = "Remind";
    private static SQLiteDatabase db;
    private SqlDataBaseHelper sqlDataBaseHelper;
    //Remind DataBase
    public static String title;
    public static String type;
    public static int color;
    public static String isAllDay = "Y";
    public static int Time[][] = new int[2][5];

    public static View last_click;
    public static LinearLayout lscl;

    public static int[] ui_circle = {R.color.remind_blue, R.color.remind_red};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (CalendarConfirgureDialog.moding_Database) {
            getMenuInflater().inflate(R.menu.menu_mod_data, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //返回建觸發
        switch (item.getItemId()) {
            case android.R.id.home:
                CalendarConfirgureDialog.moding_Database = false;
                finish();
                break;
            case R.id.button_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("你確定要刪除這筆資料嗎?");
                builder.setPositiveButton("確定", (dialog, id) -> {
                    sqlDataBaseHelper = new SqlDataBaseHelper(this, DataBaseName, null, DataBaseVersion, DataBaseTable);
                    db = sqlDataBaseHelper.getWritableDatabase();
                    db.delete(DataBaseTable, "id=" + String.format("%s", CalendarConfirgureDialog.id_modifier), null);
                    Intent intent = new Intent();
                    intent.setClass(NewRemindActivity.this, MainActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_remind);

        //新增返回建
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //初始化時間設定!!!!!!!!!!!!!!!!! ----這個程式碼由Dvlpsk_feqma提供 PLUS+++
        CalendarFragment.setDate();
        if (CalendarConfirgureDialog.moding_Database) {
            //把資料庫的東西放這裡!!!
            sqlDataBaseHelper = new SqlDataBaseHelper(this, DataBaseName, null, DataBaseVersion, DataBaseTable);
            db = sqlDataBaseHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(String.format(
                    "SELECT * FROM %s " +
                            "ORDER BY startYear,startMonth,startDate,startHour,startMinute ",
                    DataBaseTable), null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (CalendarConfirgureDialog.id_modifier == cursor.getInt(0)) {
                    actionBar.setTitle("修改" + cursor.getString(1) + "的資料");
                    EditText et = this.findViewById(R.id.editText_Title);
                    et.setText(cursor.getString(1));
                    SelectTimeDialog.s_year = Integer.parseInt(cursor.getString(5));
                    SelectTimeDialog.s_month = Integer.parseInt(cursor.getString(6));
                    SelectTimeDialog.s_date = Integer.parseInt(cursor.getString(7));
                    SelectTimeDialog.s_hour = Integer.parseInt(cursor.getString(8));
                    SelectTimeDialog.s_minute = Integer.parseInt(cursor.getString(9));
                    SelectColorDialog.selected_color = ui_circle[cursor.getInt(3)];
                    Toast.makeText(this, String.format("%s", SelectColorDialog.selected_color), Toast.LENGTH_SHORT).show();
                    ImageView imageView = this.findViewById(R.id.image_color);
                    imageView.setColorFilter(this.getColor(SelectColorDialog.selected_color));
                }
                cursor.moveToNext();
            }

            /*SelectTimeDialog.e_date = CalendarFragment.ymd[2];
            SelectTimeDialog.e_month = CalendarFragment.ymd[1];
            SelectTimeDialog.e_year = CalendarFragment.ymd[0];

            SelectTimeDialog.e_hour = CalendarFragment.ymd[3];
            SelectTimeDialog.e_minute = CalendarFragment.ymd[4];*/
        } else {
            actionBar.setTitle("新增記事");
            //使用預設資料(新增記事)
            SelectTimeDialog.s_date = CalendarFragment.ymd[2];
            SelectTimeDialog.s_month = CalendarFragment.ymd[1];
            SelectTimeDialog.s_year = CalendarFragment.ymd[0];
            SelectTimeDialog.e_date = CalendarFragment.ymd[2];
            SelectTimeDialog.e_month = CalendarFragment.ymd[1];
            SelectTimeDialog.e_year = CalendarFragment.ymd[0];
            SelectTimeDialog.s_hour = CalendarFragment.ymd[3];
            SelectTimeDialog.s_minute = CalendarFragment.ymd[4];
            SelectTimeDialog.e_hour = CalendarFragment.ymd[3];
            SelectTimeDialog.e_minute = CalendarFragment.ymd[4];

        }
        if (SelectTimeDialog.s_minute + 30 > 59) {
            SelectTimeDialog.e_minute -= 30;
            SelectTimeDialog.e_hour++;
        } else {
            SelectTimeDialog.e_minute += 30;
            //SelectTimeDialog.e_hour = SelectTimeDialog.s_hour;
        }
        //switch建觸發(AllDay)
        Switch switch_allDay = (Switch) findViewById(R.id.switch_allDay);
        switch_allDay.setChecked(true);
        switch_allDay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                isAllDay = "Y";
                Button button_startTime = (Button) findViewById(R.id.button_startTime);
                Button button_endTime = (Button) findViewById(R.id.button_endTime);
                button_startTime.setText(SelectTimeDialog.selected_time);
                button_endTime.setText(SelectTimeDialog.selected_time);
            } else {
                isAllDay = "N";
                Button button_startTime = (Button) findViewById(R.id.button_startTime);
                Button button_endTime = (Button) findViewById(R.id.button_endTime);
                button_startTime.setText(SelectTimeDialog.selected_time + SelectTimeDialog.selected_hm);
                button_endTime.setText(SelectTimeDialog.selected_time + timeFormatter(SelectTimeDialog.e_hour, SelectTimeDialog.e_minute));
                //////////////尚未製作跨日轉換

            }
        });

        try {
            Spinner sp = this.findViewById(R.id.spinner_type);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            sp.setAdapter(adapter);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code here
                    TextView tv = findViewById(R.id.text_startTime);
                    TextView ta = findViewById(R.id.text_endTime);
                    Button btn = findViewById(R.id.button_endTime);
                    Button Sbtn = findViewById(R.id.button_startTime);

                    CalendarFragment.setDate();
                    SelectTimeDialog.selected_time = String.format("%s", SelectTimeDialog.s_year) + "年" + String.format("%s", SelectTimeDialog.s_month) + "月" + String.format("%s", SelectTimeDialog.s_date) + "日      ";
                    SelectTimeDialog.selected_hm = timeFormatter(SelectTimeDialog.s_hour, SelectTimeDialog.s_minute);
                    if (isAllDay.matches("Y")) {
                        Sbtn.setText(SelectTimeDialog.selected_time);
                    } else {
                        Sbtn.setText(SelectTimeDialog.selected_time + SelectTimeDialog.selected_hm);
                    }
                    switch (type = sp.getSelectedItem().toString()) {
                        case "工作":
                            //????
                            SelectTimeDialog.notRequireCheck = true;
                            tv.setText("截止時間");
                            ta.setVisibility(View.INVISIBLE);
                            btn.setClickable(false);
                            btn.setVisibility(View.INVISIBLE);
                            break;
                        case "活動":
                            //????
                            SelectTimeDialog.notRequireCheck = false;
                            tv.setText("開始時間");
                            ta.setVisibility(View.VISIBLE);
                            ta.setText("結束時間");
                            btn.setClickable(true);
                            btn.setVisibility(View.VISIBLE);
                            CalendarFragment.setDate();
                            if (isAllDay.matches("Y")) {
                                btn.setText(SelectTimeDialog.selected_time);
                            } else {
                                if (CalendarFragment.ymd[4] + 30 >= 60) {
                                    btn.setText(SelectTimeDialog.selected_time + timeFormatter(CalendarFragment.ymd[3] + 1, CalendarFragment.ymd[4] - 30));
                                    //////////////尚未製作跨日轉換
                                } else {
                                    btn.setText(SelectTimeDialog.selected_time + timeFormatter(CalendarFragment.ymd[3], CalendarFragment.ymd[4] + 30));
                                }
                            }
                            break;
                        case "提醒":
                            //????
                            SelectTimeDialog.notRequireCheck = true;
                            tv.setText("提醒時間");
                            ta.setVisibility(View.INVISIBLE);
                            btn.setClickable(false);
                            btn.setVisibility(View.INVISIBLE);
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

        Stetho.initializeWithDefaults(this);
    }

    public void onClick_selectTime(View view) {
        DialogFragment newFragment = new SelectTimeDialog();
        newFragment.show(getSupportFragmentManager(), "selectTime");
        last_click = view;
    }

    public void onClick_selectColor(View view) {
        DialogFragment newFragment = new SelectColorDialog();
        newFragment.show(getSupportFragmentManager(), "selectColor");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick_dialogColor(View v) {
        if (lscl != null) {
            lscl.setBackground(ContextCompat.getDrawable(this, R.drawable.border_color));
        }
        switch (v.getId()) {
            case R.id.linear_blue:
                SelectColorDialog.selected_color = R.color.remind_blue;
                color = 0;
                break;
            case R.id.linear_red:
                SelectColorDialog.selected_color = R.color.remind_red;
                color = 1;
                break;
            case R.id.linear_yellow:
                SelectColorDialog.selected_color = R.color.remind_yellow;
                color = 2;
                break;
            case R.id.linear_green:
                SelectColorDialog.selected_color = R.color.remind_green;
                color = 3;
                break;
            case R.id.linear_cyan:
                SelectColorDialog.selected_color = R.color.remind_cyan;
                color = 4;
                break;
            case R.id.linear_magenta:
                SelectColorDialog.selected_color = R.color.remind_magenta;
                color = 5;
                break;
            case R.id.linear_orange:
                SelectColorDialog.selected_color = R.color.remind_orange;
                color = 6;
                break;
            case R.id.linear_purple:
                SelectColorDialog.selected_color = R.color.remind_purple;
                color = 7;
                break;
        }
        v.setBackground(ContextCompat.getDrawable(this, R.drawable.border_color_highlight));
        lscl = (LinearLayout) v;
        // SelectColorDialog.colorDl.cancel();
    }

    public void onClick_dialogAro(View view) {
        try {
            switch (view.getId()) {
                case R.id.dialog_la:
                    if (SelectTimeDialog.month != 1) {
                        SelectTimeDialog.month--;
                    } else {
                        SelectTimeDialog.year--;
                        SelectTimeDialog.month = 12;
                    }
                    if (MainActivity.isLeapYear(SelectTimeDialog.year) == 29 && SelectTimeDialog.month - 1 == 1) {
                        SelectTimeDialog.startdate -= 29 % 7;
                    } else {
                        SelectTimeDialog.startdate -= CalendarFragment.month_days[SelectTimeDialog.month - 1] % 7;
                    }

                    if (SelectTimeDialog.startdate < 1) {
                        SelectTimeDialog.startdate += 7;
                    }
                    SelectTimeDialog.recount();
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

    public static String timeFormatter(int hour, int minute) {
        String formated = String.format("%s", hour) + ":";
        if (hour < 10) {
            formated = "0" + formated;
        }
        if (minute < 10) {
            formated = formated + "0";
        }
        formated = formated + String.format("%s", minute);
        return formated;
    }

    public void onClick_save(View view) {
        //開啟資料庫
        sqlDataBaseHelper = new SqlDataBaseHelper(this, DataBaseName, null, DataBaseVersion, DataBaseTable);
        db = sqlDataBaseHelper.getWritableDatabase();
        //取得Title
        EditText editText_title = (EditText) findViewById(R.id.editText_Title);
        title = editText_title.getText().toString();
        System.out.println(editText_title.getText().toString());
        //取得時間
        Time[0][0] = SelectTimeDialog.s_year;
        Time[0][1] = SelectTimeDialog.s_month;
        Time[0][2] = SelectTimeDialog.s_date;
        Time[0][3] = SelectTimeDialog.s_hour;
        Time[0][4] = SelectTimeDialog.s_minute;
        Time[1][0] = SelectTimeDialog.e_year;
        Time[1][1] = SelectTimeDialog.e_month;
        Time[1][2] = SelectTimeDialog.e_date;
        Time[1][3] = SelectTimeDialog.e_hour;
        Time[1][4] = SelectTimeDialog.e_minute;
        if (!title.matches("")) {
            //insert
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", title);
            contentValues.put("type", type);
            contentValues.put("color", color);
            contentValues.put("isAllDay", isAllDay);
            contentValues.put("startYear", Time[0][0]);
            contentValues.put("startMonth", Time[0][1]);
            contentValues.put("startDate", Time[0][2]);
            contentValues.put("startHour", Time[0][3]);
            contentValues.put("startMinute", Time[0][4]);
            contentValues.put("endYear", Time[1][0]);
            contentValues.put("endMonth", Time[1][1]);
            contentValues.put("endDate", Time[1][2]);
            contentValues.put("endHour", Time[1][3]);
            contentValues.put("endMinute", Time[1][4]);
            //回到主頁面
            if (CalendarConfirgureDialog.moding_Database) {
                /////更新資料庫
                db.update(DataBaseTable, contentValues, "id=" + String.format("%s", CalendarConfirgureDialog.id_modifier), null);
            } else {
                /////新增資料庫
                db.insert(DataBaseTable, null, contentValues);
            }

            Intent intent = new Intent();
            intent.setClass(NewRemindActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "標題不能空白", Toast.LENGTH_SHORT).show();
        }
    }

}