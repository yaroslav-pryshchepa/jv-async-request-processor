package mate.academy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class AsyncRequestProcessorTest {

    @Test
    public void processRequest_UniqueUserId_ReturnsNewUserData() {
        // given
        String userId = "uniqueUser";
        AsyncRequestProcessor processor = new AsyncRequestProcessor(Executors.newSingleThreadExecutor());

        // when
        CompletableFuture<UserData> resultFuture = processor.processRequest(userId);
        UserData result = resultFuture.join();

        // then
        assertNotNull(result);
        assertEquals(userId, result.userId());
    }

    @Test
    public void processRequest_ValidUserId_DelayedResponse() throws Exception {
        // given
        String userId = "testUser";
        AsyncRequestProcessor processor = new AsyncRequestProcessor(Executors.newSingleThreadExecutor());

        // when
        CompletableFuture<UserData> resultFuture = processor.processRequest(userId);

        // then
        assertFalse(resultFuture.isDone()); // Check that the future is not completed immediately
        UserData result = resultFuture.get(2, TimeUnit.SECONDS);

        assertNotNull(result);
        assertEquals(userId, result.userId());
    }

    @Test
    public void processRequest_MultipleUniqueUserIds_ConcurrentProcessing() {
        // given
        String[] userIds = {"user1", "user2", "user3", "user4", "user1", "user2", "user3", "user4", "user5", "user2", "user6", "user4"};

        ExecutorService executorService = Executors.newFixedThreadPool(userIds.length);
        AsyncRequestProcessor processor = new AsyncRequestProcessor(executorService);

        CompletableFuture<UserData>[] futures = new CompletableFuture[userIds.length];

        // when
        for (int i = 0; i < userIds.length; i++) {
            futures[i] = processor.processRequest(userIds[i]);
        }

        // then
        CompletableFuture.allOf(futures).join(); // Wait for all futures to complete
        Set<String> results = Arrays.stream(futures)
                .map(CompletableFuture::join)
                .map(UserData::userId)
                .collect(Collectors.toSet());

        assertEquals(new HashSet<>(Arrays.asList(userIds)), results);
        executorService.shutdown();
    }
}
