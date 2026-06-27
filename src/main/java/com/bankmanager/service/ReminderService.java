package com.bankmanager.service;

import com.bankmanager.model.Deposit;
import com.bankmanager.model.Insurance;
import com.bankmanager.model.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    @Autowired
    private DepositService depositService;

    @Autowired
    private InsuranceService insuranceService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int DAYS_AHEAD = 90; // Show reminders for next 90 days

    /**
     * Get all upcoming reminders (maturity dates for FD/RD and renewal dates for insurance)
     */
    public List<Reminder> getUpcomingReminders() {
        List<Reminder> reminders = new ArrayList<>();

        // Add FD/RD maturity reminders
        reminders.addAll(getDepositMaturityReminders());

        // Add insurance renewal reminders
        reminders.addAll(getInsuranceRenewalReminders());

        // Sort by due date (earliest first)
        return reminders.stream()
                .sorted(Comparator.comparing(Reminder::getDueDate))
                .collect(Collectors.toList());
    }

    /**
     * Get reminders for FD/RD maturity dates
     */
    private List<Reminder> getDepositMaturityReminders() {
        List<Reminder> reminders = new ArrayList<>();
        List<Deposit> deposits = depositService.getAllDeposits();

        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(DAYS_AHEAD);

        for (Deposit deposit : deposits) {
            if (deposit.getMaturityDate() != null && !deposit.getMaturityDate().trim().isEmpty()) {
                try {
                    LocalDate maturityDate = LocalDate.parse(deposit.getMaturityDate(), DATE_FORMATTER);
                    
                    // Only include if maturity date is within the next 90 days or overdue
                    if (maturityDate.isBefore(futureDate) || maturityDate.isEqual(today) || maturityDate.isBefore(today)) {
                        long daysUntilDue = java.time.temporal.ChronoUnit.DAYS.between(today, maturityDate);
                        
                        String type = deposit.getDepositType().equals("FD") ? "FD_MATURITY" : "RD_MATURITY";
                        String title = deposit.getDepositType() + " Maturity";
                        String description = deposit.getDepositType() + " for " + deposit.getPersonName() + 
                                           " (Account: " + deposit.getDepositAccountNumber() + ") matures on " + 
                                           deposit.getMaturityDate();
                        
                        reminders.add(new Reminder(type, title, description, maturityDate, daysUntilDue, 
                                                 "Deposit", deposit.getId(), deposit.getPersonName()));
                    }
                } catch (DateTimeParseException e) {
                    // Skip invalid date formats
                }
            }
        }

        return reminders;
    }

    /**
     * Get reminders for insurance renewal dates
     */
    private List<Reminder> getInsuranceRenewalReminders() {
        List<Reminder> reminders = new ArrayList<>();
        List<Insurance> insurances = insuranceService.getAllInsurance();

        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(DAYS_AHEAD);

        for (Insurance insurance : insurances) {
            if (insurance.getRenewalDate() != null && !insurance.getRenewalDate().trim().isEmpty()) {
                try {
                    LocalDate renewalDate = LocalDate.parse(insurance.getRenewalDate(), DATE_FORMATTER);
                    
                    // Only include if renewal date is within the next 90 days or overdue
                    if (renewalDate.isBefore(futureDate) || renewalDate.isEqual(today) || renewalDate.isBefore(today)) {
                        long daysUntilDue = java.time.temporal.ChronoUnit.DAYS.between(today, renewalDate);
                        
                        String title = "Insurance Renewal";
                        String description = insurance.getInsuranceType() + " Insurance for " + insurance.getPersonName() + 
                                           " (Policy: " + insurance.getPolicyNumber() + ") renews on " + 
                                           insurance.getRenewalDate();
                        
                        reminders.add(new Reminder("INSURANCE_RENEWAL", title, description, renewalDate, daysUntilDue, 
                                                 "Insurance", insurance.getId(), insurance.getPersonName()));
                    }
                } catch (DateTimeParseException e) {
                    // Skip invalid date formats
                }
            }
        }

        return reminders;
    }

    /**
     * Get count of overdue reminders
     */
    public long getOverdueCount() {
        return getUpcomingReminders().stream()
                .filter(Reminder::isOverdue)
                .count();
    }

    /**
     * Get count of reminders due within 30 days
     */
    public long getDueSoonCount() {
        return getUpcomingReminders().stream()
                .filter(Reminder::isDueSoon)
                .count();
    }
}
