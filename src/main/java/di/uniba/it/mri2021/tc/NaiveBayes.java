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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Naive Bayes with Laplace smoothing
 *
 * @author pierpaolo
 */
public class NaiveBayes extends TextCategorization {

    private Map<String, BoW> probCond = null;

    private Map<String, Double> priors = null;

    private Set<String> vocabulary;

    private Map<String, Double> totOcc = null;

    private double trainingSize = 0;

    @Override
    public void train(List<DatasetExample> trainingset) throws IOException {
        probCond = new HashMap<>();
        priors = new HashMap<>();
        // build BoW for each categories
        int i = 0;
        for (DatasetExample e : trainingset) {
            BoW b = probCond.get(e.getCategory());
            if (b == null) {
                probCond.put(e.getCategory(), new BoW());
                priors.put(e.getCategory(), 0d);
            }
            b = BoWUtils.add(probCond.get(e.getCategory()), e.getBow());
            probCond.put(e.getCategory(), b);
            // count class occurrences
            priors.put(e.getCategory(), priors.get(e.getCategory()) + 1);
            i++;
            if (i % 1000 == 0) {
                System.out.println("[NaiveBayes] Training...(" + i + "/" + trainingset.size() + ")");
            }
        }
        vocabulary = new HashSet<>();
        for (BoW words : probCond.values()) {
            vocabulary.addAll(words.getWords());
        }
        System.out.println("[NaiveBayes] Vocabulary size: " + vocabulary.size());
        System.out.println("[NaiveBayes] Compute probabilities...");
        totOcc = new HashMap<>();
        // compute total occurrences for each class
        for (String key : priors.keySet()) {
            totOcc.put(key, probCond.get(key).getWeights().stream().mapToDouble(v -> v).sum());
        }
        trainingSize = trainingset.size();
    }

    @Override
    public List<String> test(List<DatasetExample> testingset) throws IOException {
        if (priors == null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        List<String> p = new ArrayList<>(testingset.size());
        int i = 0;
        for (DatasetExample e : testingset) {
            Double max = null;
            String cmax = "";
            for (String c : priors.keySet()) {
                double prob = Math.log(priors.get(c) / trainingSize);
                for (String k : e.getBow().getWords()) {
                    Float pt = probCond.get(c).getWeight(k);
                    if (pt == null && vocabulary.contains(k)) {
                        prob += Math.log(1 / (totOcc.get(c) + (double) vocabulary.size()));
                    } else if (pt != null) {
                        prob += Math.log((pt.doubleValue() + 1) / (totOcc.get(c) + (double) vocabulary.size()));
                    }
                }
                if (max == null || prob > max) {
                    max = prob;
                    cmax = c;
                }
            }
            p.add(cmax);
            i++;
            if (i % 1000 == 0) {
                System.out.println("[NaiveBayes] Testing...(" + i + "/" + testingset.size() + ")");
            }
        }
        return p;
    }

}
