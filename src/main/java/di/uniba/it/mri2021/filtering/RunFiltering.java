/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.filtering;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierpaolo
 */
public class RunFiltering {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            IFDataset d = new Movielens();
            d.load(new File("resources/IF/ml-1m/"));
            CollaborativeIF user2user = new UserBasedIF(d);
            List<ItemPrediction> predictions = user2user.getPredictions(new User("2"));
            Map<String, Item> itemsMap = IFDatasetUtils.itemListToMap(d.getItems());
            int top = 10;
            for (int i = 0; i < top && i < predictions.size(); i++) {
                Movie movie = (Movie) itemsMap.get(predictions.get(i).getItemid());
                System.out.println(movie.getTitle() + "\t" + predictions.get(i).getScore());
            }
        } catch (IOException ex) {
            Logger.getLogger(RunFiltering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
