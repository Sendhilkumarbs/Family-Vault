@echo off
title Family Vault
echo ============================================
echo   Family Vault
echo ============================================
echo.
echo Starting the application in background...
echo Logs are being saved to: logs\application.log
echo The browser will open automatically at http://localhost:8080
echo.
echo The application is running in the background.
echo Use StopApp.bat to stop the application.
echo ============================================
echo.

:: Create logs directory if it doesn't exist
if not exist "%~dp0logs" mkdir "%~dp0logs"

:: Wait 4 seconds then open browser
start "" cmd /c "timeout /t 4 /nobreak >nul & start http://localhost:8080"

:: Run the JAR in background with logs redirected to file
:: Option 1: Minimized window (visible in taskbar)
start /min "" java -jar "%~dp0target\family-vault-1.0.0.jar" >> "%~dp0logs\application.log" 2>&1

:: Option 2: Completely hidden (uncomment to use instead)
:: wscript "%~dp0StartAppHidden.vbs"

echo Application started successfully!
echo.
echo To view logs, open: logs\application.log
echo Or run: ViewLogs.bat
echo.
pause
