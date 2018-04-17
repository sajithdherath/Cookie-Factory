package org.sachu;

/**
 * Created by sajith on 4/7/18
 */
public class FactoryManager implements Runnable{

    private String name;
    private int runTime;
    private String productionLine;

    private FactoryManager() {
    }

    private static FactoryManager instance = new FactoryManager();

    public static FactoryManager getInstance() {
        return instance;
    }

    private String getInitMsg(){
        String msg = "Factory Manager Initiated….";
        System.out.println(msg);
        return msg;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public String getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(String productionLine) {
        this.productionLine = productionLine;
    }

    public void run() {

    }
}
