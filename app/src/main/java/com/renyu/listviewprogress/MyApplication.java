package com.renyu.listviewprogress;

import android.app.Application;

import com.renyu.listviewprogress.download.DownloadManager;
import com.renyu.listviewprogress.download.DownloadModel;
import com.renyu.listviewprogress.download.SqliteManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/10/13.
 */
public class MyApplication extends Application {

    SqliteManager manager=null;

    @Override
    public void onCreate() {
        super.onCreate();

        //添加下载状态列表
        manager=SqliteManager.getInstance(getApplicationContext());
        ArrayList<DownloadModel> models=manager.getAllDownloadInfo();
        DownloadManager.getInstance(getApplicationContext()).addStateMap(models);

    }
}
