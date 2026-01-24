@Service
public class EmailTemplateService {
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private EmailConfigProperties emailConfig;
    
    /**
     * Process template with variables and return HTML string
     */
    public String processTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        
        String templatePath = emailConfig.getTemplates().getBasePath() + templateName;
        return templateEngine.process(templatePath, context);
    }
    
    /**
     * Build welcome email from template
     */
    public String buildWelcomeEmail(String userName, String userEmail) {
        Map<String, Object> variables = Map.of(
            "userName", userName,
            "userEmail", userEmail,
            "appName", emailConfig.getFromName(),
            "supportEmail", emailConfig.getFrom(),
            "loginUrl", "https://yourapp.com/login"
        );
        
        return processTemplate(emailConfig.getTemplates().getWelcome(), variables);
    }
    
    /**
     * Build password reset email from template
     */
    public String buildPasswordResetEmail(String userName, String resetToken) {
        Map<String, Object> variables = Map.of(
            "userName", userName,
            "resetUrl", "https://yourapp.com/reset-password?token=" + resetToken,
            "appName", emailConfig.getFromName(),
            "expiryHours", 24
        );
        
        return processTemplate(emailConfig.getTemplates().getResetPassword(), variables);
    }
    
    /**
     * Build verification email from template
     */
    public String buildVerificationEmail(String userName, String verificationToken) {
        Map<String, Object> variables = Map.of(
            "userName", userName,
            "verificationUrl", "https://yourapp.com/verify?token=" + verificationToken,
            "appName", emailConfig.getFromName()
        );
        
        return processTemplate(emailConfig.getTemplates().getVerification(), variables);
    }
}
