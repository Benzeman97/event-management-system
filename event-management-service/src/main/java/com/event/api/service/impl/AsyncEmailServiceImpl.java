
@Service
public class AsyncEmailService {

      private static final Logger logger = LoggerFactory.getLogger(AsyncEmailService.class);

     /**
     * Send email asynchronously - fire and forget
     */
    @Async
    public void sendEmailAsync(EmailRequest emailRequest) {
        try {
            logger.info("Starting async email send to: {}", emailRequest.getTo());
            
            // Simulate email sending (replace with actual email service)
            Thread.sleep(2000); // Simulating network delay
            
            System.out.println("Email sent successfully to: " + emailRequest.getTo());
            System.out.println("Subject: " + emailRequest.getSubject());
            System.out.println("Body: " + emailRequest.getBody());
            
            logger.info("Email sent successfully to: {}", emailRequest.getTo());
            
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", emailRequest.getTo(), e);
            // Here you could:
            // - Save to database for retry
            // - Send to dead letter queue
            // - Trigger alert/notification
        }
    }

     /**
     * Send email asynchronously with CompletableFuture - can track result
     */
    @Async
    public CompletableFuture<Boolean> sendEmailWithResult(EmailRequest emailRequest) {
        try {
            logger.info("Starting async email send to: {}", emailRequest.getTo());
            
            // Simulate email sending
            Thread.sleep(2000);
            
            System.out.println("Email sent to: " + emailRequest.getTo());
            logger.info("Email sent successfully to: {}", emailRequest.getTo());
            
            return CompletableFuture.completedFuture(true);
            
        } catch (Exception e) {
            logger.error("Failed to send email to: {}", emailRequest.getTo(), e);
            return CompletableFuture.completedFuture(false);
        }
    }


  /**
     * Send bulk emails asynchronously
     */
    @Async
    public void sendBulkEmails(java.util.List<EmailRequest> emailRequests) {
        logger.info("Starting bulk email send for {} recipients", emailRequests.size());
        
        for (EmailRequest request : emailRequests) {
            try {
                // Send each email
                Thread.sleep(100); // Small delay between emails
                System.out.println("Bulk email sent to: " + request.getTo());
            } catch (Exception e) {
                logger.error("Failed to send bulk email to: {}", request.getTo(), e);
            }
        }
        
        logger.info("Bulk email send completed");
    }
  }
}
