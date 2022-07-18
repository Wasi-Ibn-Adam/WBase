package com.wasitech.wall.pop;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wasitech.wall.R;

public class InformationPopUp extends ThemePopUp {
    private final LinearLayout layout;

    public InformationPopUp(Context context, String[] info) {
        super(context, R.layout.pop_lay_info);
        layout = getContentView().findViewById(R.id.text_layout);
        for (String str : info)
            layout.addView(getText(context, str));
        getContentView().findViewById(R.id.positive_button).setOnClickListener(v -> onComplete());
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void onComplete() {
        dismiss();
    }

    private TextView getText(Context context, String text) {
        TextView view = new TextView(context);
        view.setText(text);
        view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return view;
    }

}
