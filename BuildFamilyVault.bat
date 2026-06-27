@echo off
title Build Family Vault Distribution
echo ============================================
echo   Building Family Vault portable package
echo ============================================
echo.

cd /d "%~dp0"

echo [1/3] Building JAR with Maven...
call mvn package -DskipTests -q
if %errorlevel% neq 0 (
    echo Build failed.
    pause
    exit /b 1
)

echo [2/3] Preparing FamilyVault folder...
set DIST=FamilyVault

if not exist "%DIST%" mkdir "%DIST%"
if not exist "%DIST%\data" mkdir "%DIST%\data"
if not exist "%DIST%\logs" mkdir "%DIST%\logs"
if not exist "%DIST%\config" mkdir "%DIST%\config"

copy /Y "target\family-vault-1.0.0.jar" "%DIST%\" >nul
copy /Y "dist\StartApp.bat" "%DIST%\" >nul
copy /Y "dist\StopApp.bat" "%DIST%\" >nul
copy /Y "dist\ViewLogs.bat" "%DIST%\" >nul
copy /Y "dist\StartAppHidden.vbs" "%DIST%\" >nul

echo [3/3] Copying bundled Java runtime...
if exist "%DIST%\runtime" rmdir /s /q "%DIST%\runtime"
xcopy /E /I /Q "dist\runtime" "%DIST%\runtime" >nul

if not exist "%DIST%\data\.gitkeep" echo.>"%DIST%\data\.gitkeep"
if not exist "%DIST%\logs\.gitkeep" echo.>"%DIST%\logs\.gitkeep"
if not exist "%DIST%\config\.gitkeep" echo.>"%DIST%\config\.gitkeep"

echo.
echo ============================================
echo   Done! Portable package ready at:
echo   %~dp0%DIST%
echo ============================================
echo.
echo Copy the FamilyVault folder to another PC and run StartApp.bat
echo.
pause
