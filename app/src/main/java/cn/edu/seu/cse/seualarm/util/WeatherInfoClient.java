package cn.edu.seu.cse.seualarm.util;

import android.util.Log;
import android.util.Xml;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.edu.seu.cse.seualarm.module.WeatherInfo;
import cz.msebera.android.httpclient.Header;

import static android.net.Uri.parse;

/**
 * Created by Coder Geass on 2016/12/16.
 */

public class WeatherInfoClient {
    private static String jsonString;
    public static String resString;
    public static boolean resCode = false;
    public static WeatherInfo weatherInfo = new WeatherInfo();

    // 获取是否下雨
    public static boolean isRain() {
        return !weatherInfo.getmRain().contains("无") && weatherInfo.getmRain().contains("雨");
    }

    // 获取天气信息
    public static void getWeathInfo() {
        RequestParams params = new RequestParams();
        AsyncHttpClient.getWeathInfo(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    jsonString = new String(responseBody, "UTF-8");
                    parse(jsonString);
                    Log.d("weatherinfoclient", jsonString);
                    parseWeathInfo(jsonString);
                    resCode = true;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    resCode = false;
                    resString = e.toString();
                    Log.d("weatherinfoclient", "error:" + resString);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                resCode = false;
                resString = String.valueOf(statusCode);
                Log.d("weatherinfoclient", "error:" + resString);
            }
        });
    }

    private static void parseWeathInfo(String htmlString) {
        Document doc = Jsoup.parse(htmlString);

        // 解析更新时间
        Element refreshInfo = doc.select("body").last().child(0);
        String refresh = refreshInfo.text();
        weatherInfo.setmRefresh(refresh);

        // 解析气象信息
        Element weatherEle = doc.select("div").get(1);
        weatherInfo.setmRain(weatherEle.text());

        // 解析温度数据
        Element tempInfo = doc.select("div").first();
        String temp = tempInfo.text();
        Log.d("weatherinfoclient", "temp " + temp);
        weatherInfo.setmTemp(temp);

        // 解析表格数据
        Element weatherInfos = doc.select("table").first();
        Elements lists = weatherInfos.getElementsByTag("tr");
        int size = lists.size();
        for (int i = 0; i < size; i++) {
            Element item = lists.get(i);
            Elements els = item.getElementsByTag("td");

            String itemInfo = "";
            for (int j = 0; j < els.size(); j++) {
                Element value = els.get(j);
                String text = value.text();
                itemInfo = itemInfo + text + "#";
            }
            parseItem(itemInfo);
            Log.d("weatheriteminfo", itemInfo);
        }
    }

    public static void getLocalWeatherInfo(String baseUrl) {
        resCode = false;
        RequestParams params = new RequestParams();
        AsyncHttpClient.getLocalWeathInfo(baseUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    jsonString = new String(responseBody, "UTF-8");
                    parse(jsonString);
                    Log.d("weatherinfoclient", jsonString);
                    parseLocalWeathInfo(jsonString);
                    resCode = true;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    resCode = false;
                    resString = e.toString();
                    Log.d("weatherinfoclient", "error:" + resString);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                resCode = false;
                resString = String.valueOf(statusCode);
                Log.d("weatherinfoclient", "error:" + resString);
            }
        });
    }

    private static void parseLocalWeathInfo(String htmlString) {
        Document doc = Jsoup.parse(htmlString);

        // 解析更新时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date refreshDate = new Date();
        weatherInfo.setmRefresh(simpleDateFormat.format(refreshDate));

        // 解析气象信息
        Element weatherEle = doc.select("div").get(1);
        weatherInfo.setmRain(weatherEle.text());

        // 解析温度数据
        Element tempInfo = doc.select("div").first();
        String temp = tempInfo.text();
        Log.d("weatherinfoclient", "temp " + temp);
        weatherInfo.setmTemp(temp);

        // 解析表格数据
        Element weatherInfos = doc.select("table").first();
        Elements lists = weatherInfos.getElementsByTag("tr");
        int size = lists.size();
        for (int i = 0; i < size; i++) {
            Element item = lists.get(i);
            Elements els = item.getElementsByTag("td");

            String itemInfo = "";
            for (int j = 0; j < els.size(); j++) {
                Element value = els.get(j);
                String text = value.text();
                itemInfo = itemInfo + text + "#";
            }
            parseItem(itemInfo);
            Log.d("weatheriteminfo", itemInfo);
        }
    }

    // 从公共接口获取天气信息
    public static void getPublicWeatherInfo(String cityName) {
        resCode = false;
        RequestParams params = new RequestParams();
        AsyncHttpClient.getPublicWeathInfo(cityName, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    jsonString = new String(responseBody, "UTF-8");
                    parse(jsonString);
                    Log.d("weatherinfoclient", jsonString);
                    parsePublicWeathInfo(jsonString);
                    resCode = true;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    resCode = false;
                    resString = e.toString();
                    Log.d("weatherinfoclient", "UnsupportedEncodingException error:" + resString);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                resCode = false;
                resString = String.valueOf(statusCode);
                Log.d("weatherinfoclient", "failure error:" + resString);
            }
        });
    }

    private static void parsePublicWeathInfo(String htmlString) throws UnsupportedEncodingException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        String refreshDate = simpleDateFormat.format(new Date());
        int hour = Integer.valueOf(refreshDate.substring(11, 13));
        boolean isNight = false;
        if (hour >= 18)
            isNight = true;
        boolean isfirst = true;
        boolean issecond = false;
        Log.d("alarm", "hour" + hour);
        Log.d("alarm", "isnight" + isNight);
        XmlPullParser xmlPullParser = Xml.newPullParser();
        InputStream inputStream = new ByteArrayInputStream(htmlString.getBytes("UTF-8"));
        try {
            xmlPullParser.setInput(inputStream, "utf-8");

            int eventCode = xmlPullParser.getEventType();
            while (eventCode != xmlPullParser.END_DOCUMENT) {
                switch (eventCode) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("updatetime".equals(xmlPullParser.getName())) {
                            weatherInfo.setmRefresh(refreshDate.substring(0, 10) + " " + xmlPullParser.nextText());
                        } else if ("wendu".equals(xmlPullParser.getName())) {
                            weatherInfo.setmTemp(xmlPullParser.nextText() + "℃");
                        } else if ("shidu".equals(xmlPullParser.getName())) {
                            weatherInfo.setmWet(xmlPullParser.nextText());
                        } else if ("fengli".equals(xmlPullParser.getName())) {
                            weatherInfo.setmHum(xmlPullParser.nextText());
                        } else if ("fengxiang".equals(xmlPullParser.getName())) {
                            weatherInfo.setmLight(xmlPullParser.nextText());
                        } else if ("pm25".equals(xmlPullParser.getName())) {
                            weatherInfo.setmPM(xmlPullParser.nextText());
                        } else if ("so2".equals(xmlPullParser.getName())) {
                            weatherInfo.setmSO(xmlPullParser.nextText());
                        } else if ("type".equals(xmlPullParser.getName())) {
                            if (isfirst || issecond) {
                                if (!isNight && isfirst)
                                    weatherInfo.setmRain(xmlPullParser.nextText());
                                if (isNight && issecond) {
                                    weatherInfo.setmRain(xmlPullParser.nextText());
                                    Log.d("alarm", weatherInfo.getmRain());
                                    issecond = false;
                                }
                                if (isfirst)
                                    issecond = true;
                                isfirst = false;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventCode = xmlPullParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        // 解析更新时间
//        tempInfo = doc.select("updatetime").first().child(0);
//        temp = tempInfo.text();
//        weatherInfo.setmRefresh(temp);

//        // 解析天气
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
//        int now = Integer.valueOf(simpleDateFormat.format(new Date()));
//        if (now >=18)
//            tempInfo = doc.select("forecast").first().child(0).select("night").get(0);
//        else
//            tempInfo = doc.select("forecast").first().child(0).select("day").get(0);
//        temp = tempInfo.text();
//        weatherInfo.setmRefresh(temp);

//        // 解析温度数据
//        tempInfo = doc.select("wendu").first();
//        temp = tempInfo.text();
//        Log.d("weatherinfoclient", "temp " + temp);
//        weatherInfo.setmTemp(temp);

//        // 解析湿度数据
//        tempInfo = doc.select("shidu").first();
//        temp = tempInfo.text();
//        Log.d("weatherinfoclient", "temp " + temp);
//        weatherInfo.setmWet(temp);

//        // 解析PM2.5数据
//        tempInfo = doc.select("pm25").first();
//        temp = tempInfo.text();
//        Log.d("weatherinfoclient", "temp " + temp);
//        weatherInfo.setmPM(temp);

//        // 无气压 设为风向
//        tempInfo = doc.select("fengxiang").first();
//        temp = tempInfo.text();
//        Log.d("weatherinfoclient", "temp " + temp);
//        weatherInfo.setmHum(temp);
//
//        // 无照度 设为风力
//        tempInfo = doc.select("fengli").first();
//        temp = tempInfo.text();
//        Log.d("weatherinfoclient", "temp " + temp);
//        weatherInfo.setmLight(temp);
    }

    // Open
    public static void getOpenWeatherInfo() {
        weatherInfo = new WeatherInfo();
        RequestParams params = new RequestParams();
        AsyncHttpClient.getOpenWeathInfo(params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    jsonString = new String(responseBody, "UTF-8");
                    parse(jsonString);
                    Log.d("weatherinfoclient", jsonString);
                    parseOpenWeathInfo(jsonString);
                    resCode = true;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    resCode = false;
                    resString = e.toString();
                    Log.d("weatherinfoclient", "UnsupportedEncodingException error:" + resString);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                resCode = false;
                resString = String.valueOf(statusCode);
                Log.d("weatherinfoclient", "failure error:" + resString);
            }
        });
    }

    private static void parseOpenWeathInfo(String jsonString) {
        try {
            JSONObject mainJsonObject = new JSONObject(jsonString);
            JSONArray jsonArray;
            JSONObject jsonObject;

            jsonArray = mainJsonObject.getJSONArray("weather");
            weatherInfo.setmRain(jsonArray.getJSONObject(0).getString("main"));

            jsonObject = mainJsonObject.getJSONObject("main");
            weatherInfo.setmTemp(jsonObject.getString("temp"));
            weatherInfo.setmHum(jsonObject.getString("pressure"));
            weatherInfo.setmWet(jsonObject.getString("humidity"));

            //
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date refreshDate = new Date(mainJsonObject.getLong("dt")*1000);
            weatherInfo.setmRefresh(simpleDateFormat.format(refreshDate));

            //
            jsonObject = mainJsonObject.getJSONObject("wind");
            weatherInfo.setmPM(jsonObject.getString("speed"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void parseItem(String item) {
        String[] items= item.split("#");

        if (items[0].equals("湿度"))
            weatherInfo.setmWet(items[2]);
        if (items[0].equals("气压"))
            weatherInfo.setmHum(items[2]);
        if (items[0].equals("光照度"))
            weatherInfo.setmLight(items[2]);
        if (items[0].equals("细颗粒物"))
            weatherInfo.setmPM(items[2]);
        if (items[0].equals("二氧化硫"))
            weatherInfo.setmSO(items[2]);
    }
}
