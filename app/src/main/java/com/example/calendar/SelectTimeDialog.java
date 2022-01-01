package com.example.calendar;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
    public static boolean notRequireCheck = true;
    public static int month;
    public static int year;
    public static int startdate = CalendarFragment.startday;
    public static boolean isLeap;
    public static TextView[] dialogTVs = new TextView[42];
    public static View sroot;
    public static String selected_time;
    public static String selected_hm;
    public static int s_year = MainActivity.dateTime[0];
    public static int s_month = MainActivity.dateTime[1];
    public static int s_date = MainActivity.dateTime[2];
    public static int s_minute = MainActivity.dateTime[3];
    public static int s_hour = MainActivity.dateTime[4];
    public static int e_year = MainActivity.dateTime[0];
    public static int e_month = MainActivity.dateTime[1];
    public static int e_date = MainActivity.dateTime[2];
    public static int e_minute;
    public static int e_hour;

    public boolean isStart;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (EditNoteActivity.last_click == getActivity().findViewById(R.id.button_startTime)) {
            isStart = true;
            month = s_month;
            year = s_year;
        } else {
            isStart = false;
            month = e_month;
            year = e_year;
        }
        View root = getLayoutInflater().inflate(R.layout.dialog_select_time, null);
        sroot = root;
        EditText ed_hour = root.findViewById(R.id.editText_hour);
        EditText ed_min = root.findViewById(R.id.editText_minute);
        if (isStart) {
            ed_hour.setText(String.format("%s", s_hour));
            ed_min.setText(String.format("%s", s_minute));
        } else {
            ed_hour.setText(String.format("%s", e_hour));
            ed_min.setText(String.format("%s", e_minute));
        }
        //日期選擇
        try {
            GridLayout gridLayout = (GridLayout) root.findViewById(R.id.grid_smallCalendar);
            //Toast.makeText(getContext(), String.format("%s",gridLayout), Toast.LENGTH_SHORT).show();
            gridLayout.removeAllViews();
            gridLayout.setColumnCount(column);
            gridLayout.setRowCount(row + 1);
            int printdate = 1;
            TextView smtitle = root.findViewById(R.id.text_smallTitle);
            if (isStart) {
                smtitle.setText(String.format("%s", s_year) + "年" + String.format("%s", s_month) + "月");
            } else {
                smtitle.setText(String.format("%s", e_year) + "年" + String.format("%s", e_month) + "月");
            }

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
                                selected_time = String.format("%s", year) + "年" + String.format("%s", month) + "月" + oImageView.getText() + "日      ";
                                if (isStart) {
                                    s_date = Integer.parseInt(oImageView.getText().toString());
                                } else {
                                    e_date = Integer.parseInt(oImageView.getText().toString());
                                }

                            }
                        });
                        ///////////////////////

                        /**/
                        if (r == 1 && c >= startdate - 1) {
                            oImageView.setText(String.format("%s", printdate));
                            printdate++;
                        } else if (r > 1 && printdate <= CalendarFragment.monthDays[CalendarFragment.calendarDate[1] - 1]) {
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

///////////////
                        //給預設日期標記 --DVLPSK
                        if (oImageView.getText().toString().equals(String.format("%s", s_date)) && isStart) {
                            oImageView.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.dialog_highlight));
                        } else if (oImageView.getText().toString().equals(String.format("%s", e_date)) && !isStart) {
                            oImageView.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.dialog_highlight));
                        }

                        gridLayout.addView(oImageView);
                    }
                }
            }
            EditText etgohome = root.findViewById(R.id.editText_hour);
            etgohome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (etgohome.getText() == null || !isNumeric(etgohome.getText().toString())) {
                        etgohome.setText("0");
                    } else if (Integer.parseInt(etgohome.getText().toString()) > 23) {
                        etgohome.setText("23");
                    } else if (Integer.parseInt(etgohome.getText().toString()) < 0) {
                        etgohome.setText("0");
                    }
                }
            });
            EditText etminute = root.findViewById(R.id.editText_minute);
            etminute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (etminute.getText() == null || !isNumeric(etminute.getText().toString())) {
                        etminute.setText("0");
                    } else if (Integer.parseInt(etminute.getText().toString()) > 59) {
                        etminute.setText("59");
                    } else if (Integer.parseInt(etminute.getText().toString()) < 0) {
                        etminute.setText("0");
                    }
                }
            });
            if (EditNoteActivity.isAllDay.matches("Y")) {
                LinearLayout layout_time = (LinearLayout) root.findViewById(R.id.layout_time);
                layout_time.setVisibility(View.INVISIBLE);
            } else {
                LinearLayout layout_time = (LinearLayout) root.findViewById(R.id.layout_time);
                layout_time.setVisibility(View.VISIBLE);
            }
            builder.setView(root)
                    // Add action buttons
                    .setPositiveButton(R.string.define, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //日期檢定
                            boolean startEndCheckPass = false;
                            int[] res_ym = {s_year, s_month};
                            int[] ree_ym = {e_year, e_month};
                            if (!notRequireCheck) {
                                if (isStart) {
                                    s_year = year;
                                    s_month = month;
                                } else {
                                    e_year = year;
                                    e_month = month;
                                }
                                if (e_year == s_year) {
                                    if (e_month == s_month) {
                                        if (e_date == s_date) {
                                            if (e_hour == s_hour) {
                                                if (e_minute > s_minute) {
                                                    startEndCheckPass = true;
                                                } else if (e_minute < s_minute) {
                                                    Toast.makeText(getContext(), "Start time can't be later.", Toast.LENGTH_LONG).show();
                                                }
                                            } else if (e_hour > s_hour) {
                                                startEndCheckPass = true;
                                            } else if (e_hour < s_hour) {
                                                Toast.makeText(getContext(), "Start time can't be later.", Toast.LENGTH_LONG).show();
                                            }
                                        } else if (e_date > s_date) {
                                            startEndCheckPass = true;
                                        } else if (e_date < s_date) {
                                            Toast.makeText(getContext(), "Start time can't be later.", Toast.LENGTH_LONG).show();
                                        }
                                    } else if (e_month > s_month) {
                                        startEndCheckPass = true;
                                    } else if (e_month < s_month) {
                                        Toast.makeText(getContext(), "Start time can't be later.", Toast.LENGTH_LONG).show();
                                    }
                                } else if (e_year > s_year) {
                                    startEndCheckPass = true;
                                } else {
                                    Toast.makeText(getContext(), "Start time can't be later.", Toast.LENGTH_LONG).show();
                                }
                            }

                            // sign in the user ...
                            if (startEndCheckPass || notRequireCheck) {
                                if (isStart) {
                                    s_year = year;
                                    s_month = month;
                                    if (Integer.parseInt(etgohome.getText().toString()) > 23 || Integer.parseInt(etgohome.getText().toString()) < 0) {
                                        Toast.makeText(getContext(), "Fuck!You've insert invalid hour,BAKA!", Toast.LENGTH_LONG).show();
                                    } else {
                                        s_hour = Integer.parseInt(etgohome.getText().toString());
                                    }
                                    if (Integer.parseInt(etminute.getText().toString()) > 59 || Integer.parseInt(etminute.getText().toString()) < 0) {
                                        Toast.makeText(getContext(), "Fuck!You've insert invalid minute,BAKA!", Toast.LENGTH_LONG).show();
                                    } else {
                                        s_minute = Integer.parseInt(etminute.getText().toString());
                                    }
                                    if (EditNoteActivity.isAllDay.matches("Y")) {
                                        selected_hm = "";
                                    } else {
                                        selected_hm = EditNoteActivity.timeFormatter(s_hour, s_minute);
                                    }
                                } else {
                                    e_year = year;
                                    e_month = month;
                                    if (Integer.parseInt(etgohome.getText().toString()) > 23 || Integer.parseInt(etgohome.getText().toString()) < 0) {
                                        Toast.makeText(getContext(), "Fuck!You've insert invalid hour,BAKA!", Toast.LENGTH_LONG).show();
                                    } else {
                                        e_hour = Integer.parseInt(etgohome.getText().toString());
                                    }
                                    if (Integer.parseInt(etminute.getText().toString()) > 59 || Integer.parseInt(etminute.getText().toString()) < 0) {
                                        Toast.makeText(getContext(), "Fuck!You've insert invalid minute,BAKA!", Toast.LENGTH_LONG).show();
                                    } else {
                                        e_minute = Integer.parseInt(etminute.getText().toString());
                                    }
                                    if (EditNoteActivity.isAllDay.matches("Y")) {
                                        selected_hm = "";
                                    } else {
                                        selected_hm = EditNoteActivity.timeFormatter(e_hour, e_minute);
                                    }
                                }
                                ((TextView) EditNoteActivity.last_click).setText(selected_time + selected_hm);
                            } else {
                                if (isStart) {
                                    s_year = res_ym[0];
                                    s_month = res_ym[1];
                                } else {
                                    e_year = ree_ym[0];
                                    e_month = ree_ym[1];
                                }

                            }


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
                    } else if (r > 1 && printdate <= CalendarFragment.monthDays[month - 1]) {
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

    private boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }
}
