package com.example.calendar.ui.calendar;

import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.calendar.MainActivity;
import com.example.calendar.R;
import com.example.calendar.databinding.FragmentCalendarBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    public static int yearsTillLeapYear;
    public static int startdate = 4;
    public static int startmonth = 12;
    public static int year = 2021;
    public static int[] month_days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static View froot;
    public static int[] ymd = {0, 0, 0};

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
    public static void setDate(){
        LocalDateTime curTime = LocalDateTime.now();
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy", Locale.TAIWAN);
        ymd[0] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("MM", Locale.TAIWAN);
        ymd[1] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("dd", Locale.TAIWAN);
        ymd[2] = Integer.parseInt(formmat1.format(curTime));
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
                    printdate++;
                } else {
                    text_date.setText("");
                }
                layout_date.addView(text_date);
                if (ymd[0] == year && ymd[1] == startmonth && ymd[2] == printdate -1) {
                    layout_date.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.broder_today));
                }else{
                    layout_date.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.broder));
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
    }
}