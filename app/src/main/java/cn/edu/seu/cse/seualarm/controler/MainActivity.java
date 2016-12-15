package cn.edu.seu.cse.seualarm.controler;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.util.ArrayList;

import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.controler.alarm.AlarmAddActivity;
import cn.edu.seu.cse.seualarm.controler.alarm.AlarmListFragment;
import cn.edu.seu.cse.seualarm.module.AlarmInfo;
import cn.edu.seu.cse.seualarm.util.Constants;
import devlight.io.library.ntb.NavigationTabBar;

/**
 * Created by Coder Geass on 2016/12/8.
 */

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Fab mFab;
    private MaterialSheetFab materialSheetFab;
    private NavigationTabBar mNtb;
    private int lastSelected;

    private WeatherViewFragment weatherViewFragment;
    private AlarmListFragment alarmListFragment;
    private RunInfoFragment runInfoFragment;
    private AboutFragment aboutFragment;
    private boolean mIsChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastSelected = 1;
        initUI();
    }

    private void initUI() {
        mViewPager = (ViewPager)findViewById(R.id.container);

        // 减少卡顿
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        initNtb();
        initFtb();
    }

    // 初始化FAB
    private void initFtb() {
        mFab = (Fab) findViewById(R.id.fab);

        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.sienna);
        // 在XML文件中设置不支持API21以下
        mFab.setBackgroundTintList(ColorStateList.valueOf(fabColor));

        materialSheetFab = new MaterialSheetFab<>(mFab, sheetView, overlay, sheetColor, fabColor);

        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {

            }

            @Override
            public void onHideSheet() {

            }
        });

        findViewById(R.id.fab_sheet_item_run).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialSheetFab.hideSheet();
//                        startActivity(
//                                new Intent(MainActivity.this, AlarmAddActivity.class)
//                        );
                    }
                }
        );

        findViewById(R.id.fab_sheet_item_alarm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialSheetFab.hideSheet();
                        Intent addAlarmIntent = new Intent(MainActivity.this, AlarmAddActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.ALARM_INFO, new AlarmInfo());
                        bundle.putSerializable(Constants.ALARM_POSITION, -1);
                        addAlarmIntent.putExtras(bundle);
                        startActivityForResult(addAlarmIntent, Constants.ADD_ALARM);
                    }
                }
        );
    }

    // 初始化NTB
    private void initNtb() {
        final String[] colors = getResources().getStringArray(R.array.my_navigation_bar_colors);
        mNtb = (NavigationTabBar)findViewById(R.id.ntb);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_weather_white_24dp),
                        Color.parseColor(colors[0]))
                        .title("Weather")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_alarm_white_24dp),
                        Color.parseColor(colors[1]))
                        .title("Alarm")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_run_white_24dp),
                        Color.parseColor(colors[2]))
                        .title("Run")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_about_white_24dp),
                        Color.parseColor(colors[3]))
                        .title("About")
                        .build()
        );

        mNtb.setModels(models);
        mNtb.setViewPager(mViewPager, lastSelected);

        mNtb.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                updatePage(position);
                lastSelected = position;
//                mIsChanged = true;
//                lastSelected = position;
            }

            private void updatePage(int position) {
                switch (position) {
                    case 0:
                        materialSheetFab.hideSheetThenFab();
                        if (weatherViewFragment != null)
                            weatherViewFragment.mWeatherView.startAnimation();
                        break;
                    case 1:
                        materialSheetFab.showFab();
                        if (weatherViewFragment != null)
                            weatherViewFragment.mWeatherView.cancelAnimation();
                        break;
                    case 2:
                        materialSheetFab.showFab();
                        if (weatherViewFragment != null)
                            weatherViewFragment.mWeatherView.cancelAnimation();
                        break;
                    case 3:
                        materialSheetFab.hideSheetThenFab();
                        if (weatherViewFragment != null)
                            weatherViewFragment.mWeatherView.cancelAnimation();
                        break;
                    default:
                        materialSheetFab.hideSheetThenFab();
                        if (weatherViewFragment != null)
                            weatherViewFragment.mWeatherView.cancelAnimation();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
//                if(state==ViewPager.SCROLL_STATE_IDLE){
//                    if(mIsChanged){
//                        mIsChanged = false;
//                        updatePage(lastSelected);
//                    }
//                }
            }
        });

//        mNtb.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < mNtb.getModels().size(); i++) {
//                    final NavigationTabBar.Model model = mNtb.getModels().get(i);
//                    mNtb.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            model.showBadge();
//                        }
//                    }, i * 100);
//                }
//            }
//        }, 500);
    }

    @Override
    public void onBackPressed() {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
        }
    }

    //////////////////////////////////////////////////////////////////////
    // FragmentPagerAdapter??????????????????????????????????
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (weatherViewFragment == null)
                        weatherViewFragment = new WeatherViewFragment();
                    lastSelected = 0;
                    return weatherViewFragment;
                case 1:
                    if (alarmListFragment == null)
                        alarmListFragment = new AlarmListFragment();
                    lastSelected = 1;
                    return alarmListFragment;
                case 2:
                    if (runInfoFragment == null)
                        runInfoFragment = new RunInfoFragment();
                    lastSelected = 2;
                    return runInfoFragment;
                case 3:
                    if (aboutFragment == null)
                        aboutFragment = new AboutFragment();
                    lastSelected = 3;
                    return aboutFragment;
                default:
                    if (weatherViewFragment == null)
                        weatherViewFragment = new WeatherViewFragment();
                    lastSelected = 0;
                    return weatherViewFragment;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "WeatherView";
                case 1:
                    return "AlarmView";
                case 2:
                    return "RunView";
                case 4:
                    return "AboutView";
                default:
                    return "WeatherView";
            }
        }

//        @Override
//        public boolean isViewFromObject(final View view, final Object object) {
//            return view.equals(object);
//        }
//
//        @Override
//        public void destroyItem(final View container, final int position, final Object object) {
//            ((ViewPager) container).removeView((View) object);
//        }
//
//        @Override
//        public Object instantiateItem(final ViewGroup container, final int position) {
//            final View view = LayoutInflater.from(
//                    getBaseContext()).inflate(R.layout.item_vp, null, false);
//
//            final TextView txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
//            txtPage.setText(String.format("Page #%d", position));
//
//            container.addView(view);
//            return view;
//        }
    }

    // 更新视图
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        Log.d("alarm", "main activity reauestcode[" + requestCode + "]resultcode[" + resultCode+"]");
        if (requestCode == Constants.ADD_ALARM && resultCode == Constants.ADD_ALARM_DONE) {
            Bundle bundle = data.getExtras();
            Log.d("alarm", "main activity get resultcode" + resultCode);
            AlarmInfo alarmInfo = (AlarmInfo) bundle.getSerializable(Constants.ALARM_INFO);
            Log.d("alarm", "add alarm done get");
            if (alarmInfo != null)
                alarmListFragment.insertAlarm(alarmInfo);
        } else if (/*requestCode == Constants.UPDATE_ALARM && */resultCode == Constants.UPDATE_ALARM_DONE) {
            Bundle bundle = data.getExtras();
            Log.d("alarm", "main activity get resultcode" + resultCode);
            AlarmInfo alarmInfo = (AlarmInfo) bundle.getSerializable(Constants.ALARM_INFO);
            Log.d("alarm", "update alarm done get");
            int position = bundle.getInt(Constants.ALARM_POSITION, -1);
            if (position != -1)
                alarmListFragment.updateAlarmAt(position, alarmInfo);
        }

    }
//    public class JustPagerAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return 4;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return false;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            View view;
//            switch (position) {
//                case 0:
//                    if (weatherViewFragment == null)
//                        weatherViewFragment = new WeatherViewFragment();
//                    lastSelected = 0;
//                    view = weatherViewFragment.mView;
//                    break;
//                case 1:
//                    if (alarmListFragment == null)
//                        alarmListFragment = new AlarmListFragment();
//                    lastSelected = 1;
//                    view = alarmListFragment.mView;
//                    break;
//                case 2:
//                    if (runInfoFragment == null)
//                        runInfoFragment = new RunInfoFragment();
//                    lastSelected = 2;
//                    view = runInfoFragment.mView;
//                    break;
//                case 3:
//                    if (aboutFragment == null)
//                        aboutFragment = new AboutFragment();
//                    lastSelected = 3;
//                    view = aboutFragment.mView;
//                    break;
//                default:
//                    if (weatherViewFragment == null)
//                        weatherViewFragment = new WeatherViewFragment();
//                    view = weatherViewFragment.mView;
//            }
//
//            //container.addView(view);
//            return view;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
//    }
}


