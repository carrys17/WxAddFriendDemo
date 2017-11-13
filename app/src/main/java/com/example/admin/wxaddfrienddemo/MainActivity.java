 package com.example.admin.wxaddfrienddemo;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

 public class MainActivity extends AppCompatActivity {

     private static final String TAG = "xyz";


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         if (isWxAviliable()){
             gotoWxFriend();
         }

//        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isWxAviliable()){
//                    gotoWxFriend();
//                }
//            }
//        });
    }



    //  是否安装了微信
     private boolean isWxAviliable() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> list = pm.getInstalledPackages(0);
        for (PackageInfo info : list){
            String pkg = info.packageName;
            if (pkg.equals("com.tencent.mm")){
                Log.i(TAG, "isWxAviliable: "+true);
                return true;
            }
        }
         Log.i(TAG, "isWxAviliable: "+false);
         return false;
     }

     private void gotoWxFriend() {
          // 微信主界面可以通过这种代码的方式直接打开，但是这次的目标是到加好友的界面，所以得采用adb命令的形式
//         Intent intent = new Intent(Intent.ACTION_MAIN);
//         intent.addCategory(Intent.CATEGORY_LAUNCHER);
//         ComponentName cn = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
//         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//         intent.setComponent(cn);
//         startActivity(intent);



         // ei表示插入的bundle是int类型，es表示是String，注意加上""（这里用转义字符）， ez表示boolean类型
         try {
             //I/set     ( 2625): intent = Intent { cmp=com.tencent.mm/.plugin.profile.ui.ContactInfoUI (has extras) }
            // I/set     ( 2625): bundle = Bundle[{Contact_NeedShowChangeRemarkButton=false, Contact_QuanPin=wxidmj208er62zri22,
             // Contact_PyInitial=WXIDMJ208ER62ZRI22, Contact_RegionCode=, Contact_BrandList=<brandlist count="0" ver="684150198"></brandlist>,
             // Contact_KSnsBgId=0, add_more_friend_search_scene=2, Contact_KWeiboNick=null, Contact_KSnsBgUrl=null, Contact_KSnsIFlag=0,
             // Contact_KWeibo_flag=0, Contact_Signature=null, Contact_BIZ_KF_WORKER_ID=null, Contact_VUser_Info=null, Contact_KHideExpose=true,
             // Contact_Nick=明, Contact_User=v1_cecdf08b2bda1a00e1263cca852eb5aefa6cbdec9fde130f32b1d1d7b1a80ff363bfd4426332556365b479beadc44169@stranger,
             // Contact_VUser_Info_Flag=0, Contact_Sex=0, Contact_Alias=lyming1965, Contact_Scene=3, Contact_KWeibo=null, Contact_NeedShowChangeSnsPreButton=false}]
             // 通过微信号搜索的形式
             String s = "adb shell am start -n com.tencent.mm/com.tencent.mm.plugin.profile.ui.ContactInfoUI "+
//                     "--ez Contact_NeedShowChangeRemarkButton false "+   // 都是false，可以不用管
//                     "--es Contact_QuanPin \"wxidmj208er62zri22\" "+      // 微信id ，有些人没有就是微信号，这个asd就是没有的那种。
//                     "--es Contact_PyInitial \"WXIDMJ208ER62ZRI22\" "+
//                     "--es Contact_RegionCode  \"\" "+        // 地区，去掉也行，会自己显示出来
//                     "--ei add_more_friend_search_scene 2 "+  // 都为2，无论是通过手机号或者是微信号,可不管
//                     "--ez Contact_KHideExpose true "+     // 都是true ，去掉也行
//                     "--es Contact_Nick \"明\" "+  // 微信昵称 ,可以改动，去掉显示微信号了
                     // 只要有这个v1就可以了，去掉就不行了
                     "--es Contact_User \"v1_f2beaa1797ef0252d0421242b04ab14613f59c18c49bab2e395fc26badaf517a@stranger\" "+   // v1 值

//                     "--ei Contact_VUser_Info_Flag 0 "+   // 都为0，可去掉
//                     "--ei Contact_Sex 1 "+    //  性别，0为女 1为男。设置错误或者去掉的话，性别就没有显示出来了
//                     "--es Contact_Alias \"lyming1965\" "+  // 微信号
                     "--ei Contact_Scene 15 "  +// 微信号搜索的为3，手机号搜索为15
//                     "--ez Contact_NeedShowChangeSnsPreButton false "  + // 都是false，可以去掉

//                     "Contact_Search_Mobile  \"13430013863\" "+     // 通过手机搜索的方式，多这个参数
                     "";

             // 通过手机号搜索的形式，多了这个字段 Contact_Search_Mobile=13430013863; 然后就是Contact_Scene 为15 ，其他都一样
           //  Runtime.getRuntime().exec(s);


             // 现在通过群跳转到加人的界面，有两种，第一种是直接在聊天界面点击头像的情况，

             //I/set     ( 2625): bundle = Bundle[{Contact_NeedShowChangeRemarkButton=false, room_name=636235605@chatroom,
             // Is_RoomOwner=false, Contact_RoomNickname=吃瓜的鸿, Contact_User=wxid_n24mdcaqj37s22,
             // Contact_Scene=14, Contact_NeedShowChangeSnsPreButton=false}]

             String s1 = "adb shell am start -n com.tencent.mm/com.tencent.mm.plugin.profile.ui.ContactInfoUI "+
//                     "--ez Contact_NeedShowChangeRemarkButton false "+   // 都是false，可以不用管
//                     "--es room_name \"636235605@chatroom\" "+      // 群号，加不加都行。。。
//                     "--ez Is_RoomOwner false "+                 // 全为false 一开始猜测是否是群主，但是结果不是。
//                     "--es Contact_RoomNickname  \"吃瓜的鸿\" "+        // 群昵称,可修改，去掉不显示
                     "--es Contact_User \"wxid_n24mdcaqj37s22\" "+  // 微信id，只给这个就可以跳到加人界面了
                     "--ei Contact_Scene 15 "  +// 微信号搜索的为3，手机号搜索为15,群的方式是14
//                     "--ez Contact_NeedShowChangeSnsPreButton false "  + // 都是false，可以去掉
                     // 第二种是点击右上角显示群信息后再点击群员到加人的界面，多了如下三个字段
//                     "--es Contact_ChatRoomId \"636235605@chatroom\" "+
//                     "--es Contact_Nick \"Hong\" "+    // 昵称
//                     "--ez Contact_RoomMember true "+   // 全为true
                     "";
             Runtime.getRuntime().exec(s1);

            // 通讯录方式
             //I/set     ( 2625): bundle = Bundle[{Contact_NeedShowChangeRemarkButton=false,
             // Contact_Mobile_MD5=270bae1b55a890759987f9047b81c027, Contact_RegionCode=CN_Guangdong_Dongguan,
             // Contact_ShowUserName=false, Contact_Signature= boeey, Contact_Nick=桌球,
             // Contact_User=v1_f2beaa1797ef0252d0421242b04ab14613f59c18c49bab2e395fc26badaf517a@stranger,
             // Contact_Sex=1, Contact_Alias=, Contact_Scene=13, Contact_NeedShowChangeSnsPreButton=false}]

             String s2 = "adb shell am start -n com.tencent.mm/com.tencent.mm.plugin.profile.ui.ContactInfoUI "+
                     "--es Contact_Mobile_MD5 \"270bae1b55a890759987f9047b81c027\" "+      // 手机号码的MD5值
                     "--ez Contact_ShowUserName true "+                 // 全为false
//                     "--es Contact_Signature  \"boeey\" "+                 // 个性签名
                     "--es Contact_Nick \"桌球\" "+                       // 昵称
                     // v1 值，只给这个就可以跳到加人界面了
                     "--es Contact_User \"v1_f2beaa1797ef0252d0421242b04ab14613f59c18c49bab2e395fc26badaf517a@stranger\" "+
                     "--ei Contact_Scene 13 "  +                          // 微信号搜索的为3，手机号搜索为15,群的方式是14，通讯录是13
                     "--ei Contact_Sex 1 " +                              // 性别，1为男，0为女
                     "--ez Contact_NeedShowChangeSnsPreButton false "  + // 都是false，可以去掉


                     "";
          //   Runtime.getRuntime().exec(s2);

         } catch (IOException e) {
             e.printStackTrace();
         }


     }
 }
