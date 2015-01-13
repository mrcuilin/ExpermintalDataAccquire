Attribute VB_Name = "Timer1"
Private Declare Sub GetLocalTime Lib "kernel32" (lpSystemTime As SYSTEMTIME)
Declare Function timeGetTime Lib "winmm.dll" () As Long


Private Type SYSTEMTIME
    wYear As Integer
    wMonth As Integer
    wDayOfWeek As Integer
    wDay As Integer
    wHour As Integer
    wMinute As Integer
    wSecond As Integer
    wMilliseconds As Integer
End Type

Public Function getTime() As String
    Dim T As SYSTEMTIME
    GetLocalTime T
    Dim D As String
    D = Format(DateSerial(T.wYear, T.wMonth, T.wDay), "yyyy-mm-dd")
    Dim TT As String
    TT = Format(TimeSerial(T.wHour, T.wMinute, T.wSecond), "Hh:Nn:Ss")

    getTime = D & " " & TT & "." & T.wMilliseconds
End Function

Public Function getTimeinMS() As String
    Dim lngTmp As Long
    lngTmp = timeGetTime
    getTimeinMS = lngTmp
End Function
