package com.example.calendar;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.calendar.ui.calendar.CalendarFragment;

public class SelectTimeDialog extends DialogFragment {
    String[] week = {"日", "一", "二", "三", "四", "五", "六"};
    final int row = 7;
    final int column = 7;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();


        View root = getLayoutInflater().inflate(R.layout.dialog_select_time, null);
        //日期選擇
        try {
            GridLayout gridLayout = (GridLayout) root.findViewById(R.id.grid_smallCalendar);
            //Toast.makeText(getContext(), String.format("%s",gridLayout), Toast.LENGTH_SHORT).show();
            gridLayout.removeAllViews();
            gridLayout.setColumnCount(column);
            gridLayout.setRowCount(row + 1);
            int printdate = 1;
            TextView smtitle = root.findViewById(R.id.text_smallTitle);
            smtitle.setText(String.format("%s", CalendarFragment.year)+"年"+String.format("%s", CalendarFragment.startmonth)+"月");
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < column; c++) {
                    GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                    if (r == 0) {
                        TextView oImageView = new TextView(new ContextThemeWrapper(getContext(), R.style.calendar));
                        oImageView.setText(week[c]);
                        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                        param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                        param.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                        param.rowSpec = GridLayout.spec(r);
                        oImageView.setLayoutParams(param);
                        gridLayout.addView(oImageView);
                    } else {
                        TextView oImageView = new TextView(new ContextThemeWrapper(getContext(), R.style.calendarDate));
                        if (r == 1 && c >= CalendarFragment.startdate - 1) {
                            oImageView.setText(String.format("%s", printdate));
                            printdate++;
                        } else if (r > 1 && printdate <= CalendarFragment.month_days[CalendarFragment.startmonth - 1]) {
                            oImageView.setText(String.format("%s", printdate));
                            printdate++;
                        } else {
                            oImageView.setText("");
                        }
                        param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                        param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                        param.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                        param.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
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
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SelectTimeDialog.this.getDialog().cancel();
                        }
                    });
        } catch (Exception ex) {
            Toast.makeText(getContext(), String.format("%s", ex), Toast.LENGTH_SHORT).show();
        }
        //時間選擇
        return builder.create();
    }

    public void OnClick_SelectTime(View view) {

    }
}
