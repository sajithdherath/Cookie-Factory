package org.sachu;

import org.sachu.models.Cookie;
import org.sachu.models.Material;
import org.sachu.models.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sajith on 4/7/18
 */
public class InventoryManager extends Thread{
    private Warehouse warehouse;
    private List<Material> materialSupplyList;

    private InventoryManager() {
    warehouse = new Warehouse();
    materialSupplyList = new ArrayList<Material>();
    }

    private static InventoryManager instance = new InventoryManager();

    public static InventoryManager getInstance() {
        return instance;
    }/**/

    public Material getMaterial(String readData){
        String[] splitData = readData.split(" ");
        Material material = new Material(splitData[0],Integer.parseInt(splitData[1]));
        //warehouse.getMaterials().add(material);
        return material;
    }





    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void addRawMaterial(String readData){
        String[] splitData = readData.split(" ");
        Material material = new Material(splitData[0],Integer.parseInt(splitData[1]),Integer.parseInt(splitData[2]));
        materialSupplyList.add(material);
    }

    public void addCookies(String readData){
        String[] splitData = readData.split(" ");
        Cookie cookie = new Cookie();
        cookie.setCode(splitData[0]);
        cookie.setName(cookie.getCode());
        cookie.setQuantity(Integer.parseInt(splitData[1]));
        warehouse.getCookies().add(cookie);
    }

    @Override
    public void run() {
        outerloop:
        for(int j=0;j<materialSupplyList.size();j++){
            Material material = materialSupplyList.get(j);
            if(Thread.currentThread().getName().equals(material.getCode())){
                for (int i=0;i<warehouse.getMaterials().size();i++){
                    Material materialWaerhoue= warehouse.getMaterials().get(i);
                    if(material.getCode().equals(materialWaerhoue.getCode())){
                        materialWaerhoue.setQuantity(materialWaerhoue.getQuantity()+material.getQuantity());
                        warehouse.getMaterials().set(i,materialWaerhoue);
                        System.out.println("RM Arrival "+material.getName()+" "+material.getArrivalTime()+" "+material.getQuantity()+"kg ["+materialWaerhoue.getCode()+" "+materialWaerhoue.getQuantity()+"]");
                        break outerloop;
                    }
                }
            }

        }
    }
}
