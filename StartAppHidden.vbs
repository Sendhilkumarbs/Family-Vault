Set WshShell = CreateObject("WScript.Shell")
Set fso = CreateObject("Scripting.FileSystemObject")

' Get the directory where this script is located
scriptPath = fso.GetParentFolderName(WScript.ScriptFullName)

' Create logs directory if it doesn't exist
logsDir = scriptPath & "\logs"
If Not fso.FolderExists(logsDir) Then
    fso.CreateFolder(logsDir)
End If

' Log file path
logFile = logsDir & "\application.log"

' Java executable and JAR paths
jarFile = scriptPath & "\target\family-vault-1.0.0.jar"

' Command to run (using system Java)
cmd = "java -jar """ & jarFile & """ >> """ & logFile & """ 2>&1"

' Run in hidden window (0 = hidden)
WshShell.Run cmd, 0, False

' Wait a bit then open browser
WScript.Sleep 4000
WshShell.Run "http://localhost:8080", 1, False
