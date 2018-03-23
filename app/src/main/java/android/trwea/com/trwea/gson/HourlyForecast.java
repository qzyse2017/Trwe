package android.trwea.com.trwea.gson;

public class HourlyForecast {

    public condition cond;
    public String date;
    public String hum;//relative humidity
    public String pop;//precipitation probability
    public String  pres;//air pressure
    public String tmp;//temperature
    public String vis;//visibility
    public DailyForecast.Wind wind;

   public class condition{
       public String code;
       public String txt; //e.g. sunny
   }

}
