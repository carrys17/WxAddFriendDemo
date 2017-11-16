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



        Double random = Math.random();
        System.out.println("random = "+random.toString());
        String str = random.toString().substring(2,11);
        System.out.println("生成的数字为: "+str);


        String s1 = "wxidlnt4ahvg2esr22";
        System.out.println("初始值："+s1);
        String s2 = s1.toUpperCase();
        System.out.println("转为大写："+s2);

        double ran = Math.random();
        long lon = (long) (1500 + ran *1000);
        System.out.println("RANDOM ："+lon);
    }
}
