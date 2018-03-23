package android.trwea.com.trwea.Component;

import android.app.Activity;
import android.content.Context;
import android.trwea.com.trwea.R;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.AttributeSet;
public class WindShow extends LinearLayout {
    private   TextView direction;
    private  TextView Sc;
    private TextView Spd;
    public WindShow(Context context,AttributeSet attrset)
    {

        super(context,attrset);
        LayoutInflater.from(context).inflate(R.layout.wind,this);
        direction = (TextView)findViewById(R.id.direction);
        Sc = (TextView) findViewById(R.id.sc);
        Spd = (TextView) findViewById(R.id.spd);
    }

    public void  setDirection(String Dir)
    {
        direction.setText(Dir);
    }
    public void  setSc(String sc)
    {
        Sc.setText(sc+"级"
        );
    }
    public void  setSpd(String spd)
    {
        Spd.setText(spd+" 千米/时");
    }
}
