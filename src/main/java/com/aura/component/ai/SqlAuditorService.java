package com.aura.component.ai;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SqlAuditorService implements MigrationAuditor {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;

    // This pulls your .st file from the resources
    @Value("classpath:prompts/sql-audit.st")
    private Resource auditTemplate;

    public SqlAuditorService(ChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @Override
    @CircuitBreaker(name = "aiService", fallbackMethod = "auditFallback")
    public void performAudit(String sql) {
        log.info("RAG-Enhanced Audit Starting...");

        // 1. Retrieve
        List<Document> similarDocs = vectorStore.similaritySearch(sql);
        assert similarDocs != null;
        String context = similarDocs.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        // 2. Map Template to Data
        PromptTemplate template = new PromptTemplate(auditTemplate);
        Map<String, Object> map = Map.of(
                "context", context,
                "sql", sql
        );

        // 3. Generate
        String response = chatModel.call(template.create(map)).getResult().getOutput().getContent();
        log.info("KNOWLEDGE-GROUNDED AUDIT: \n{}", response);
    }

    public void auditFallback(String sql, Throwable t) {
        log.warn("AI Circuit Breaker tripped. Reason: {}", t.getMessage());
    }
}