package com.wasitech.wall.classes;

import com.android.volley.VolleyError;
import com.wasitech.wpermissions.classes.Utils;

import java.util.Arrays;

public class Issue {
    public static void print(Exception e, String c) {
        e.printStackTrace();
    }

    public static void Internet(VolleyError volleyError) {
        Utils.Log("volley error");
        volleyError.printStackTrace();
    }

    public static void Log(Exception e, String function) {
        Utils.Log("<<---------------------------------------------------->>");
        Utils.Log("BASIC-FUN-ERROR->> " + function);
        Utils.Log(Arrays.toString(e.getStackTrace()));
        e.printStackTrace();
        Utils.Log("<<--!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!->>");
    }
}
