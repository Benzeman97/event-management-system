@Service
public class TaskServiceImpl {

    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    public void executeTask() {
        taskExecutor.execute(() -> {
            System.out.println("Executing task on thread: " + Thread.currentThread().getName());
            // Your business logic here
            performWork();
        });
    }

   private void performWork() {
        try {
            Thread.sleep(2000);
            System.out.println("Work completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


   public CompletableFuture<String> processAsync() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Processing on: " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Completed";
        }, taskExecutor);
    }

  public void chainTasks() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("Step 1 on: " + Thread.currentThread().getName());
            return "Step1 Result";
        }, taskExecutor)
        .thenApplyAsync(result -> {
            System.out.println("Step 2 on: " + Thread.currentThread().getName());
            return result + " -> Step2 Result";
        }, taskExecutor)
        .thenAccept(finalResult -> {
            System.out.println("Final: " + finalResult);
        });
    }
}
