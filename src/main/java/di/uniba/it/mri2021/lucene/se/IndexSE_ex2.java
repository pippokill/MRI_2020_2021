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
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author pierpaolo
 */
public class IndexSE_ex2 {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            File dir = new File(args[0]);
            if (dir.isDirectory()) {
                FieldType ft = new FieldType(TextField.TYPE_NOT_STORED);
                if (args[2].equalsIgnoreCase("tv")) {
                    ft.setStoreTermVectors(true);
                } else if (args[2].equalsIgnoreCase("tvp")) {
                    ft.setStoreTermVectors(true);
                    ft.setStoreTermVectorPositions(true);
                } else if (args[2].equalsIgnoreCase("tvo")) {
                    ft.setStoreTermVectors(true);
                    ft.setStoreTermVectorPositions(true);
                    ft.setStoreTermVectorOffsets(true);
                } else {
                    throw new IllegalArgumentException("Field type not valid: " + args[2]);
                }
                FSDirectory fsdir = FSDirectory.open(new File(args[1]).toPath());
                IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                IndexWriter writer = new IndexWriter(fsdir, iwc);
                File[] files = dir.listFiles();
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        Document doc = new Document();
                        doc.add(new StringField("id", file.getAbsolutePath(), Field.Store.YES));
                        doc.add(new Field("text", new FileReader(file), ft));
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
