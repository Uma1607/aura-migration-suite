package com.aura.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Slf4j
public class AiConfig {

    // 1. Injected from application.properties with a fallback default
    @Value("${aura.ai.vector-store.path:data/vector-store-data.json}")
    private String vectorStorePath;

    private SimpleVectorStore store;

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        this.store = SimpleVectorStore.builder(embeddingModel).build();
        Path path = Paths.get(vectorStorePath);

        try {
            if (Files.exists(path)) {
                log.info("Loading Vector Store from: {}", path.toAbsolutePath());
                this.store.load(path.toFile());
            } else {
                log.info("Initializing new Vector Store directory at: {}", path.getParent());
                Files.createDirectories(path.getParent());
            }
        } catch (Exception e) {
            log.error("Failed to initialize Vector Store", e);
        }

        return this.store;
    }

    @PreDestroy
    public void shutdown() {
        if (this.store != null) {
            try {
                File file = new File(vectorStorePath);
                this.store.save(file);
                log.info("Vector Store successfully saved to: {}", file.getAbsolutePath());
            } catch (Exception e) {
                log.error("Error saving Vector Store on shutdown", e);
            }
        }
    }
}
