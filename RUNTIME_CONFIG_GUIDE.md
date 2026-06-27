# 🔧 Runtime Configuration Guide

## Overview

Your application is now **runtime configurable**! You can change settings without rebuilding the application.

---

## 📁 Configuration File Location

The runtime configuration file is located at:
```
config/application.properties
```

This file is created automatically when you first run the application.

---

## 🎯 How to Configure

### Option 1: Web UI (Recommended)

1. **Open the application** in your browser: `http://localhost:8080`
2. **Click "Settings"** in the top navigation bar
3. **Update email settings** using the form
4. **Click "Save Settings"**
5. **Restart the application** for changes to take effect

### Option 2: Edit Config File Directly

1. **Navigate to** `config/application.properties` (in the same folder as your JAR)
2. **Edit the file** with any text editor
3. **Save the file**
4. **Restart the application** for changes to take effect

---

## ⚙️ Configurable Settings

### Email Notifications

```properties
# Enable/disable email notifications
email.notifications.enabled=true

# Enable/disable mail service
spring.mail.enabled=true

# SMTP Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.from=your-email@gmail.com
```

---

## 🔄 How It Works

1. **Default Config**: Application starts with `src/main/resources/application.properties`
2. **External Config**: On first run, creates `config/application.properties`
3. **Priority**: External config overrides default config
4. **Runtime Changes**: Edit external config file or use Settings UI
5. **Restart Required**: Most changes need application restart

---

## 📝 Example: Enable Email Notifications

### Using Web UI:
1. Go to `http://localhost:8080/settings`
2. Check "Enable Email Notifications"
3. Check "Enable Mail Service"
4. Enter your email credentials
5. Click "Save Settings"
6. Restart the application

### Using Config File:
1. Open `config/application.properties`
2. Change:
   ```properties
   email.notifications.enabled=true
   spring.mail.enabled=true
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   spring.mail.from=your-email@gmail.com
   ```
3. Save the file
4. Restart the application

---

## 🎨 Settings Page Features

- ✅ **Visual Form**: Easy-to-use web interface
- ✅ **Password Masking**: Passwords are hidden for security
- ✅ **Quick Help**: Built-in instructions for Gmail/Outlook
- ✅ **Config File Location**: Shows where the config file is stored
- ✅ **Validation**: Form validation for required fields

---

## 🔒 Security Notes

- **Password Storage**: Passwords are stored in plain text in the config file
- **File Permissions**: Make sure `config/application.properties` has restricted permissions
- **Backup**: Don't commit the config file to version control if it contains passwords

---

## 🚀 Benefits

✅ **No Rebuild Required**: Change settings without recompiling  
✅ **Easy Updates**: Use web UI or edit file directly  
✅ **Persistent**: Settings survive application updates  
✅ **Flexible**: Works with any email provider  
✅ **User-Friendly**: Web interface for non-technical users  

---

## 📍 File Structure

```
BankDetailsManager/
├── config/
│   └── application.properties  ← Runtime config (editable)
├── src/
│   └── main/
│       └── resources/
│           └── application.properties  ← Default config (in JAR)
└── bank-details-manager-1.0.0.jar
```

---

## 💡 Tips

1. **Backup Config**: Before making changes, copy `config/application.properties`
2. **Test Changes**: After updating, restart and test email notifications
3. **Check Logs**: If something doesn't work, check application logs
4. **Multiple Environments**: You can have different config files for different environments

---

## 🆘 Troubleshooting

### Settings Not Saving?
- Check file permissions on `config/` directory
- Ensure the application has write access
- Check application logs for errors

### Changes Not Taking Effect?
- **Restart the application** - most settings require restart
- Check if the config file was saved correctly
- Verify the settings in the config file

### Config File Not Created?
- The file is created on first access to Settings page
- Or manually create `config/application.properties`
- Copy content from `src/main/resources/application.properties`

---

Enjoy your runtime configurable application! 🎉
