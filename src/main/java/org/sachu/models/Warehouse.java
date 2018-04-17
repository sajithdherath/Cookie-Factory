package org.sachu.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajith on 4/7/18
 */
public class Warehouse {
    private int storageCapacity;
    private List<Material> materials;

    public Warehouse() {
        materials = new ArrayList<Material>();
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
}
