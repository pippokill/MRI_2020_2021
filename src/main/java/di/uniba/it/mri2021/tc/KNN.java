/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tc;

import di.uniba.it.mri2021.rocchio.BoWUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pierpaolo
 */
public class KNN extends TextCategorization {

    private final int k;

    private List<DatasetExample> trainingset = null;

    public KNN(int k) {
        this.k = k;
    }

    @Override
    public void train(List<DatasetExample> trainingset) throws IOException {
        this.trainingset = trainingset;
        // nothing to do, I am a memory/lazy learner
    }

    @Override
    public List<String> test(List<DatasetExample> testingset) throws IOException {
        if (trainingset == null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        List<String> p = new ArrayList<>();
        int c = 0;
        for (DatasetExample etest : testingset) {
            List<CategoryEntry> o = new ArrayList<>();
            for (DatasetExample e : trainingset) {
                float sim = BoWUtils.sim(e.getBow(), etest.getBow());
                o.add(new CategoryEntry(e.getCategory(), sim));
            }
            Collections.sort(o, Collections.reverseOrder());
            Map<String, CategoryEntry> map = new HashMap<>();
            for (int i = 0; i < k && i < o.size(); i++) {
                CategoryEntry ce = map.get(o.get(i).getCategory());
                if (ce == null) {
                    map.put(o.get(i).getCategory(), new CategoryEntry(o.get(i).getCategory(), 1f));
                } else {
                    ce.setScore(ce.getScore() + 1);
                }
            }
            List<CategoryEntry> out = new ArrayList<>(map.values());
            Collections.sort(out, Collections.reverseOrder());
            p.add(out.get(0).getCategory());
            c++;
            if (c % 1000 == 0) {
                System.out.println("KNN testing " + c + "/" + testingset.size());
            }
        }
        return p;
    }

}
