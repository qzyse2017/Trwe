package android.trwea.com.trwea.gson;
import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("cnty")
    public String Country;

    @SerializedName("id")
    public String weatherId;

    @SerializedName("lat")
    public String Latitude;

    @SerializedName("lon")
    public String Longtitude;


    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;

        @SerializedName("utc")
        public String updataTimeutc;
    }

}
