package cortez.bancodeservidores;

/**
 * Created by Usuario on 21/10/2016.
 */

public class ServiceProvider {

    //fields
    private int sid;
    private int uid;
    private double avarage;
    private String date_added;

    ServiceProvider(){

    }


    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public double getAvarage() {
        return avarage;
    }

    public void setAvarage(double avarage) {
        this.avarage = avarage;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }
}
