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
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.viewpager.widget.PagerAdapter;

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
    public static void init(View root) {
        int[] ymd = {0, 0, 0};
        TextView mt = root.findViewById(R.id.month_title);
        GridLayout gridLayout = (GridLayout) root.findViewById(R.id.calendar_grid);
        gridLayout.removeAllViews();

        String[] weekdate = {"日", "一", "二", "三", "四", "五", "六"};
        String[] monthName = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

        LocalDateTime curTime = LocalDateTime.now();
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy", Locale.TAIWAN);
        ymd[0] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("MM", Locale.TAIWAN);
        ymd[1] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("dd", Locale.TAIWAN);
        ymd[2] = Integer.parseInt(formmat1.format(curTime));
        int printdate = 1;
        mt.setTextSize(30.0f);
        mt.setText(String.format("%s", year) + "," + monthName[startmonth - 1] + "月");

        try {
            int total = 49;
            int column = 7;
            int row = total / column;
            gridLayout.setColumnCount(column);
            gridLayout.setRowCount(row + 1);

            for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
                if (c == column) {
                    c = 0;
                    r++;
                }
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                TextView oImageView;

                if (r == 0) {
                    oImageView = new TextView(new ContextThemeWrapper(root.getContext(), R.style.calendar));
                    oImageView.setText(weekdate[c]);
                    param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    //param.setGravity(Gravity.CENTER_HORIZONTAL);
                    param.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                    param.rowSpec = GridLayout.spec(r);
                    oImageView.setLayoutParams(param);
                    gridLayout.addView(oImageView);
                } else {
                    LinearLayout layout_date = new LinearLayout(root.getContext());
                    oImageView = new TextView(new ContextThemeWrapper(root.getContext(), R.style.calendarDate));
                    if (printdate == ymd[2] && year == ymd[0] && startmonth == ymd[1]) {
                        ///////當天日期HIGHLIGHT
                        layout_date.setBackgroundColor(Color.parseColor("#97CBFF"));

                    }

                    if (r == 1 && c >= startdate - 1) {
                        oImageView.setText(String.format("%s", printdate));
                        printdate++;
                    } else if (r > 1 && printdate <= month_days[startmonth - 1]) {
                        oImageView.setText(String.format("%s", printdate));
                        printdate++;
                    } else {
                        oImageView.setText("");
                    }

                    param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    param.width = GridLayout.LayoutParams.MATCH_PARENT;
                    //oImageView.setLayoutParams(param);

                    layout_date.addView(oImageView);

                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                    layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    //layout_date.setBackgroundColor(500034);
                    //param.setGravity(Gravity.CENTER_HORIZONTAL);

                    layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                    layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                    layout_date.setOrientation(LinearLayout.VERTICAL);
                    layout_date.setLayoutParams(layoutParams);
                    gridLayout.addView(layout_date);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
            //Toast.makeText(this,ex.toString(),Toast.LENGTH_LONG).show();
        }


    }
}