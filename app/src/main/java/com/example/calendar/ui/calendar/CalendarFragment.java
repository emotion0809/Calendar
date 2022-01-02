package com.example.calendar.ui.calendar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.calendar.MainActivity;
import com.example.calendar.R;
import com.example.calendar.DataBase;
import com.example.calendar.SelectColorDialog;
import com.example.calendar.SelectNoteDialog;
import com.example.calendar.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {

    //建構Fragment
    private static LayoutInflater inflater;
    private FragmentCalendarBinding binding;
    //UI紀錄
    public static LinearLayout[] ll_date = new LinearLayout[42];
    public static TextView[] tv_date = new TextView[42];
    public static TextView[][] tv_note = new TextView[42][4];
    //DataBase
    private static SQLiteDatabase db;
    private static DataBase dataBase;
    //記事背景顏色
    public static int[] colorBackground = {
            R.drawable.note_blue,
            R.drawable.note_red,
            R.drawable.note_green,
            R.drawable.note_yellow,
            R.drawable.note_magenta,
            R.drawable.note_cyan,
            R.drawable.note_purple,
            R.drawable.note_orange
    };
    public static int startDay = 6;
    public static int calendarDate[] = new int[3];
    public static int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static View root;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //建構Fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        //開啟資料庫
        dataBase = new DataBase(root.getContext(), "Calendar", null, 1, "Note");
        db = dataBase.getWritableDatabase();
        //取得日期
        MainActivity.getDate();
        calendarDate[0] = MainActivity.dateTime[0];
        calendarDate[1] = MainActivity.dateTime[1];
        calendarDate[2] = MainActivity.dateTime[2];
        init();
        setListener();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void init() {
        final int column = 7;
        final int row = 7;
        final String[] weekdate = {"日", "一", "二", "三", "四", "五", "六"};
        //年份和月份
        TextView text_month = root.findViewById(R.id.tv_month);
        text_month.setTextSize(20);
        text_month.setText(String.format("%d年 %d月", calendarDate[0], calendarDate[1]));
        GridLayout gl_calendar = (GridLayout) root.findViewById(R.id.gl_calendar);
        gl_calendar.removeAllViews();
        gl_calendar.setColumnCount(column);
        gl_calendar.setRowCount(row);
        //星期
        for (int c = 0, r = 0; c < column; c++) {
            TextView tv_week = new TextView(new ContextThemeWrapper(root.getContext(), R.style.calendar));
            tv_week.setText(weekdate[c]);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.columnSpec = GridLayout.spec(c, 1f);
            param.rowSpec = GridLayout.spec(r);
            tv_week.setLayoutParams(param);
            gl_calendar.addView(tv_week);
        }
        //日期
        for (int r = 1; r < row; r++) {
            for (int c = 0; c < column; c++) {
                //格子的編號
                int n = (r - 1) * column + c;
                //將ll_date加入gl_calendar
                ll_date[n] = new LinearLayout(root.getContext());
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                param.columnSpec = GridLayout.spec(c, 1f);
                param.rowSpec = GridLayout.spec(r, 1f);
                ll_date[n].setOrientation(LinearLayout.VERTICAL);
                ll_date[(r - 1) * column + c].setLayoutParams(param);
                ll_date[n].setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.bg_calendar));
                gl_calendar.addView(ll_date[n]);
                //將tv_date加入ll_date
                tv_date[n]
                        = new TextView(new ContextThemeWrapper(root.getContext(), R.style.calendarDate));
                ll_date[n].addView(tv_date[n]);
                //將tv_note加入llm_date
                for (int i = 0; i < 3; i++) {
                    tv_note[n][i]
                            = new TextView(new ContextThemeWrapper(root.getContext(), R.style.calendarNote));
                    GridLayout.LayoutParams param2 = new GridLayout.LayoutParams();
                    param2.setMargins(3, 3, 3, 3);
                    param2.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    param2.width = GridLayout.LayoutParams.MATCH_PARENT;
                    tv_note[n][i].setLayoutParams(param2);
                    tv_note[n][i].setSingleLine(true);
                    tv_note[n][i].setMaxEms(2);
                    ll_date[n].addView(tv_note[n][i]);
                }
            }
        }
        //放入日期
        for (int d = 0; d < monthDays[calendarDate[1] - 1]; d++) {
            tv_date[d + startDay].setText(String.format("%d", d + 1));
            ll_date[d + startDay].setId(d + 1);
            //標記當天
            if (MainActivity.dateTime[0] == calendarDate[0] && MainActivity.dateTime[1] == calendarDate[1] && MainActivity.dateTime[2] == d + 1) {
                ll_date[d + startDay]
                        .setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.bg_today));
            }
        }
        int[] n = new int[31];
        for (int i = 0; i < n.length; i++) {
            n[i] = 0;
        }
        String sql = String.format(
                "SELECT title, color, startDate FROM Note " +
                        "Where startYear = %d AND startMonth = %d " +
                        "ORDER BY startDate",
                calendarDate[0], calendarDate[1]
        );
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            //取得資料
            String title = cursor.getString(0);
            int color = cursor.getInt(1);
            int startDate = cursor.getInt(2);
            if (n[startDate - 1] < 3) {
                tv_note[startDate + startDay - 1][n[startDate - 1]].setText(title);
                tv_note[startDate + startDay - 1][n[startDate - 1]]
                        .setBackground(ContextCompat.getDrawable(root.getContext(), colorBackground[color]));
                n[startDate - 1]++;
            }
            cursor.moveToNext();
        }
    }

    public void setListener() {
        for (int d = 0; d < monthDays[calendarDate[1] - 1]; d++) {
            ll_date[d + startDay].setOnClickListener(v -> {
                SelectNoteDialog.date = v.getId();
                DialogFragment newFragment = new SelectNoteDialog();
                newFragment.show(getParentFragmentManager(), "");
            });
        }
    }
}