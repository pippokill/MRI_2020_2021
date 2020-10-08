/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;

/**
 *
 * @author pierpaolo
 */
public class TikaExample {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws org.apache.tika.exception.TikaException
     */
    public static void main(String[] args) throws IOException, TikaException {
        //class for accessing Tika functionalities: provides simple methods for many common parsing and type detection operations.
        Tika tika = new Tika();
        //Detect the media type of a given file. The type detection is based on the document content and a potential known file extension.
        String type = tika.detect(new File(args[0]));
        System.out.println("File type: " + type);
        Metadata metadata = new Metadata();
        //extract text and metadata
        String text = tika.parseToString(new FileInputStream(new File(args[0])), metadata);
        System.out.println("Metadata");
        //iterate over metadata names
        String[] names = metadata.names();
        for (String name:names) {
            String value = metadata.get(name);
            if (value != null) {
                System.out.println(name + "=" + value);
            }
        }
        //print the extracted text
        System.out.println("Text: " + text);
    }

}
