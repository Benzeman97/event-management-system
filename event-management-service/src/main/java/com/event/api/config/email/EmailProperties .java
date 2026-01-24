package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource("classpath:email.yml")
@ConfigurationProperties(prefix = "email")
public class EmailProperties {

    private Smtp smtp;
    private From from;
    private Templates templates;

    // Getters and Setters
    public Smtp getSmtp() {
        return smtp;
    }

    public void setSmtp(Smtp smtp) {
        this.smtp = smtp;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public Templates getTemplates() {
        return templates;
    }

    public void setTemplates(Templates templates) {
        this.templates = templates;
    }

    // --- Inner Classes ---
    public static class Smtp {
        private String host;
        private int port;
        private String username;
        private String password;

        // Getters and Setters
        public String getHost() {
            return host;
        }
        public void setHost(String host) {
            this.host = host;
        }
        public int getPort() {
            return port;
        }
        public void setPort(int port) {
            this.port = port;
        }
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class From {
        private String address;
        private String name;

        // Getters and Setters
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Templates {
        private String welcome;
        private String resetPassword;

        // Getters and Setters
        public String getWelcome() {
            return welcome;
        }
        public void setWelcome(String welcome) {
            this.welcome = welcome;
        }
        public String getResetPassword() {
            return resetPassword;
        }
        public void setResetPassword(String resetPassword) {
            this.resetPassword = resetPassword;
        }
    }
}
