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
public interface DatasetReader {

    public List<DatasetExample> getExamples(File file) throws IOException;

}
