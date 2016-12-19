package cn.edu.seu.cse.seualarm.controler.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.philliphsu.bottomsheetpickers.time.BottomSheetTimePickerDialog;
import com.philliphsu.bottomsheetpickers.time.numberpad.NumberPadTimePickerDialog;

import java.util.ArrayList;
import java.util.List;

import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.controler.MainActivity;
import cn.edu.seu.cse.seualarm.dao.AlarmInfoDao;
import cn.edu.seu.cse.seualarm.module.AlarmInfo;
import cn.edu.seu.cse.seualarm.util.Constants;

/**
 * Created by Coder Geass on 2016/12/10.
 */

public class AlarmAddActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout time_rl, label_rl, ring_rl;
    private TextView tv_time_value, tv_repeat_value, tv_ring_value, tv_label_value;
    private SwitchCompat weather_if, vibrator_if;
    private List<ToggleButton> tb_weeks;
    //Toolbar toolbar;
    private Button set_btn;
    private Button cel_btn;
    private AlarmInfo alarmInfo;
    private boolean[] bWeeks;
    private int defColor;
    private int disColor;
    private boolean isNew;
    private String ringID;
    private int position;
    private boolean vibrate;
    private boolean rain;
    private AlarmAddActivity alarmAddActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);
        defColor = getResources().getColor(R.color.black);
        disColor = getResources().getColor(R.color.black_trans40);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//        }
        set_btn = (Button) findViewById(R.id.alarm_add_ok);
        cel_btn = (Button) findViewById(R.id.alarm_add_cancel);
        set_btn.setOnClickListener(this);
        cel_btn.setOnClickListener(this);

        time_rl = (RelativeLayout) findViewById(R.id.time_rl);
        time_rl.setOnClickListener(this);
        label_rl = (RelativeLayout) findViewById(R.id.label_rl);
        label_rl.setOnClickListener(this);
        ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        ring_rl.setOnClickListener(this);

        weather_if = (SwitchCompat) findViewById(R.id.weather_if);
        vibrator_if = (SwitchCompat) findViewById(R.id.vibrator_if);
        weather_if.setOnClickListener(this);
        vibrator_if.setOnClickListener(this);

        tv_time_value = (TextView) findViewById(R.id.tv_time_value);
        tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
        tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);
        tv_label_value = (TextView) findViewById(R.id.tv_label_value);

        tb_weeks = new ArrayList<>();
        tb_weeks.add((ToggleButton) findViewById(R.id.day0));
        tb_weeks.add((ToggleButton) findViewById(R.id.day1));
        tb_weeks.add((ToggleButton) findViewById(R.id.day2));
        tb_weeks.add((ToggleButton) findViewById(R.id.day3));
        tb_weeks.add((ToggleButton) findViewById(R.id.day4));
        tb_weeks.add((ToggleButton) findViewById(R.id.day5));
        tb_weeks.add((ToggleButton) findViewById(R.id.day6));

        for (int i = 0; i < 7; i++)
            tb_weeks.get(i).setTextColor(disColor);

        /*String []sWeeks = new String[] { "周一","周二","周三","周四","周五","周六","周日" };
        for (int i = 0; i< 7; i++) {
            tb_weeks.get(i).setTextOff(sWeeks[i]);
            tb_weeks.get(i).setTextOn(sWeeks[i]);
        }*/

        bWeeks = new boolean[]{false, false, false, false, false, false, false};

        ////////////////////////////////////////////////////////////////////////////////////////
        // 重复日期选择
        // 设置监听事件
        tb_weeks.get(0).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bWeeks[0] = b;
                Log.d("weeks", "down 0" + b);
                updateDays(0);
            }
        });
        tb_weeks.get(1).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bWeeks[1] = b;
                Log.d("weeks", "down 1" + b);
                updateDays(1);
            }
        });
        tb_weeks.get(2).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bWeeks[2] = b;
                Log.d("weeks", "down 2");
                updateDays(2);
            }
        });
        tb_weeks.get(3).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bWeeks[3] = b;
                Log.d("weeks", "down 3");
                updateDays(3);
            }
        });
        tb_weeks.get(4).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bWeeks[4] = b;
                Log.d("weeks", "down 4");
                updateDays(4);
            }
        });
        tb_weeks.get(5).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bWeeks[5] = b;
                Log.d("weeks", "down 5");
                updateDays(5);
            }
        });
        tb_weeks.get(6).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bWeeks[6] = b;
                Log.d("weeks", "down 6");
                updateDays(6);
            }
        });

        isNew = true;
        Bundle bundle = getIntent().getExtras();
        alarmInfo = (AlarmInfo) bundle.getSerializable(Constants.ALARM_INFO);
        position = bundle.getInt(Constants.ALARM_POSITION, -1);

//        alarmInfo = (AlarmInfo) savedInstanceState.getSerializable(Constants.ALARM_INFO);
//        position = savedInstanceState.getInt(Constants.ALARM_POSITION, -1);

        if (position == -1) {
            Log.d("alarm", "get add alarm call");
            isNew = true;
//            initView();
        } else {
            isNew = false;
            Log.d("alarm", alarmInfo.getSId());
            Log.d("alarm", alarmInfo.toString());

//            ringID = alarmInfo.getRingResId();
//            bindData(alarmInfo);
//            bindWeeks(alarmInfo.getDayOfWeek());
//            bindDays(alarmInfo.getDayOfWeek());
        }

        initView();
        alarmAddActivity = this;
    }

    // 新建闹铃
    private void initView() {
//        alarmInfo = new AlarmInfo();
        ringID = alarmInfo.getRingResId();
        bindData(alarmInfo);
        bindWeeks(alarmInfo.getDayOfWeek());
        bindDays(alarmInfo.getDayOfWeek());
    }

    // 绑定数据到界面
    public void bindData(AlarmInfo alarmInfo) {
        String sTime = getFullTime(alarmInfo.getHour(), alarmInfo.getMinute());
        String sRing = alarmInfo.getRingtone();
        String sLabel = alarmInfo.getLabel();
        vibrate = alarmInfo.getVibrate() == 1;
        rain = alarmInfo.getRain() == 1;

        tv_time_value.setText(sTime);
        tv_ring_value.setText(sRing);
        tv_label_value.setText(sLabel);
        vibrator_if.setChecked(vibrate);
        weather_if.setChecked(rain);
    }

    // 绑定boolean[] weeks
    private void bindWeeks(int[] dayOfWeek) {
        for (int d : dayOfWeek) {
            bWeeks[d-1] = true;
            Log.d("day", ""+d);
        }
    }

    // 初始化绑定重复天数数据
    // 需要先绑定bweeks
    private void bindDays(int []weeks) {
        String sweeks = getStrWeeks(weeks);
        Log.d("sweeks", sweeks);
        tv_repeat_value.setText(sweeks);

        for (int i = 0; i < 7; i++) {
            tb_weeks.get(i).setChecked(bWeeks[i]);
        }
    }

    // 更新重复天数数据
    private void updateDays(int day) {
        String sweeks = getStrWeeks(getWeeks());
        tv_repeat_value.setText(sweeks);

        if (bWeeks[day]) {
            tb_weeks.get(day).setTextColor(defColor);
            //tb_weeks.get(i).setBackgroundColor(defColor);
        } else {
            tb_weeks.get(day).setTextColor(disColor);
            //tb_weeks.get(i).setBackgroundColor(bacColor);
        }
    }

    // 由bweeks得到int[] weeks
    private int[] getWeeks() {
        String []sWeeks = new String[] { "1","2","3","4","5","6","7" };
        String strWeeks = "";

        for (int i = 0; i < 7; i++) {
            if(bWeeks[i]) {
                strWeeks += sWeeks[i] + ",";
            }
        }

        Log.d("weeks", strWeeks);
        return AlarmInfoDao.getAlarmDayofWeek(strWeeks);
    }

    // 获取字符串重复天数
    private String getStrWeeks(int []weeks) {
        String[] sweeks = {"周一","周二","周三","周四","周五","周六","周日"};
        String sRecuDays = "";

        if (weeks.length == 7)
            sRecuDays = "每一天";
        else if (weeks.length == 0)
            sRecuDays = "仅一次";
        else {
            for (int i = 0; i < weeks.length; i++) {
                int day = weeks[i];
                if (i == weeks.length - 1) {
                    sRecuDays = sRecuDays + sweeks[day-1];
                } else {
                    sRecuDays = sRecuDays + sweeks[day-1] + ", ";
                }
            }
        }

        return sRecuDays;
    }

    // 获取HH:MM格式时间
    private String getFullTime(int h,  int m) {
        String sh = "" + h;
        String sm = "" + m;
        if (sh.length() == 1) sh = "0" + sh;
        if (sm.length() == 1) sm = "0" + sm;

        String sTime = "" + sh + ":" + sm;
        return sTime;
    }
    // 监听事件
    @Override
    public void onClick(View view) {
        ViewCompat.animate(view)
                .setDuration(200)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setInterpolator(new CycleInterpolator())
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {

                    }

                    @Override
                    public void onAnimationEnd(final View view) {
                        switch (view.getId()) {
                            ////////////////////////////////////////////////////////////////////////////
                            // 更改时间
                            case R.id.time_rl:
                                NumberPadTimePickerDialog dialog = NumberPadTimePickerDialog.newInstance(
                                        new BottomSheetTimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(ViewGroup viewGroup, int hourOfDay, int minute) {
                                                alarmInfo.setHour(hourOfDay);
                                                alarmInfo.setMinute(minute);
                                                String stime = getFullTime(hourOfDay, minute);
                                                tv_time_value.setText(stime);
                                            }
                                        }
                                );
                                dialog.show(getSupportFragmentManager(), "AlarmAddActivity");
                                break;

                            /////////////////////////////////////////////////////////////////////////////
                            // 更改标签
                            case R.id.label_rl:
                                // 更改标签
                                new MaterialDialog.Builder(alarmAddActivity)
                                        .title("编辑标签")
                                        .negativeText("取消")
                                        .positiveText("确定")
                                        .inputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                                        .inputRange(0, 10)
                                        .input("标签", alarmInfo.getLabel(), new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                                alarmInfo.setLabel(input.toString());
                                                tv_label_value.setText(input.toString());
                                            }
                                        }).show();
                                break;

                            /////////////////////////////////////////////////////////////////////////////
                            // 更改闹铃
                            case R.id.ring_rl:
                                Bundle bundle= new Bundle();
                                Intent ringSetItent = new Intent(AlarmAddActivity.this, RingSetActivity.class);
                                bundle.putString(Constants.RING_NAME, alarmInfo.getRingtone());
                                bundle.putString(Constants.RING_ID, alarmInfo.getRingResId());
                                ringSetItent.putExtras(bundle);
                                startActivityForResult(ringSetItent, Constants.ASK_RING_SET);

                                break;

                            //////////////////////////////////////////////////////////////////////////////
                            // 震动
                            case R.id.vibrator_if:
                                vibrate = !vibrate;
                                break;

                            //////////////////////////////////////////////////////////////////////////////
                            // 雨天取消
                            case R.id.weather_if:
                                rain = !rain;
                                break;

                            /////////////////////////////////////////////////////////////////////////////
                            // 取消
                            case R.id.alarm_add_cancel:
                                // 取消后活动清除
                                finish();
                                break;

                            /////////////////////////////////////////////////////////////////////////////
                            // 完成
                            case R.id.alarm_add_ok:
                                setClock();
                                finish();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onAnimationCancel(final View view) {

                    }
                })
                .withLayer()
                .start();
    }

    // 点击动画
    private class CycleInterpolator implements android.view.animation.Interpolator {

        private final float mCycles = 0.5f;

        @Override
        public float getInterpolation(final float input) {
            return (float) Math.sin(2.0f * mCycles * Math.PI * input);
        }
    }

    private void setClock() {
        // 完成闹钟更新或新建
        alarmInfo.setEnable(1);
        alarmInfo.setRingtone(tv_ring_value.getText().toString());
        alarmInfo.setRingResId(ringID);
        alarmInfo.setLabel(tv_label_value.getText().toString());
        alarmInfo.setVibrate(vibrate ? 1 : 0);
        alarmInfo.setRain(rain ? 1 : 0);
        alarmInfo.setDayOfWeek(getWeeks());

//        AlarmInfoDao dao = new AlarmInfoDao(this);
//        if (isNew) {
//            Log.d("alarm", "add alarm");
//            dao.addAlarmInfo(alarmInfo);
//        }
//        else {
//            Log.d("alarm", "update alarm");
//            dao.updateAlarm(alarmInfo);
//        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ALARM_POSITION, position);
        bundle.putSerializable(Constants.ALARM_INFO, alarmInfo);
        intent.putExtras(bundle);
//        intent.setClass(this, MainActivity.class);
        if (isNew) {
            Log.d("alarm", "add alarm send");
            setResult(Constants.ADD_ALARM_DONE, intent);
        } else {
            Log.d("alarm", "update alarm send");
            setResult(Constants.UPDATE_ALARM_DONE, intent);
        }
        ///////////////////////////////////
        //startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ASK_RING_SET && resultCode == Constants.RING_SET_DONE) {
            String ringName = data.getStringExtra(Constants.RING_NAME);
            ringID = data.getStringExtra(Constants.RING_ID);
            tv_ring_value.setText(ringName);
            alarmInfo.setRingResId(ringID);
            alarmInfo.setRingtone(ringName);
            Log.d("alarm", ringName + ":" + ringID);
            Toast.makeText(this, "铃声已设置", Toast.LENGTH_SHORT).show();
        }
    }
}

