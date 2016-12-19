package cn.edu.seu.cse.seualarm.controler;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
//import android.content.Context;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.Toast;

//import com.github.pwittchen.weathericonview.WeatherIconView;
//import com.kelin.calendarlistview.library.BaseCalendarItemAdapter;
//import com.kelin.calendarlistview.library.BaseCalendarItemModel;
//import com.kelin.calendarlistview.library.BaseCalendarListAdapter;
//import com.kelin.calendarlistview.library.CalendarHelper;
//import com.kelin.calendarlistview.library.CalendarListView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
//import java.util.TreeMap;

import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.dao.RunInfoDao;
//import cn.edu.seu.cse.seualarm.module.CustomRing;
import cn.edu.seu.cse.seualarm.module.RunInfo;
import cn.edu.seu.cse.seualarm.module.WeatherInfo;
import cn.edu.seu.cse.seualarm.util.Constants;
import cn.edu.seu.cse.seualarm.util.PrefUtil;

/**
 * Created by Coder Geass on 2016/12/9.
 */

public class RunInfoFragment extends BaseFragment{
    private MaterialCalendarView materialCalendarView;
    private TextView mRun, mLeft;
    private List<RunInfo> runInfoList;
    private List<CalendarDay> calendarDays = new ArrayList<>();
    private RunInfoDao runInfoDao;
    private int runTimes = 0;
    private int leftTimes;
    private int totalTimes;

    public static final SimpleDateFormat YEAR_MONTH_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        runInfoDao = new RunInfoDao(getActivity());
        runInfoList = runInfoDao.getAllRunInfo();
        runTimes = PrefUtil.getInt(getContext(), Constants.RUN_TIMES, runInfoList.size());
        totalTimes = PrefUtil.getInt(getContext(), Constants.TOTAL_TIMES, 45);
        leftTimes = totalTimes - runTimes;

        // 测试数据
        if (runInfoList.size() == 0) {
            String []rain = new String[]{"晴", "雨"};
            String []mems0 = new String[]{"今天","明天","我","小明","大家", "同学"};
            String []mems1 = new String[]{"去","要","很想","不能","忘了", "准备"};
            String []mems2 = new String[]{"吃饭","打篮球","看电影","看小说","跑步", "熬夜"};
            for (int i = 0; i < 40; i++) {
                RunInfo runInfo = new RunInfo();
                WeatherInfo weatherInfo = new WeatherInfo();
                Random random = new Random();
                String date = "2016-" + (7 + random.nextInt(5)) + "-" + (1+random.nextInt(29));
                weatherInfo.setmRain(rain[random.nextInt(2)]);
                runInfo.setRunDate(getDateByYearMonthDay(date));
                runInfo.setRunMem(mems0[random.nextInt(6)] + mems1[random.nextInt(6)] + mems2[random.nextInt(6)]);
                runInfo.setIsRun(1);
                runInfo.setRunWeather(weatherInfo);
                runInfoList.add(runInfo);
            }

            Collections.sort(runInfoList);
            for (int i = 0; i < runInfoList.size(); i++) {
                runInfoList.get(i).setRunTimes(i+1);
                runInfoDao.addRunInfo(runInfoList.get(i));
            }

            runTimes = runInfoList.size();
            leftTimes = totalTimes - runTimes;
            PrefUtil.putInt(getContext(), Constants.RUN_TIMES, runTimes);
        }

        for (RunInfo r : runInfoList) {
            calendarDays.add(new CalendarDay(r.getRunDate()));
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_run_info, null);
        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        mRun = (TextView) view.findViewById(R.id.tv_days_run);
        mLeft = (TextView) view.findViewById(R.id.tv_days_left);

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                for (RunInfo r : runInfoList) {
                    String s = YEAR_MONTH_DAY_FORMAT.format(r.getRunDate());
                    String d = YEAR_MONTH_DAY_FORMAT.format(date.getDate());
                    Log.d("alarm", "get select");
                    if (s.equals(d)) {
                        new MaterialDialog.Builder(getContext())
                                .title("跑操心情记录")
                                .positiveText("确定")
                                .negativeText("取消")
                                .content(r.getRunMem())
                                .show();
                        break;
                    }
                }
            }
        });

        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        updateView();
    }

    public void addRunInfo(RunInfo runInfo) {
        runTimes++;
        leftTimes--;
        runInfoList.add(runInfo);
        calendarDays.add(new CalendarDay(runInfo.getRunDate()));
        runInfoDao.addRunInfo(runInfo);
    }

    public void updateView() {
//        materialCalendarView.addDecorator(new HighlightWeekendsDecorator());
        materialCalendarView.addDecorator(new EventDecorator(Color.WHITE, calendarDays));
        materialCalendarView.invalidateDecorators();
        mRun.setText(String.valueOf(runTimes));
        mLeft.setText(String.valueOf(leftTimes));
    }

    // 由字符串得到Date
    public static Date getDateByYearMonthDay(String yearMonthDay) {
        Date date = new Date();
        try {
            date.setTime(YEAR_MONTH_DAY_FORMAT.parse(yearMonthDay).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // 标记周末
    public class HighlightWeekendsDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();
        private final Drawable highlightDrawable;
        private final int color = Color.parseColor("#76afcf");

        public HighlightWeekendsDecorator() {
            highlightDrawable = new ColorDrawable(color);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setBackgroundDrawable(highlightDrawable);
        }
    }

    // 标记跑操日期
    public class EventDecorator implements DayViewDecorator {
        private int color;
        private HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(10, color));
        }
    }

    //    private TextView mRun, mLeft, mYearMonth;
//    private CalendarListView calendarListView;
//    private TreeMap<String, List<RunInfo>> listTreeMap = new TreeMap<>();
//    private TreeMap<String, CustomCalendarItemModel> modelTreeMap = new TreeMap<>();
//    private RunInfoDao runInfoDao;
//    private CalendarItemAdapter calendarItemAdapter;
//    private ListItemAdapter listItemAdapter;
//    private int runTimes = 0;
//    private int leftTimes;
//    private int totalTimes;
//
//    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");
//    public static final SimpleDateFormat YEAR_MONTH_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
//    public static final SimpleDateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat("yyyy年MM月");
//
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////    }
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
////        View view = (ViewGroup) inflater.inflate(R.layout.fragment_run_info, container, false);
////        mRun = (TextView) view.findViewById(R.id.tv_days_run);
////        mLeft = (TextView) view.findViewById(R.id.tv_days_left);
////        calendarListView = (CalendarListView) view.findViewById(R.id.calendar_listview);
////        runInfoDao = new RunInfoDao(getContext());
////        return view;
////    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mActivity = getActivity();
//        // 加载跑操日历数据
//        runInfoDao = new RunInfoDao(mActivity);
//        List <RunInfo> runInfoList = runInfoDao.getAllRunInfo();
//
//        // 测试数据
//        if (runInfoList.size() == 0) {
//            String []rain = new String[]{"晴", "雨"};
//            String []mems0 = new String[]{"今天","明天","我","小明","大家", "同学"};
//            String []mems1 = new String[]{"去","要","很想","不能","忘了", "准备"};
//            String []mems2 = new String[]{"吃饭","打篮球","看电影","看小说","跑步", "熬夜"};
//            for (int i = 0; i < 40; i++) {
//                RunInfo runInfo = new RunInfo();
//                WeatherInfo weatherInfo = new WeatherInfo();
//                Random random = new Random();
//                String date = "2016-" + (7 + random.nextInt(5)) + "-" + (1+random.nextInt(29));
//                weatherInfo.setmRain(rain[random.nextInt(2)]);
//                runInfo.setRunDate(getDateByYearMonthDay(date));
//                runInfo.setRunMem(mems0[random.nextInt(6)] + mems1[random.nextInt(6)] + mems2[random.nextInt(6)]);
//                runInfo.setIsRun(1);
//                runInfo.setRunWeather(weatherInfo);
//                runInfoList.add(runInfo);
//            }
//
//            Collections.sort(runInfoList);
//            for (int i = 0; i < runInfoList.size(); i++) {
//                runInfoList.get(i).setRunTimes(i+1);
//                runInfoDao.addRunInfo(runInfoList.get(i));
//            }
//        }
//
//        for (RunInfo r : runInfoList) {
//            List<RunInfo> l = new ArrayList<>();
//            l.add(r);
//            listTreeMap.put(YEAR_MONTH_DAY_FORMAT.format(r.getRunDate()), l);
//            CustomCalendarItemModel customCalendarItemModel = new CustomCalendarItemModel();
//            customCalendarItemModel.setRun(true);
//            modelTreeMap.put(YEAR_MONTH_DAY_FORMAT.format(r.getRunDate()), customCalendarItemModel);
//        }
//        runTimes = PrefUtil.getInt(mActivity, Constants.RUN_TIMES, runInfoList.size());
//        totalTimes = PrefUtil.getInt(mActivity, Constants.TOTAL_TIMES, 45);
//        leftTimes = totalTimes - runTimes;
//    }
//
//    @Override
//    public View initView() {
//        View view = View.inflate(mActivity, R.layout.fragment_run_info, null);
//        mRun = (TextView) view.findViewById(R.id.tv_days_run);
//        mLeft = (TextView) view.findViewById(R.id.tv_days_left);
//        mYearMonth = (TextView) view.findViewById(R.id.tv_year_month);
//        calendarListView = (CalendarListView) view.findViewById(R.id.calendar_listview);
//
//        calendarListView.setOnMonthChangedListener(new CalendarListView.OnMonthChangedListener() {
//            @Override
//            public void onMonthChanged(String yearMonth) {
//                Calendar calendar = CalendarHelper.getCalendarByYearMonth(yearMonth);
//                loadCalendarData(yearMonth);
//                mYearMonth.setText(YEAR_MONTH_FORMAT.format(calendar.getTime()));
//                Toast.makeText(mActivity, YEAR_MONTH_FORMAT.format(calendar.getTime()),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        calendarListView.setOnCalendarViewItemClickListener(new CalendarListView.OnCalendarViewItemClickListener() {
//            @Override
//            public void onDateSelected(View View, String selectedDate, int listSection, SelectedDateRegion selectedDateRegion) {
//
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    protected void initData() {
//        if (calendarItemAdapter == null)
//            calendarItemAdapter = new CalendarItemAdapter(mActivity);
//        if (listItemAdapter == null)
//            listItemAdapter = new ListItemAdapter(mActivity);
//        calendarItemAdapter.setDayModelList(modelTreeMap);
//        listItemAdapter.setDateDataMap(listTreeMap);
//        calendarItemAdapter.notifyDataSetChanged();
//        listItemAdapter.notifyDataSetChanged();
//
//        calendarListView.setCalendarListViewAdapter(calendarItemAdapter, listItemAdapter);
//        mRun.setText(String.valueOf(runTimes));
//        mLeft.setText(String.valueOf(leftTimes));
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -7);
//        mYearMonth.setText(YEAR_MONTH_FORMAT.format(calendar.getTime()));
//
////        calendarListView.setOnListPullListener(new CalendarListView.onListPullListener() {
////            @Override
////            public void onRefresh() {
////                String date = listTreeMap.firstKey();
////                Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);
////                calendar.add(Calendar.MONTH, -1);
////                calendar.set(Calendar.DAY_OF_MONTH, 1);
////                loadNewsList(DAY_FORMAT.format(calendar.getTime()));
////            }
////
////            @Override
////            public void onLoadMore() {
////                String date = listTreeMap.lastKey();
////                Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);
////                calendar.add(Calendar.MONTH, 1);
////                calendar.set(Calendar.DAY_OF_MONTH, 1);
////                loadNewsList(DAY_FORMAT.format(calendar.getTime()));
////            }
////        });
//
//
//        // 显示最近一周
////        Calendar calendar = Calendar.getInstance();
////        calendar.add(Calendar.MONTH, -7);
//    }
//
//    // 加载列表数据 一次一个月
////    private void loadListData(String date) {
////
////    }
//
//    // 加载日历数据  一次一个月 date (yyyy-MM)
//    private void loadCalendarData(String date) {
//        if (date.equals(calendarListView.getCurrentSelectedDate().substring(0, 7))) {
//            for (String d : listTreeMap.keySet()) {
//                if (date.equals(d.substring(0, 7))) {
//                    CustomCalendarItemModel customCalendarItemModel = calendarItemAdapter.getDayModelList().get(d);
//                    if (customCalendarItemModel != null) {
//                        if (listTreeMap.containsKey(d))
//                            customCalendarItemModel.setRun(listTreeMap.get(d).get(0).getIsRun() == 1);
//                    }
//
//                }
//            }
//            calendarItemAdapter.notifyDataSetChanged();
//        }
//    }
//
//    // 由字符串得到Calendar
//    public static Calendar getCalendarByYearMonthDay(String yearMonthDay) {
//        Calendar calendar = Calendar.getInstance();
//        try {
//            calendar.setTimeInMillis(DAY_FORMAT.parse(yearMonthDay).getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return calendar;
//    }
//
//    // 由字符串得到Date
//    public static Date getDateByYearMonthDay(String yearMonthDay) {
//        Date date = new Date();
//        try {
//            date.setTime(YEAR_MONTH_DAY_FORMAT.parse(yearMonthDay).getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//    }
//
//    // 更新日历和列表数据
//    public void updateView() {
//        listItemAdapter.notifyDataSetChanged();
//        calendarItemAdapter.notifyDataSetChanged();
//    }
//
//    // 新建跑操信息
//    public void addRunInfo(RunInfo runInfo) {
//        runInfoDao.addRunInfo(runInfo);
//        List<RunInfo> l = new ArrayList<>();
//        l.add(runInfo);
//        listTreeMap.put(YEAR_MONTH_DAY_FORMAT.format(runInfo.getRunDate()), l);
//        listItemAdapter.setDateDataMap(listTreeMap);
//        listItemAdapter.notifyDataSetChanged();
//        calendarItemAdapter.notifyDataSetChanged();
//    }
//
//    // 自定义日历项
//    public class CustomCalendarItemModel extends BaseCalendarItemModel {
//        //data memo.
//        private boolean isRun;
//
//        public CustomCalendarItemModel() {
//            this.isRun = false;
//        }
//
//        public CustomCalendarItemModel(boolean isRun) {
//            this.isRun = isRun;
//        }
//
//        public boolean isRun() {
//            return isRun;
//        }
//
//        public void setRun(boolean run) {
//            isRun = run;
//        }
//    }
//
//    // 日历项适配器
//    public class CalendarItemAdapter extends BaseCalendarItemAdapter<CustomCalendarItemModel> {
//        public CalendarItemAdapter(Context context) {
//            super(context);
//        }
//
//        //date format:"yyyy-MM-dd"
//        @Override
//        public View getView(String date, CustomCalendarItemModel model, View convertView, ViewGroup parent) {
//            // CustomCalendarItemModel model = dayModelList.get(date); is supported.
//
//            ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.custom_calendar_item, null);
//            TextView dayNum = (TextView) view.findViewById(R.id.tv_day_num);
//            dayNum.setText(model.getDayNumber());
//
//            if (model.isToday()) {
//                dayNum.setTextColor(mContext.getResources().getColor(com.kelin.calendarlistview.library.R.color.red_ff725f));
//                dayNum.setText(mContext.getResources().getString(com.kelin.calendarlistview.library.R.string.today));
//            }
//
//            if (model.isHoliday()) {
//                dayNum.setTextColor(mContext.getResources().getColor(com.kelin.calendarlistview.library.R.color.red_ff725f));
//            }
//
//
//            if (model.getStatus() == BaseCalendarItemModel.Status.DISABLE) {
//                dayNum.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
//            }
//
//            if (!model.isCurrentMonth()) {
//                dayNum.setTextColor(mContext.getResources().getColor(com.kelin.calendarlistview.library.R.color.gray_bbbbbb));
//                view.setClickable(true);
//            }
//
//            ImageView isRunImageView = (ImageView) view.findViewById(R.id.image_is_run);
//            if (model.isRun()) {
//                isRunImageView.setVisibility(View.VISIBLE);
//            } else {
//                isRunImageView.setVisibility(View.GONE);
//            }
//
//            return view;
//        }
//    }
//
//    // 日历列表适配器
//    public class ListItemAdapter extends BaseCalendarListAdapter<RunInfo> {
//
//        public ListItemAdapter(Context context) {
//            super(context);
//        }
//
//        //date format:'yyyy-MM-dd'
//        @Override
//        public View getSectionHeaderView(String date, View convertView, ViewGroup parent) {
//            HeaderViewHolder headerViewHolder;
//            if (convertView != null) {
//                headerViewHolder = (HeaderViewHolder) convertView.getTag();
//            } else {
//                convertView = inflater.inflate(R.layout.listitem_calendar_header, null);
//                headerViewHolder = new HeaderViewHolder();
//                headerViewHolder.dayText = (TextView) convertView.findViewById(R.id.header_day);
//                headerViewHolder.yearMonthText = (TextView) convertView.findViewById(R.id.header_year_month);
//                headerViewHolder.isRunImage = (ImageView) convertView.findViewById(R.id.header_btn_run);
//                convertView.setTag(headerViewHolder);
//            }
//
//            Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            String dayStr = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
//            if (day < 10) {
//                dayStr = "0" + dayStr;
//            }
//            headerViewHolder.dayText.setText(dayStr);
//            headerViewHolder.yearMonthText.setText(YEAR_MONTH_FORMAT.format(calendar.getTime()));
//            return convertView;
//
//        }
//
//        @Override
//        public View getItemView(RunInfo model, String date, int pos, View convertView, ViewGroup parent) {
//            //you can get model by follow code.
//            //List<ListModel> modelList = dateDataMap.get(date);
//            //model = modelList.get(pos)
//            //custom style of List Items
//
//            ContentViewHolder contentViewHolder;
//            if (convertView != null) {
//                contentViewHolder = (ContentViewHolder) convertView.getTag();
//            } else {
//                convertView = inflater.inflate(R.layout.listitem_calendar_content, null);
//                contentViewHolder = new ContentViewHolder();
//                contentViewHolder.memoTextView = (TextView) convertView.findViewById(R.id.memo);
//                contentViewHolder.timesTextView = (TextView) convertView.findViewById(R.id.run_times);
//                contentViewHolder.weatherImageView = (WeatherIconView) convertView.findViewById(R.id.weather_image);
//                convertView.setTag(contentViewHolder);
//            }
//
//            contentViewHolder.memoTextView.setText(model.getRunMem());
//            contentViewHolder.timesTextView.setText(String.valueOf(model.getRunTimes()));
//            if (model.getRunWeather().getmRain().contains("雨")) {
//                contentViewHolder.weatherImageView.setIconResource(
//                        mActivity.getResources().getString(R.string.wi_day_rain));
//                // 设置天气图标
//            } else
//                contentViewHolder.weatherImageView.setIconResource(
//                        mActivity.getResources().getString(R.string.wi_day_sunny));
////        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(convertView.getResources())
////                .setRoundingParams(RoundingParams.asCircle())
////                .build();
////        contentViewHolder.weatherImageView.setHierarchy(hierarchy);
////        contentViewHolder.weatherImageView.setImageURI(Uri.parse(model.getImages().get(0)));
//            return convertView;
//        }
//    }
//
//    // 列表表头
//    private static class HeaderViewHolder {
//        TextView dayText;
//        TextView yearMonthText;
//        ImageView isRunImage;
//    }
//
//    // 列表内容
//    private static class ContentViewHolder {
//        TextView memoTextView;
//        TextView timesTextView;
//        WeatherIconView weatherImageView;
//    }
//}
}
