package android.trwea.com.trwea.gson;

public class Suggestion {

    public Comfortable comf;

    public CarWash cw;

    public DressSuggestion drsg;

    public Flu flu;
    public Sport sport;
    public  Travel trav;
    public  Uv uv;

    public class Uv extends BasicDescription{
    }//ultraviolet
    public class Travel extends BasicDescription{
    }
    public class Sport extends BasicDescription{
    }

    public class Flu extends BasicDescription{
    }
    public class DressSuggestion extends BasicDescription{
    }
    public class CarWash extends BasicDescription{
    }
    public class Comfortable extends BasicDescription{
    }
    public class BasicDescription{
        public String brf;//brief introduction
        public String txt;//text description

    }
}
