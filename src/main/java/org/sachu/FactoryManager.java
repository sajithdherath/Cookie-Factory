package org.sachu;

import org.sachu.models.Cookie;
import org.sachu.models.Factory;
import org.sachu.models.Invoice;
import org.sachu.models.Material;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sajith on 4/7/18
 */
public class FactoryManager extends Thread {

    private int runTime;
    private List<Cookie> recipeLines;
    public static int productionLine;
    private int occupiedProductionLine = 0;
    private List<Invoice> invoices = new ArrayList<Invoice>();
    private InventoryManager inventoryManager;
    private Factory factory;
    public static int totalManufactured=0;
    private List<Cookie> manufacturedCookies;
    public static int totalDispatched=0;
    private FactoryManager() {
        inventoryManager = InventoryManager.getInstance();
        recipeLines = new ArrayList<Cookie>();
        factory = new Factory();
        manufacturedCookies = new ArrayList<Cookie>();
    }

    private static FactoryManager instance = new FactoryManager();

    public static FactoryManager getInstance() {
        return instance;
    }

    private String getInitMsg() {
        String msg = "Factory Manager Initiated….";
        System.out.println(msg);
        return msg;
    }


    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }


    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public void addRecipeLine(String readData) {
        String pattern = "(?<=\\[).*?(?=\\|)";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(readData);
        Cookie cookie = new Cookie();
        cookie.setCode(readData.substring(0, 2));
        cookie.setName(cookie.getCode());
        if (matcher.find()) {
            String found = matcher.group();
            String splitData[] = found.split(" ");
            for (String s : splitData) {
                cookie.getMaterials().add(inventoryManager.getMaterial(s.replace(":", " ")));
            }
        }
        pattern = "(?<=\\|\\s).*?(?=\\])";
        r = Pattern.compile(pattern);
        matcher = r.matcher(readData);
        if (matcher.find()) {
            cookie.setProcessTime(Integer.parseInt(matcher.group()));
        }
        recipeLines.add(cookie);
    }

    public void addInvoice(String readData) {
        String pattern = "(?<=\\[).*?(?=\\])";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(readData);
        Invoice invoice = new Invoice(Integer.parseInt(readData.split(" ")[1]));
        invoice.setId(readData.split(" ")[0]);

        if (matcher.find()) {
            String[] found = matcher.group().split(",");
            for (String s : found) {
                Cookie cookie = new Cookie();
                cookie.setCode(s.trim().split(" ")[0]);
                cookie.setName(cookie.getCode());
                cookie.setQuantity(Integer.parseInt(s.trim().split(" ")[1]));
                invoice.getCookies().add(cookie);
            }
        }

        invoices.add(invoice);
    }


    synchronized public void run() {
        occupiedProductionLine++;
        int totalTime = 0;
        if (occupiedProductionLine <= productionLine) {
            String id = Thread.currentThread().getName();
            Invoice invoice = null;
            for (Invoice in : invoices) {
                if (in.getId().equals(id)) {
                    invoice = in;
                    String cookies="";
                    for (Cookie cookie:invoice.getCookies()){
                        cookies+=cookie.getName()+" "+cookie.getQuantity()+",";
                    }
                    System.out.println("Invoice "+invoice.getId()+" arrival: "+invoice.getArrivalTime() +"s ["+cookies+"]");
                }
            }
            if (invoice != null) {
                for (Cookie cookie : invoice.getCookies()) {
                    for (int i = 0; i < inventoryManager.getWarehouse().getCookies().size(); i++) {
                        Cookie cookieStock = inventoryManager.getWarehouse().getCookies().get(i);
                        if (cookie.getCode().equals(cookieStock.getCode()) && cookie.getQuantity() > cookieStock.getQuantity()) {
                            if (cookie.getQuantity() > cookieStock.getQuantity()) {
                                int requiredCookies = cookie.getQuantity() - cookieStock.getQuantity();
                                totalDispatched+=cookieStock.getQuantity();
                                cookieStock.setQuantity(0);
                                inventoryManager.getWarehouse().getCookies().set(i, cookieStock);
                                for (Cookie cookieRecipe : recipeLines) {
                                    if (cookieRecipe.getCode().equals(cookie.getCode())) {
                                        for (Material material : cookieRecipe.getMaterials()) {
                                            for (int j = 0; j < inventoryManager.getWarehouse().getMaterials().size(); j++) {
                                                Material materialWearhouese = inventoryManager.getWarehouse().getMaterials().get(j);
                                                if (material.getCode().equals(materialWearhouese.getCode())) {
                                                    materialWearhouese.setQuantity(materialWearhouese.getQuantity() - material.getQuantity());
                                                    if (materialWearhouese.getQuantity() < 0) {
                                                        try {
                                                            wait();

                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        inventoryManager.getWarehouse().getMaterials().set(j, materialWearhouese);

                                                    }

                                                }

                                            }
                                        }
                                    }


                                }
                                for (Cookie cookieRecipe : recipeLines) {
                                    if (cookieRecipe.getCode().equals(cookie.getCode())) {
                                        for (Cookie cookie1:inventoryManager.getWarehouse().getCookies()){
                                            if(cookie.getCode().equals(cookie1.getCode())){
                                                int k=inventoryManager.getWarehouse().getCookies().indexOf(cookie1);
                                                int availableCookies=inventoryManager.getWarehouse().getCookies().get(k).getQuantity();
                                                if(requiredCookies<1000){
                                                    inventoryManager.getWarehouse().getCookies().get(k).setQuantity(availableCookies+(100-requiredCookies));
                                                }

                                            }
                                        }
                                        totalTime += getRequiredTime(requiredCookies, cookieRecipe) * 1000;
                                        cookie.setQuantity(requiredCookies);
                                        manufacturedCookies.add(cookie);
                                    }
                                }
                            } else {
                                try {
                                    wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }
                try {
                    sleep((long) (totalTime));
                    synchronized (this) {
                        notify();
                        System.out.println("Completed"+invoice.getId());
                        occupiedProductionLine--;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    private float getRequiredTime(int required, Cookie cookie) {
        totalManufactured+=required;
        float time = (cookie.getProcessTime() / 100f) * required;
        return time;
    }

    public List<Cookie> getRecipeLines() {
        return recipeLines;
    }

    public void setRecipeLines(List<Cookie> recipeLines) {
        this.recipeLines = recipeLines;
    }

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    public int getTotalManufactured() {
        return totalManufactured;
    }

    public void setTotalManufactured(int totalManufactured) {
        this.totalManufactured = totalManufactured;
    }

    public List<Cookie> getManufacturedCookies() {
        return manufacturedCookies;
    }

    public void setManufacturedCookies(List<Cookie> manufacturedCookies) {
        this.manufacturedCookies = manufacturedCookies;
    }
    public float getEffectiveThroughput(){
        return ((float)(totalManufactured)/(float)(totalDispatched))*100f;
    }
}
