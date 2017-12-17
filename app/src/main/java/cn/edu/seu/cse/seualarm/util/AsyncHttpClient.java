package cn.edu.seu.cse.seualarm.util;

import com.loopj.android.http.*;

import cn.edu.seu.cse.seualarm.module.WeatherInfo;

/**
 * Created by Coder Geass on 2016/12/8.
 */

public class AsyncHttpClient {
    private static final String BASE_URL = "https://mikukonai.github.io/";
    private static final String WEATHER_URL = "weather.html";
    private static final String PUBLIC_URL = "http://wthrcdn.etouch.cn/WeatherApi?";
    private static final String PUBLIC_END = "citykey=101190101";
    private static final String CITY_END = "city=";
    private static final String APP_ID = "e7736456fc8ac5ba15881baeb2fbcc0a";
    private static final String OPEN_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String OPEN_END = "id=1799962&appid=" + APP_ID + "&units=metric";


    private static com.loopj.android.http.AsyncHttpClient client = new com.loopj.android.http.AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getWeathInfo(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        get(WEATHER_URL, params, responseHandler);
    }

    public static void getPublicWeathInfo(String cityName, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getPublicAbsoluteUrl(cityName), params, responseHandler);
    }

    public static void getOpenWeathInfo(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getOpenAbsoluteUrl(), params, responseHandler);
    }

    private static String getOpenAbsoluteUrl() {
        return OPEN_URL + OPEN_END;
    }

//    private static String getPublicAbsoluteUrl() {
//        return PUBLIC_URL + PUBLIC_END;
//    }

    private static String getPublicAbsoluteUrl(String cityName) {
        return PUBLIC_URL + CITY_END + cityName;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void getLocalWeathInfo(String baseUrl, RequestParams params, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        client.get("http://" + baseUrl + "/realtime.php", params, asyncHttpResponseHandler);
    }
}
