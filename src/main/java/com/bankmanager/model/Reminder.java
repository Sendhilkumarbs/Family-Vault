package com.bankmanager.model;

import java.time.LocalDate;

/**
 * DTO class to represent reminders/alerts for the dashboard.
 * This is not a JPA entity, just a data transfer object.
 */
public class Reminder {
    private String type;              // "FD_MATURITY", "RD_MATURITY", "INSURANCE_RENEWAL"
    private String title;              // e.g. "FD Maturity", "Insurance Renewal"
    private String description;        // e.g. "FD for John Doe matures on 15/01/2026"
    private LocalDate dueDate;         // The date when the reminder is due
    private long daysUntilDue;         // Days until due (negative if overdue)
    private String entityType;         // "Deposit", "Insurance"
    private Long entityId;             // ID of the related entity
    private String personName;         // Person name for quick reference

    public Reminder() {
    }

    public Reminder(String type, String title, String description, LocalDate dueDate, 
                   long daysUntilDue, String entityType, Long entityId, String personName) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.daysUntilDue = daysUntilDue;
        this.entityType = entityType;
        this.entityId = entityId;
        this.personName = personName;
    }

    // ─── Getters and Setters ─────────────────────────────────

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public long getDaysUntilDue() {
        return daysUntilDue;
    }

    public void setDaysUntilDue(long daysUntilDue) {
        this.daysUntilDue = daysUntilDue;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public boolean isOverdue() {
        return daysUntilDue < 0;
    }

    public boolean isDueSoon() {
        return daysUntilDue >= 0 && daysUntilDue <= 30;
    }
}
