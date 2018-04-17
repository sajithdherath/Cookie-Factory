package org.sachu.models;

/**
 * Created by sajith on 4/7/18
 */
public class Material {

    private String code;
    private String name;
    private int quantity;

    public Material(String code, int quantity) {
        this.code = code;
        this.quantity = quantity;
        setName(code);
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


}
