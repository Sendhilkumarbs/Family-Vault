# SMS Notification Setup Guide

## Overview
This guide explains how to set up SMS notifications for reminders (FD/RD maturity dates and insurance renewals).

---

## 📋 Requirements Checklist

### 1. **SMS Gateway Account** (Choose One)

#### Option A: Twilio (Recommended - Global)
- **Sign up:** https://www.twilio.com
- **Cost:** ~$0.0075 - $0.02 per SMS
- **What you need:**
  - Account SID
  - Auth Token
  - Twilio Phone Number (provided by Twilio)

#### Option B: TextLocal (India)
- **Sign up:** https://www.textlocal.in
- **Cost:** ~₹0.20 - ₹0.50 per SMS
- **What you need:**
  - API Key
  - Sender ID (6 characters, e.g., "BANKDT")

#### Option C: MSG91 (India)
- **Sign up:** https://msg91.com
- **Cost:** ~₹0.20 - ₹0.50 per SMS
- **What you need:**
  - Auth Key
  - Sender ID (6 characters)

#### Option D: AWS SNS (If using AWS)
- **Requires:** AWS Account
- **Cost:** ~$0.00645 per SMS
- **What you need:**
  - AWS Access Key ID
  - AWS Secret Access Key
  - AWS Region

---

## 🔧 Implementation Steps

### Step 1: Add Maven Dependency

**For Twilio:**
```xml
<dependency>
    <groupId>com.twilio.sdk</groupId>
    <artifactId>twilio</artifactId>
    <version>9.14.0</version>
</dependency>
```

**For TextLocal/MSG91:**
No additional dependency needed (uses Spring's RestTemplate/WebClient)

---

### Step 2: Add Configuration to `application.properties`

```properties
# SMS Configuration
sms.enabled=true
sms.provider=twilio  # Options: twilio, textlocal, msg91, aws-sns

# Twilio Configuration
sms.twilio.account-sid=your_account_sid_here
sms.twilio.auth-token=your_auth_token_here
sms.twilio.phone-number=+1234567890

# TextLocal Configuration
sms.textlocal.api-key=your_api_key_here
sms.textlocal.sender-id=BANKDT

# MSG91 Configuration
sms.msg91.auth-key=your_auth_key_here
sms.msg91.sender-id=BANKDT

# AWS SNS Configuration
sms.aws.access-key=your_access_key
sms.aws.secret-key=your_secret_key
sms.aws.region=us-east-1
```

---

### Step 3: Add Phone Number Fields

**Deposit Entity:**
- Add `phoneNumber` field (for SMS notifications)

**Insurance Entity:**
- Add `phoneNumber` field (for SMS notifications)

**BankAccount:**
- Already has `registeredMobileNumber` ✓

---

### Step 4: Create SMS Service

Create `SmsService.java` with methods:
- `sendSms(String phoneNumber, String message)`
- Support for multiple providers (Twilio, TextLocal, MSG91, AWS SNS)

---

### Step 5: Create Scheduled Task

Create `SmsNotificationScheduler.java`:
- Runs daily (e.g., at 9:00 AM)
- Checks for reminders due in next 7 days
- Sends SMS for each reminder
- Tracks sent notifications to avoid duplicates

---

### Step 6: Add Notification Preferences (Optional)

Add fields to entities:
- `enableSmsNotifications` (boolean)
- `smsNotificationDaysBefore` (int) - e.g., 7 days before

---

## 💰 Cost Estimation

### Example: 10 reminders per month
- **Twilio:** ~$0.15/month
- **TextLocal (India):** ~₹5/month
- **MSG91 (India):** ~₹5/month
- **AWS SNS:** ~$0.06/month

---

## 📱 SMS Message Format

**Example Messages:**

1. **FD Maturity:**
   ```
   Reminder: FD for Rajesh Kumar (A/C: FD123456) matures on 15/02/2026 (in 7 days). Amount: ₹1,00,000
   ```

2. **Insurance Renewal:**
   ```
   Reminder: Life Insurance for Priya Kumar (Policy: POL789) renews on 20/02/2026 (in 12 days). Premium: ₹25,000
   ```

---

## 🔒 Security Considerations

1. **Store credentials securely:**
   - Use environment variables or encrypted properties
   - Never commit API keys to Git

2. **Phone number validation:**
   - Validate format before sending
   - Support international format (+91XXXXXXXXXX)

3. **Rate limiting:**
   - Implement daily SMS limits
   - Prevent spam/abuse

---

## ✅ Testing Checklist

- [ ] SMS gateway account created
- [ ] API credentials configured
- [ ] Phone number fields added to entities
- [ ] SmsService implemented
- [ ] Scheduled task created
- [ ] Test SMS sent successfully
- [ ] Reminder SMS sent for test data
- [ ] Error handling tested

---

## 🚀 Quick Start (Twilio Example)

1. Sign up at https://www.twilio.com
2. Get Account SID and Auth Token from dashboard
3. Get a Twilio phone number
4. Add credentials to `application.properties`
5. Add Twilio dependency to `pom.xml`
6. Implement `SmsService` and scheduler
7. Test with a reminder

---

## 📞 Support

For issues:
- Check SMS gateway dashboard for delivery status
- Verify phone number format
- Check API rate limits
- Review error logs
