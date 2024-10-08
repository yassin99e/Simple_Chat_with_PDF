package ma.ensa.rag_ollama.Service;

import org.springframework.ai.embedding.EmbeddingModel;

import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter; // New vector store


import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Component
public class DataLoader {

    @Value("classpath:/pdfs/free.pdf")
    private Resource PdfFile;

    @Value("Vs2.json")
    private String vectorstorename;

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);



    @Bean
    public SimpleVectorStore simplevectorStore(EmbeddingModel embeddingModel) {

        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingModel);
        String path= Path.of("src", "main", "resources", "vectorstore").toFile().getAbsolutePath()+"/"+vectorstorename;

        File filestore = new File(path);

        if(filestore.exists()){
            simpleVectorStore.load(filestore);
        }
        else{
            PagePdfDocumentReader reader = new PagePdfDocumentReader(PdfFile);
            List<Document> documents = reader.get();
            TextSplitter splitter = new TokenTextSplitter();
            List<Document> chunks = splitter.split(documents);

            // Assuming that MongoDBVectorStore has a similar API to add documents
            simpleVectorStore.add(chunks);
            simpleVectorStore.save(filestore);
        }

        return simpleVectorStore;
    }
}
