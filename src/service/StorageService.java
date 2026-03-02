package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StorageService {

    private static final int POOL_SIZE = 5;
    private final ExecutorService executor;

    private static StorageService instance;

    private StorageService() {
        executor = Executors.newFixedThreadPool(POOL_SIZE);
    }

    public static StorageService getInstance() {
        if (instance == null) {
            instance = new StorageService();
        }
        return instance;
    }

    public void submitTask(Runnable task) {
        executor.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                System.out.println("Error executing task: " + e.getMessage());
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}