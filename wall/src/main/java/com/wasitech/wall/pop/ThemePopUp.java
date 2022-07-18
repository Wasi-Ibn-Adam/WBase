package com.wasitech.wall.pop;

import android.content.Context;
import android.graphics.RegionIterator;
import android.graphics.drawable.ColorDrawable;
import android.transition.Transition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.wasitech.wall.R;
import com.wasitech.wall.classes.Issue;

public abstract class ThemePopUp extends PopupWindow {

    public ThemePopUp(Context context, int res) {
        super(context);
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(res, null);
        setFocusable(true);
        setAnimationStyle(android.R.anim.cycle_interpolator);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public ThemePopUp(Context context, int res, Transition in, Transition out) {
        super(context);
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(res, null);
        setFocusable(true);
        setEnterTransition(in);
        setExitTransition(out);
        setAnimationStyle(android.R.anim.cycle_interpolator);
        setContentView(view);
        setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void showCenter() {
        try {
            showAtLocation(getContentView(), Gravity.CENTER, 0, 0);
        }
        catch (Exception e){
            Issue.print(e, WaitingPopUp.class.getName());
        }
    }

    /**
     * @implNote if background id is set '_pop_bg'  it will work else throw an Exception
     * @param res Drawable res
     * @see #setBackground(int,int)
     * */
    public void setBackground(int res){
        getContentView().findViewById(R.id._pop_bg).setBackgroundResource(res);
    }
    /**
     * @param id id of top root view (Layout)
     * @param res Drawable res
     * @see #setBackground(int)
     * */
    public void setBackground(int id,int res){
        getContentView().findViewById(id).setBackgroundResource(res);
    }
}
