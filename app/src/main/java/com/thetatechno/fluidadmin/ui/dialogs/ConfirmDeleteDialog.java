package com.thetatechno.fluidadmin.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnConfirmDeleteListener;

public class ConfirmDeleteDialog extends Dialog {
    OnConfirmDeleteListener listener;
    Context context;
    TextView okButton;
    TextView cancelButton;
    Object object;
    public ConfirmDeleteDialog(@NonNull Context context,Object object) {
        super(context);
        this.object = object;
        if (context instanceof OnConfirmDeleteListener) {
            listener = (OnConfirmDeleteListener) context;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_delete_view);
        Window window = getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        okButton = findViewById(R.id.okBtn);
        cancelButton = findViewById(R.id.cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              listener.onOkButtonClicked(object);
            }
        });
    }
}
