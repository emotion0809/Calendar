package com.example.calendar;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.gridlayout.widget.GridLayout;

public class SelectTimeDialog extends DialogFragment {
    String[] week = {"日", "一", "二", "三", "四", "五", "六"};
    final int row = 7;
    final int column = 7;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_select_time, null))
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
        Dialog dialog = builder.create();
        //日期選擇
        try {
            GridLayout gridLayout = (GridLayout) dialog.findViewById(R.id.grid_smallCalendar);
            System.out.println(gridLayout);
            gridLayout.removeAllViews();
            gridLayout.setColumnCount(column);
            gridLayout.setRowCount(row + 1);

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
                    }
                }
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
        //時間選擇
        return dialog;
    }

    public void OnClick_SelectTime(View view){

    }
}
