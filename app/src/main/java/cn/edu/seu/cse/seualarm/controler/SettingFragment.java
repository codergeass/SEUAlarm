package cn.edu.seu.cse.seualarm.controler;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.util.Constants;
import cn.edu.seu.cse.seualarm.util.PrefUtil;

public class SettingFragment extends BaseFragment implements View.OnClickListener{
    private RelativeLayout run_rl, run_reset_rl, city_rl;
    private TextView tv_run_value, tv_weather_src_value, tv_city_value;
    private SwitchCompat alarm_weather_if;
    private RadioButton []srcs;
    private int src;
    private boolean alarm_weather;
    private String runDest, weather_src, cityName, ipAddress;
    private String []ssrc = new String[]{ "公共网络", "局域网络", "公共气象源"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int totalTimes = PrefUtil.getInt(getContext(), Constants.TOTAL_TIMES, 45);
        alarm_weather = PrefUtil.getBoolean(getContext(), Constants.ALARM_WEATHER, true);
        src = PrefUtil.getInt(getContext(), Constants.WEATHER_SRC, 0);
        cityName = PrefUtil.getString(getContext(), Constants.CITY_NAME, "南京");
        ipAddress = PrefUtil.getString(getContext(), Constants.IP_WEB, "223.3.173.237");
        srcs = new RadioButton[3];
        runDest = "" + totalTimes;
        weather_src = src == 1 ? ipAddress : ssrc[src];

        super.onCreate(savedInstanceState);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_setting, null);

        city_rl = (RelativeLayout) view.findViewById(R.id.city_rl);
        run_rl = (RelativeLayout) view.findViewById(R.id.run_rl);
        run_reset_rl = (RelativeLayout) view.findViewById(R.id.run_reset_rl);
        city_rl.setOnClickListener(this);
        run_rl.setOnClickListener(this);
        run_reset_rl.setOnClickListener(this);

        tv_city_value = (TextView) view.findViewById(R.id.tv_city_value);
        tv_run_value = (TextView) view.findViewById(R.id.tv_run_value);
        tv_weather_src_value = (TextView) view.findViewById(R.id.tv_weather_src_value);

        alarm_weather_if = (SwitchCompat) view.findViewById(R.id.alarm_weather_if);
        alarm_weather_if.setOnClickListener(this);

        srcs[0] = (RadioButton) view.findViewById(R.id.rb_common_net);
        srcs[1] = (RadioButton) view.findViewById(R.id.rb_local_net);
        srcs[2] = (RadioButton) view.findViewById(R.id.rb_other_net);
        srcs[0].setOnClickListener(this);
        srcs[1].setOnClickListener(this);
        srcs[2].setOnClickListener(this);

        return view;
    }

    @Override
    protected void initData() {
        srcs[src].setChecked(true);
        tv_weather_src_value.setText(weather_src);
        tv_run_value.setText(runDest);
        tv_city_value.setText(cityName);
        alarm_weather_if.setChecked(alarm_weather);

        super.initData();
    }

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
                            case R.id.rb_common_net:
                                src = 0;
                                srcs[1].setChecked(false);
                                srcs[2].setChecked(false);
                                PrefUtil.putInt(getContext(), Constants.WEATHER_SRC, src);
                                weather_src = ssrc[src];
                                tv_weather_src_value.setText(ssrc[src]);
                                break;
                            case R.id.rb_local_net:
                                src = 1;
                                srcs[0].setChecked(false);
                                srcs[2].setChecked(false);

                                new MaterialDialog.Builder(getContext())
                                        .title("设置网络地址")
                                        .negativeText("取消")
                                        .positiveText("确定")
                                        .inputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS)
                                        .inputRange(0, 15)
                                        .input("输入网络IP地址", PrefUtil.getString(getContext(), Constants.IP_WEB, "223.3.173.237"), new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                            }
                                        })
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                ipAddress = dialog.getInputEditText().getText().toString();
                                                PrefUtil.putString(getContext(), Constants.IP_WEB, ipAddress);
                                                weather_src = ipAddress;
                                                tv_weather_src_value.setText(ipAddress);
                                            }
                                        })
                                        .show();

                                PrefUtil.putInt(getContext(), Constants.WEATHER_SRC, src);
                                break;

                            case R.id.rb_other_net:
                                src = 2;
                                srcs[0].setChecked(false);
                                srcs[1].setChecked(false);
                                PrefUtil.putInt(getContext(), Constants.WEATHER_SRC, src);
                                weather_src = ssrc[src];
                                tv_weather_src_value.setText(ssrc[src]);
                                break;

                            // 天气预报城市
                            case R.id.city_rl:
                                new MaterialDialog.Builder(getContext())
                                        .title("设置天气预报城市")
                                        .negativeText("取消")
                                        .positiveText("确定")
                                        .inputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS)
                                        .inputRange(0, 15)
                                        .input("输入城市名", PrefUtil.getString(getContext(), Constants.CITY_NAME, "南京"), new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                            }
                                        })
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                cityName = dialog.getInputEditText().getText().toString();
                                                tv_city_value.setText(cityName);
                                                PrefUtil.putString(getContext(), Constants.CITY_NAME, dialog.getInputEditText().getText().toString());
                                            }
                                        })
                                        .show();
                                break;

                            ////////////////////////////////////////////////////////////////////////////
                            // 更改跑操目标
                            case R.id.run_rl:
                                new MaterialDialog.Builder(getContext())
                                        .title("设置跑操目标")
                                        .negativeText("取消")
                                        .positiveText("确定")
                                        .inputType(InputType.TYPE_NUMBER_FLAG_SIGNED)
                                        .inputRange(0, 10)
                                        .input("跑操目标", runDest, new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                            }
                                        })
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog,
                                                                @NonNull DialogAction which) {
                                                runDest = dialog.getInputEditText().getText().toString();
                                                int total = Integer.valueOf(runDest);
                                                PrefUtil.putInt(getContext(), Constants.TOTAL_TIMES, total);
                                                tv_run_value.setText(runDest);
                                            }
                                        })
                                        .show();
                                break;

                            /////////////////////////////////////////////////////////////////////////////
                            // 重置跑操数据
                            case R.id.run_reset_rl:
                                final AlertPopWindow alertPopWindow;
                                alertPopWindow = new AlertPopWindow(mActivity);
                                alertPopWindow.setHint("确认重置跑操信息？");
                                alertPopWindow.showPopup(getView());

                                alertPopWindow.setOnAlertListenr(new AlertPopWindow.AlertListner() {
                                    @Override
                                    public void obtainMessage(int flag) {
                                        if (flag == 1) {
                                            PrefUtil.putInt(getContext(), Constants.RUN_TIMES, 0);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(),
                                                            "跑操信息已重置", Toast.LENGTH_SHORT).show();
                                                }
                                            }, 500);
                                        }
                                    }
                                });
                                break;

                            //////////////////////////////////////////////////////////////////////////////
                            // 闹钟提示天气信息设置
                            case R.id.alarm_weather_if:
                                alarm_weather = !alarm_weather;
                                PrefUtil.putBoolean(getContext(), Constants.ALARM_WEATHER, alarm_weather);
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

    private class CycleInterpolator implements android.view.animation.Interpolator {

        private final float mCycles = 0.5f;

        @Override
        public float getInterpolation(final float input) {
            return (float) Math.sin(2.0f * mCycles * Math.PI * input);
        }
    }
}
