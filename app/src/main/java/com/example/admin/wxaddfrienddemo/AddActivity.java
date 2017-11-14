package com.example.admin.wxaddfrienddemo;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by admin on 2017/11/8.
 */

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "xyz";

    Button btnSelect, btnStart;
    EditText etScene;
    EditText etHello;

    TextView tvFilePath;
    private static final int SELECT_CODE = 1;

    private String filePath;

    private List<String> list = new ArrayList<>();

    String hello;
    int scene;

    private AccessibilityService mService;

    EditText etTime;

    long time;

    TextView tvSum;

    int sum, successNum, failNum , num;

    Handler handler;

    String wxid;

    StringBuilder saveString = new StringBuilder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btnSelect = findViewById(R.id.select);
        etScene = findViewById(R.id.et_scene);
        etHello = findViewById(R.id.et_hello);
        tvFilePath = findViewById(R.id.tv_hint);
        btnStart = findViewById(R.id.start);
        etTime = findViewById(R.id.time);
        tvSum = findViewById(R.id.sum);

        handler = new Handler(){ // 更新已添加的总数
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 666:
                        tvSum.setText(""+sum);
                        break;
                    case 111:
                        Log.i("xyz","接受到111");
                        // 在主线程中弹吐司，虽然子线程可以吐司，但是有时显示不出来，被微信自己的覆盖了
                        showToast();
                        break;
                }
            }
        };

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() { // 开启子线程是为了线程间的通信和同步操作

                    @Override
                    public void run() {
                        Looper.prepare();
                        startJump();
                        Looper.loop();
                    }
                }).start();

            }
        });
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startJump() {
        if (TextUtils.isEmpty(etScene.getText()) || TextUtils.isEmpty(etHello.getText())) {
            Toast.makeText(AddActivity.this, "场景和验证信息不能为空", LENGTH_SHORT).show();
        } else {
            scene = Integer.parseInt(etScene.getText().toString().trim());
            hello = etHello.getText().toString().trim();
            if (TextUtils.isEmpty(etTime.getText())){
                time = 10 * 1000;
            }else {
                time = Long.parseLong(etTime.getText().toString().trim()) * 1000;
            }
            num = list.size();
            for (String info : list) {
                wxid = info;
                setCnt(0);
                // 跳转到加人界面
                AddUtils.addFriend(info, scene);
                //showToast();
                // 线程睡眠，让出cpu
                waitfor(20000);
                // 是否跳转成功，点击添加到通讯录按钮
                if(getCnt() >= 1){
                    setCnt(0);
                    if (!hasAddBtn()){ // 没有添加按钮，说明已经是好友了,直接返回
                        Log.i("xyz","没有添加按钮，说明已经是好友了");
                        finishAndReturn();
                    }
                    // 点击添加按钮
                    clickAddButton();
                    // 显示尝试添加的吐司
//                     showToast();
                    sendToUI();
                    try {
                        // 输出log到本地
                        writeToLocal();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   // showTextToast("尝试添加 "+wxid);
                    // 是否跳转成功,填入验证信息，点击发送按钮
                    waitfor(20000);
                    if (getCnt()>= 1){
                        setCnt(0);
                        // 判断是否需要验证
                        if (isNeedVerify()){
                            // 设置验证信息并发送
                            sendMessage();
                            // 更新添加个数
                            refrensh();
                            waitfor(20000);
                            if (getCnt()>=1){
                                // 是否发送成功，点击左上角返回
                                finishAndReturn();
                                SystemClock.sleep(time);
                            }
                        }else {
                            finishAndReturn();
                            SystemClock.sleep(time);
                        }
                    }
                }
            }
        }
    }

    private void sendToUI() {
        Message message = new Message();
        message.what = 111;
        Log.i("xyz","发送111给主线程");
        handler.sendMessage(message);
    }

    private void writeToLocal() throws IOException {
        //  路径名
        File file = new File("/sdcard/tmp/");
        if (!file.exists()){
            file.mkdirs();  // 创建目录
        }
        // 具体文件名 , 路径 + 文件
        File localFile = new File(file,"log.txt");
        if (!localFile.exists()){
            localFile.createNewFile();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());
        String date = format.format(curDate);
        saveString.append(date +": 尝试添加 "+wxid+"\n");
        Log.i("xyz",saveString.toString());

        FileOutputStream fos = new FileOutputStream(localFile,false); // 这里的第二个参数代表追加还是覆盖，true为追加，false为覆盖
        fos.write(saveString.toString().getBytes());
        fos.close();

    }

    private void showToast() {
        Log.i("xyz","调用了toast");
        Toast.makeText(getApplicationContext(),"尝试添加"+wxid,Toast.LENGTH_SHORT).show();

    }

    //设置Toast对象
    private Toast mToast = null;
    private void showTextToast(String msg) {
        //判断队列中是否包含已经显示的Toast
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }

    private boolean hasAddBtn() {
        AccessibilityNodeInfo root = getRoot();
        // 获取到添加按钮
        // List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ahp");
        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText("添加");
        if (list.size() > 0){
            Log.i("xyz","找到添加按钮");
           return true;
        }
        return false;
    }

    private AccessibilityNodeInfo findReturn(AccessibilityNodeInfo root) {
        if (root == null){
            return null;
        }
        AccessibilityNodeInfo res = null;
        for (int i = 0; i < root.getChildCount(); i++) {
            AccessibilityNodeInfo nodeInfo = root.getChild(i);
            if (nodeInfo.getClassName().equals("android.widget.ImageView")) {
                Log.i("xyz","获取到ImageView");
                Log.i("xyz","nodeInfo = "+nodeInfo);
                Rect rect = new Rect();
                nodeInfo.getBoundsInScreen(rect);
                int x = rect.centerX();
                int y = rect.centerY();
                if (0 < x && x < 35 && 15 < y && y < 50) {
                    res =  nodeInfo;
                    Log.i("xyz","找到返回键");
                    break; // 这里必须有这个break，表示找到返回键之后就会打破循环，将找到的值返回
                }
            }else {
                res = findReturn(nodeInfo);
                if (res != null){
                    return res;
                }
            }
        }
        return res;
    }

    private void refrensh() {
        Message message = new Message();
        message.what = 666;
        sum++;
        Log.i("xyz","sum = "+sum);
        handler.sendMessage(message);

        // 是否添加成功
//        if (isNeedVerify()){
//            Log.i("xyz","添加失败");
//            failNum++;
//            Log.i("xyz","添加"+ wxid  +"失败,当前总个数"+sum+"/"+num+"其中成功个数为： "+successNum+
//                    " 失败个数为： "+failNum);
//            Toast.makeText(getApplication(),"添加"+ wxid  +"失败,当前总个数"+sum+"/"+num+"其中成功个数为： "+successNum+
//                    " 失败个数为： "+failNum,Toast.LENGTH_LONG).show();
//        }else {
//            Log.i("xyz","添加成功");
//            successNum++;
//            Log.i("xyz","添加"+ wxid  +"成功,当前总个数"+sum+"/"+num+"其中成功个数为： "+successNum+
//                    " 失败个数为： "+failNum);
//            Toast.makeText(getApplicationContext(),"添加"+ wxid  +"成功,当前总个数"+sum+"/"+num+"其中成功个数为： "+successNum+
//                    " 失败个数为： "+failNum,Toast.LENGTH_LONG).show();
//        }
        SystemClock.sleep(1000);
    }

    private boolean isNeedVerify() {
        AccessibilityNodeInfo rootInfo = getRoot();
        List<AccessibilityNodeInfo> list =  rootInfo.findAccessibilityNodeInfosByText("验证");
        if (list.size()<=0){
            return false;
        }else {
            return true;
        }
    }

    // 线程睡眠，让出cpu
    public void waitfor(long overTime){
        long before = System.currentTimeMillis();
        do{
            long now = System.currentTimeMillis();
            if (now - before >= overTime){
                Log.i("xyz","等待超时");
                return;
            }
            SystemClock.sleep(500);
        }while (getCnt() == 0);


    }



    private int getCnt(){
        int i;
        synchronized (MyService.gs_lockObj){
            i = MyService.cnt;
            Log.i("xyz","get cnt = "+ MyService.cnt);
        }
        return i;
    }

    private void setCnt(int i){
        synchronized (MyService.gs_lockObj){
            MyService.cnt = i;
            Log.i("xyz","set cnt = "+ MyService.cnt);
        }
    }
    AccessibilityNodeInfo returnInfo;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void finishAndReturn() {

        Log.i("xyz","开始查找返回键");
       // 找到左上角的返回键
        AccessibilityNodeInfo root = getRoot();

        returnInfo = findReturn(root);

        if (returnInfo == null){
            Log.i("xyz","找到的返回为null");
        }else {
            Log.i("xyz","找到的返回不为null");
            while (!returnInfo.isClickable()) {
                returnInfo = returnInfo.getParent();
            }
            // 点击返回
            returnInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }


    }


    // 每次都等待1s后获取root根节点信息
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private AccessibilityNodeInfo getRoot() {
        mService = (AccessibilityService) MyService.getContext();
        AccessibilityNodeInfo root;
        do {
            root = mService.getRootInActiveWindow();
            SystemClock.sleep(200);
        }while (root==null);
        return root;
    }

    // 验证信息EditText框
    AccessibilityNodeInfo edit;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void sendMessage() {
        AccessibilityNodeInfo root = getRoot();

       // printfAll(root);
        // 找到输入框
        edit = findEditText(root);
//        if (edit == null){
//            Log.i("xyz","edit为空");
//        }else {
//            Log.i("xyz","edit不为空");
//        }
        // 清除原本信息



        if (!TextUtils.isEmpty(edit.getText())){ // 如果不为空。清理
            ClearAllText(edit);
        }
        // 设置验证信息
        ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text = hello;
        ClipData data = ClipData.newPlainText("text",text);
        manager.setPrimaryClip(data);
        edit.performAction(AccessibilityNodeInfo.ACTION_PASTE);


        // 点击发送按钮
        //List<AccessibilityNodeInfo> sendList = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/gy");
        List<AccessibilityNodeInfo> sendList = root.findAccessibilityNodeInfosByText("发送");
        for (AccessibilityNodeInfo info : sendList){
            Log.i("xyz","点击了发送");
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }



    }

    // 找到验证输入框
    private AccessibilityNodeInfo findEditText(AccessibilityNodeInfo root) {
        if (root == null){
            return null;
        }
        AccessibilityNodeInfo res = null;

        for (int i = 0; i < root.getChildCount(); i++) {
            AccessibilityNodeInfo nodeInfo = root.getChild(i);
            if (nodeInfo.getClassName().equals("android.widget.EditText")) {
                Log.i("xyz","获取到editteXt");
                Log.i("xyz","nodeInfo = "+nodeInfo);
                Rect rect = new Rect();
                nodeInfo.getBoundsInScreen(rect);
                int x = rect.centerX();
                int y = rect.centerY();
                if (9 < x && x < 371 && 80 < y && y < 108) {
                    res =  nodeInfo;
                    break; // 这里必须有这个break，表示找到输入框之后就会打破循环，将找到的值返回
                }
            }else {
                res = findEditText(nodeInfo);
                if (res != null){
                    return res;
                }
            }
        }
        return res;
    }

    private void printfAll(AccessibilityNodeInfo root){
        for (int i = 0; i < root.getChildCount(); i++) {
            AccessibilityNodeInfo nodeInfo = root.getChild(i);
            Log.i("xyz","class name = "+nodeInfo.getClassName());
            printfAll(nodeInfo);
        }
    }

    // 清除原来的文本信息
    private void ClearAllText(AccessibilityNodeInfo info) {
        // 每次只能删除一个字符
        String adb = "adb shell input keyevent 67";

        int length = info.getText().length();
        Log.i("xyz","原先文本长度 "+ length);
        for (int i = 0; i < length; i++) {
            try {
                Runtime.getRuntime().exec(adb);
                Log.i("xyz","执行一次");
                SystemClock.sleep(500); // 睡眠是为了保证能够全部清除
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    // 点击添加按钮
    private void clickAddButton() {

        AccessibilityNodeInfo root = getRoot();
        // 获取到添加按钮
        // List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ahp");
        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText("添加");
        if (list.size() > 0){
            Log.i("xyz","找到添加按钮");
            for (AccessibilityNodeInfo info : list){
                // 点击按钮
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
        }
    }

    private void selectFile() {
        // 打开系统文件浏览功能
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select a file path"), SELECT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SELECT_CODE) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {  // 使用第三方应用打开
                filePath = uri.getPath();
                tvFilePath.setText(filePath);
                Log.i(TAG, "filePath = " + filePath);
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {     // 4.4以后
                filePath = getPathKitKat(this, uri);
                tvFilePath.setText(filePath);
                Log.i(TAG, "filePath = " + filePath);
            } else {
                filePath = getPathUnderKitKat(uri);          // 4.4 以前
                tvFilePath.setText(filePath);
                Log.i(TAG, "filePath = " + filePath);
            }


            // 获取txt文本信息
            if (TextUtils.isEmpty(filePath)) {
                Toast.makeText(AddActivity.this, "请选择文件路径", LENGTH_SHORT).show();
            } else {
                getTxtInfo();
            }
        }
    }

    private String getPathUnderKitKat(Uri uri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(index);
            cursor.close();
        }
        return res;
    }

    // 4.4 以后从uri获取文件的绝对路径
    @SuppressLint("NewApi")
    private String getPathKitKat(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // 文件提供者 DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore （general）
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // file文件类型
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    // 返回从uri获取到的数据column ，这种只对MediaStore有效。其他的都是ContentProviders
    private String getDataColumn(Context context, Uri contentUri, String selection, String[] selectArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] proj = {column};
        cursor = context.getContentResolver().query(contentUri, proj, selection, selectArgs, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    // 是否是Media路径下的
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    // 是否是下载路径下的
    private boolean isDownloadDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());

    }

    // 是否是ExternalStorage路径下的
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    // 遍历文本中的每一行，将信息放入list中
    private void getTxtInfo() {
        File file = new File(filePath);
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
