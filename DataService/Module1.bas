Attribute VB_Name = "Constants"
Public globalPumpNumber As Integer
Public globalPumpAddresses() As Integer
Public availablePorts() As Integer

Public CommPort As String
Public CommSet As String

Public ReceiveBytes() As Byte

Public conn As ADODB.Connection

Public DoingLoop  As Boolean

Public Sub Initialize()
    DSN = "DataService"
    Account = "root"
    Pwd = "root"
    
    
End Sub


Public Sub globalInitialize()
    
    ReDim availablePorts(0) As Integer
    
    CommPort = "COM 1"
    CommSet = "9600,N,8,1"
    
End Sub

Public Sub wait(ms As Integer)
    Dim oldV As Double
    oldV = getTimeinMS()
    While (oldV + ms) > getTimeinMS()
        DoEvents
    Wend
End Sub

Public Sub sendCmd(sendbuffer() As Byte)
    Dim Success As Boolean
    Dim retry As Integer
    Dim CheckSum As Long
    
    Success = False
    retry = 0
    
    Dim waitReply As Integer
    waitReply = Val(DataAccquireForm.CommWaitReply.Text)
    
    
    While (Not Success) And (retry < 3)
        DataAccquireForm.MSComm1.InputLen = 0
        ReDim Constants.ReceiveBytes(0)
        DataAccquireForm.MSComm1.Output = sendbuffer
        Dim oldV As Double
        oldV = getTimeinMS()
        
        While ((oldV + waitReply) > getTimeinMS()) And (UBound(Constants.ReceiveBytes) < 9)
            DoEvents
        Wend
        
        If (UBound(Constants.ReceiveBytes) >= 9) Then
            CheckSum = (CLng(ReceiveBytes(0)) + CLng(ReceiveBytes(1)) * 256 + _
                    CLng(ReceiveBytes(2)) + CLng(ReceiveBytes(3)) * 256 + _
                    CLng(ReceiveBytes(4)) + CLng(ReceiveBytes(5)) * 256 + _
                    CLng(ReceiveBytes(6)) + CLng(ReceiveBytes(7)) * 256 + _
                    CLng(sendbuffer(0)) - 128) Mod 65536
            If (CheckSum = CLng(ReceiveBytes(8)) + CLng(ReceiveBytes(9)) * 256) Then
                Success = True
            End If
        End If

        retry = retry + 1
            
    Wend
End Sub

Public Function composeSendData(port As Integer) As Byte()
    Dim buffer(7) As Byte
    Dim CheckSum As Integer
    buffer(0) = 128 + port
    buffer(1) = 128 + port
    buffer(2) = 82
    buffer(3) = 12
    buffer(4) = 0
    buffer(5) = 0
    CheckSum = CLng(buffer(3)) * 256 + 82 + port
    
    buffer(6) = CByte(CheckSum Mod 256)
    buffer(7) = CByte(CheckSum \ 256)
        
    composeSendData = buffer
    
End Function

Public Sub BeginAcc()
    Dim count As Integer
    Dim D As DataValue
    Dim DBC As DBControll
    Dim i As Integer
    Dim T() As Byte
    Set DBC = New DBControll
    
    Dim sampleItv As Integer
    sampleItv = Val(DataAccquireForm.SampleInterval.Text)
    
    Dim loopItv As Integer
    loopItv = Val(DataAccquireForm.loopInterval.Text)
    While Constants.DoingLoop
        For i = 1 To 3
            
            On Error Resume Next
            count = count + 1
            T = composeSendData(i)
            sendCmd T
            
            Set D = parseResult()
            If ERR.Description <> "" Then GoTo NEXTS
            
            D.nodeName = "NODE" & i
            DataAccquireForm.STATUS.Text = count & ":" & " Get Node" & i & " Data OK"
            DoEvents
            
            On Error Resume Next
            DBC.insert D
            If ERR.Description <> "" Then GoTo NEXTS
            
            DataAccquireForm.STATUS.Text = count & ":" & " Write Node" & i & " Data to DB OK"
            
            GoTo CONTINUE
NEXTS:
            DataAccquireForm.STATUS.Text = count & ":" & " Error on node " & i & " : " & ERR.Description
            ERR.Clear
CONTINUE:
            ReDim Constants.ReceiveBytes(0)
            
            wait (sampleItv)
        Next i
        wait (loopItv)
    Wend
End Sub

Public Function getSelfTime() As String
    Dim TIMEs
    Dim TL As String
    TL = Timer1.getTime
    
    TIMEs = Split(TL, ".")
    While (Len(TIMEs(1)) < 3)
        TIMEs(1) = "0" + TIMEs(1)
    Wend
    Dim r As String
    r = TIMEs(0) & "." & TIMEs(1)
    r = Replace(r, "/", "-")
    
    Dim TIME2
    TIME2 = Split(r, " ")
    If Len(TIME2(1)) < 12 Then
        r = TIME2(0) & " 0" & TIME2(1)
    End If
    getSelfTime = r
    
End Function

Public Function parseResult() As DataValue
    Dim PV As Long
    Dim SP As Long
    Dim OP As Long
    Dim Point As Long
    Dim Ratio As Double
    
    On Error Resume Next
    Dim r As DataValue
    Set r = New DataValue
    r.SampleTime = getSelfTime()
    
    
    PV = CLng(Constants.ReceiveBytes(0)) + CLng(Constants.ReceiveBytes(1)) * 256
    If (PV > 32768) Then PV = PV - 65536
    SP = CLng(Constants.ReceiveBytes(2)) + CLng(Constants.ReceiveBytes(3)) * 256
    If (SP > 32768) Then SP = SP - 65536
    OP = CLng(Constants.ReceiveBytes(4))
    If (OP > 32768) Then OP = OP - 65536
    
    Point = CLng(Constants.ReceiveBytes(6)) + CLng(Constants.ReceiveBytes(7)) * 256
     If (Point > 32768) Then Point = Point - 65536
    If Point = 0 Then
        Ratio = 1#
    ElseIf Point = 1 Then
        Ratio = 0.1
    ElseIf Point = 2 Then
        Ratio = 0.01
    ElseIf Point = 3 Then
        Ratio = 0.001
    ElseIf Point = 128 Then
        Ratio = 0.1
    ElseIf Point = 129 Then
        Ratio = 0.01
    ElseIf Point = 130 Then
        Ratio = 0.001
    ElseIf Point = 131 Then
        Ratio = 0.0001
    End If

    r.OP_Raw = OP
    r.PV_Raw = PV
    r.SP_Raw = SP
    
    r.OP_Value = OP
    r.PV_Value = PV * Ratio
    r.SP_Value = SP * Ratio
    
     
    Set parseResult = r
End Function


