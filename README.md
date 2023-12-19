# Asynchronous user request processing and caching

### Objective
Develop a Java application that processes user requests asynchronously using `CompletableFuture` and caches the results in a `Map` interface instance for quick retrieval in future requests.

### Requirements

1. **Request processing module**:
    - Implement a method `processRequest(String userId)` that simulates processing a user request based on their `userId`.
    - The processing can be a simulated task, like fetching user data from a database (mock this with a delay).
    - The result of the processing should be a `UserData` object (a simple class with user details).

2. **Asynchronous execution**:
    - Use `CompletableFuture` to handle the processing of requests asynchronously.
    - The `processRequest` method should return a `CompletableFuture<UserData>`.

3. **Caching with ConcurrentHashMap**:
    - Declare a `Map<String, UserData> cache = new ConcurrentHashMap<>();` to cache the results of processed requests. NOTE: `ConcurrentHashMap` is a thread-safe implementation of the `Map` interface. We will cover it in details in next topics.
    - Before processing a request, check if the `userId` is already in the cache. If yes, return the cached result immediately.
    - After processing a request, store the result in the cache.

### Expected output
The application should be able to process multiple user requests concurrently, returning results asynchronously. Repeated requests for the same `userId` should be served from the cache, significantly reducing the response time.

Input data: 

`String[] userIds = {"user1", "user2", "user3", "user1"}; // Note: "user1" is repeated`


Console output: 
```text
Processed: UserData[userId=user1, details=Details for user1]
Processed: UserData[userId=user2, details=Details for user2]
Processed: UserData[userId=user1, details=Details for user1]
Processed: UserData[userId=user3, details=Details for user3]

```