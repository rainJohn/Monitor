package com.example.pxshl.yc_monitior.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;


/**
 * Created by pxshl on 17-7-23.
 */

public class Tools {

    public static String pwdToMd5(String pwd) {
        //复合字符串，密码+时间戳（精确到十位）+混淆字符串
        String recombination = pwd + '_' + (System.currentTimeMillis() / 1000L / 100L + 1) + "_topsky";
        StringBuilder builder = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(recombination.getBytes());
            byte[] m = md5.digest();//加密

            for (byte b : m) {
                if ((b & 0xff) < 0x10) {
                    //一个byte为16字节，用两个char表示
                    //0x5 & 0xff = 5,0x05 & 0xff = 05 （用字符串表示便会有区别）
                    builder.append('0');
                }
                builder.append(Integer.toHexString(b & 0xff));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString().toUpperCase();
    }

    public static boolean canLiveFromMoniotr(Context context){

            if (Data.BSSID == null || Data.BSSID.equals("")){
                return false;
            }

            WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);


            if (wifiManager.isWifiEnabled()){

                if (Tools.isWifi(context)){
                    WifiInfo info = wifiManager.getConnectionInfo();
                    if (info != null){
                        String bssid = info.getBSSID();
                        if (bssid != null && bssid.equals(Data.BSSID)){
                            return true;
                        }
                    }

                }
            }

            return false;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    /**
     * The random seed
     */
    static final long seed = System.currentTimeMillis();
    // static final long seed=0;

    private static java.util.Random rand = new java.util.Random(seed);

    /**
     * Returns a random integer
     */
    public static int nextInt() {
        return rand.nextInt();
    }


    /**
     * Returns a random long
     */
    public static long nextLong() {
        return rand.nextLong();
    }

    //返回下载文件夹下所有文件的文件名（文件名经过处理，包含其代表日期的文件夹名）
    public static List<String> getFileList(String strPath) {

        List<String> fileList = new ArrayList<>();
        File[] folders = new File(strPath).listFiles(); // 该文件目录下文件全部放入数组(都是文件夹)

        if (folders != null) {
            for (File folder : folders) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        fileList.add(folder.getName() + "/" + file.getName());
                    }
                }
            }
        }

        return fileList;
    }



}
