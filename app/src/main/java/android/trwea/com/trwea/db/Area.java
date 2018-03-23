package android.trwea.com.trwea.db;

import org.litepal.crud.DataSupport;

public class Area extends DataSupport {

    private String id;

    private String AreaName;
    private String Province;
    private String status;
    private String WeatherId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String countyName) {
        this.AreaName = countyName;
    }

    public String getWeatherId() {
        return WeatherId;
    }

    public void setWeatherId(String weatherId) {
        this.WeatherId = weatherId;
    }

    public String getProvince(){return Province;}
    public  void setProvince(String Province){this.Province=Province;}
    public void setStatus(String State){ this.status=State;}
    public String getStatus(){return status;}

}

