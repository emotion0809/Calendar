package com.example.calendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.security.PublicKey;

public class SelectColorDialog extends DialogFragment {
    public static Dialog colorDl;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        colorDl = this.getDialog();
        builder.setView(inflater.inflate(R.layout.dialog_select_color, null))
                // Add action buttons
                .setPositiveButton(R.string.define, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(),"用戶已取消", Toast.LENGTH_LONG).show();
                        SelectColorDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
