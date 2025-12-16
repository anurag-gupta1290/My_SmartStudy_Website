package com.example.login_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @PostConstruct
    public void init() {
        System.out.println("\n📧 ========== EMAIL SERVICE INITIALIZATION ==========");
        try {
            if (mailSender instanceof JavaMailSenderImpl) {
                JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;

                System.out.println("📧 SMTP Host: " + mailSenderImpl.getHost());
                System.out.println("📧 SMTP Port: " + mailSenderImpl.getPort());
                System.out.println("📧 Username: " + mailSenderImpl.getUsername());
                System.out.println("📧 Protocol: " + mailSenderImpl.getProtocol());

                // Check JavaMail properties
                Properties props = mailSenderImpl.getJavaMailProperties();
                System.out.println("📧 SMTP Auth: " + props.getProperty("mail.smtp.auth"));
                System.out.println("📧 StartTLS: " + props.getProperty("mail.smtp.starttls.enable"));

                // Test connection
                System.out.println("📧 Testing SMTP connection...");
                mailSenderImpl.testConnection();
                System.out.println("✅ SMTP Connection Test: SUCCESS");
            } else {
                System.out.println("⚠️  JavaMailSender is not an instance of JavaMailSenderImpl");
            }
        } catch (Exception e) {
            System.err.println("❌ Email Configuration Error: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("📧 ====================================================\n");
    }

    public void sendEmail(String to, String subject, String body) {
        System.out.println("\n📧 ========== ATTEMPTING TO SEND EMAIL ==========");
        System.out.println("📧 To: " + to);
        System.out.println("📧 Subject: " + subject);
        System.out.println("📧 Body length: " + (body != null ? body.length() : 0) + " characters");

        if (to == null || to.trim().isEmpty()) {
            System.err.println("❌ Error: Recipient email is null or empty");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            // Set sender (IMPORTANT: Must match Gmail username)
            message.setFrom("2023408565.anurag@ug.sharda.ac.in"); // ✅ Correct email

            // Set recipient
            message.setTo(to.trim());

            // Set subject and body
            message.setSubject(subject);
            message.setText(body);

            System.out.println("📧 From: " + message.getFrom());
            System.out.println("📧 Sending email...");

            // Send email
            mailSender.send(message);

            System.out.println("✅ ✅ ✅ EMAIL SENT SUCCESSFULLY!");
            System.out.println("✅ To: " + to);
            System.out.println("✅ Time: " + new java.util.Date());

        } catch (Exception e) {
            System.err.println("\n❌ ❌ ❌ EMAIL SENDING FAILED!");
            System.err.println("❌ Error: " + e.getMessage());

            // More detailed error information
            if (e.getCause() != null) {
                System.err.println("❌ Cause: " + e.getCause().getMessage());
            }

            // Common Gmail errors
            String errorMsg = e.getMessage().toLowerCase();
            if (errorMsg.contains("authentication")) {
                System.err.println("❌ Possible Solutions:");
                System.err.println("   1. Check if 'Less secure apps' is enabled in Google Account");
                System.err.println("   2. Use App Password instead of regular password");
                System.err.println("   3. Verify username/password in application.properties");
            } else if (errorMsg.contains("connection") || errorMsg.contains("timeout")) {
                System.err.println("❌ Network/SMTP Connection Issue");
                System.err.println("   1. Check internet connection");
                System.err.println("   2. Try changing port to 465 with SSL");
            } else if (errorMsg.contains("recipient")) {
                System.err.println("❌ Invalid recipient email address");
            }

            e.printStackTrace();
            System.err.println("❌ ============================================\n");
        }

        System.out.println("📧 ==============================================\n");
    }

    // Test method to send email to yourself
    public void sendTestEmail() {
        String testEmail = "2023408565.anurag@ug.sharda.ac.in";
        String subject = "📧 Test Email from StudySmart Pro";
        String body = """
            Hello,
            
            This is a test email sent from StudySmart Pro application.
            
            Time: %s
            
            If you're receiving this, email configuration is working! 🎉
            
            Regards,
            StudySmart Pro Team
            """.formatted(new java.util.Date());

        sendEmail(testEmail, subject, body);
    }

    // Welcome email template
    public void sendWelcomeEmail(String to, String username) {
        String subject = "🎉 Welcome to StudySmart Pro, " + username + "!";
        String body = """
            Dear %s,
            
            Welcome to StudySmart Pro! Your registration was successful.
            
            Account Details:
            • Username: %s
            • Email: %s
            • Registration Date: %s
            
            Get started with your learning journey:
            1. Complete your profile
            2. Explore courses
            3. Set study goals
            
            Login here: http://localhost:8089/auth/login
            
            Need help? Contact our support team.
            
            Happy Learning! 📚
            
            Best regards,
            StudySmart Pro Team
            """.formatted(username, username, to, new java.util.Date());

        sendEmail(to, subject, body);
    }

    // Registration success email
    public void sendRegistrationSuccessEmail(String to, String username) {
        sendWelcomeEmail(to, username);
    }
}