package com.aura.component.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
@Component
@Slf4j
@RequiredArgsConstructor
public class VectorDataLoader implements CommandLineRunner {

    private final VectorStore vectorStore;

    @Value("classpath:migration-rules.txt")
    private Resource rulesFile;

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking Vector Knowledge Base...");

        // 1. Read file
        String content = new String(rulesFile.getInputStream().readAllBytes());
        List<String> rawRules = Arrays.stream(content.split("---"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        // 2. Simple logic: If we have many rules but store is empty, load them
        // In a real app, you'd check for specific metadata to avoid duplicates
        List<Document> documents = rawRules.stream()
                .map(Document::new)
                .toList();

        vectorStore.add(documents);
        log.info("Vector Store synchronized with {} rules.", documents.size());
    }
}
