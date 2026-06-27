@echo off
title View Application Logs
echo ============================================
echo   View Application Logs
echo ============================================
echo.

if not exist "%~dp0logs\application.log" (
    echo Log file not found: logs\application.log
    echo The application may not have started yet.
    pause
    exit /b
)

echo Opening log file...
echo.
echo Press Ctrl+C to close this window when done viewing.
echo ============================================
echo.

:: Open log file in notepad
notepad "%~dp0logs\application.log"
