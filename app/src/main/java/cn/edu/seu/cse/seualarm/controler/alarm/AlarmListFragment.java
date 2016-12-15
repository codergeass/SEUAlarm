package cn.edu.seu.cse.seualarm.controler.alarm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.philliphsu.bottomsheetpickers.time.BottomSheetTimePickerDialog;
import com.philliphsu.bottomsheetpickers.time.numberpad.NumberPadTimePickerDialog;

import java.util.List;

import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.controler.BaseFragment;
import cn.edu.seu.cse.seualarm.controler.MainActivity;
import cn.edu.seu.cse.seualarm.dao.AlarmInfoDao;
import cn.edu.seu.cse.seualarm.module.AlarmInfo;
import cn.edu.seu.cse.seualarm.util.Constants;
import cn.edu.seu.cse.seualarm.util.MyAlarmManager;

/**
 * Created by Coder Geass on 2016/12/9.
 */

public class AlarmListFragment extends BaseFragment {
    private RecyclerView alarmListView;
    private TextView emptyView;
    public AlarmListViewAdapter alarmListViewAdapter;
    private List<AlarmInfo> mAlarmInfoList;
    private AlarmInfoDao mDao;
    int defColor;
    int disColor;

    protected void initData() {
        mDao = new AlarmInfoDao(mActivity);
        mAlarmInfoList = mDao.getAllInfo();
        defColor = getResources().getColor(R.color.white);
        disColor = getResources().getColor(R.color.black_trans40);

        // 测试数据
//        if (mAlarmInfoList.size() < 1) {
//            int []days = new int[]{1,2,3};
//
//            AlarmInfo alarmInfo = new AlarmInfo(21, 5, 1, 1, 1, "everybody.mp3", "测试", days);
//            mAlarmInfoList.add(alarmInfo);
//            mDao.addAlarmInfo(alarmInfo);
//            Log.d("ALARM_LIST", "new alarm");
//        }

        // 没有闹铃

        ifEmpty();

        // 绑定列表数据
        alarmListView.setLayoutManager(new LinearLayoutManager(mActivity));
        if (alarmListViewAdapter == null) {
            alarmListViewAdapter = new AlarmListViewAdapter();
            alarmListViewAdapter.setItemListener(new AlarmItemListener());
        }

        alarmListView.setAdapter(alarmListViewAdapter);


        Log.d("ALARM_LIST", "setAdapter");
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_alarm_list, null);
        alarmListView = (RecyclerView) view.findViewById(R.id.alarm_list_view);

        emptyView = (TextView) view.findViewById(R.id.empty_view);
        emptyView.setText("闹钟列表为空");

//        Intent intent=new Intent(mActivity, BootReceiver.class);
//        mActivity.sendBroadcast(intent);

        Log.d("ALARM_LIST", "initView");
        return view;
    }

    // 更新所有闹钟列表
    public void updateView() {
        mAlarmInfoList = mDao.getAllInfo();
        alarmListViewAdapter.notifyDataSetChanged();
    }

    // 更新一个闹钟
    public void updateAlarmAt(int position, AlarmInfo newAlarmInfo) {
        mDao.updateAlarm(newAlarmInfo);
        mAlarmInfoList.set(position, newAlarmInfo);
        alarmListViewAdapter.notifyItemChanged(position);
    }

    // 插入一个新的闹钟
    public void insertAlarm(AlarmInfo alarmInfo) {
        int position = mAlarmInfoList.size();
        mAlarmInfoList.add(alarmInfo);
        mDao.addAlarmInfo(alarmInfo);
        alarmListViewAdapter.notifyItemInserted(position);
        ifEmpty();
    }

    // 删除一个闹钟
    public void deleteAlarm(int position) {
        alarmListViewAdapter.notifyItemRemoved(position);
        ifEmpty();
    }

    // 列表为空时的视图
    public void ifEmpty() {
        if (mAlarmInfoList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }

    }

    // ViewAdapter
    public class AlarmListViewAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
        AlarmItemListener itemListener;

        @Override
        public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_alarm_item, parent, false);
            return new AlarmViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmViewHolder holder, final int position) {
            holder.bindData(position);

            if (itemListener != null) {
                // 绑定监听事件
                holder.mSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemListener.onItemClick(view, position);
                    }
                });
                holder.mTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemListener.onItemClick(view, position);
                    }
                });
                holder.mLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemListener.onItemClick(view, position);
                    }
                });
                holder.mItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemListener.onItemClick(view, position);
                    }
                });
                holder.mDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemListener.onItemClick(view, position);
                    }
                });
            }

            Log.d("ALARM_LIST", "BindViewHolder");
        }

        // 设置监听器
        public void setItemListener(AlarmItemListener alarmItemListener) {
            this.itemListener = alarmItemListener;
        }

        @Override
        public int getItemCount() {
            return mAlarmInfoList.size();
        }
    }

    // 子元素时间监听器
    public class AlarmItemListener {
        public void onItemClick(View view, final int position) {
            Log.d("alarmItemListener", "" + position);
            if (view.getId() == R.id.time) {
                // 更改时间
                NumberPadTimePickerDialog dialogTimeSet = NumberPadTimePickerDialog.newInstance(
                        new BottomSheetTimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(ViewGroup viewGroup, int hourOfDay, int minute) {
                                AlarmInfo alarmInfo = mAlarmInfoList.get(position);
                                alarmInfo.setHour(hourOfDay);
                                alarmInfo.setMinute(minute);
                                alarmInfo.setEnable(1);

                                // 更新
                                updateAlarmAt(position, alarmInfo);
                                MyAlarmManager.startAlarmClock(mActivity, alarmInfo);
                            }
                        }
                );
                dialogTimeSet.show(getActivity().getSupportFragmentManager(), "MainActivity");
            } else if(view.getId() == R.id.on_off_switch) {
                // 事件响应单独处理
            } else if(view.getId() == R.id.label){
                // 更改标签
                final AlarmInfo alarmInfo = mAlarmInfoList.get(position);
                new MaterialDialog.Builder(mActivity)
                        .title("编辑标签")
                        .negativeText("取消")
                        .positiveText("确定")
                        .inputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                        .inputRange(0, 10)
                        .input("标签", alarmInfo.getLabel(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                alarmInfo.setLabel(input.toString());
                            }
                        }).show();

                // 更新
                updateAlarmAt(position, alarmInfo);

            } else if(view.getId() == R.id.delete){

                // 删除
                final AlarmDeletePopWindow alarmDeletePopWindow;
                alarmDeletePopWindow = new AlarmDeletePopWindow(mActivity);
                alarmDeletePopWindow.showPopup(alarmListView);

                alarmDeletePopWindow.setOnDeleteAlarmListenr(new AlarmDeletePopWindow.DeleteAlarmListner() {
                    @Override
                    public void obtainMessage(int flag) {
                        AlarmInfo alarmInfo = mAlarmInfoList.get(position);
                        if (flag == 1) {
                            if (alarmInfo.getEnable() == 1)
                                MyAlarmManager.cancelAlarmClock(mActivity, alarmInfo);

                            // 删除闹铃
                            mAlarmInfoList.remove(position);
                            mDao.deleteAlarm(alarmInfo);
                            deleteAlarm(position);
                        }
                    }
                });
            } else {

                //
                // 详细编辑
                AlarmInfo alarmInfo = mAlarmInfoList.get(position);
                Intent addAlarmIntent = new Intent(getActivity(), AlarmAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ALARM_INFO, alarmInfo);
                bundle.putInt(Constants.ALARM_POSITION, position);
                addAlarmIntent.putExtras(bundle);
                Log.d("alarm", "into alarm edit");
                startActivityForResult(addAlarmIntent, Constants.UPDATE_ALARM);
            }
        }

        // 删除闹铃
//            public void onLongItemClick(View view) {
//                final AlarmDeletePopWindow alarmDeletePopWindow;
//                alarmDeletePopWindow = new AlarmDeletePopWindow(mActivity);
//                alarmDeletePopWindow.showPopup(alarmListView);
//
//                alarmDeletePopWindow.setOnDeleteAlarmListenr(new AlarmDeletePopWindow.DeleteAlarmListner() {
//                    @Override
//                    public void obtainMessage(int flag) {
//                        AlarmInfo alarmInfo = mAlarmInfoList.get(getAdapterPosition());
//                        if (flag == 1) {
//                            if (alarmInfo.getEnable() == 1)
//                                MyAlarmManager.cancelAlarmClock(mActivity, alarmInfo);
//
//                            // 删除闹铃
//                            mDao.deleteAlarm(alarmInfo);
//                            mAlarmInfoList.remove(getAdapterPosition());
//                            deleteAlarm(getAdapterPosition());
//                        }
//                    }
//                });
//            }
    }

    // ViewHolder
    public class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView mTime;
        TextView mLabel;
        TextView mRecurDays;
        TextView mWeatherIf;
        SwitchCompat mSwitch;
        View mItemView;
        ImageView mDelete;

        public AlarmViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;

            mTime = (TextView)itemView.findViewById(R.id.time);
            mLabel = (TextView)itemView.findViewById(R.id.label);
            mSwitch = (SwitchCompat)itemView.findViewById(R.id.on_off_switch);
            mRecurDays = (TextView)itemView.findViewById(R.id.recurring_days);
            mWeatherIf = (TextView)itemView.findViewById(R.id.tv_weather_if);
            mDelete = (ImageView) itemView.findViewById(R.id.delete);

            // 更改闹钟状态
            ///////////////////////////////////////////////////////////////////////////
            // 无法使用NotifyItemChanged
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int position = getAdapterPosition();
                    AlarmInfo alarmInfo = mAlarmInfoList.get(position);

                    if (!b) {
                        Log.d("alarm", "cancel alarm");
                        alarmInfo.setEnable(0);
                        MyAlarmManager.cancelAlarmClock(mActivity, alarmInfo);
                    } else {
                        Log.d("alarm", "start alarm");
                        alarmInfo.setEnable(1);
                        MyAlarmManager.startAlarmClock(mActivity, alarmInfo);
                    }
                    ///////////////////////////////////////////////////////////////////
                    // alarmInfo 的SID
                    mDao.updateAlarm(alarmInfo);
                    mAlarmInfoList.set(position, alarmInfo);
                    // bindData(position);
                    bindTime(alarmInfo);
                    bindLabel(alarmInfo);
                    bindRecuDays(alarmInfo);
                    bindWeatherIf(alarmInfo);
                }
            });
        }

        public void bindData(int position) {
            AlarmInfo alarmInfo = mAlarmInfoList.get(position);

            boolean enabled = (alarmInfo.getEnable() == 1);
            bindTime(alarmInfo);
            bindLabel(alarmInfo);
            bindSwitch(enabled);
            bindRecuDays(alarmInfo);
            bindWeatherIf(alarmInfo);
            Log.d("ALARM_LIST", "bindData" + position);
        }
        private void bindRecuDays(AlarmInfo alarmInfo) {
            int []recuDays = alarmInfo.getDayOfWeek();
            String sRecuDays = "";
            String[] weeks = {"周一","周二","周三","周四","周五","周六","周日"};
            if (recuDays.length == 7)
                sRecuDays = "每一天";
            else if (recuDays.length == 0)
                sRecuDays = "仅一次";
            else {
                for (int i = 0; i < recuDays.length; i++) {
                    int day = recuDays[i];
                    if (i == recuDays.length - 1) {
                        sRecuDays = sRecuDays + weeks[day-1];
                    } else {
                        sRecuDays = sRecuDays + weeks[day-1] + ", ";
                    }
                }
            }

            mRecurDays.setText(sRecuDays);
            mRecurDays.setTextColor(alarmInfo.getEnable() == 1 ? defColor : disColor);
        }
        private void bindLabel(AlarmInfo alarmInfo) {
            String label = alarmInfo.getLabel();
            if (label.length() > 0) {
                mLabel.setVisibility(View.VISIBLE);
                mLabel.setText(label);
                mLabel.setTextColor(alarmInfo.getEnable() == 1 ? defColor : disColor);
            }
            else
                mLabel.setVisibility(View.GONE);
        }
        private void bindWeatherIf(AlarmInfo alarmInfo) {
            String weatherIf = "";
            if (alarmInfo.getRain() == 1)
                weatherIf = "雨天取消";

            mWeatherIf.setText(weatherIf);
            mWeatherIf.setTextColor(alarmInfo.getEnable() == 1 ? defColor : disColor);
        }
        private void bindTime(AlarmInfo alarmInfo) {
            String hour=alarmInfo.getHour()+"";
            String minute=alarmInfo.getMinute()+"";
            if(alarmInfo.getHour()<10) hour="0"+alarmInfo.getHour();
            if(alarmInfo.getMinute()<10) minute = "0" + alarmInfo.getMinute();
            mTime.setText( hour+ ":" + minute);

            // 设置闹铃开启与关闭时的显示颜色
            mTime.setTextColor(alarmInfo.getEnable() == 1 ? defColor : disColor);
        }
        private void bindSwitch(boolean enabled) {
            mSwitch.setChecked(enabled);
        }
    }
    // End AlarmViewHolder
}
