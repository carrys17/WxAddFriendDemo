package com.example.admin.wxaddfrienddemo;

import android.app.AndroidAppHelper;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;


/**
 * Created by admin on 2017/12/5.
 */

public class Module implements IXposedHookLoadPackage {


    public static Context applicationContext = null;

    public static Context getApplicationContext() {
        return applicationContext;
    }

//    ContentResolver resolver;
//    Uri uri = Uri.parse("content://com.example.admin.wxaddfrienddemo.provider");

    MyBroadcastReceiver receiver = null;


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (loadPackageParam.packageName.equals("com.tencent.mm")){
            Log.i("xyz","进来handleLoadPackage方法");

            // 获取到当前进程的上下文
            try{
                Class<?> launcherUI = XposedHelpers.findClass("com.tencent.mm.ui.LauncherUI", loadPackageParam.classLoader);
                XposedHelpers.findAndHookMethod(launcherUI, "onResume", new XC_MethodHook() {
//                Class<?> ContextClass = findClass("android.content.ContextWrapper",loadPackageParam.classLoader);
//                findAndHookMethod(ContextClass, "getApplicationContext", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        applicationContext = (Context) param.thisObject;
//                        Log.i("xyz","Module得到上下文");
                        XposedBridge.log("Module得到上下文");


                        if (receiver == null){
                            IntentFilter filter = new IntentFilter("com.example.admin.wxaddfrienddemo.broadcast");
                            receiver  = new MyBroadcastReceiver(applicationContext);
                            AndroidAppHelper.currentApplication().getApplicationContext().registerReceiver(receiver,filter);
                            Log.i("xyz","Xposed注册广播");
                        }

                    }
                });
            }catch (Throwable throwable){
                XposedBridge.log("Module获取上下文失败 "+throwable);
                Log.i("xyz","Module获取上下文失败");
            }

        }

    }


}
