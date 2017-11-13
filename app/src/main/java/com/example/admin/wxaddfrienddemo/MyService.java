package com.example.admin.wxaddfrienddemo;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by admin on 2017/11/8.
 */

public class MyService extends AccessibilityService {

    private static Context sInstance = null;

    public static Context getContext(){
        return sInstance;
    }


    public static int cnt = 0;
    public static Object gs_lockObj=new Object();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int type = event.getEventType();
        // 界面是否跳转，利用全局变量
        if (type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){

            synchronized (gs_lockObj){
                cnt ++;
            }
        }

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // 获取到this必须在这个方法里面
        if (sInstance == null){
            sInstance = this;
        }

    }

    @Override
    public void onInterrupt() {
        Log.i("xyz","服务中断");
    }

}
