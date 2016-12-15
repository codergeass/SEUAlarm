package cn.edu.seu.cse.seualarm.controler.alarm;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.module.CustomRing;
import cn.edu.seu.cse.seualarm.util.Constants;

/**
 * Created by Coder Geass on 2016/12/12.
 */

public class RingSetActivity extends AppCompatActivity {

    private ListView lv_ring;
    private TextView tv_custom_ring;

    // 铃声资源
    private String[] ringName = new String[]{
            "Everybody", "荆棘鸟", "加勒比海盗", "圣斗士(慎点)",
            "Flower", "Time Traval", "Thank you for",
            "律动", "Morning", "Echo", "Alarm Clock"};
    private String[] songId = new String[]{
            "everybody.mp3", "bird.mp3", "galebi.mp3", "shendoushi.mp3",
            "flower.mp3", "timetravel.mp3", "thankufor.mp3", "mx1.mp3",
            "mx2.mp3", "echo.mp3", "clock.mp3"};


    private ArrayList<String> ringList;
    private ArrayList<String> ringIDList;
    private int currentItem;
    private MyAdapter mAdapter;
    private MediaPlayer mPlayer;

    // 最终选定的名字
    private String setRingName;
    private String setRingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_set);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doneRing();
                }
            });
        }

        iniView();
        initAdapter();
        setListViewHeightBasedOnChildren(lv_ring);
        initListener();
    }

    // 用于嵌套ScrollView
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void iniView() {
        lv_ring = (ListView) findViewById(R.id.lv_ring_set);
        tv_custom_ring = (TextView) findViewById(R.id.tv_cutom_ring);

        // 自定义铃声监听事件
        tv_custom_ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent customRingSetIntent = new Intent(RingSetActivity.this, CustomRingSetActivity.class);
                startActivityForResult(customRingSetIntent, Constants.ASK_CUSTOM_RING_SET);
            }
        });

        ringList = new ArrayList<String>();
        ringIDList = new ArrayList<String>();

        Bundle bundle = getIntent().getExtras();
        setRingName = bundle.getString(Constants.RING_NAME);
        setRingId = bundle.getString(Constants.RING_ID);

        // 自定义铃声已经设置
        if (setRingId != null) {
            Log.d("alarm", "ringid " + setRingId);
            Log.d("alarm", "ringname " + setRingName);
            if (setRingId.contains("/")) {
                ringIDList.add(setRingId);
                ringList.add(setRingName);
            }
        }


        currentItem = 0;
        Log.d("alarm", "默认得到的id" + setRingId);
    }

    private void initAdapter() {
        for (int i = 0; i < ringName.length; i++) {
            ringList.add(ringName[i]);
            ringIDList.add(songId[i]);
        }

        mAdapter = new MyAdapter();
        lv_ring.setAdapter(mAdapter);
    }

    private void initListener() {
        lv_ring.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setRingName = ringList.get(position);
                setRingId = ringIDList.get(position);
                currentItem = position;
                Log.d("alarm", "position:" + position);
                Log.d("alarm", "setRingName:" + setRingName);
                Log.d("alarm", "setRingId:" + setRingId);
                mAdapter.notifyDataSetChanged();
                /*if(isPlaying){
                    stopTheSong();
                }*/
                ringTheSong(position);
            }
        });
    }

    //播放音乐
    private void ringTheSong(int position) {
        AssetFileDescriptor assetFileDescriptor = null;
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        mPlayer.reset();
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            if (position == 0 && !ringList.get(position).equals(ringName[0])) {
                mPlayer.setDataSource(ringIDList.get(0));
            } else {
                assetFileDescriptor = this.getAssets().openFd(ringIDList.get(position));
                mPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            }
            mPlayer.setVolume(1f, 1f);
            mPlayer.setLooping(true);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //isPlaying=true;
    }

    private void stopTheSong() {
        if (mPlayer != null) {
            Log.d("ring", "mPlay" + mPlayer.toString());
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
        }
    }

    static class ViewHolder {
        TextView Name;
        RadioButton Radio;
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(RingSetActivity.this, R.layout.list_ring_set_item, null);
                holder.Name = (TextView) convertView.findViewById(R.id.tv_name_ring);
                holder.Radio = (RadioButton) convertView.findViewById(R.id.rb_check_ring);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.Name.setText(ringList.get(position));
            if (position == currentItem) {
                holder.Radio.setChecked(true);
            } else {
                holder.Radio.setChecked(false);
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return ringList.size();
        }

        @Override
        public Object getItem(int position) {
            return ringList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private void doneRing() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        setRingName = ringList.get(currentItem);
        setRingId = ringIDList.get(currentItem);

        bundle.putString(Constants.RING_NAME, setRingName);
        bundle.putString(Constants.RING_ID, setRingId);
        intent.putExtras(bundle);

        setResult(Constants.RING_SET_DONE, intent);
        Log.d("alarm", "ring set done");
        Log.d("alarm", "setRingName:" + setRingName);
        Log.d("alarm", "setRingId:" + setRingId);
        finish();
    }

    @Override
    public void onBackPressed() {
        doneRing();
    }

    @Override
    protected void onDestroy() {
        stopTheSong();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.CUSTOM_RING_SET_DONE) {
            CustomRing customRing = (CustomRing) data.getExtras().getSerializable(Constants.CUSTOM_RING);
            ringList.add(0, customRing.getRingName());
            ringIDList.add(0, customRing.getRingPath());
            currentItem = 0;
            setRingId = ringIDList.get(0);
            setRingName = ringList.get(0);
            mAdapter.notifyDataSetChanged();
            //Toast.makeText(this, "自定义铃声已设置", Toast.LENGTH_SHORT).show();
        }
    }
}

