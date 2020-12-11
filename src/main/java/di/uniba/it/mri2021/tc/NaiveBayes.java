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

    private Map<String, BoW> m = null;

    private Map<String, Double> priors = null;

    @Override
    public void train(List<DatasetExample> trainingset) throws IOException {
        m = new HashMap<>();
        priors = new HashMap<>();
        // build categories set
        Set<String> categories = trainingset.stream().map(e -> e.getCategory()).collect(Collectors.toSet());
        // init structures
        for (String category : categories) {
            m.put(category, new BoW());
            priors.put(category, 0d);
        }
        // build BoW for each categories
        int i = 0;
        for (DatasetExample e : trainingset) {
            BoW b = BoWUtils.add(m.get(e.getCategory()), e.getBow());
            m.put(e.getCategory(), b);
            // count classes
            priors.put(e.getCategory(), priors.get(e.getCategory()) + 1);
            i++;
            System.out.println("[NaiveBayes] Training...(" + i + "/" + trainingset.size() + ")");
        }
        Set<String> vocabulary = new HashSet<>();
        for (BoW words : m.values()) {
            vocabulary.addAll(words.getWords());
        }
        System.out.println("[NaiveBayes] Vocabulary size: " + vocabulary.size());
        System.out.println("[NaiveBayes] Compute probabilities...");
        // compute probabilities
        for (String key : priors.keySet()) {
            // prior probabilities
            priors.put(key, priors.get(key) / (float) trainingset.size());
            // cond. probabilities
            BoW cbow = m.get(key);
            for (String word : cbow.getWords()) {
                float prob = (cbow.getWeight(word) + 1f) / (float) (cbow.size() + vocabulary.size());
                cbow.putWord(word, prob);
            }
        }
    }

    @Override
    public List<String> test(List<DatasetExample> testingset) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
