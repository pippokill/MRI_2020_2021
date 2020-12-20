/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.filtering;

import java.util.Objects;

/**
 *
 * @author pierpaolo
 */
public class Item {
    
    private String itemID;

    public Item(String itemID) {
        this.itemID = itemID;
    }

    public Item() {
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.itemID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (!Objects.equals(this.itemID, other.itemID)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return itemID;
    }
    
    
    
}
