package com.wasitech.wall.pop;

import android.content.Context;
import android.transition.Fade;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.wasitech.wall.R;

public class ItemsLongClickPopUp extends ThemePopUp {
    protected ImageButton share, cancel, delete, action, detail;
    private LinearLayout layout;

    /**
     * Default five actions<br>
     * share, cancel, delete, action, detail;
     */
    public ItemsLongClickPopUp(Context context) {
        super(context, R.layout.pop_lay_long_click, new Fade(Fade.IN), new Fade(Fade.OUT));
        init(context);
        extra();
    }

    /**
     * @param context contxt of activity where this pop up should visible
     * @param actions list of drawables for actions and there ids will be 1 2 3 ... according to resource
     * @implNote provide custom action list
     */
    public ItemsLongClickPopUp(Context context, int[] actions) {
        super(context, R.layout.pop_lay_long_click, new Fade(Fade.IN), new Fade(Fade.OUT));
        layout = getContentView().findViewById(R.id._pop_bg);
        init(context, actions);
        extra();
    }

    private void init(Context context) {
        share = getContentView().findViewById(R.id.share_btn);
        cancel = getContentView().findViewById(R.id.cancel_btn);
        delete = getContentView().findViewById(R.id.delete_btn);
        action = getContentView().findViewById(R.id.action_btn);
        detail = getContentView().findViewById(R.id.detail_btn);
        action.setImageDrawable(ContextCompat.getDrawable(context, setActionImg()));

        share.setOnClickListener(v -> OnShare());
        cancel.setOnClickListener(v -> OnCancel());
        delete.setOnClickListener(v -> OnDelete());
        action.setOnClickListener(v -> OnAction());
        detail.setOnClickListener(v -> OnDetail());
        
    }

    private void init(Context context, int[] actions) {
        for (int i = 0; i < actions.length; i++) {
            ImageButton btn = getButton(context, actions[i], i + 1);
            layout.addView(btn);
        }
    }

    private ImageButton getButton(Context context, int res, int id) {
        ImageButton btn = new ImageButton(context);
        btn.setImageResource(res);
        btn.setBackground(null);
        btn.setId(id);
        btn.setOnClickListener(v -> buttonClicked(id));
        btn.setOnLongClickListener(v -> buttonLongClicked(id));
        return btn;
    }

    public void buttonClicked(int id) {
    }

    public boolean buttonLongClicked(int id) {
        return false;
    }

    protected void OnShare() {
        dismiss();
    }

    protected void OnCancel() {
        dismiss();
    }

    protected void OnDelete() {
        dismiss();
    }

    protected void OnAction() {
        dismiss();
    }

    protected void OnDetail() {
        dismiss();
    }

    protected int setActionImg() {
        return 0;
    }
    protected void extra(){}
}
