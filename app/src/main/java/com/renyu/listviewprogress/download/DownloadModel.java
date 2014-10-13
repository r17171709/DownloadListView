package com.renyu.listviewprogress.download;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/10/13.
 */
public class DownloadModel implements Serializable {

    String DOWNLOAD_NAME="";
    int DOWNLOAD_STATE=ParamsManager.State_NORMAL;
    String DOWNLOAD_TOTALSIZE="";

    public String getDOWNLOAD_NAME() {
        return DOWNLOAD_NAME;
    }

    public void setDOWNLOAD_NAME(String DOWNLOAD_NAME) {
        this.DOWNLOAD_NAME = DOWNLOAD_NAME;
    }

    public int getDOWNLOAD_STATE() {
        return DOWNLOAD_STATE;
    }

    public void setDOWNLOAD_STATE(int DOWNLOAD_STATE) {
        this.DOWNLOAD_STATE = DOWNLOAD_STATE;
    }

    public String getDOWNLOAD_TOTALSIZE() {
        return DOWNLOAD_TOTALSIZE;
    }

    public void setDOWNLOAD_TOTALSIZE(String DOWNLOAD_TOTALSIZE) {
        this.DOWNLOAD_TOTALSIZE = DOWNLOAD_TOTALSIZE;
    }
}
