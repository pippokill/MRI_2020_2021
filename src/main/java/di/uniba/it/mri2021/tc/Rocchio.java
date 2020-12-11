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

    private float alpha = 0.8f;

    private float beta = 0.2f;

    @Override
    public void train(List<DatasetExample> trainingset) throws IOException {
        centroids = new HashMap<>();
        // count vectors in positive example
        Map<String, Integer> count = new HashMap<>();
        int i = 0;
        for (DatasetExample e : trainingset) {
            BoW c = centroids.get(e.getCategory());
            if (c == null) {
                c = new BoW();
                count.put(e.getCategory(), 0);
            }
            centroids.put(e.getCategory(), BoWUtils.add(c, e.getBow()));
            count.put(e.getCategory(), count.get(e.getCategory()) + 1);
            i++;
            if (i % 1000 == 0) {
                System.out.println("[Rocchio] Training...(" + i + "/" + trainingset.size() + ")");
            }
        }
        System.out.println("[Rocchio] Centroids building...");
        // finalize centroids
        Map<String, BoW> negativeCentroids = new HashMap<>();
        for (String c : centroids.keySet()) {
            BoW cneg = new BoW();
            for (String c1 : centroids.keySet()) {
                if (!c1.equals(c)) {
                    cneg = BoWUtils.add(cneg, centroids.get(c1));
                }
            }
            BoWUtils.scalarProduct(-beta / ((float) trainingset.size() - count.get(c).floatValue()), cneg);
            negativeCentroids.put(c, cneg);
        }
        for (String c : centroids.keySet()) {
            BoWUtils.scalarProduct(alpha / count.get(c).floatValue(), centroids.get(c));
            BoW p = BoWUtils.add(centroids.get(c), negativeCentroids.get(c));
            centroids.put(c, p);
        }
    }

    @Override
    public List<String> test(List<DatasetExample> testingset) throws IOException {
        if (centroids == null) {
            throw new UnsupportedOperationException("No training data.");
        }
        List<String> p = new ArrayList<>();
        int i = 0;
        for (DatasetExample e : testingset) {
            String cat = "";
            Float maxsim = null;
            for (String c : centroids.keySet()) {
                float sim = BoWUtils.sim(centroids.get(c), e.getBow());
                if (maxsim == null || sim > maxsim) {
                    maxsim = sim;
                    cat = c;
                }
            }
            p.add(cat);
            i++;
            if (i % 1000 == 0) {
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
