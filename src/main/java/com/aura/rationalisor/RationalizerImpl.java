package com.aura.rationalisor;

import com.aura.model.Checkpoint;
import com.aura.repository.CheckpointRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Service("default")
public class RationalizerImpl implements Rationalizer {

    private final CheckpointRepository checkpointRepository;

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
        System.out.println("Resuming Rationalization from ID: " + startPoint);

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
                    System.err.println("Error in bucket " + bucketId + ": " + e.getMessage());
                } finally {
                    backpressureBouncer.release();
                }
            }, engineExecutor);
        }
    }

    // Use the atomic native query you defined in the Repository
    private void saveProgress(long bucketId) {
        // No more findById! No more manual checks!
        // Let the DB handle the "ON CONFLICT" logic.
        checkpointRepository.upsertCheckpoint(
                "RATIONALIZATION",
                bucketId,
                LocalDateTime.now()
        );
    }

    private void processBucket(long id) {
        System.out.println("Thread [" + Thread.currentThread().getName() + "] processing Bucket: " + id);
        try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

}