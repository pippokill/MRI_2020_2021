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
public class ItemBasedIF extends CollaborativeIF {

    public ItemBasedIF(IFDataset dataset) {
        super(dataset);
    }

    @Override
    public double getPrediction(User user, Item item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ItemPrediction> getPredictions(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
