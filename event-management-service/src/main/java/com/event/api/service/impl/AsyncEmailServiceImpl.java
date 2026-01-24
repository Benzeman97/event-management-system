
@Service
public class AsyncEmailServiceImpl {

      private static final Logger logger = LoggerFactory.getLogger(AsyncEmailService.class);

      @Autowired
      private JavaMailSender mailSender;

      @Autowired
      private EmailConfigProperties emailConfig;

      @Autowired
      private EmailTemplateService templateService;

     /**
     * Send email asynchronously - fire and forget
     */
    @Async("emailTaskExecutor")
    public void sendEmailAsync(EmailRequest emailRequest) {

          // Check if email is enabled
        if (!emailConfig.isEnabled()) {
            logger.warn("Email sending is disabled in configuration");
            return;
        }

        int attempt = 0;
        int maxAttempts = emailConfig.getRetry().getMaxAttempts();
        long retryDelay = emailConfig.getRetry().getDelay();

        while (attempt < maxAttempts) {
            try {
                attempt++;
                logger.info("Attempt {}/{} - Sending email to: {}", 
                    attempt, maxAttempts, emailRequest.getTo());
                
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                
                // Use configured 'from' address
                helper.setFrom(emailConfig.getFrom(), emailConfig.getFromName());
                helper.setTo(emailRequest.getTo());
                helper.setSubject(emailRequest.getSubject());
                helper.setText(emailRequest.getBody(), true); // true = HTML
                
                mailSender.send(message);
                
                logger.info("Email sent successfully to: {}", emailRequest.getTo());
                return; // Success, exit method
                
            }   catch (Exception e) {
                logger.error("Attempt {}/{} failed to send email to: {}", 
                    attempt, maxAttempts, emailRequest.getTo(), e);
                
                if (attempt < maxAttempts) {
                    try {
                        logger.info("Retrying in {}ms...", retryDelay);
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        logger.error("Retry interrupted", ie);
                        return;
                    }
                } else {
                    logger.error("All {} attempts failed for email to: {}", 
                        maxAttempts, emailRequest.getTo());
                    // Here you could:
                    // - Save to database for manual retry
                    // - Send to dead letter queue
                    // - Trigger alert
                }
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

    @Async
    public void sendWelcomeEmail(String toEmail, String userName) {
        try {
            // Generate HTML from template
            String htmlBody = templateService.buildWelcomeEmail(userName, toEmail);
            
            // Create email request
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(toEmail);
            emailRequest.setSubject("Welcome to " + emailConfig.getFromName() + "!");
            emailRequest.setBody(htmlBody);
            
            // Send email
            sendEmailAsync(emailRequest);
            
        } catch (Exception e) {
            logger.error("Failed to send welcome email to: {}", toEmail, e);
        }
    }

       /**
     * Send password reset email using HTML template
     */
    @Async
    public void sendPasswordResetEmail(String toEmail, String userName, String resetToken) {
        try {
            // Generate HTML from template
            String htmlBody = templateService.buildPasswordResetEmail(userName, resetToken);
            
            // Create email request
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(toEmail);
            emailRequest.setSubject("Reset Your Password - " + emailConfig.getFromName());
            emailRequest.setBody(htmlBody);
            
            // Send email
            sendEmailAsync(emailRequest);
            
        } catch (Exception e) {
            logger.error("Failed to send password reset email to: {}", toEmail, e);
        }
    }

       @Async
    public void sendVerificationEmail(String toEmail, String userName, String verificationToken) {
        try {
            // Generate HTML from template
            String htmlBody = templateService.buildVerificationEmail(userName, verificationToken);
            
            // Create email request
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(toEmail);
            emailRequest.setSubject("Verify Your Email - " + emailConfig.getFromName());
            emailRequest.setBody(htmlBody);
            
            // Send email
            sendEmailAsync(emailRequest);
            
        } catch (Exception e) {
            logger.error("Failed to send verification email to: {}", toEmail, e);
        }
    }
}
