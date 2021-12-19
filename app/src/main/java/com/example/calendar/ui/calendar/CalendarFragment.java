package com.example.calendar.ui.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.calendar.R;
import com.example.calendar.databinding.FragmentCalendarBinding;

import org.w3c.dom.Text;

import java.util.Objects;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        GridLayout gridLayout = (GridLayout) root.findViewById(R.id.calendar_grid);
        gridLayout.removeAllViews();

        String[] weekdate = {"日", "一", "二", "三", "四", "五", "六"};
        int[] month_days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int startdate = 3;
        int printdate = 1;

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
                    oImageView = new TextView(new ContextThemeWrapper(getContext(), R.style.calendarWeek));
                    oImageView.setText(weekdate[c]);
                    param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    //param.setGravity(Gravity.CENTER_HORIZONTAL);
                    param.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                    param.rowSpec = GridLayout.spec(r);
                    oImageView.setLayoutParams(param);
                    gridLayout.addView(oImageView);
                } else {
                    LinearLayout layout_date = new LinearLayout(getContext());
                    oImageView = new TextView(new ContextThemeWrapper(getContext(), R.style.calendarDate));

                    if (r == 1 && c >= startdate) {
                        oImageView.setText(String.format("%s", printdate));
                        printdate++;
                    } else if (r > 1 && printdate <= month_days[11]) {
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


        return root;


    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }


}