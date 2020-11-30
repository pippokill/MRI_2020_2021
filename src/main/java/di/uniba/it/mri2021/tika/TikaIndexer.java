/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;

/**
 *
 * @author pierpaolo
 */
public class TikaIndexer {

    private static Tika tika;

    private static IndexWriter writer;

    private static FieldType ft;

    private static final int MAX_BYTES = 10 * 1024 * 1024; // 10 MBytes

    private static void index(File file) {
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File f : listFiles) {
                index(f);
            }
        } else if (file.isFile()) {
            if (file.length() < MAX_BYTES) {
                try {
                    // detect the media type of a given file. The type detection is based on the document content and a potential known file extension.
                    String type = tika.detect(file);
                    System.out.println("Index file type: " + file.getAbsolutePath() + ", type: " + type);
                    Metadata metadata = new Metadata();
                    // extract text and metadata
                    String text = tika.parseToString(new FileInputStream(file), metadata);
                    Document doc = new Document();
                    doc.add(new StringField("id", file.getAbsolutePath(), Field.Store.YES));
                    doc.add(new Field("text", text, ft));
                    // iterate over metadata names
                    String[] names = metadata.names();
                    for (String name : names) {
                        String value = metadata.get(name);
                        if (value != null) {
                            doc.add(new StringField(name, value, Field.Store.YES));
                        }
                    }
                    writer.addDocument(doc);
                } catch (IOException | TikaException ex) {
                    Logger.getLogger(TikaIndexer.class.getName()).log(Level.SEVERE, "Error to index document: {0}", file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * arguments: "directory to index" "index path" "posting flag"
     *
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws Exception {
        if (args.length > 2) {
            File dir = new File(args[0]);
            if (dir.isDirectory()) {
                ft = new FieldType(TextField.TYPE_NOT_STORED);
                if (args[2].equalsIgnoreCase("tv")) {
                    ft.setStoreTermVectors(true);
                    System.out.println("Store term vectors");
                } else if (args[2].equalsIgnoreCase("tvp")) {
                    ft.setStoreTermVectors(true);
                    ft.setStoreTermVectorPositions(true);
                    System.out.println("Store term and positions vectors");
                } else if (args[2].equalsIgnoreCase("tvo")) {
                    ft.setStoreTermVectors(true);
                    ft.setStoreTermVectorPositions(true);
                    ft.setStoreTermVectorOffsets(true);
                    System.out.println("Store term, positions and offsets vectors");
                } else {
                    throw new IllegalArgumentException("Field type not valid: " + args[2]);
                }
                FSDirectory fsdir = FSDirectory.open(new File(args[1]).toPath());
                IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
                iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                writer = new IndexWriter(fsdir, iwc);
                tika = new Tika();
                index(dir);
                writer.close();
            } else {
                System.err.println("The first argument is not a directory.");
            }
        }
    }

}
