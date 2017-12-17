package cn.edu.seu.cse.seualarm.controler;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.module.WeatherInfo;
import cn.edu.seu.cse.seualarm.util.Constants;
import cn.edu.seu.cse.seualarm.util.PrefUtil;
import cn.edu.seu.cse.seualarm.util.WeatherInfoClient;

/**
 * Created by Coder Geass on 2016/12/15.
 */

public class SplashActivity extends Activity {
    private WeatherInfo weatherInfo = new WeatherInfo();
    private boolean loadOk = false;
    private int src = 0;
    private TextView seualarmName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        src = PrefUtil.getInt(getApplicationContext(), Constants.WEATHER_SRC, 0);

        // 解决初次安装后打开后按home返回后重新打开重启问题。。。
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        overridePendingTransition(R.anim.zoomin, 0);
        setContentView(R.layout.activity_welcome);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 状态栏透明
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        iniView();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void iniView() {
        if (src == 1)
            WeatherInfoClient.getLocalWeatherInfo(
                    PrefUtil.getString(getApplicationContext(), Constants.IP_WEB, "223.3.173.237"));
        else if (src == 2)
            WeatherInfoClient.getPublicWeatherInfo(
                    PrefUtil.getString(getApplicationContext(), Constants.CITY_NAME, "南京"));
        else
            WeatherInfoClient.getWeathInfo();

        seualarmName = (TextView) findViewById(R.id.seualarm_name);
        Typeface fontFace = Typeface.createFromAsset(getResources().getAssets(), "fonts/FELIXTI.TTF");
        seualarmName.setTypeface(fontFace);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (WeatherInfoClient.resCode) {
                    weatherInfo = WeatherInfoClient.weatherInfo;
                    loadOk = true;
                }
                else
                    loadOk = false;
                Log.d("alarm", "splash activity run loadok:" + loadOk);
            }
        }, 2000);

        startAnimation();
    }

    private void startAnimation() {
        final View splashIv = findViewById(R.id.seualarm_logo);

        ValueAnimator animator = ValueAnimator.ofObject(new FloatEvaluator(), 1.0f, 1.2f);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                if (value != 1.2f) {
                    splashIv.setScaleX(value);
                    splashIv.setScaleY(value);
                } else {
                    goToActivity();
                }
            }

            private void goToActivity() {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.WEATHER_INFO, weatherInfo);
                bundle.putBoolean(Constants.LOAD_OK, loadOk);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtras(bundle);
                Log.d("alarm", "splash activity loadok:" + loadOk);
                startActivity(intent);
                overridePendingTransition(0, android.R.anim.fade_out);
                finish();
            }
        });
        animator.start();
    }

//    private void setSlogan() {
//        try {
//            AssetManager mgr = getAssets();
//            Typeface fontFace = Typeface.createFromAsset(mgr, "fonts/weac_slogan.ttf");
////            TextView SloganTv = (TextView) findViewById(R.id.weac_slogan_tv);
////            SloganTv.setTypeface(fontFace);
//        } catch (Exception e) {
//            Log.e(LOG_TAG, "Typeface.createFromAsset: " + e.toString());
//        }
//    }
//
//    private void setVersion() {
////        TextView versionTv = (TextView) findViewById(R.id.version_tv);
////        versionTv.setText(getString(R.string.weac_version, MyUtil.getVersion(this)));
//    }
}
