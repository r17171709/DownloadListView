package com.renyu.listviewprogress.download;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/10/13.
 */
public class SqliteManager {

    private static SqliteManager manager=null;
    private static SqliteHelper helper=null;

    private SqliteManager() {

    }

    public static synchronized SqliteManager getInstance(Context context) {
        if (manager==null) {
            helper=new SqliteHelper(context);
            manager=new SqliteManager();
        }
        return manager;
    }

    /**
     * 更新表状态
     * @param url
     * @param state
     * @param totalSize
     */
    public void updateDownloadData(String url, int state, String totalSize) {
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cs=db.query(SqliteHelper.TABLENAME, null, SqliteHelper.DOWNLOAD_NAME+"=?", new String[]{url}, null, null, null);
        cs.moveToFirst();
        int count=cs.getCount();
        cs.close();
        if (count>0) {
            ContentValues cv=new ContentValues();
            cv.put(SqliteHelper.DOWNLOAD_STATE, state);
            db.update(SqliteHelper.TABLENAME, cv, SqliteHelper.DOWNLOAD_NAME+"=?", new String[]{url});
        }
        else {
            ContentValues cv=new ContentValues();
            cv.put(SqliteHelper.DOWNLOAD_NAME, url);
            cv.put(SqliteHelper.DOWNLOAD_TOTALSIZE, totalSize);
            cv.put(SqliteHelper.DOWNLOAD_STATE, state);
            db.insert(SqliteHelper.TABLENAME, null, cv);
        }
        db.close();
    }

    /**
     * 获取所有下载信息
     * @return
     */
    public ArrayList<DownloadModel> getAllDownloadInfo() {
        ArrayList<DownloadModel> models=new ArrayList<DownloadModel>();
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cs=db.query(SqliteHelper.TABLENAME, null, null, null, null, null, null);
        cs.moveToFirst();
        int count=cs.getCount();
        for (int i=0;i<count;i++) {
            cs.moveToPosition(i);
            DownloadModel model=new DownloadModel();
            model.setDOWNLOAD_NAME(cs.getString(cs.getColumnIndex(SqliteHelper.DOWNLOAD_NAME)));
            model.setDOWNLOAD_STATE(cs.getInt(cs.getColumnIndex(SqliteHelper.DOWNLOAD_STATE)));
            model.setDOWNLOAD_TOTALSIZE(cs.getString(cs.getColumnIndex(SqliteHelper.DOWNLOAD_TOTALSIZE)));
            models.add(model);
        }
        cs.close();
        db.close();
        return models;
    }
}
