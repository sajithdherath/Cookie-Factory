package org.sachu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

        for (String s : args) {
            readFiles(s);
        }
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
            FileReader fileReader = new FileReader(path);
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
                                factoryManager.setName(data.split(": ")[1]);
                            } else if (splittedData[0].equals("Production Line")) {
                                factoryManager.setProductionLine(data.split(": ")[1]);
                            } else if (splittedData[0].equals("Warehouse storage capacity")) {
                                inventoryManager.getWarehouse().setStorageCapacity(Integer.parseInt(data.split(": ")[1]));

                            } else if (splittedData[0].equals("Factory storage capacity")) {
                                System.out.println(splittedData[1]);
                            } else if (splittedData[0].equals("Run time")) {
                                Pattern pattern = Pattern.compile("\\d+");
                                Matcher matcher = pattern.matcher(splittedData[1]);
                                while (matcher.find()) {
                                    factoryManager.setRunTime(Integer.parseInt(matcher.group()));
                                }
                            }
                        } else if (section.equals(separators.get(1))) {
                            inventoryManager.getRecipeLines().add(inventoryManager.getRecipeLine(data));
                        } else if (section.equals(separators.get(2))) {
                            inventoryManager.getWarehouse().getMaterials().add(inventoryManager.getMaterial(data));
                        } else if (section.equals(separators.get(3))) {

                        }
                    }
                } else if (fileName.equals("invoice.dat")) {
                    //System.out.println(data);
                } else if (fileName.equals("rmarrival.dat")) {

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
