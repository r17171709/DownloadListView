package com.renyu.listviewprogress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.renyu.listviewprogress.download.DownloadManager;
import com.renyu.listviewprogress.download.ParamsManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/10/9.
 */
public class MyAdapter extends BaseAdapter {

    Context context=null;
    ArrayList<String> tags=null;
    ListView listview=null;

    public MyAdapter(Context context, ArrayList<String> tags, ListView listview) {
        this.context=context;
        this.tags=tags;
        this.listview=listview;
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Object getItem(int i) {
        return tags.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (view==null) {
            view= LayoutInflater.from(context).inflate(R.layout.adapter_text, null);
            holder=new ViewHolder();
            holder.adapter_textview=(TextView) view.findViewById(R.id.adapter_textview);
            view.setTag(holder);
        }
        else {
            holder= (ViewHolder) view.getTag();
        }
        final String tag=tags.get(i);
        final TextView t=holder.adapter_textview;
        t.setTag(tag);
        switch (DownloadManager.getInstance(context).state(tag)) {
            case ParamsManager.State_NORMAL:
                t.setText("0");
                break;
            case ParamsManager.State_WAIT:
                t.setText("wait");
                break;
            case ParamsManager.State_PAUSE:
                t.setText("pause");
                break;
            case ParamsManager.State_FINISH:
                t.setText("100");
                break;
            case ParamsManager.State_DOWNLOAD:
                t.setText("download");
                break;
        }
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DownloadManager.getInstance(context).isDownloading(tag)) {
                    DownloadManager.getInstance(context).pauseDownload(tag);
                }
                else {
                    DownloadManager.getInstance(context).startDownload(tag);
                }
            }
        });
        return view;
    }
}

class ViewHolder {
    TextView adapter_textview=null;
}