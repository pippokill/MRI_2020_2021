/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.lucene.se;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author pierpaolo
 */
public class IndexSE {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            File dir = new File(args[0]);
            if (dir.isDirectory()) {
                FSDirectory fsdir = FSDirectory.open(new File(args[1]).toPath());
                IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                IndexWriter writer = new IndexWriter(fsdir, iwc);
                File[] files = dir.listFiles();
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        Document doc = new Document();
                        doc.add(new StringField("id", file.getAbsolutePath(), Field.Store.YES));
                        doc.add(new TextField("text", new FileReader(file)));
                        writer.addDocument(doc);
                    }
                }
                writer.close();
            } else {
                System.err.println("The first argument is not a directory.");
            }
        }
    }

}
