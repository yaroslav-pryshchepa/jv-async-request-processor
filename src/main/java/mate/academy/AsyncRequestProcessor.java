package mate.academy;

import java.util.Map;
import java.util.Random;
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
        if (cache.containsKey(userId)) {
            return CompletableFuture.completedFuture(cache.get(userId));
        }
        CompletableFuture<UserData> completableFuture = this.create(userId);
        completableFuture = completableFuture.thenApply(userData -> {
            cache.put(userId, userData);
            return userData;
        });
        return completableFuture;
    }

    public CompletableFuture<UserData> create(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                String details = generateRandomString(10);
                return new UserData(userId, details);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    private static String generateRandomString(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            sb.append(alphabet.charAt(index));
        }
        return sb.toString();
    }
}
