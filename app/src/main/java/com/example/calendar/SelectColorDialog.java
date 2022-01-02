package com.example.calendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.security.PublicKey;

public class SelectColorDialog extends DialogFragment {
    public static int selected_color;
    private ImageView iv_color;
    private TextView tv_color;
    public static String[] colorString = {
            "藍色","紅色","綠色","黃色","洋紅色","青色","紫色","橘色"
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View root = getLayoutInflater().inflate(R.layout.dialog_select_color, null);
        iv_color = getActivity().findViewById(R.id.iv_color);
        tv_color = getActivity().findViewById(R.id.tv_color);
        builder.setView(root)
                .setPositiveButton(R.string.define, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        iv_color.setColorFilter(getContext().getColor(selected_color));
                        tv_color.setText(colorString[EditNoteActivity.color]);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SelectColorDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
