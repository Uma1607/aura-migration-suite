package com.aura.rationalisor;

import com.aura.model.Checkpoint;
import com.aura.repository.CheckpointRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Service("default")
@Slf4j
public class RationalizerImpl implements Rationalizer {

    private final CheckpointRepository checkpointRepository;

    //node telemetry
    private final Executor engineExecutor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2
    );

    private final Semaphore backpressureBouncer = new Semaphore(10); // Lower for testing

    public RationalizerImpl(CheckpointRepository checkpointRepository) {
        this.checkpointRepository = checkpointRepository;
    }

    @Override
    public void rationalise() {
        // 1. Get the last watermark
        Checkpoint cp = checkpointRepository.findById("RATIONALIZATION")
                .orElse(new Checkpoint("RATIONALIZATION", 0L, LocalDateTime.now()));

        long startPoint = cp.getLastProcessedId() + 1;
        log.info("Resuming Rationalization from ID: {}", startPoint);

        // 2. Start from the NEXT bucket, not from 1
        for (long i = startPoint; i <= startPoint + 10; i++) {
            final long bucketId = i;
            CompletableFuture.runAsync(() -> {
                try {
                    backpressureBouncer.acquire();
                    processBucket(bucketId);

                    // 3. Persist the progress after every bucket
                    saveProgress(bucketId);

                } catch (Exception e) {
                    log.error("Error in bucket: {} " + ":{}", bucketId, e.getMessage());
                } finally {
                    backpressureBouncer.release();
                }
            }, engineExecutor);
        }
    }

    // Use the atomic native query, defined in the Repository
    private void saveProgress(long bucketId) {
        // DB handles the "ON CONFLICT" logic.
        checkpointRepository.upsertCheckpoint(
                "RATIONALIZATION",
                bucketId,
                LocalDateTime.now()
        );
    }

    private void processBucket(long id) {
        log.info("Processing bucket: {} on thread: {}", id, Thread.currentThread().getName());
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

}