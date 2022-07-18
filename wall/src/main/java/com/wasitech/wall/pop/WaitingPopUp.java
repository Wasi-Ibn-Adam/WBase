package com.wasitech.wall.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wasitech.wall.R;
import com.wasitech.wall.classes.Issue;
import com.wasitech.wall.theme.Theme;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class WaitingPopUp extends ThemePopUp {

    private CircleImageView bgColor;
    private TextView text;


    public WaitingPopUp(Context context) {
        super(context, R.layout.pop_lay_waiting);
        init(context);
    }

    public WaitingPopUp(Context context, boolean show) {
        super(context, R.layout.pop_lay_waiting);
        init(context);
        if (show) {
            showCenter();
        } else {
            dismiss();
        }
    }

    public WaitingPopUp(Context context, String txt) {
        super(context, R.layout.pop_lay_waiting);
        init(context);
        text.setText(txt);
    }

    private void init(Context context) {
        ImageView gif = getContentView().findViewById(R.id.videoView);
        bgColor = getContentView().findViewById(R.id.loading_bg_color);
        text= getContentView().findViewById(R.id.loading_txt);

        Glide.with(context).asGif().load(gif()).into(gif);
        Glide.with(context).load(new ColorDrawable(Color.LTGRAY)).into(bgColor);

        setOnDismissListener(this::onClose);
        setFocusable(false);
        new Handler(context.getMainLooper()).post(this::runner);
        setView();
    }

    public int GIF() {
        return 0;
    }

    private int gif() {
        if (GIF() == 0) return R.drawable.loading_3;
        if (GIF() == 1) return R.drawable.loading;
        return GIF();
    }

    private void setView() {
        try {
            if (GIF() == 0 || GIF() == 1) {
                Theme.textView(text);
            } else {
                text.setVisibility(View.INVISIBLE);
                bgColor.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            Issue.print(e, WaitingPopUp.class.getName());
        }

    }

    protected abstract void runner();

    public abstract void onClose();

}
