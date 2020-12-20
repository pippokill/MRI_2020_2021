/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author pierpaolo
 */
public abstract class TextCategorization {

    public abstract void train(List<DatasetExample> trainingset) throws IOException;

    public abstract List<String> test(List<DatasetExample> testingset) throws IOException;

    public double accuracy(List<DatasetExample> testingset, List<String> predicted) throws IllegalArgumentException {
        if (testingset.size() != predicted.size()) {
            throw new IllegalArgumentException("Incompatible predictions");
        }
        double correct = 0;
        for (int i = 0; i < predicted.size(); i++) {
            if (predicted.get(i).equals(testingset.get(i).getCategory())) {
                correct++;
            }
        }
        if (predicted.isEmpty()) {
            return correct;
        } else {
            return correct / (double) predicted.size();
        }
    }

    public void evaluate(List<DatasetExample> testingset, List<String> predicted) throws IllegalArgumentException {
        if (testingset.size() != predicted.size()) {
            throw new IllegalArgumentException("Incompatible predictions");
        }
        Set<String> categories = testingset.stream().map(e -> e.getCategory()).collect(Collectors.toSet());
        Map<String, Integer> idx = new HashMap();
        int i = 0;
        for (String c : categories) {
            idx.put(c, i);
            i++;
        }
        int[][] confMatrix = new int[categories.size()][categories.size()];
        for (i = 0; i < predicted.size(); i++) {
            if (predicted.get(i).equals(testingset.get(i).getCategory())) {
                int j = idx.get(predicted.get(i));
                confMatrix[j][j]++;
            } else {
                int j = idx.get(predicted.get(i));
                int k = idx.get(testingset.get(i).getCategory());
                confMatrix[j][k]++;
            }
        }
        System.out.print("          ");
        for (String c : categories) {
            System.out.format("%10s", c);
        }
        System.out.println();
        for (String c : categories) {
            System.out.format("%10s", c);
            int j = idx.get(c);
            for (i = 0; i < categories.size(); i++) {
                System.out.format("%10d", confMatrix[j][i]);
            }
            System.out.println();
        }
        System.out.println();

        //micro P, R, F
        int tp = 0;
        int fn = 0;
        int fp = 0;
        for (int j = 0; j < categories.size(); j++) {
            for (int k = 0; k < categories.size(); k++) {
                if (j != k) {
                    fp += confMatrix[j][k];
                    fn += confMatrix[k][j];
                } else {
                    tp += confMatrix[j][j];
                }
            }
        }
        double microP = (tp + fp) == 0 ? 0 : (double) tp / (double) (tp + fp);
        double microR = (tp + fn) == 0 ? 0 : (double) tp / (double) (tp + fn);
        System.out.format("Micro-P: %.4f%n", microP);
        System.out.format("Micro-R: %.4f%n", microR);
        System.out.format("Micro-F: %.4f%n", 2 * microP * microR / (microP + microR));
        double macroP = 0;
        double macroR = 0;
        for (int j = 0; j < categories.size(); j++) {
            tp = 0;
            fn = 0;
            fp = 0;
            for (int k = 0; k < categories.size(); k++) {
                if (j == k) {
                    tp += confMatrix[j][k];
                } else {
                    fp += confMatrix[j][k];
                    fn += confMatrix[k][j];
                }
            }
            macroP += (tp + fp) == 0 ? 0 : (double) tp / (double) (tp + fp);
            macroR += (tp + fn) == 0 ? 0 : (double) tp / (double) (tp + fn);
        }
        macroP = macroP / (double) categories.size();
        macroR = macroR / (double) categories.size();
        System.out.format("Macro-P: %.4f%n", macroP);
        System.out.format("Macro-R: %.4f%n", macroR);
        System.out.format("Macro-F: %.4f%n", 2 * macroP * macroR / (macroP + macroR));
    }

}
