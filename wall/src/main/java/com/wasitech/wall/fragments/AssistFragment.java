package com.wasitech.wall.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wasitech.wall.classes.Issue;
import com.wasitech.wall.interfaces.AssistFragInterface;
import com.wasitech.wall.interfaces.Events;


public abstract class AssistFragment extends Fragment implements AssistFragInterface {
    protected Events task;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        task = (Events) context;
    }

    protected int setLayout() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setLayout(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            setViews(view);
        } catch (Exception e) {
            Issue.Log(e, "setViews(view)");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = requireView();
        try {
            setTheme(view);
        } catch (Exception e) {
            Issue.Log(e, "setTheme(view)");
        }
        try {
            setPermission(view);
        } catch (Exception e) {
            Issue.Log(e, "setPermission(view)");
        }
        try {
            setAnimation(view);
        } catch (Exception e) {
            Issue.Log(e, "setAnimation(view)");
        }
        try {
            setValues(view);
        } catch (Exception e) {
            Issue.Log(e, "setValues(view)");
        }
        try {
            setFixed();
        } catch (Exception e) {
            Issue.Log(e, "setFixed()");
        }
        try {
            setActions(view);
        } catch (Exception e) {
            Issue.Log(e, "setActions(view)");
        }
        try {
            setExtra(view);
        } catch (Exception e) {
            Issue.Log(e, "setExtra(view)");
        }
    }

    protected void setFixed() {
    }

}
