# 📧 Free Email Notifications Setup Guide

## ✅ What's Implemented

Your application now has **FREE email notifications** for reminders! No SMS costs, no API fees - completely free!

---

## 🎯 Features

- ✅ **Automatic email reminders** for FD/RD maturity dates
- ✅ **Automatic email reminders** for insurance renewals
- ✅ **Scheduled daily check** at 9:00 AM
- ✅ **Sends emails 7 days before** due date (and for overdue items)
- ✅ **Completely FREE** - uses your existing email account

---

## ⚙️ Setup Instructions

### Step 1: Choose Your Email Provider

The application works with any SMTP email provider:
- **Gmail** (Recommended - easiest)
- **Outlook/Hotmail**
- **Yahoo Mail**
- **Custom SMTP** (any email provider)

---

### Step 2: Configure Email Settings

Edit `src/main/resources/application.properties`:

#### For Gmail (Recommended):

```properties
# Enable email notifications
email.notifications.enabled=true

# Gmail Configuration
spring.mail.enabled=true
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.from=your-email@gmail.com
```

**Important for Gmail:**
1. Enable **2-Step Verification** on your Google account
2. Generate an **App Password**: https://myaccount.google.com/apppasswords
   - Select "Mail" and "Other (Custom name)" → Enter "Bank Details Manager"
   - Copy the 16-character password
   - Use this App Password (not your regular Gmail password)

#### For Outlook/Hotmail:

```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
spring.mail.username=your-email@outlook.com
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.from=your-email@outlook.com
```

#### For Yahoo:

```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=your-email@yahoo.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.from=your-email@yahoo.com
```

---

### Step 3: Add Email Addresses to Your Records

1. **For Deposits (FD/RD):**
   - When adding/editing a deposit, enter the email address in the "Email Address" field
   - This email will receive maturity reminders

2. **For Insurance:**
   - When adding/editing insurance, enter the email address in the "Email Address" field
   - This email will receive renewal reminders

3. **For Bank Accounts:**
   - Already has `emailLinked` field - use this for any account-related reminders

---

## 📅 How It Works

1. **Daily Check:** The application checks for reminders every day at 9:00 AM
2. **7-Day Window:** Emails are sent 7 days before the due date
3. **Overdue Alerts:** Overdue items also trigger email notifications
4. **Automatic:** No manual action needed - fully automated!

---

## 📧 Email Content

### FD/RD Maturity Reminder:
```
Subject: Reminder: FD Maturity - Rajesh Kumar

Dear Rajesh Kumar,

This is a reminder about your FD maturity:

Person: Rajesh Kumar
Account Number: FD123456
Maturity Date: 15/02/2026
Maturity Amount: ₹1,00,000
Status: Due in 7 days

Please take necessary action.

---
Family Bank Details Manager
This is an automated reminder.
```

### Insurance Renewal Reminder:
```
Subject: Reminder: Insurance Renewal - Priya Kumar

Dear Priya Kumar,

This is a reminder about your insurance renewal:

Person: Priya Kumar
Insurance Type: Life Insurance
Policy Number: POL789
Renewal Date: 20/02/2026
Premium Amount: ₹25,000
Status: Due in 12 days

Please renew your policy before the due date.

---
Family Bank Details Manager
This is an automated reminder.
```

---

## 🔧 Troubleshooting

### Emails Not Sending?

1. **Check Configuration:**
   - Verify `email.notifications.enabled=true`
   - Verify `spring.mail.enabled=true`
   - Check email credentials are correct

2. **Gmail Issues:**
   - Make sure you're using an **App Password** (not regular password)
   - Enable 2-Step Verification first
   - Check if "Less secure app access" is enabled (if not using App Password)

3. **Check Logs:**
   - Look for email-related errors in the application logs
   - Check if email addresses are provided in the records

4. **Test Email:**
   - Add a deposit/insurance with a date 7 days from today
   - Wait for the scheduled check (or manually trigger it)

### Disable Email Notifications:

Set in `application.properties`:
```properties
email.notifications.enabled=false
```

---

## 💡 Tips

1. **Multiple Recipients:** You can add the same email to multiple records
2. **Different Emails:** Each deposit/insurance can have a different email
3. **No Email = No Notification:** If email field is empty, no email will be sent (but dashboard reminders still work)
4. **Free Forever:** This uses your existing email account - no additional costs!

---

## 🎉 Benefits

✅ **100% Free** - No SMS costs, no API fees  
✅ **Reliable** - Uses standard email (SMTP)  
✅ **Automatic** - No manual intervention needed  
✅ **Flexible** - Works with any email provider  
✅ **Private** - Emails sent directly from your account  

---

## 📝 Notes

- Emails are sent from the address configured in `spring.mail.from`
- The scheduled task runs daily at 9:00 AM
- You can change the notification window (currently 7 days) in `EmailNotificationScheduler.java`
- All email sending is logged for debugging

Enjoy your free email notifications! 🎊
