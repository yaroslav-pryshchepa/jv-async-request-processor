package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        // Return cached result if available
        UserData cachedData = cache.get(userId);
        if (cachedData != null) {
            return CompletableFuture.completedFuture(cachedData);
        }

        // Process request asynchronously if not cached
        return CompletableFuture.supplyAsync(() -> {
            // Simulate time-consuming request processing
            try {
                TimeUnit.SECONDS.sleep(1); // simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            UserData userData = new UserData(userId, "Details for " + userId);
            cache.putIfAbsent(userId, userData); // use putIfAbsent for thread safety
            return userData;
        }, executor);
    }
}
