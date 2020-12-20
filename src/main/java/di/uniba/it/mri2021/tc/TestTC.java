/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tc;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author pierpaolo
 */
public class TestTC {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // load data
        DatasetReader tr = new CSVDatasetReader();
        List<DatasetExample> traingset = tr.getExamples(new File("resources/TC/train.csv"));
        DatasetReader ts = new CSVDatasetReader();
        List<DatasetExample> testset = ts.getExamples(new File("resources/TC/test.csv"));
       
        // Dummy       
        TextCategorization dummy = new DummyClassifier();
        dummy.train(traingset);
        List<String> pdummy = dummy.test(testset);
        System.out.println("Dummy accuracy: " + dummy.accuracy(testset, pdummy));
        dummy.evaluate(testset, pdummy);
        // KNN        
        TextCategorization knn = new KNN(13);
        knn.train(traingset);
        List<String> pknn = knn.test(testset);
        System.out.println("KNN accuracy: " + knn.accuracy(testset, pknn));
        knn.evaluate(testset, pknn);
        // Rocchio
        TextCategorization rocchio = new Rocchio();
        rocchio.train(traingset);
        List<String> procchio = rocchio.test(testset);
        System.out.println("Rocchio accuracy: " + rocchio.accuracy(testset, procchio));
        rocchio.evaluate(testset, procchio);
        // Naive Bayes
        TextCategorization nb = new NaiveBayes();
        nb.train(traingset);
        List<String> pnb = nb.test(testset);
        System.out.println("Naive Bayes accuracy: " + nb.accuracy(testset, pnb));
        nb.evaluate(testset, pnb);
    }

}
