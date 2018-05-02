package org.sliit.models;

import org.sliit.InventoryManager;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sajith on 4/7/18
 */
public class Material {

    private String code;
    private String name;
    private int quantity;
    private int arrivalTime;
    private InventoryManager inventoryManager;

    Timer timer = new Timer();
    TimerTask addMaterial = new TimerTask() {
        public void run() {
            inventoryManager = InventoryManager.getInstance();
            Thread thread = new Thread(inventoryManager,code);
            thread.start();
        }
    };


    public Material(String code, int quantity) {
        this.code = code;
        this.quantity = quantity;
        setName(code);


    }

    public Material(String code,  int quantity, int arrivalTime) {
        this.code = code;
        setName(code);
        this.quantity = quantity;
        this.arrivalTime = arrivalTime;
        if(arrivalTime>0){
            timer.schedule(addMaterial, new Date(System.currentTimeMillis()+arrivalTime*1000));
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String code) {
        if(code.equals("FL")){
            this.name = "Flour";
        }else if(code.equals("BT")){
            this.name = "Butter";
        }else if(code.equals("CH")){
            this.name = "Chocolate Chips";
        }else if(code.equals("MK")){
            this.name = "Milk";
        }else if(code.equals("SG")){
            this.name = "Suggar";
        }else if(code.equals("GN")){
            this.name = "Ginger";
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
