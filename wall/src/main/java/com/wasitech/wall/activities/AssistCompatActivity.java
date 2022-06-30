package com.wasitech.wall.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.wasitech.wall.R;
import com.wasitech.wall.classes.Issue;
import com.wasitech.wall.interfaces.AssistAcInterface;
import com.wasitech.wall.interfaces.AssistDrawOvers;
import com.wasitech.wall.theme.Theme;
import com.wasitech.wpermissions.WCompatActivity;
import com.wasitech.wpermissions.classes.Utils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public abstract class AssistCompatActivity extends WCompatActivity implements AssistAcInterface, AssistDrawOvers {
    protected final ActivityResultLauncher<Intent> head = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            drawOverDenied();
        } else {
            drawOverAct();
        }
    });

    private final int res;

    public AssistCompatActivity(int res) {
        this.res = res;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (res != 0)
            setContentView(res);
        try {
            setViews();
        } catch (Exception e) {
            Issue.Log(e, "setView");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            setValues();
        } catch (Exception e) {
            Issue.Log(e, "setValues");
        }
        try {
            setExtras();
        } catch (Exception e) {
            Issue.Log(e, "setExtras");
        }
        try {
            setTheme();
        } catch (Exception e) {
            Issue.Log(e, "setTheme");
        }
        try {
            setActions();
        } catch (Exception e) {
            Issue.Log(e, "setActions");
        }
    }

    protected void setToolbar(int id) {
        try {
            androidx.appcompat.widget.Toolbar toolbar = findViewById(id);
            onCreateOptionsMenu(toolbar.getMenu());
            toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        } catch (Exception e) {
            Issue.Log(e, getClass().getName());
        }
    }

    /**
     * @implSpec ImageView or ImageButton id
     * @see #setBack(View)
     */
    protected void setBack(int id) {
        try {
            ImageButton btn = (ImageButton) findViewById(id);
            setBack(btn);
        } catch (Exception e) {
            Issue.Log(e, getClass().getName());
        }
    }

    /**
     * @implSpec ImageView or ImageButton View
     * @see #setBack(int)
     */
    protected void setBack(View view) {
        try {
            ImageButton btn = (ImageButton) view;
            setBack(btn);
        } catch (Exception e) {
            Issue.Log(e, getClass().getName());
        }
    }

    private void setBack(ImageButton btn) {
        try {
            btn.setBackground(null);
            btn.setImageResource(R.drawable.arrow_back);
            btn.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            btn.setOnClickListener(v -> onBackPressed());
        } catch (Exception e) {
            Issue.Log(e, getClass().getName());
        }
    }


    protected Menu setToolBar(int id, int menu, Toolbar.OnMenuItemClickListener listener) {
        try {
            Toolbar bar = findViewById(id);
            bar.inflateMenu(menu);
            bar.setOnMenuItemClickListener(listener);
            return bar.getMenu();
        } catch (Exception e) {
            Issue.print(e, getClass().getName());
        }
        return null;
    }

    private LinearLayout empLay;


    protected void EmptyLay(boolean show) {
        TextPrintedLay(show, "Nothing to Show");
    }

    /**
     * @param show should visible or not
     * @param res  string to be print
     * @implSpec Parent layout must be Constraint and have id "bg"
     */

    protected void TextPrintedLay(boolean show, String res) {
        try {
            if (show) {
                if (empLay != null) {
                    empLay.setVisibility(View.VISIBLE);
                    return;
                }
                empLay = new LinearLayout(this);
                empLay.setPadding(10, 0, 0, 10);
                ((ConstraintLayout) findViewById(R.id.bg)).addView(empLay, MATCH_PARENT, MATCH_PARENT);
                empLay.setGravity(Gravity.CENTER);
                {
                    ImageView view = new ImageView(this);
                    empLay.addView(view);
                    view.setImageResource(R.drawable.list);
                    view.getLayoutParams().height = 150;
                    view.getLayoutParams().width = 150;
                    Theme.imageView(view);
                }
                {
                    TextView view = new TextView(this);
                    empLay.addView(view);
                    view.setText(res);
                    view.setTextSize(36);
                    view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    view.getLayoutParams().width = WRAP_CONTENT;
                    view.getLayoutParams().height = WRAP_CONTENT;
                    view.setTypeface(ResourcesCompat.getFont(this, R.font.allura), Typeface.BOLD);
                    view.setPadding(0, 10, 10, 10);
                    Theme.textView(view);
                }

            } else {
                if (empLay != null) {
                    empLay.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            Issue.print(e, getClass().getName());
        }
    }

    // DRAW OVERS

    @Override
    public void drawOverCheck() {
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            head.launch(Utils.Intents.gotoDrawOver(getPackageName()));
        } else {
            drawOverAct();
        }
    }

    @Override
    public void drawOverAct() {
    }

    @Override
    public void drawOverDenied() {
    }

}
