package com.example.admin.wxaddfrienddemo;

import android.os.SystemClock;

import java.util.Random;

import javax.net.ssl.SSLContext;

/**
 * Created by admin on 2017/11/16.
 */

// 可忽略
public class SpiltTest {

    public static void main(String []args){
        String  s = "wxid_lnt4ahvg2esr22";
        System.out.println("初始值："+s);
        String [] ss = s.split("_");
        String res = "";
        for (String s1:ss){
            res +=s1;
        }
        System.out.println("结果为："+res);


        String s1 = "qsss2312";
        System.out.println("初始值："+s1);
        String s2 = s1.substring(0,4);
        if (s2.equals("wxid")){
            String s3 = s1.substring(4);
            s1 = s2 + "_"+s3;
            System.out.println("结果为： "+s);
        }

        Double random = Math.random();
        System.out.println("random = "+random.toString());
        String str = random.toString().substring(2,11);
        System.out.println("生成的数字为: "+str);


        String s11 = "wxidlnt4ahvg2esr22";
        System.out.println("初始值："+s11);
        String s22 = s1.toUpperCase();
        System.out.println("转为大写："+s22);

        double ran = Math.random();
        long lon = (long) (1500 + ran *1000);
        System.out.println("RANDOM ："+lon);
    }
}
