package com.example.calendar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class LoginDialog extends DialogFragment {
    private EditText et_username;
    private EditText et_password;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View root = getLayoutInflater().inflate(R.layout.dialog_login, null);
        builder.setView(root)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        et_username = root.findViewById(R.id.et_username);
                        et_password = root.findViewById(R.id.et_password);
                        String username = et_username.getText().toString();
                        String password = et_password.getText().toString();
                        if(username.matches("Emotion") && password.matches("123")){
                            Toast.makeText(getActivity(), "登入成功", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), "帳號密碼錯誤", Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


}
