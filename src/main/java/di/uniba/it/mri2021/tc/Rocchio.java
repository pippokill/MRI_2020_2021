/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tc;

import di.uniba.it.mri2021.rocchio.BoW;
import di.uniba.it.mri2021.rocchio.BoWUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pierpaolo
 */
public class Rocchio extends TextCategorization {

    private Map<String, BoW> centroids;

    @Override
    public void train(List<DatasetExample> trainingset) throws IOException {
        Map<String, Integer> count = new HashMap<>();
        for (DatasetExample e : trainingset) {
            BoW c = centroids.get(e.getCategory());
            if (c == null) {
                centroids.put(e.getCategory(), e.getBow());
                count.put(e.getCategory(), 1);
            } else {
                centroids.put(e.getCategory(), BoWUtils.add(c, e.getBow()));
                count.put(e.getCategory(), count.get(e.getCategory()) + 1);
            }
        }
        for (String c:centroids.keySet()) {
            BoWUtils.scalarProduct(1/count.get(c).floatValue(), centroids.get(c));
        }
    }

    @Override
    public List<String> test(List<DatasetExample> testingset) throws IOException {
        // implement classification
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
