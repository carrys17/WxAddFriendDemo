package com.example.admin.wxaddfrienddemo;

import java.io.IOException;

/**
 * Created by admin on 2017/11/8.
 */

public class AddUtils {
    /**
     *  通过v1或者是wxid 加人
     * @param v1        对方的v1值
     * @param scene     场景方式
     */
    public static void addFriend(String v1,int scene){
        String s = "adb shell am start -n com.tencent.mm/com.tencent.mm.plugin.profile.ui.ContactInfoUI "+
                "--es Contact_User \""+v1+"\" "+
                "--ei Contact_Scene "+ scene +" "+
                "";
        try {
            Runtime.getRuntime().exec(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     *  通过v1值加人
//     * @param Wxid       对方的Wxid值
//     * @param scene      场景方式
//     */
//    public static void addByWxid(String Wxid,int scene){
//        String s = "adb shell am start -n com.tencent.mm/com.tencent.mm.plugin.profile.ui.ContactInfoUI "+
//                "--es Contact_User \""+Wxid+"\" "+
//                "--ei Contact_Scene "+ scene +" "+
//                "";
//        try {
//            Runtime.getRuntime().exec(s);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
