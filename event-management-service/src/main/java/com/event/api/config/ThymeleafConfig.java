@Configuration
public class ThymeleafConfig {
    
    /**
     * Template resolver for email templates
     */
    @Bean
    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        
        // Look for templates in src/main/resources/email-templates/
        templateResolver.setPrefix("email-templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false); // Set to true in production for performance
        templateResolver.setOrder(1);
        
        return templateResolver;
    }
    
    /**
     * Template engine for processing email templates
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(emailTemplateResolver());
        return templateEngine;
    }
}
