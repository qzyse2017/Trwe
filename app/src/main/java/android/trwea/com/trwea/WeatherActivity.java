package android.trwea.com.trwea;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.trwea.com.trwea.Component.WindShow;
import android.trwea.com.trwea.gson.HourlyForecast;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.trwea.com.trwea.gson.DailyForecast;
import android.trwea.com.trwea.gson.Weather;
import android.trwea.com.trwea.service.AutoUpdateService;
import android.trwea.com.trwea.util.HttpUtil;
import android.trwea.com.trwea.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.trwea.com.trwea.R.id.city_button;
import static android.trwea.com.trwea.R.id.forecast_layout;
import static android.trwea.com.trwea.R.id.hourly_forecast_layout;
import static android.trwea.com.trwea.R.id.personal_log_button;
import static android.trwea.com.trwea.R.id.setting_button;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;

    public SwipeRefreshLayout swipeRefresh;

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private WindShow now_wind;

    private TextView fl_arg;
    private TextView hum_arg;
    private TextView pcpn_arg;
    private TextView pres_arg;
    private TextView vis_arg;

    private LinearLayout forecastLayout;

    private  LinearLayout HourlyForecastLayout;

    private Button SettingButton;

    private  Button CityButton;

    private Button PersonalLogButton;

    private TextView co;
    private TextView no2;
    private TextView o3;
    private TextView pm10;
    private TextView pm25;
    private TextView qlty;
    private TextView so2;
    private TextView aqi;

    private TextView ComfortText;
    private TextView CarWashText;
    private TextView DressText;
    private TextView FlutText;
    private TextView SportText;
    private TextView TravText;
    private TextView UvText;

   //  private ImageView WeatherIcon;

    private String mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        // initialize the content
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        //title
        titleCity = (TextView) findViewById(R.id.title_area);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);

        //now
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
       // WeatherIcon = (ImageView) findViewById(R.id.weather_icon);
        now_wind=(WindShow) findViewById(R.id.now_wind);
        fl_arg=(TextView) findViewById(R.id.fl_arg);
        hum_arg=(TextView) findViewById(R.id.hum_arg);
        pcpn_arg=(TextView) findViewById(R.id.pcpn_arg);
        pres_arg=(TextView) findViewById(R.id.pres_arg);
        vis_arg =(TextView) findViewById(R.id.vis_arg);

        //daily_forecast
        forecastLayout=(LinearLayout) findViewById(forecast_layout);

        //hourly_forecast
        HourlyForecastLayout=(LinearLayout) findViewById(hourly_forecast_layout);

        //aqi
        co=(TextView) findViewById(R.id.co);
        o3=(TextView) findViewById(R.id.o3);
        no2=(TextView) findViewById(R.id.no2);
        pm10=(TextView) findViewById(R.id.pm10);
        pm25=(TextView) findViewById(R.id.pm25);
        qlty=(TextView) findViewById(R.id.qlty);
        so2=(TextView) findViewById(R.id.so2);
        aqi=(TextView) findViewById(R.id.aqi);

        //suggeston for life
        ComfortText = (TextView) findViewById(R.id.comfort_text);
        CarWashText = (TextView) findViewById(R.id.car_wash_text);
        DressText=(TextView) findViewById(R.id.dress_text);
        FlutText = (TextView) findViewById(R.id.flu_text);
        SportText = (TextView) findViewById(R.id.sport_text);
        TravText =(TextView) findViewById(R.id.trav_text);
        UvText =(TextView) findViewById(R.id.uv_text);

        //button
        SettingButton =(Button) findViewById(setting_button);
        CityButton=(Button) findViewById(city_button);
        PersonalLogButton=(Button) findViewById(personal_log_button);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);

        SettingButton.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
             startActivity(new Intent(WeatherActivity.this,SettingsActivity.class));
            }
        });

        CityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        PersonalLogButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //TODO
            }
        });


        if (weatherString != null) {
            // see if there is available info
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            // no available info,query from server
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
    }

    /**
     * use city id for weather request
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * process and show data
     */
    private void showWeatherInfo(Weather weather) {
        //title
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        titleUpdateTime.setText(updateTime);
        String cityName = weather.basic.cityName;
        titleCity.setText(cityName);

        //now
        String weatherInfo = weather.now.cond.txt;
        String degree = weather.now.tmp+ "℃";
        String now_spd=weather.now.wind.spd;
        String now_sc=weather.now.wind.sc;
        String now_dir=weather.now.wind.dir;
        String fl_arg_str=weather.now.fl;
        String hum_arg_str=weather.now.hum;
        String pcpn_arg_str=weather.now.pcpn;
        String pres_arg_str=weather.now.pres;
        String vis_arg_str = weather.now.vis;
        String cond_code=weather.now.cond.code;

        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
       // loadIcon(cond_code);
        now_wind.setSpd(now_spd);
        now_wind.setSc(now_sc);
        now_wind.setDirection(now_dir);
        fl_arg.setText(fl_arg_str);
        hum_arg.setText(hum_arg_str);
        pcpn_arg.setText(pcpn_arg_str);
        pres_arg.setText(pres_arg_str+"hPa");
        vis_arg.setText(vis_arg_str);
        //hourly_forecast
        HourlyForecastLayout.removeAllViews();
        for (HourlyForecast hourly_forecast : weather.hourlyForecast) {
            View view = LayoutInflater.from(this).inflate(R.layout.hourly_item, HourlyForecastLayout, false);
            TextView Degree = (TextView) view.findViewById(R.id.degree_text);
            TextView WeatherInfo = (TextView) view.findViewById(R.id.weather_info_text);
            WindShow HourlyWind = (WindShow) view.findViewById(R.id.hourly_wind);
            TextView HumArg = (TextView) view.findViewById(R.id.hum_arg);
            TextView PopArg = (TextView) view.findViewById(R.id.pop_arg);
            TextView PresArg = (TextView) view.findViewById(R.id.pres_arg);
            TextView update_time=(TextView) view.findViewById(R.id.hourly_time);

            //show info
            Degree.setText(hourly_forecast.tmp);
            WeatherInfo.setText(hourly_forecast.cond.txt);
            HourlyWind.setDirection(hourly_forecast.wind.dir);
            HourlyWind.setSc(hourly_forecast.wind.sc);
            HourlyWind.setSpd(hourly_forecast.wind.spd);
            HumArg.setText(hourly_forecast.hum);
            PopArg.setText(hourly_forecast.pop);
            PresArg.setText(hourly_forecast.pres+"hPa");
            HourlyForecastLayout.addView(view);
            update_time.setText(hourly_forecast.date);
        }

        //daily_forecast
        forecastLayout.removeAllViews();
        for (DailyForecast daily_forecast : weather.forecastList) {

            View view = LayoutInflater.from(this).inflate(R.layout.daily_item,forecastLayout,false );
            TextView dateArg = (TextView) view.findViewById(R.id.date_arg);
            TextView sunrise=(TextView) view.findViewById(R.id.sr);
            TextView sunset=(TextView) view.findViewById(R.id.ss);
            TextView moonrise=(TextView) view.findViewById(R.id.mr);
            TextView moonset=(TextView) view.findViewById(R.id.ms);
               TextView WeatherArg = (TextView) view.findViewById(R.id.weather_arg);
                TextView maxText = (TextView) view.findViewById(R.id.max_text);
                TextView minText = (TextView) view.findViewById(R.id.min_text);
                TextView HumArg = (TextView) view.findViewById(R.id.hum_arg);
                TextView PcpnArg = (TextView) view.findViewById(R.id.pcpn_arg);
                TextView PopArg = (TextView) view.findViewById(R.id.pop_arg);
                TextView PresArg = (TextView) view.findViewById(R.id.pres_arg);
                TextView VisArg = (TextView) view.findViewById(R.id.vis_arg);
                WindShow DailyWind = (WindShow) view.findViewById(R.id.daily_wind);

            //show info
            dateArg.setText(daily_forecast.date);
            WeatherArg.setText(daily_forecast.today_condition.txt_d+'/'+daily_forecast.today_condition.txt_n);
            maxText.setText(daily_forecast.tmp.max+"/");
            minText.setText(daily_forecast.tmp.min);
            HumArg.setText(daily_forecast.hum);
            PcpnArg.setText(daily_forecast.pcpn);
            PopArg.setText(daily_forecast.pop);
            PresArg.setText(daily_forecast.pres+"hPa");
            VisArg.setText(daily_forecast.vis);
            DailyWind.setDirection(daily_forecast.wind.dir);
            DailyWind.setSc(daily_forecast.wind.sc);
            DailyWind.setSpd(daily_forecast.wind.spd);
            sunrise.setText(daily_forecast.astroData.SunRise+"/");
            sunset.setText(daily_forecast.astroData.SunSet);
            moonrise.setText(daily_forecast.astroData.MoonRise+"/");
            moonset.setText(daily_forecast.astroData.MoonSet);

            forecastLayout.addView(view);

        }


        //aqi
       aqi.setText(weather.aqi.city.aqindex);
       co.setText(weather.aqi.city.co);
       o3.setText(weather.aqi.city.o3);
       no2.setText(weather.aqi.city.no2);
       pm10.setText(weather.aqi.city.pm10);
       pm25.setText(weather.aqi.city.pm25);
       qlty.setText(weather.aqi.city.qlty);
       so2.setText(weather.aqi.city.so2);

       //suggestion for life
        String Comfort = "舒适度指数：" + weather.suggestion.comf.brf+'\n'+weather.suggestion.comf.txt;
        String CarWash = "洗车指数：" + weather.suggestion.cw.brf+'\n'+weather.suggestion.cw.txt;
        String DressSuggestion = "穿衣指数：" + weather.suggestion.drsg.brf+'\n'+weather.suggestion.drsg.txt;
        String Flu = "感冒指数：" + weather.suggestion.flu.brf+'\n'+weather.suggestion.flu.txt;
        String Sport = "运动指数：" + weather.suggestion.sport.brf+'\n'+weather.suggestion.sport.txt;
        String Travel = "旅游指数：" + weather.suggestion.trav.brf+'\n'+weather.suggestion.trav.txt;
        String Uv = "紫外线指数：" + weather.suggestion.uv.brf+'\n'+weather.suggestion.uv.txt;

        ComfortText.setText(Comfort);
        CarWashText.setText(CarWash);
        DressText.setText(DressSuggestion);
        FlutText.setText(Flu);
        SportText.setText(Sport);
        TravText.setText(Travel);
        UvText.setText(Uv);

        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        SharedPreferences auto_prefs=PreferenceManager.getDefaultSharedPreferences(this);
        Boolean auto_update=auto_prefs.getBoolean("check_auto_update",true);
        if(auto_update)
        startService(intent);
    }
/*
    private void loadIcon(String cond_code) {
        String requestIcon = " http://cdn.heweather.com/cond_icon/"+cond_code+".png";
        HttpUtil.sendOkHttpRequest(requestIcon, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String icon_pic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("icon", icon_pic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(icon_pic).into(WeatherIcon);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
*/
}
