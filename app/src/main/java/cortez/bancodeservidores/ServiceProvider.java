package cortez.bancodeservidores;

import java.util.List;

/**
 * Created by Usuario on 21/10/2016.
 */

public class ServiceProvider {

    //fields
    private int sid;
    private int uid;
    private String userFirst_name;
    private String userLast_name;
    private String first_name;
    private String last_name;
    private double avarage;
    private double nota;
    private String date_added;
    private List<String> category;

    ServiceProvider() {

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

    public String getUserFirst_name() {
        return userFirst_name;
    }

    public void setUserFirst_name(String userFirst_name) {
        this.userFirst_name = userFirst_name;
    }

    public String getUserLast_name() {
        return userLast_name;
    }

    public void setUserLast_name(String iserLast_name) {
        this.userLast_name = iserLast_name;
    }

    public double getAvarage() {
        return avarage;
    }

    public void setAvarage(double avarage) {
        this.avarage = avarage;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
