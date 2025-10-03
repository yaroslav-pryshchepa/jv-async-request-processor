package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {

    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        UserData cached = cache.get(userId);
        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }
        return create(userId)
                .thenApply(userData -> {
                    cache.put(userId, userData);
                    return userData;
                });
    }

    private CompletableFuture<UserData> create(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                String details = "Details for " + userId;
                return new UserData(userId, details);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
}
