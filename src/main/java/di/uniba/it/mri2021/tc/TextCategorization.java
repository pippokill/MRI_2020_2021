/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tc;

import java.io.IOException;
import java.util.List;

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

}
