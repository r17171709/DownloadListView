package com.renyu.listviewprogress.download;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2014/10/10.
 */
public class DownloadManager {

    static Context context=null;
    Handler handler=null;
    static DownloadManager manager=null;
    //下载队列map
    static HashMap<String, DownloadTask> taskmap=null;
    //状态map
    static HashMap<String, Integer> statemap=null;
    //上一次刷新页面的时间戳
    long lastRefresh=0;

    private DownloadManager() {

    }

    public synchronized static DownloadManager getInstance(Context context_) {
        if (manager==null) {
            manager=new DownloadManager();
            taskmap=new HashMap<String, DownloadTask>();
            statemap=new HashMap<String, Integer>();
            context=context_;
        }
        return manager;
    }


    DownloadTaskListener listener=new DownloadTaskListener() {
        @Override
        public void updateProcess(DownloadTask mgr) {
            statemap.put(mgr.getDownloadUrl(), ParamsManager.State_DOWNLOAD);
            if (handler!=null) {
                long currentTime=System.currentTimeMillis();
                if (currentTime-lastRefresh>ParamsManager.TIMEEXTRA) {
                    lastRefresh=currentTime;
                    Message m=new Message();
                    m.what=ParamsManager.State_DOWNLOAD;
                    m.obj=mgr.getDownloadUrl();
                    Bundle bundle=new Bundle();
                    bundle.putString("percent", ""+mgr.getDownloadPercent());
                    m.setData(bundle);
                    handler.sendMessage(m);
                }
            }
        }

        @Override
        public void finishDownload(DownloadTask mgr) {
            SqliteManager.getInstance(context).updateDownloadData(mgr.getDownloadUrl(), ParamsManager.State_FINISH, "0");
            statemap.put(mgr.getDownloadUrl(), ParamsManager.State_FINISH);
            if (taskmap.containsKey(mgr.getDownloadUrl())) {
                taskmap.remove(mgr.getDownloadUrl());
            }
            if (handler!=null) {
                Message m=new Message();
                m.what=ParamsManager.State_FINISH;
                m.obj=mgr.getDownloadUrl();
                handler.sendMessage(m);
            }
        }

        @Override
        public void preDownload(DownloadTask mgr) {
            statemap.put(mgr.getDownloadUrl(), ParamsManager.State_WAIT);
            if (handler!=null) {
                Message m=new Message();
                m.what=ParamsManager.State_WAIT;
                m.obj=mgr.getDownloadUrl();
                handler.sendMessage(m);
            }
        }

        @Override
        public void errorDownload(DownloadTask mgr, int error) {
            SqliteManager.getInstance(context).updateDownloadData(mgr.getDownloadUrl(), ParamsManager.State_PAUSE, "0");
            statemap.put(mgr.getDownloadUrl(), ParamsManager.State_NORMAL);
            if (taskmap.containsKey(mgr.getDownloadUrl())) {
                taskmap.remove(mgr.getDownloadUrl());
            }
            if (handler!=null) {
                Message m=new Message();
                m.what=ParamsManager.State_NORMAL;
                m.obj=mgr.getDownloadUrl();
                handler.sendMessage(m);
            }
        }

        @Override
        public void cancelDownload(DownloadTask mgr) {
            SqliteManager.getInstance(context).updateDownloadData(mgr.getDownloadUrl(), ParamsManager.State_PAUSE, "0");
            statemap.put(mgr.getDownloadUrl(), ParamsManager.State_PAUSE);
            if (taskmap.containsKey(mgr.getDownloadUrl())) {
                taskmap.remove(mgr.getDownloadUrl());
            }
            if (handler!=null) {
                Message m=new Message();
                m.what=ParamsManager.State_PAUSE;
                m.obj=mgr.getDownloadUrl();
                handler.sendMessage(m);
            }
        }
    };

    public void setHandler(Handler handler) {
        this.handler=handler;
    }

    /**
     * 开启下载
     * @param url
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void startDownload(String url) {
        try {
            if (taskmap.containsKey(url)) {
                return;
            }
            DownloadTask task=new DownloadTask(context, url, ParamsManager.DIR, listener);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            taskmap.put(url, task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停下载
     * @param url
     */
    public void pauseDownload(String url) {
        if (taskmap.containsKey(url)) {
            taskmap.remove(url).cancel();
        }
    }

    /**
     * 判断是否在下载队列中
     * @param url
     * @return
     */
    public boolean isDownloading(String url) {
        return statemap.containsKey(url)&&statemap.get(url)==ParamsManager.State_DOWNLOAD;
    }

    public int state(String url) {
        if (!statemap.containsKey(url)) {
            return ParamsManager.State_NORMAL;
        }
        else {
            return statemap.get(url);
        }
    }

    /**
     * 添加下载状态列表
     * @param models
     */
    public void addStateMap(ArrayList<DownloadModel> models) {
        for (int i=0;i<models.size();i++) {
            DownloadModel model=models.get(i);
            if (model.getDOWNLOAD_STATE()==ParamsManager.State_FINISH) {
                statemap.put(model.getDOWNLOAD_NAME(), ParamsManager.State_FINISH);
            }
            else {
                statemap.put(model.getDOWNLOAD_NAME(), ParamsManager.State_PAUSE);
                SqliteManager.getInstance(context).updateDownloadData(model.getDOWNLOAD_NAME(), ParamsManager.State_PAUSE, "0");
            }
        }
    }

}
