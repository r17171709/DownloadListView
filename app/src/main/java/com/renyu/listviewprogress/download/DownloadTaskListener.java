package com.renyu.listviewprogress.download;

/**
 * Created by Administrator on 2014/10/10.
 */
public interface DownloadTaskListener {
    // 更新进度
    public void updateProcess(DownloadTask mgr);
    // 完成下载
    public void finishDownload(DownloadTask mgr);
    // 准备下载
    public void preDownload(DownloadTask mgr);
    // 下载错误
    public void errorDownload(DownloadTask mgr, int error);
    // 暂停
    public void cancelDownload(DownloadTask mgr);

}
