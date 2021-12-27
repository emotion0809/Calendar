package com.example.calendar;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import java.security.PublicKey;

public class CalendarConfirgureDialog extends DialogFragment {
    public static Dialog colorDl;
    public static int selected_color;
    private static final String DataBaseName = "Calendar";
    private static final int DataBaseVersion = 1;
    private static final String DataBaseTable = "Remind";
    private static SQLiteDatabase db;
    private static SqlDataBaseHelper sqlDataBaseHelper;
    public static boolean moding_Database = false;
    public static int id_modifier;
    public static int[] colorBackground = {R.drawable.remind_blue, R.drawable.remind_red};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        try {
            //colorDl = this.getDialog();
            View root = getLayoutInflater().inflate(R.layout.dialog_cal_config, null);
            ImageView iv = getActivity().findViewById(R.id.image_color);

            sqlDataBaseHelper = new SqlDataBaseHelper(root.getContext(), DataBaseName, null, DataBaseVersion, DataBaseTable);
            db = sqlDataBaseHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(String.format(
                    "SELECT * FROM %s " +
                            "ORDER BY startYear,startMonth,startDate,startHour,startMinute ",
                    DataBaseTable), null);
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                LinearLayout layout_list = (LinearLayout) root.findViewById(R.id.linear_config);
                //第一層Layout
                LinearLayout layout1 = new LinearLayout(root.getContext());
                layout1.setOrientation(LinearLayout.HORIZONTAL);
                layout1.setBackground(ContextCompat.getDrawable(root.getContext(), colorBackground[cursor.getInt(3)]));
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = GridLayout.LayoutParams.MATCH_PARENT;
                param.setMargins(150, 20, 150, 20);
                layout1.setLayoutParams(param);
                //title
                TextView text_title = new TextView(new ContextThemeWrapper(root.getContext(), R.style.CalendarConfigureTitle));
                text_title.setText(cursor.getString(1));
                text_title.setWidth(550);
                text_title.setId(cursor.getInt(0));
                //Time
                TextView text_time = new TextView(new ContextThemeWrapper(root.getContext(), R.style.CalendarConfigureTime));

                if (cursor.getString(4).matches("Y")) {
                    text_time.setText("全天");
                } else {
                    text_time.setText(String.format("%2d:%2d", cursor.getInt(8), cursor.getInt(9)));
                }
                text_title.setOnClickListener(v -> {
                    id_modifier = text_title.getId();
                    moding_Database = true;
                    Intent intent = new Intent();
                    intent.setClass(root.getContext(), NewRemindActivity.class);
                    startActivity(intent);
                });
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
                            // sign in the user ...

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            CalendarConfirgureDialog.this.getDialog().cancel();
                        }
                    });
        } catch (Exception ex) {
            Toast.makeText(getContext(), String.format("%s", ex), Toast.LENGTH_SHORT).show();
        }

        return builder.create();
    }
}
