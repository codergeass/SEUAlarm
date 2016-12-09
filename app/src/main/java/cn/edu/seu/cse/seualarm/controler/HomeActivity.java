package cn.edu.seu.cse.seualarm.controler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;

import android.support.v7.widget.Toolbar;

import cn.edu.seu.cse.seualarm.R;

/**
 * Created by Coder Geass on 2016/12/8.
 */
public class HomeActivity extends AppCompatActivity {
    int lastSelectedPosition = 0;
    ListViewFragment mListViewFragment;
    RecyclerViewFragment mRecyclerViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
//
//        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
//
//        bottomNavigationBar
//                .addItem(new BottomNavigationItem(R.drawable.ic_weather_white_24dp, "Weather").setActiveColor(R.color.colorBlue))
//                .addItem(new BottomNavigationItem(R.drawable.ic_alarm_white_24dp, "Alarm").setActiveColor(R.color.colorRed))
//                .addItem(new BottomNavigationItem(R.drawable.ic_run_white_24dp, "Run").setActiveColor(R.color.colorPurple))
//                .addItem(new BottomNavigationItem(R.drawable.ic_about_white_24dp, "About").setActiveColor(R.color.colorGray))
//                .setFirstSelectedPosition(lastSelectedPosition)
//                .initialise();
//
//        bottomNavigationBar.setTabSelectedListener(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

//    private void setDefaultFragment() {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        if (mListViewFragment == null)
//        mListViewFragment = new ListViewFragment();
//        transaction.replace(R.id.tb, mListViewFragment);
//        transaction.commit();
//    }
//
//    @Override
//    public void onTabSelected(int position) {
//        Log.d("TabSelected", "onTabSelected() called with: " + "position = [" + position + "]");
//        FragmentManager fm = this.getFragmentManager();
//        //开启事务
//        FragmentTransaction transaction = fm.beginTransaction();
//        switch (position) {
//            case 0:
//                if (mListViewFragment == null) {
//                    mListViewFragment = new ListViewFragment();
//                    transaction.replace(R.id.tb, mListViewFragment);
//                } else {
//                    transaction.show(mListViewFragment);
//                }
//
//                break;
//            case 1:
//                if (mRecyclerViewFragment == null) {
//                    mRecyclerViewFragment = new RecyclerViewFragment();
//                }
//                transaction.replace(R.id.tb, mRecyclerViewFragment);
//                break;
//            case 2:
//                if (mListViewFragment == null) {
//                    mListViewFragment = new ListViewFragment();
//                }
//                transaction.replace(R.id.tb, mListViewFragment);
//                break;
//            case 3:
//                if (mListViewFragment == null) {
//                    mListViewFragment = new ListViewFragment();
//                }
//                transaction.replace(R.id.tb, mListViewFragment);
//                break;
//            default:
//                break;
//        }
//        // 事务提交
//        transaction.commit();
//    }
//
//    @Override
//    public void onTabUnselected(int position) {
//        Log.d("TabUnselected", "onTabUnselected() called with: " + "position = [" + position + "]");
//    }
//
//    @Override
//    public void onTabReselected(int position) {
//        Log.d("TabSelected", "onTabUnselected() called with: " + "position = [" + position + "]");
//    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ListViewFragment();
                case 1:
                default:
                    return new RecyclerViewFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ListView";
                case 1:
                default:
                    return "RecyclerView";
            }
        }
    }

}
