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
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.calendar.R;
import com.example.calendar.Remind;
import com.example.calendar.SqlDataBaseHelper;
import com.example.calendar.databinding.FragmentCalendarBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    public static int yearsTillLeapYear;
    public static int startdate = 4;
    public static int startmonth = 12;
    public static int year = 2021;
    public static int[] month_days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static View froot;
    public static int[] ymd = {0, 0, 0, 0, 0};
    //DataBase
    private static final String DataBaseName = "Calendar";
    private static final int DataBaseVersion = 1;
    private static final String DataBaseTable = "Remind";
    private static SQLiteDatabase db;
    private static SqlDataBaseHelper sqlDataBaseHelper;
    //
    public static LinearLayout[] layout_date = new LinearLayout[31];
    public static int[] colorBaground = {R.drawable.remind_blue,R.drawable.remind_red};

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        froot = root;
        init(root);
        return root;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void setDate() {
        LocalDateTime curTime = LocalDateTime.now();
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy", Locale.TAIWAN);
        ymd[0] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("MM", Locale.TAIWAN);
        ymd[1] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("dd", Locale.TAIWAN);
        ymd[2] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("HH", Locale.TAIWAN);
        ymd[3] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("mm", Locale.TAIWAN);
        ymd[4] = Integer.parseInt(formmat1.format(curTime));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void init(View root) {
        setDate();
        String[] weekdate = {"日", "一", "二", "三", "四", "五", "六"};
        //年份和月份
        TextView text_month = root.findViewById(R.id.text_month);
        text_month.setTextSize(20);
        text_month.setText(String.format("%d年 %d月", year, startmonth));
        final int column = 7;
        final int row = 7;
        GridLayout gridLayout = (GridLayout) root.findViewById(R.id.calendar_grid);
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row);
        //星期
        for (int c = 0, r = 0; c < column; c++) {
            TextView text_week = new TextView(new ContextThemeWrapper(root.getContext(), R.style.calendar));
            text_week.setText(weekdate[c]);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.columnSpec = GridLayout.spec(c, 1f);
            param.rowSpec = GridLayout.spec(r);
            text_week.setLayoutParams(param);
            gridLayout.addView(text_week);
        }
        //日期
        int printdate = 1;
        for (int r = 1; r < row; r++) {
            for (int c = 0; c < column; c++) {
                LinearLayout layout_date = new LinearLayout(root.getContext());
                layout_date.setOrientation(LinearLayout.VERTICAL);
                TextView text_date = new TextView(new ContextThemeWrapper(root.getContext(), R.style.calendarDate));
                if (r == 1 && c >= startdate - 1 || r > 1 && printdate <= month_days[startmonth - 1]) {
                    text_date.setText(String.format("%d", printdate));
                    CalendarFragment.layout_date[printdate - 1] = layout_date;
                    printdate++;
                } else {
                    text_date.setText("");
                }
                layout_date.addView(text_date);
                //標記當天
                if (ymd[0] == year && ymd[1] == startmonth && ymd[2] == printdate - 1) {
                    layout_date.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.border_today));
                } else {
                    layout_date.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.border));
                }
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                param.columnSpec = GridLayout.spec(c, 1f);
                param.rowSpec = GridLayout.spec(r, 1f);
                layout_date.setLayoutParams(param);
                gridLayout.addView(layout_date);
            }
        }
        //提醒
        sqlDataBaseHelper = new SqlDataBaseHelper(froot.getContext(), DataBaseName, null, DataBaseVersion, DataBaseTable);
        db = sqlDataBaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s Where startYear = %d AND startMonth = %d ORDER BY startDate", DataBaseTable, year, startmonth), null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            TextView text_remind = new TextView(root.getContext());
            text_remind.setBackground(ContextCompat.getDrawable(root.getContext(), colorBaground[cursor.getInt(3)]));
            text_remind.setText(cursor.getString(1));
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.setMargins(10,10,10,10);
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            text_remind.setLayoutParams(param);
            CalendarFragment.layout_date[cursor.getInt(7) - 1].addView(text_remind);
            cursor.moveToNext();
        }
    }

}