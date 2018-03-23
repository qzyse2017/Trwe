package android.trwea.com.trwea.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    @SerializedName("aqi")
    public AQI aqi;

    public Basic basic;

    @SerializedName("daily_forecast")
    public List<DailyForecast> forecastList;

    @SerializedName("hourly_forecast")
    public  List<HourlyForecast> hourlyForecast;

    public Now now;

    public String status;

    public Suggestion suggestion;

}
