import java.util.concurrent.ConcurrentHashMap;

interface RateLimiter{
    public boolean allowRequest(String userId);
}

class FixRequestRateLimiter implements RateLimiter{
    int maxRequests;
    long windowSize;
    private final ConcurrentHashMap<String, Window> userWindows = new ConcurrentHashMap<>();
    class Window{
        long windowStart;
        int request = 0;
    }
    public FixRequestRateLimiter(int maxRequests, long windowSize){
        this.maxRequests = maxRequests;
        this.windowSize = windowSize;
    }
    @Override
    public boolean allowRequest(String userId){
       long curTime = System.currentTimeMillis();
       Window window = userWindows.computeIfAbsent(userId, k -> new Window());
       if(curTime - window.windowStart > windowSize){
           window.windowStart = curTime;
           window.request = 0;
       }
       if(window.request + 1 <= maxRequests){
           return true;
       }
       return false;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}