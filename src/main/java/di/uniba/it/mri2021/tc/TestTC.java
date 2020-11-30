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
        TextCategorization tc=new KNN(13);
        DatasetReader tr=new CSVDatasetReader();
        tc.train(tr.getExamples(new File("resources/TC/train.csv")));
        DatasetReader ts=new CSVDatasetReader();
        List<DatasetExample> testset = ts.getExamples(new File("resources/TC/test.csv"));
        List<String> p = tc.test(testset);
        System.out.println(tc.accuracy(testset, p));
    }
    
}
