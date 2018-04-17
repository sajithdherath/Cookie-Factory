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
public class InventoryManager {
    private Warehouse warehouse;
    private List<Cookie> recipeLines;

    private InventoryManager() {
    warehouse = new Warehouse();
    recipeLines = new ArrayList<Cookie>();
    }

    private static InventoryManager instance = new InventoryManager();

    public static InventoryManager getInstance() {
        return instance;
    }

    public Material getMaterial(String readData){
        String[] splitData = readData.split(" ");
        Material material = new Material(splitData[0],Integer.parseInt(splitData[1]));
        return material;
    }

    public Cookie getRecipeLine(String readData){
        String pattern = "(?<=\\[).*?(?=\\|)";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(readData);
        Cookie cookie = new Cookie();
        cookie.setCode(readData.substring(0,2));
        if(matcher.find()){
            String found = matcher.group();
            String splitData[] = found.split(" ");
            for(String s:splitData){
                cookie.getMaterials().add(getMaterial(s.replace(":"," ")));
            }
        }
        pattern = "(?<=\\|\\s).*?(?=\\])";
        r = Pattern.compile(pattern);
        matcher = r.matcher(readData);
        if(matcher.find()){
            cookie.setProcessTime(Integer.parseInt(matcher.group()));
        }
        return cookie;
    }

    public List<Cookie> getRecipeLines() {
        return recipeLines;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }
}
