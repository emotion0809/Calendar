package com.example.calendar.ui.work;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.calendar.R;
import com.example.calendar.DataBase;
import com.example.calendar.databinding.FragmentWorkBinding;

public class WorkFragment extends Fragment {

    private WorkViewModel workViewModel;
    private FragmentWorkBinding binding;
    //DataBase
    private static final String DataBaseName = "Calendar";
    private static final int DataBaseVersion = 1;
    private static final String DataBaseTable = "Remind";
    private static SQLiteDatabase db;
    private static DataBase sqlDataBaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workViewModel =
                new ViewModelProvider(this).get(WorkViewModel.class);

        binding = FragmentWorkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(),
                R.array.typeAndAll_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        init(root);
        return root;
    }

    public static void init(View root){
        try {
            sqlDataBaseHelper = new DataBase(root.getContext(), DataBaseName, null, DataBaseVersion, DataBaseTable);
            db = sqlDataBaseHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(String.format(
                    "SELECT * FROM %s " +
                    "ORDER BY startYear,startMonth,startDate,startHour,startMinute ",
                    DataBaseTable), null);
            cursor.moveToFirst();
            for(int i = 0;i < cursor.getCount();i++){
                LinearLayout layout_list = (LinearLayout) root.findViewById(R.id.layout_list);
                //第一層Layout設置
                LinearLayout layout1 = new LinearLayout(root.getContext());
                layout1.setOrientation(LinearLayout.HORIZONTAL);
                layout1.setBackground(ContextCompat.getDrawable(root.getContext(), R.drawable.remind_blue));
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = GridLayout.LayoutParams.MATCH_PARENT;
                param.setMargins(10,10,10,10);
                layout1.setLayoutParams(param);
                //標題
                TextView text_title = new TextView(root.getContext());
                text_title.setText(cursor.getString(1));
                layout1.addView(text_title);
                layout_list.addView(layout1);
                /*LinearLayout layout1 = new LinearLayout(root.getContext());
                layout1.setOrientation(LinearLayout.HORIZONTAL);
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = GridLayout.LayoutParams.MATCH_PARENT;
                layout1.setLayoutParams(param);
                layout_list.addView(layout1);*/
                cursor.moveToNext();

            }
        }catch (Exception ex){
            System.out.println(ex.toString());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}