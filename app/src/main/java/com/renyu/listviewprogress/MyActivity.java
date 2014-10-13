package com.renyu.listviewprogress;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ListView;
import android.widget.TextView;

import com.renyu.listviewprogress.download.DownloadManager;
import com.renyu.listviewprogress.download.ParamsManager;

import java.util.ArrayList;


public class MyActivity extends ActionBarActivity {

    DownloadManager manager=null;

    ArrayList<String> tags=null;

    ListView listview=null;
    MyAdapter adapter=null;

    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View v=listview.findViewWithTag(msg.obj.toString());
            if (v==null) {
                return ;
            }
            switch (msg.what) {
                case ParamsManager.State_NORMAL:
                    ((TextView) v).setText("0");
                    break;
                case ParamsManager.State_DOWNLOAD:
                    Bundle bundle= msg.getData();
                    ((TextView) v).setText(bundle.getString("percent"));
                    break;
                case ParamsManager.State_FINISH:
                    ((TextView) v).setText("100");
                    break;
                case ParamsManager.State_PAUSE:
                    ((TextView) v).setText("pause");
                    break;
                case ParamsManager.State_WAIT:
                    ((TextView) v).setText("wait");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        manager=DownloadManager.getInstance(getApplicationContext());
        manager.setHandler(handler);

        tags=new ArrayList<String>();

        tags.add("http://gdown.baidu.com/data/wisegame/c841c436b812cd9d/nvshengpai_5.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/6e301de74f680eea/PhotoWonder_130.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/a2d7ebceadb2ddc2/meipai_170.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/0c843031eaaaa6cd/MoXiuLauncher_459.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/25d0855f1586ee81/anzhuobizhi_108.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/7047d6e19b654426/kechenggezi_86.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/be40eed2397d8b10/DouguoRecipe_128.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/de2852bb17d081d9/FingerScanner_64.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/d8748549c0c6995c/baobaozhidao_40.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/a3e60ba4ddf7f50e/wumi_2002001.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/cc41739db6a47fac/suopingjingling_3330.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/083b553642f64881/aipinche_280.apk");
        tags.add("http://gdown.baidu.com/data/wisegame/1a8214b93bcfe24f/babaqunaerqinzibaodian_100.apk");

        listview=(ListView) findViewById(R.id.listview);
        adapter=new MyAdapter(MyActivity.this, tags, listview);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.setHandler(null);
    }
}
