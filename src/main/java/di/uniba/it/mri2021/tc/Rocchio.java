/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tc;

import di.uniba.it.mri2021.rocchio.BoW;
import di.uniba.it.mri2021.rocchio.BoWUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author pierpaolo
 */
public class Rocchio extends TextCategorization {

    private Map<String, BoW> centroids = null;

    private float alpha = 0.5f;

    private float beta = 0.5f;

    @Override
    public void train(List<DatasetExample> trainingset) throws IOException {
        centroids = new HashMap<>();
        // count vectors in positive example
        Map<String, Integer> count = new HashMap<>();
        // structures for negative vectors
        Map<String, Integer> countNeg = new HashMap<>();
        Map<String, BoW> centroidsNeg = new HashMap<>();
        // build categories set
        Set<String> categories = trainingset.stream().map(e -> e.getCategory()).collect(Collectors.toSet());
        // init structures
        for (String category : categories) {
            centroids.put(category, new BoW());
            centroidsNeg.put(category, new BoW());
            count.put(category, 0);
            countNeg.put(category, 0);
        }
        int i = 0;
        for (DatasetExample e : trainingset) {
            for (String cat : centroids.keySet()) {
                // positive vector
                if (cat.equals(e.getCategory())) {
                    BoW c = centroids.get(cat);
                    centroids.put(cat, BoWUtils.add(c, e.getBow()));
                    count.put(cat, count.get(cat) + 1);
                } else { // negative vector
                    BoW c = centroidsNeg.get(cat);
                    centroidsNeg.put(cat, BoWUtils.add(c, e.getBow()));
                    countNeg.put(cat, count.get(cat) + 1);
                }
            }
            i++;
            if (i % 100 == 0) {
                System.out.println("[Rocchio] Training...(" + i + "/" + trainingset.size() + ")");
            }
        }
        // finalize negative centroids
        for (String c : centroidsNeg.keySet()) {
            BoWUtils.scalarProduct(-beta / countNeg.get(c).floatValue(), centroidsNeg.get(c));
        }
        // finalize centroids
        for (String c : centroids.keySet()) {
            BoWUtils.scalarProduct(alpha / count.get(c).floatValue(), centroids.get(c));
            BoW p = BoWUtils.add(centroids.get(c), centroidsNeg.get(c));
            centroids.put(c, p);
        }
    }

    @Override
    public List<String> test(List<DatasetExample> testingset) throws IOException {
        if (centroids==null)
            throw new UnsupportedOperationException("No training data.");
        List<String> p = new ArrayList<>();
        int i = 0;
        for (DatasetExample e : testingset) {
            String cat = "";
            float maxsim = -1;
            for (String c : centroids.keySet()) {
                float sim = BoWUtils.sim(centroids.get(c), e.getBow());
                if (sim > maxsim) {
                    maxsim = sim;
                    cat = c;
                }
            }
            p.add(cat);
            i++;
            if (i % 100 == 0) {
                System.out.println("[Rocchio] Testing...(" + i + "/" + testingset.size() + ")");
            }
        }
        return p;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getBeta() {
        return beta;
    }

    public void setBeta(float beta) {
        this.beta = beta;
    }

    public Map<String, BoW> getCentroids() {
        return centroids;
    }

}
