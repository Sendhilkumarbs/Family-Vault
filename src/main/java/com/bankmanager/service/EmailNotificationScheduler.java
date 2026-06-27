package com.bankmanager.service;

import com.bankmanager.model.Deposit;
import com.bankmanager.model.Insurance;
import com.bankmanager.model.Reminder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Scheduled task to send email notifications for upcoming reminders
 * Runs daily at 9:00 AM
 */
@Component
public class EmailNotificationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationScheduler.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int NOTIFICATION_DAYS_BEFORE = 7; // Send email 7 days before due date

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private EmailService emailService;

    @Value("${email.notifications.enabled:true}")
    private boolean notificationsEnabled;

    /**
     * Check for reminders and send email notifications
     * Runs daily at 9:00 AM
     */
    @Scheduled(cron = "0 0 9 * * ?") // 9:00 AM every day
    public void sendReminderEmails() {
        if (!notificationsEnabled) {
            logger.debug("Email notifications are disabled");
            return;
        }

        logger.info("Starting scheduled email notification check...");
        
        List<Reminder> reminders = reminderService.getUpcomingReminders();
        int sentCount = 0;
        int skippedCount = 0;

        LocalDate today = LocalDate.now();

        for (Reminder reminder : reminders) {
            // Only send email if due within notification window (7 days) or overdue
            if (reminder.getDaysUntilDue() <= NOTIFICATION_DAYS_BEFORE) {
                boolean sent = false;

                if ("Deposit".equals(reminder.getEntityType())) {
                    sent = sendDepositReminderEmail(reminder);
                } else if ("Insurance".equals(reminder.getEntityType())) {
                    sent = sendInsuranceReminderEmail(reminder);
                }

                if (sent) {
                    sentCount++;
                } else {
                    skippedCount++;
                }
            }
        }

        logger.info("Email notification check completed. Sent: {}, Skipped: {}", sentCount, skippedCount);
    }

    /**
     * Send email reminder for deposit (FD/RD)
     */
    private boolean sendDepositReminderEmail(Reminder reminder) {
        try {
            Deposit deposit = depositService.getDepositById(reminder.getEntityId());
            if (deposit == null) {
                return false;
            }

            String email = deposit.getEmailAddress();
            if (email == null || email.trim().isEmpty()) {
                logger.debug("No email address for deposit ID: {}", deposit.getId());
                return false;
            }

            return emailService.sendDepositMaturityReminder(
                email,
                deposit.getPersonName(),
                deposit.getDepositType(),
                deposit.getDepositAccountNumber(),
                deposit.getMaturityDate(),
                deposit.getMaturityAmount(),
                reminder.getDaysUntilDue()
            );
        } catch (Exception e) {
            logger.error("Error sending deposit reminder email: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Send email reminder for insurance
     */
    private boolean sendInsuranceReminderEmail(Reminder reminder) {
        try {
            Insurance insurance = insuranceService.getInsuranceById(reminder.getEntityId());
            if (insurance == null) {
                return false;
            }

            String email = insurance.getEmailAddress();
            if (email == null || email.trim().isEmpty()) {
                logger.debug("No email address for insurance ID: {}", insurance.getId());
                return false;
            }

            return emailService.sendInsuranceRenewalReminder(
                email,
                insurance.getPersonName(),
                insurance.getInsuranceType(),
                insurance.getPolicyNumber(),
                insurance.getRenewalDate(),
                insurance.getPremiumAmount(),
                reminder.getDaysUntilDue()
            );
        } catch (Exception e) {
            logger.error("Error sending insurance reminder email: {}", e.getMessage());
            return false;
        }
    }
}
