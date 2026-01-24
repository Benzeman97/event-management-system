
@Service
public class AsyncEmailService {

      private static final Logger logger = LoggerFactory.getLogger(AsyncEmailService.class);

     /**
     * Send email asynchronously - fire and forget
     */
    @Async("emailTaskExecutor")
    public void sendEmailAsync(EmailRequest emailRequest) {
        try {
            logger.info("Starting async email send to: {}", emailRequest.getTo());
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
              
            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());
            helper.setText(emailRequest.getBody(), true);

            mailSender.send(message); // This naturally takes 1-3 seconds
            
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
    @Async("emailTaskExecutor")
    public CompletableFuture<Boolean> sendEmailWithResult(EmailRequest emailRequest) {
        try {
            logger.info("Starting async email send to: {}", emailRequest.getTo());
            
           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());
            helper.setText(emailRequest.getBody(), true);

            mailSender.send(message);
            
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
    @Async("emailTaskExecutor")
    public void sendBulkEmails(java.util.List<EmailRequest> emailRequests) {
        logger.info("Starting bulk email send for {} recipients", emailRequests.size());
        
        for (EmailRequest request : emailRequests) {
            try {
                  
               MimeMessage message = mailSender.createMimeMessage();
               MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(request.getTo());        
            helper.setSubject(request.getSubject()); 
            helper.setText(request.getBody(), true);

            mailSender.send(message);
            
            logger.info("Email sent successfully to: {}", emailRequest.getTo());

            } catch (Exception e) {
                logger.error("Failed to send bulk email to: {}", request.getTo(), e);
            }
        }
        
        logger.info("Bulk email send completed");
    }
}
