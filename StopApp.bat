@echo off
title Stop Family Vault
echo ============================================
echo   Stop Family Vault
echo ============================================
echo.
echo Stopping the application...
echo.

:: Kill all Java processes (this will stop the application)
taskkill /F /IM java.exe >nul 2>&1

if %errorlevel% equ 0 (
    echo Application stopped successfully.
) else (
    echo No running application found.
)

echo.
echo ============================================
pause
