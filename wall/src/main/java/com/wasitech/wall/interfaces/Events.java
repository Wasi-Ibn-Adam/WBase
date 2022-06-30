package com.wasitech.wall.interfaces;

public interface Events {
    void onComplete();
    void onAction(int act);
    void onError(int err);
    void onPermission(int per);
}
