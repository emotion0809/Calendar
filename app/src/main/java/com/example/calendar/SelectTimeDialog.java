package com.example.calendar;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.calendar.ui.calendar.CalendarFragment;

public class SelectTimeDialog extends DialogFragment {

    final static int row = 7;
    final static int column = 7;
    //編輯物件
    public static Button editBt_time;
    public static GridLayout gl_calendar;
    public static TextView tv_month;
    public static TextView tv_date[] = new TextView[42];
    public static EditText et_hour;
    public static EditText et_minute;
    //日期資料
    final static String[] week = {"日", "一", "二", "三", "四", "五", "六"};
    public static int calendarDate[] = new int[3];
    public static int startDay = 6;
    public static int monthDays[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static int isStart;
    public static View root;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        root = getLayoutInflater().inflate(R.layout.dialog_select_time, null);
        //取得時間
        if (isStart == 0) {
            for (int i = 0; i < 3; i++) {
                calendarDate[i] = EditNoteActivity.time[0][i];
            }
        } else {
            for (int i = 0; i < 3; i++) {
                calendarDate[i] = EditNoteActivity.time[1][i];
            }
        }
        setCalendar();
        et_hour = root.findViewById(R.id.et_hour);
        et_minute = root.findViewById(R.id.et_minute);
        //設定預設時間
        et_hour.setText(String.format("%d",EditNoteActivity.time[isStart][3]));
        et_minute.setText(String.format("%d",EditNoteActivity.time[isStart][4]));
        //時間編輯
        et_hour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (et_hour.getText() == null || !isNumeric(et_hour.getText().toString())) {
                    et_hour.setText("0");
                } else if (Integer.parseInt(et_hour.getText().toString()) > 23) {
                    et_hour.setText("23");
                } else if (Integer.parseInt(et_hour.getText().toString()) < 0) {
                    et_hour.setText("0");
                }
            }
        });
        et_minute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (et_minute.getText() == null || !isNumeric(et_minute.getText().toString())) {
                    et_minute.setText("0");
                } else if (Integer.parseInt(et_minute.getText().toString()) > 59) {
                    et_minute.setText("59");
                } else if (Integer.parseInt(et_minute.getText().toString()) < 0) {
                    et_minute.setText("0");
                }
            }
        });
        //是否顯示時間
        if (EditNoteActivity.isAllDay.matches("Y")) {
            LinearLayout layout_time = (LinearLayout) root.findViewById(R.id.layout_time);
            layout_time.setVisibility(View.INVISIBLE);

        } else {
            LinearLayout layout_time = (LinearLayout) root.findViewById(R.id.layout_time);
            layout_time.setVisibility(View.VISIBLE);
            try {

            }catch (Exception ex){
                System.out.println(ex.toString());
            }
        }
        builder.setView(root)
                .setPositiveButton(R.string.define, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditNoteActivity.time[isStart][0] = calendarDate[0];
                        EditNoteActivity.time[isStart][1] = calendarDate[1];
                        EditNoteActivity.time[isStart][2] = calendarDate[2];
                        if (EditNoteActivity.isAllDay.matches("N")) {
                            EditNoteActivity.time[isStart][3] = Integer.parseInt(et_hour.getText().toString());
                            EditNoteActivity.time[isStart][4] = Integer.parseInt(et_minute.getText().toString());
                            if (isStart == 0) {
                                EditNoteActivity.time[1][0] = EditNoteActivity.time[0][0];
                                EditNoteActivity.time[1][1] = EditNoteActivity.time[0][1];
                                EditNoteActivity.time[1][2] = EditNoteActivity.time[0][2];
                                EditNoteActivity.time[1][3] = EditNoteActivity.time[0][3] + 1;
                                //結束時間比起始時間晚1小時
                                if (EditNoteActivity.time[1][3] > 23) {
                                    EditNoteActivity.time[1][3] = 0;
                                    EditNoteActivity.time[1][2] += 1;
                                    if(EditNoteActivity.time[1][2] > monthDays[EditNoteActivity.time[1][1] - 1]){
                                        EditNoteActivity.time[1][2] = 1;
                                        EditNoteActivity.time[1][1] += 1;
                                        if(EditNoteActivity.time[1][1] > 12){
                                            EditNoteActivity.time[1][1] = 1;
                                            EditNoteActivity.time[1][0] += 1;
                                        }
                                    }
                                }
                                EditNoteActivity.time[1][4] = EditNoteActivity.time[0][4];
                            }
                        }
                        EditNoteActivity.setTvTime();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SelectTimeDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public static void setCalendar() {
        gl_calendar = (GridLayout) root.findViewById(R.id.gl_calendar);
        gl_calendar.removeAllViews();
        gl_calendar.setColumnCount(column);
        gl_calendar.setRowCount(row);
        tv_month = root.findViewById(R.id.tv_month);
        tv_month.setText(String.format("%d年%d月", calendarDate[0], calendarDate[1]));
        //星期
        for (int c = 0, r = 0; c < column; c++) {
            TextView tv_week = new TextView(new ContextThemeWrapper(root.getContext(), R.style.calendar));
            tv_week.setText(week[c]);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.columnSpec = GridLayout.spec(c, 1f);
            param.rowSpec = GridLayout.spec(r);
            tv_week.setLayoutParams(param);
            gl_calendar.addView(tv_week);
        }
        //月份
        for (int r = 1; r < row; r++) {
            for (int c = 0; c < column; c++) {
                int n = (r - 1) * column + c;
                tv_date[n] = new TextView(new ContextThemeWrapper(root.getContext(), R.style.smallCalendar));
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                param.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                param.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                tv_date[n].setLayoutParams(param);
                gl_calendar.addView(tv_date[n]);
            }
        }
        for (int d = 0; d < monthDays[calendarDate[1] - 1]; d++) {
            tv_date[d + startDay].setText(String.format("%d", d + 1));
            tv_date[d + startDay].setOnClickListener(v -> {
                TextView tv_date = (TextView) v;
                for (int r1 = 0; r1 < 6; r1++) {
                    for (int c1 = 0; c1 < 7; c1++) {
                        SelectTimeDialog.tv_date[r1 * 7 + c1].setBackground(null);
                    }
                }
                tv_date.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.bg_highlight));
                SelectTimeDialog.calendarDate[2] = Integer.parseInt(tv_date.getText().toString());
            });
            //標記選擇日期
            if (MainActivity.dateTime[0] == calendarDate[0] && MainActivity.dateTime[1] == calendarDate[1] && MainActivity.dateTime[2] == d + 1) {
                tv_date[d + startDay]
                        .setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.bg_today));
            }
        }
    }

    private boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }
}