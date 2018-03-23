package android.trwea.com.trwea.util;

import android.trwea.com.trwea.db.Area;
import android.trwea.com.trwea.gson.Weather;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<Area> handleCityResponse(String response) {
        try {
            List<Area> city_infos = new ArrayList<Area>();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            if (jsonArray.getJSONObject(0).getString("status") .equals( "ok")) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject city_info_object = jsonArray.getJSONObject(i);
                    Area city = new Area();
                    city.setId(city_info_object.getJSONObject("basic").getString("id"));
                    city.setAreaName(city_info_object.getJSONObject("basic").getString("city"));
                    city.setProvince(city_info_object.getJSONObject("basic").getString("prov"));
                    city.setStatus(city_info_object.getString("status"));
                    city.setWeatherId(city_info_object.getJSONObject("basic").getString("id"));
                    city_infos.add(city);
                }
                return city_infos;
            } else if (jsonArray.getJSONObject(0).getString("status") .equals( "unknown")) {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
