/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.filtering;

import java.util.List;

/**
 *
 * @author pierpaolo
 */
public abstract class CollaborativeIF {

    private final IFDataset dataset;

    public CollaborativeIF(IFDataset dataset) {
        this.dataset = dataset;
    }

    public IFDataset getDataset() {
        return dataset;
    }

    public abstract double getPrediction(User user, Item item);

    public abstract List<ItemPrediction> getPredictions(User user);

}
