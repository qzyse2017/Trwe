package android.trwea.com.trwea.gson;
import com.google.gson.annotations.SerializedName;

public class DailyForecast {

    @SerializedName("astro")//astronomical data
    public Astro astroData;

    @SerializedName("cond")//weather conditions
    public TodayCondition today_condition;

    public String date;
    public String hum;//relative humidity
    public String pcpn;//precipitation
    public String pop;//precipitation probability
    public String  pres;//air pressure
    public Temperature tmp;//temperature
    public String vis;//visibilty
    public Temperature temperature;
    public Wind wind;
    public String status;
    public Suggestion suggestion;
    public class Wind{
        public String deg;//degree of the wind to describe the direction of the wind
        public String dir;//direction
        public String sc;//wind levels
        public String spd;//wind speed,kmph
    }
    public class Temperature {

        public String max;

        public String min;

    }

   public class TodayCondition{

        public String code_d;
        public String code_n;
        public String txt_d;
        public String txt_n;
    }

    public class  Astro{
        @SerializedName("mr")
        public String MoonRise;

        @SerializedName("ms")
        public String MoonSet;

        @SerializedName("sr")
        public String SunRise;

        @SerializedName("ss")
        public String SunSet;

    }
}

