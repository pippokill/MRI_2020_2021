/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tc;

import di.uniba.it.mri2021.rocchio.BoW;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author pierpaolo
 */
public class CSVDatasetReader implements DatasetReader {

    @Override
    public List<DatasetExample> getExamples(File file) throws IOException {
        List<DatasetExample> dataset = new ArrayList<>();
        Reader in = new FileReader(file);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(',').withQuote('"').parse(in);
        int c = 0;
        for (CSVRecord record : records) {
            String category = record.get(0);
            String[] tokens = record.get(1).toLowerCase().trim().split("\\s+");
            BoW bow = new BoW();
            for (String token : tokens) {
                Float w = bow.getWeight(token);
                if (w == null) {
                    bow.putWord(token, 1);
                } else {
                    bow.putWord(token, w + 1);
                }
            }
            dataset.add(new DatasetExample(category, bow));
            c++;
        }
        in.close();
        System.out.println("Loaded " + c + " examples.");
        return dataset;
    }

}
