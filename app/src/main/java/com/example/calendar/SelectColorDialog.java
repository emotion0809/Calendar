package com.example.calendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.security.PublicKey;

public class SelectColorDialog extends DialogFragment {
    public static Dialog colorDl;
    public static int selected_color;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        try {
            //colorDl = this.getDialog();
            View root = getLayoutInflater().inflate(R.layout.dialog_select_color, null);
            ImageView iv = getActivity().findViewById(R.id.image_color);

            builder.setView(root)
                    // Add action buttons
                    .setPositiveButton(R.string.define, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                            Toast.makeText(getContext(), String.format("%s", selected_color), Toast.LENGTH_SHORT).show();
                            iv.setColorFilter(getContext().getColor(selected_color));
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getContext(), "用戶已取消", Toast.LENGTH_LONG).show();
                            SelectColorDialog.this.getDialog().cancel();
                        }
                    });
        } catch (Exception ex) {
            Toast.makeText(getContext(), String.format("%s", ex), Toast.LENGTH_SHORT).show();
        }

        return builder.create();
    }
}
