package com.bankmanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.enabled:false}")
    private boolean emailEnabled;

    @Value("${spring.mail.from:noreply@bankdetailsmanager.com}")
    private String fromEmail;

    /**
     * Send reminder email for FD/RD maturity or insurance renewal
     */
    public boolean sendReminderEmail(String toEmail, String subject, String message) {
        if (!emailEnabled || mailSender == null) {
            logger.debug("Email notifications are disabled or mail sender not configured");
            return false;
        }

        if (toEmail == null || toEmail.trim().isEmpty()) {
            logger.debug("No email address provided, skipping email");
            return false;
        }

        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(fromEmail);
            email.setTo(toEmail);
            email.setSubject(subject);
            email.setText(message);
            
            mailSender.send(email);
            logger.info("Reminder email sent successfully to: {}", toEmail);
            return true;
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", toEmail, e.getMessage());
            return false;
        }
    }

    /**
     * Send reminder email for FD/RD maturity
     */
    public boolean sendDepositMaturityReminder(String email, String personName, String depositType,
                                                 String accountNumber, String maturityDate, 
                                                 String maturityAmount, long daysUntilDue) {
        String subject = String.format("Reminder: %s Maturity - %s", depositType, personName);
        
        String status = daysUntilDue < 0 
            ? String.format("OVERDUE by %d days", -daysUntilDue)
            : String.format("Due in %d days", daysUntilDue);
        
        String message = String.format(
            "Dear %s,\n\n" +
            "This is a reminder about your %s maturity:\n\n" +
            "Person: %s\n" +
            "Account Number: %s\n" +
            "Maturity Date: %s\n" +
            "Maturity Amount: ₹%s\n" +
            "Status: %s\n\n" +
            "Please take necessary action.\n\n" +
            "---\n" +
            "Family Vault\n" +
            "This is an automated reminder.",
            personName, depositType, personName, accountNumber, maturityDate, 
            maturityAmount != null ? maturityAmount : "N/A", status
        );

        return sendReminderEmail(email, subject, message);
    }

    /**
     * Send reminder email for insurance renewal
     */
    public boolean sendInsuranceRenewalReminder(String email, String personName, String insuranceType,
                                                String policyNumber, String renewalDate,
                                                String premiumAmount, long daysUntilDue) {
        String subject = String.format("Reminder: Insurance Renewal - %s", personName);
        
        String status = daysUntilDue < 0 
            ? String.format("OVERDUE by %d days", -daysUntilDue)
            : String.format("Due in %d days", daysUntilDue);
        
        String message = String.format(
            "Dear %s,\n\n" +
            "This is a reminder about your insurance renewal:\n\n" +
            "Person: %s\n" +
            "Insurance Type: %s\n" +
            "Policy Number: %s\n" +
            "Renewal Date: %s\n" +
            "Premium Amount: ₹%s\n" +
            "Status: %s\n\n" +
            "Please renew your policy before the due date.\n\n" +
            "---\n" +
            "Family Vault\n" +
            "This is an automated reminder.",
            personName, personName, insuranceType, policyNumber, renewalDate,
            premiumAmount != null ? premiumAmount : "N/A", status
        );

        return sendReminderEmail(email, subject, message);
    }
}
