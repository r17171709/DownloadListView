package com.renyu.listviewprogress.download;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;

/**
 * Created by Administrator on 2014/10/10.
 */
public class CommonUtils {

    /**
     * 得到SD卡可使用容量
     * @return
     */
    public static long getAvailableStorage() {
        String storageDirectory= Environment.getExternalStorageDirectory().toString();
        try {
            StatFs stat = new StatFs(storageDirectory);
            long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
            return avaliableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }

    /**
     * 判断网络是否正常
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni=cm.getActiveNetworkInfo();
            return ni!=null ? ni.isConnectedOrConnecting() : false;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
