package com.example.calendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.calendar.ui.calendar.CalendarFragment;

public class SelectNoteDialog extends DialogFragment {
    //資料庫
    private static SQLiteDatabase db;
    private static DataBase sqlDataBaseHelper;
    public static int[] colorBackground = {
            R.drawable.note_blue,
            R.drawable.note_red,
            R.drawable.note_green,
            R.drawable.note_yellow,
            R.drawable.note_magenta,
            R.drawable.note_cyan,
            R.drawable.note_purple,
            R.drawable.note_orange};
    //資料庫資料
    private int id;
    private String title;
    private int color;
    private String isAllDay;
    private int startHour;
    private int startMinute;

    public static int date;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //colorDl = this.getDialog();
        View root = getLayoutInflater().inflate(R.layout.dialog_cal_config, null);
        ImageView iv = getActivity().findViewById(R.id.iv_color);

        sqlDataBaseHelper = new DataBase(root.getContext(), "Calendar", null, 1, "Note");
        db = sqlDataBaseHelper.getWritableDatabase();
        String sql = String.format(
                "SELECT id,title,color,isAllDay,startHour,startMinute FROM Note " +
                        "where startYear = %d And startMonth = %d And startDate = %d " +
                        "ORDER BY isAllDay,startHour,startMinute "
                , CalendarFragment.calendarDate[0], CalendarFragment.calendarDate[1], date
        );
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            id = cursor.getInt(0);
            title = cursor.getString(1);
            color = cursor.getInt(2);
            isAllDay = cursor.getString(3);
            startHour = cursor.getInt(4);
            startMinute = cursor.getInt(5);
            LinearLayout layout_list = (LinearLayout) root.findViewById(R.id.linear_config);
            //第一層Layout
            LinearLayout layout1 = new LinearLayout(root.getContext());
            layout1.setOrientation(LinearLayout.HORIZONTAL);
            layout1.setBackground(ContextCompat.getDrawable(root.getContext(), colorBackground[color]));
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.MATCH_PARENT;
            param.setMargins(150, 20, 150, 20);
            layout1.setLayoutParams(param);
            layout1.setId(id);
            layout1.setOnClickListener(v -> {
                EditNoteActivity.updateNoteId = layout1.getId();
                EditNoteActivity.isUpdateNote = true;
                Intent intent = new Intent();
                intent.setClass(root.getContext(), EditNoteActivity.class);
                startActivity(intent);
            });
            //title
            TextView text_title = new TextView(new ContextThemeWrapper(root.getContext(), R.style.CalendarConfigureTitle));
            text_title.setText(title);
            text_title.setWidth(550);
            //Time
            TextView text_time = new TextView(new ContextThemeWrapper(root.getContext(), R.style.CalendarConfigureTime));
            text_title.setWidth(550);
            if (isAllDay.matches("Y")) {
                text_time.setText("全天");
            } else {
                text_time.setText(String.format("%s:%s", EditNoteActivity.timeFormatter(startHour), EditNoteActivity.timeFormatter(startMinute)));
            }
            //addView
            layout1.addView(text_title);
            layout1.addView(text_time);
            layout_list.addView(layout1);
            cursor.moveToNext();
        }

        builder.setView(root)
                // Add action buttons
                .setPositiveButton(R.string.append, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditNoteActivity.isUpdateNote = false;
                        EditNoteActivity.isEnterbyDialog = true;
                        Intent intent = new Intent();
                        intent.setClass(root.getContext(), EditNoteActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SelectNoteDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }
}
