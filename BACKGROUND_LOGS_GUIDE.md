# 📝 Background Logs Guide

## Overview

The application now runs in the background with all logs saved to a file automatically.

---

## 🚀 How It Works

### StartApp.bat
- **Runs application in minimized window**
- **Logs saved to:** `logs\application.log`
- **Browser opens automatically**
- **Window appears minimized in taskbar**

### StartAppHidden.vbs (Alternative)
- **Runs application completely hidden** (no window)
- **Logs saved to:** `logs\application.log`
- **Browser opens automatically**
- **No window in taskbar** (truly background)

---

## 📁 Log File Location

```
BankDetailsManager/
├── logs/
│   └── application.log  ← All application logs
└── StartApp.bat
```

---

## 🔍 Viewing Logs

### Method 1: ViewLogs.bat
1. Double-click `ViewLogs.bat`
2. Logs open in Notepad
3. View real-time logs (refresh to see updates)

### Method 2: Manual
1. Navigate to `logs\application.log`
2. Open with any text editor
3. View logs

### Method 3: Command Line
```batch
type logs\application.log
```

---

## ⚙️ Configuration Options

### Option 1: Minimized Window (Default)
- Window appears minimized in taskbar
- Can see it's running
- Easy to find and close if needed

### Option 2: Completely Hidden
1. Edit `StartApp.bat`
2. Comment out the `start /min` line
3. Uncomment the `wscript` line:
   ```batch
   :: start /min "" java -jar ...
   wscript "%~dp0StartAppHidden.vbs"
   ```
4. Save and run

---

## 🛑 Stopping the Application

### Method 1: StopApp.bat
- Double-click `StopApp.bat`
- Kills all Java processes
- Application stops immediately

### Method 2: Task Manager
1. Open Task Manager (Ctrl+Shift+Esc)
2. Find `java.exe` process
3. End task

---

## 📊 Log File Details

### What's Logged
- Application startup messages
- Database operations
- Email sending attempts
- Errors and exceptions
- Scheduled task executions
- All Spring Boot logs

### Log Rotation
- Logs append to the same file
- File grows over time
- Manually delete/archive if needed

### Log File Size
- Monitor file size periodically
- Archive old logs if needed
- Delete `logs\application.log` to start fresh

---

## 💡 Tips

1. **Check Logs for Errors**: If something doesn't work, check `logs\application.log`
2. **Real-time Monitoring**: Keep `ViewLogs.bat` open and refresh to see new logs
3. **Archive Logs**: Periodically backup or archive old log files
4. **Clean Start**: Delete `logs\application.log` to start with fresh logs

---

## 🔧 Troubleshooting

### Logs Not Appearing?
- Check if `logs` directory exists
- Verify application is running
- Check file permissions

### Application Not Starting?
- Check `logs\application.log` for errors
- Verify Java is installed (for root StartApp.bat)
- Check if port 8080 is available

### Can't Find Application?
- Check Task Manager for `java.exe`
- Use `StopApp.bat` to stop
- Check `logs\application.log` for status

---

## ✅ Benefits

✅ **Background Operation**: Runs without blocking  
✅ **Persistent Logs**: All logs saved to file  
✅ **Easy Monitoring**: View logs anytime  
✅ **Clean Interface**: No console window clutter  
✅ **Production Ready**: Suitable for continuous operation  

---

Enjoy your background-running application! 🎉
