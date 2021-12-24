package com.example.calendar;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.calendar.ui.calendar.CalendarFragment;

public class SelectTimeDialog extends DialogFragment {
    String[] week = {"日", "一", "二", "三", "四", "五", "六"};
    final int row = 7;
    final int column = 7;
    public static int month;
    public static int year;
    public static int startdate;
    public static boolean isLeap;
    public static TextView[] dialogTVs = new TextView[42];
    public static View sroot;
    public static String selected_time;
    public static int s_year;
    public static int s_month;
    public static int s_date;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        month = CalendarFragment.startmonth;
        year = CalendarFragment.year;
        View root = getLayoutInflater().inflate(R.layout.dialog_select_time, null);
        sroot = root;
        //日期選擇
        try {
            GridLayout gridLayout = (GridLayout) root.findViewById(R.id.grid_smallCalendar);
            //Toast.makeText(getContext(), String.format("%s",gridLayout), Toast.LENGTH_SHORT).show();
            gridLayout.removeAllViews();
            gridLayout.setColumnCount(column);
            gridLayout.setRowCount(row + 1);
            int printdate = 1;
            TextView smtitle = root.findViewById(R.id.text_smallTitle);
            smtitle.setText(String.format("%s", CalendarFragment.year) + "年" + String.format("%s", CalendarFragment.startmonth) + "月");
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < column; c++) {
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                    if (r == 0) {
                        TextView oImageView = new TextView(new ContextThemeWrapper(getContext(), R.style.calendar));
                        oImageView.setText(week[c]);
                        oImageView.setClickable(true);
                        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                        param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                        param.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                        param.rowSpec = GridLayout.spec(r);
                        oImageView.setLayoutParams(param);
                        gridLayout.addView(oImageView);
                    } else {
                        TextView oImageView = new TextView(new ContextThemeWrapper(getContext(), R.style.calendarDate));
                        oImageView.setOnClickListener(v -> {
                            if (oImageView.getText() != "") {
                                for (int r1 = 0; r1 < 6; r1++) {
                                    for (int c1 = 0; c1 < 7; c1++) {
                                        dialogTVs[r1 * 7 + c1].setBackground(null);
                                        dialogTVs[r1 * 7 + c1].setBackgroundColor(Color.parseColor("#FFFFFF"));
                                    }
                                }
                                v.setBackgroundColor(Color.parseColor("#222222"));
                                v.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.dialog_highlight));
                                selected_time = String.format("%s", year) + "年" + String.format("%s", month) + "月" + oImageView.getText() + "日";
                                s_date = Integer.parseInt(oImageView.getText().toString());
                            }
                        });
                        /**/
                        if (r == 1 && c >= CalendarFragment.startdate - 1) {
                            oImageView.setText(String.format("%s", printdate));
                            printdate++;
                        } else if (r > 1 && printdate <= CalendarFragment.month_days[CalendarFragment.startmonth - 1]) {
                            oImageView.setText(String.format("%s", printdate));
                            printdate++;
                        } else {
                            oImageView.setText("");
                        }
                        dialogTVs[(r - 1) * 7 + c] = oImageView;
                        oImageView.setGravity(Gravity.CENTER);
                        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                        param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                        param.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                        param.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                        oImageView.setPadding(0, 30, 0, 30);
                        //oImageView.setTextSize(10F);
                        //oImageView.setGravity(Gravity.CENTER_HORIZONTAL);
                        oImageView.setLayoutParams(param);
                        gridLayout.addView(oImageView);
                    }
                }
            }
            builder.setView(root)
                    // Add action buttons
                    .setPositiveButton(R.string.define, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                            ((TextView) NewRemindActivity.last_click).setText(selected_time);
                            s_year = year;
                            s_month = month;
                            //s_date=startdate;
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getContext(), "用戶已取消", Toast.LENGTH_LONG).show();
                            SelectTimeDialog.this.getDialog().cancel();
                        }
                    });
        } catch (Exception ex) {
            Toast.makeText(getContext(), String.format("%s", ex), Toast.LENGTH_SHORT).show();
        }
        //時間選擇
        return builder.create();
    }

    public static void recount() {
        TextView smtitle = sroot.findViewById(R.id.text_smallTitle);
        smtitle.setText(String.format("%s", year) + "年" + String.format("%s", month) + "月");
        int printdate = 1;
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 7; c++) {
                if (r != 0) {
                    if (r == 1 && c >= startdate - 1) {
                        dialogTVs[(r - 1) * 7 + c].setText(String.format("%s", printdate));
                        printdate++;
                    } else if (r > 1 && printdate <= CalendarFragment.month_days[month - 1]) {
                        dialogTVs[(r - 1) * 7 + c].setText(String.format("%s", printdate));
                        printdate++;
                    } else {
                        dialogTVs[(r - 1) * 7 + c].setText("");
                    }
                    dialogTVs[(r - 1) * 7 + c].setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        }
    }
}
