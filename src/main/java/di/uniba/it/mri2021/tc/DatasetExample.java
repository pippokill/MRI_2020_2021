/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tc;

import di.uniba.it.mri2021.rocchio.BoW;

/**
 *
 * @author pierpaolo
 */
public class DatasetExample {
    
    private String category;
    
    private BoW bow;

    public DatasetExample(String category, BoW bow) {
        this.category = category;
        this.bow = bow;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BoW getBow() {
        return bow;
    }

    public void setBow(BoW bow) {
        this.bow = bow;
    }
    
    
    
}
