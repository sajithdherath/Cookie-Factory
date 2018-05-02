package org.sliit.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajith on 4/7/18
 */
public class Cookie {

    private String code;
    private String name;
    private List<Material> materials= new ArrayList<Material>();
    private int processTime;
    private int quantity;

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
        if(code.equals("BB")){
            this.name = "Base Biscuit";
        }else if(code.equals("BC")){
            this.name = "Butter Cookies";
        }else if(code.equals("CC")){
            this.name = "Chocolate Chip Cookies";
        }else if(code.equals("GC")){
            this.name = "Ginger Cookies";
        }
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public int getProcessTime() {
        return processTime;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
