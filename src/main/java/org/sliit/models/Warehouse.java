package org.sliit.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajith on 4/7/18
 */
public class Warehouse {
    private int storageCapacity;
    private List<Material> materials;
    private List<Cookie> cookies;

    public Warehouse() {
        materials = new ArrayList<Material>();
        cookies = new ArrayList<Cookie>();
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(int storageCapacity) {
        this.storageCapacity = storageCapacity;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }
}
