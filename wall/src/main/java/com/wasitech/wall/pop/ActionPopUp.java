package com.wasitech.wall.pop;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wasitech.wall.R;

public abstract class ActionPopUp extends ThemePopUp{

    public ActionPopUp(Context context,String name, String txt) {
        super(context, R.layout.pop_lay_action);

        TextView detail = getContentView().findViewById(R.id.action_text);
        Button action = getContentView().findViewById(R.id._action);
        Button cancel = getContentView().findViewById(R.id._cancel);

        detail.setText(txt);
        action.setText(name);
        cancel.setText("Cancel");
        action.setOnClickListener(this::onAction);
        cancel.setOnClickListener(this::onCancel);
    }
    protected abstract void onAction(View v);
    protected abstract void onCancel(View v);

}
