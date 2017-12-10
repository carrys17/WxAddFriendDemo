package com.example.admin.wxaddfrienddemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by admin on 2017/12/5.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    Context mContext;

    public MyBroadcastReceiver(Context context){
        mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("xyz","进入onReceive");
        Bundle bundle = intent.getExtras();
        String wxid = bundle.getString("wxid");
        int scene = bundle.getInt("scene");
        String tuiWxid = bundle.getString("tuiWxid");
        String tuiNickName = bundle.getString("tuiNickName");
        AddUtils.addFriendByWxid(mContext,wxid,scene,tuiWxid,tuiNickName);

    }


}
