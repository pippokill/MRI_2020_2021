/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pierpaolo
 */
public class DummyClassifier extends TextCategorization {

    private String maxclass = null;

    @Override
    public void train(List<DatasetExample> trainingset) throws IOException {
        Map<String, Integer> count = new HashMap<>();
        for (DatasetExample e : trainingset) {
            Integer v = count.get(e.getCategory());
            if (v == null) {
                count.put(e.getCategory(), 1);
            } else {
                count.put(e.getCategory(), v + 1);
            }
        }
        maxclass = "";
        int max = 0;
        for (String key : count.keySet()) {
            if (count.get(key) > max) {
                max = count.get(key);
                maxclass = key;
            }
        }
        System.out.println("[Dummy] Training, most frequentclass: " + maxclass);
    }

    @Override
    public List<String> test(List<DatasetExample> testingset) throws IOException {
        if (maxclass == null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        List<String> p = new ArrayList<>(testingset.size());
        for (int i = 0; i < testingset.size(); i++) {
            p.add(maxclass);
        }
        return p;
    }

}
