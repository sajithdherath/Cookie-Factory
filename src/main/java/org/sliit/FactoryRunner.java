package org.sliit;

import org.sliit.models.Cookie;
import org.sliit.models.Material;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sajith on 4/7/18
 */
public class FactoryRunner {

    private static FactoryManager factoryManager;
    private static InventoryManager inventoryManager;

    public static void main(String[] args) {
        factoryManager = FactoryManager.getInstance();
        inventoryManager = InventoryManager.getInstance();

        Timer timer = new Timer();
        TimerTask exitApp = new TimerTask() {
            public void run() {
                String manufactured="";
                for(Cookie cookie:factoryManager.getManufacturedCookies()){
                    manufactured+=cookie.getCode()+" "+cookie.getQuantity()+" ";
                }

                System.out.println("\nTotal Manufactured ["+factoryManager.getTotalManufactured()+"] "+manufactured);
                System.out.println("Total Dispatched "+FactoryManager.totalDispatched);
                System.out.println("Effective throughput "+factoryManager.getEffectiveThroughput());
                System.exit(0);
            }
        };

        for (String s : args) {
            readFiles(s);
        }
        timer.schedule(exitApp, new Date(System.currentTimeMillis()+factoryManager.getRunTime()*1000));

        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy  hh:mm:ss");
        System.out.println("\n\n");
        System.out.println("+-----------------------------------------------------------------------+");
        System.out.println("|               "+factoryManager.getFactory().getName().toUpperCase()+" OPERATION REPORT \t\t|");
        System.out.println("+-----------------------------------------------------------------------+");
        System.out.println("|\t\t\t\t\t\t\t\t\t|");
        System.out.println("|    Start time \t\t             : "+date.format(new Date( ))+"\t|");
        System.out.println("|    Production Line \t \t\t     : "+FactoryManager.productionLine+"\t\t\t|");
        System.out.println("|    Run Time\t\t                     : "+factoryManager.getRunTime()+"s\t\t\t|");
        System.out.println("|    Warehouse Storage Capacity              : "+inventoryManager.getWarehouse().getStorageCapacity()+" Containers\t\t|");
        System.out.println("|    Factory Storage Capacity                : "+factoryManager.getFactory().getStorageCapacity()+" Containers\t\t|");
        System.out.println("|\t\t\t\t\t\t\t\t\t|");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("\n\n");

        for (Material material:inventoryManager.getWarehouse().getMaterials()){
            System.out.println(material.getName()+" - "+material.getCode());
        }
        System.out.println();
    }

    public static void readFiles(String fileName) {
        List<String> separators = new ArrayList<String>();
        separators.add("#base data");
        separators.add("#recipe line");
        separators.add("#warehouse inventory");
        separators.add("#Merchandise inventory");
        String section = "";
        String baseDir = System.getProperty("user.dir");
        String path = baseDir + "/" + fileName;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                if (fileName.equals("cookieFactory.info")) {
                    if (separators.contains(data)) {
                        section = data;
                        continue;
                    } else {
                        if (section.equals(separators.get(0))) {
                            String[] splittedData = data.split(":");
                            if (splittedData[0].equals("Name")) {
                                factoryManager.getFactory().setName(data.split(": ")[1]);

                            } else if (splittedData[0].equals("Production line")) {
                                FactoryManager.productionLine=Integer.parseInt(data.split(": ")[1]);


                            } else if (splittedData[0].equals("Warehouse storage capacity")) {
                                inventoryManager.getWarehouse().setStorageCapacity(Integer.parseInt(data.split(": ")[1]));

                            } else if (splittedData[0].equals("Factory storage capacity")) {
                                factoryManager.getFactory().setStorageCapacity(Integer.parseInt(splittedData[1].trim()));

                            } else if (splittedData[0].equals("Run time")) {
                                Pattern pattern = Pattern.compile("\\d+");
                                Matcher matcher = pattern.matcher(splittedData[1]);
                                while (matcher.find()) {
                                    factoryManager.setRunTime(Integer.parseInt(matcher.group()));

                                }
                            }
                        } else if (section.equals(separators.get(1))) {
                            factoryManager.addRecipeLine(data);
                        } else if (section.equals(separators.get(2))) {
                            Material material = inventoryManager.getMaterial(data);
                            inventoryManager.getWarehouse().getMaterials().add(material);

                        } else if (section.equals(separators.get(3))) {
                            inventoryManager.addCookies(data);
                        }
                    }
                } else if (fileName.equals("invoice.dat")) {
                    factoryManager.addInvoice(data);
                } else if (fileName.equals("rmarrival.dat")) {
                    inventoryManager.addRawMaterial(data);

                }
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

