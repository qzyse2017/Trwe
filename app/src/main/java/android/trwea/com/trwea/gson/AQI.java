package android.trwea.com.trwea.gson;

import com.google.gson.annotations.SerializedName;

public class AQI {

    public AQICity city;

    public class AQICity {
        @SerializedName("aqi")
        public String aqindex;//air quality index
        public String co;
        public String no2;
        public String o3;
        public String pm10;
        public String pm25;
        public String qlty;//quality
        public String so2;

    }

}


