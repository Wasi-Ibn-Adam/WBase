package com.wasitech.wall.classes;

import android.content.Context;
import android.os.Environment;

import com.wasitech.wall.app.BaseApp;

import java.io.File;
import java.util.Date;

public class Storage {
    public static final String DOWN = Environment.DIRECTORY_DOWNLOADS;
    public static final String REC = Environment.DIRECTORY_MUSIC;
    public static final String IMG = Environment.DIRECTORY_DCIM;
    public static final String VID = Environment.DIRECTORY_MOVIES;
    public static final String APP = Environment.DIRECTORY_DOCUMENTS;
    public static final String DB = Environment.DIRECTORY_DOCUMENTS;


    public static File getFolder(String root, String folder) {
        File dir = Environment.getExternalStoragePublicDirectory(root);
        File file = new File(dir, folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    private static String fileName() {
        return Utils.Time.time(new Date().getTime());
    }


    public static File CreateBaseFile(Context context) {
        return CreateBaseFile(context, "_");
    }


    public static File CreateBaseFile(Context context, String type) {
        File dir = context.getDir("Database", Context.MODE_PRIVATE);
        File db = new File(dir, type + "_" + fileName() + ".db");
        if (db.exists())
            db.delete();
        return db;
    }


    public static File CreateDataFile(String dir, String folder, String postfix) {
        return CreateDataFile(dir, folder, fileName(), postfix);
    }


    public static File CreateDataFile(String dir, String folder, String fileName, String postfix) {
        File directory = getFolder(dir, folder);
        File file = new File(directory, fileName + postfix);
        if (file.exists()) {
            file.delete();
        }
        return file;
    }


    public static File GetDataFile(String dir, String folder, String fileName, String postfix) {
        File directory = getFolder(dir, folder);
        File file = new File(directory, fileName + postfix);
        if (file.exists()) {
            return file;
        }
        return null;
    }


    public static File CreateDataEncrypt(String folder, String fileName) {
        File directory = getFolder(DB, folder);
        return new File(directory, fileName + ".assist");
    }


    public static File CreateDataDecrypt(Context context, String name) {
        File dir = context.getDir("Database", Context.MODE_PRIVATE);
        return new File(dir, name + ".txt");
    }

    public static boolean clearCache(Context context) {
        try {
            File dir = context.getApplicationContext().getCacheDir();
            Storage.deleteDir(dir);
        } catch (Exception e) {
            Issue.print(e, Storage.class.getName());
        }
        return false;
    }

    public static boolean deleteDir(File dir) {
        try {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                if (children != null) {
                    for (String child : children) {
                        if (!deleteDir(new File(dir, child))) {
                            return false;
                        }
                    }
                }
                return dir.delete();
            } else if (dir != null && dir.isFile()) {
                return dir.delete();
            } else {
                return false;
            }
        } catch (Exception e) {
            Issue.print(e, BaseApp.class.getName());
            return false;
        }
    }

}
