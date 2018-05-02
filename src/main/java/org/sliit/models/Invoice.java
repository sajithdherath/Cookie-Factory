package org.sliit.models;

import org.sliit.FactoryManager;

import java.util.*;

/**
 * Created by sajith on 4/21/18
 */
public class Invoice {

    private String id;
    private int arrivalTime;
    private List<Cookie> cookies= new ArrayList<Cookie>();
    private FactoryManager factoryManager;

    Timer timer = new Timer();
    TimerTask production = new TimerTask() {
        public void run() {
            factoryManager = FactoryManager.getInstance();
            Thread thread = new Thread(factoryManager,id);
            thread.start();
        }
    };

    public Invoice(int arrivalTime) {
        this.arrivalTime = arrivalTime;
        timer.schedule(production, new Date(System.currentTimeMillis()+this.arrivalTime*1000));
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

}
