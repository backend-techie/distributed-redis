package demo.distributedRedis.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceManager {
    private static volatile ExecutorService executorService;

    private ExecutorServiceManager() {
    }

    public static ExecutorService getExecutorService() {
        if (executorService == null) {
            synchronized (ExecutorServiceManager.class) {
                if (executorService == null) {
                    executorService = Executors.newFixedThreadPool(10);
                }
            }
        }
        return executorService;
    }

    public static void shutdownExecutorService() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }
}
