VERSION 5.00
Object = "{648A5603-2C6E-101B-82B6-000000000014}#1.1#0"; "MSCOMM32.OCX"
Begin VB.Form DataAccquireForm 
   Caption         =   "485 Service"
   ClientHeight    =   5070
   ClientLeft      =   60
   ClientTop       =   450
   ClientWidth     =   5385
   LinkTopic       =   "Form1"
   ScaleHeight     =   5070
   ScaleWidth      =   5385
   StartUpPosition =   3  'Windows Default
   Begin VB.CommandButton Command2 
      Caption         =   "Command2"
      Height          =   495
      Left            =   360
      TabIndex        =   20
      Top             =   3000
      Width           =   615
   End
   Begin VB.TextBox DSN 
      Height          =   285
      Left            =   2400
      TabIndex        =   13
      Text            =   "DataService"
      Top             =   2640
      Width           =   1455
   End
   Begin VB.TextBox Account 
      Height          =   285
      IMEMode         =   3  'DISABLE
      Left            =   2400
      PasswordChar    =   "*"
      TabIndex        =   12
      Text            =   "root"
      Top             =   3000
      Width           =   1455
   End
   Begin VB.TextBox Pwd 
      Height          =   285
      IMEMode         =   3  'DISABLE
      Left            =   2400
      PasswordChar    =   "*"
      TabIndex        =   11
      Text            =   "root"
      Top             =   3360
      Width           =   1455
   End
   Begin VB.TextBox CommWaitReply 
      Height          =   285
      Left            =   2400
      TabIndex        =   10
      Text            =   "600"
      Top             =   3840
      Width           =   1455
   End
   Begin VB.TextBox SampleInterval 
      Height          =   285
      Left            =   2400
      TabIndex        =   9
      Text            =   "300"
      Top             =   4200
      Width           =   1455
   End
   Begin VB.TextBox loopInterval 
      Height          =   285
      Left            =   2400
      TabIndex        =   8
      Text            =   "500"
      Top             =   4560
      Width           =   1455
   End
   Begin VB.TextBox STATUS 
      Height          =   375
      Left            =   240
      TabIndex        =   6
      Top             =   1560
      Width           =   4935
   End
   Begin VB.CommandButton Command1 
      Caption         =   "STOP"
      Height          =   375
      Left            =   2760
      TabIndex        =   5
      Top             =   1080
      Width           =   2055
   End
   Begin VB.ComboBox CommSpeedList 
      Height          =   315
      ItemData        =   "DataAccquireForm.frx":0000
      Left            =   1800
      List            =   "DataAccquireForm.frx":0011
      Style           =   2  'Dropdown List
      TabIndex        =   2
      Top             =   120
      Width           =   2655
   End
   Begin VB.ComboBox portsList 
      Height          =   315
      Left            =   1800
      Style           =   2  'Dropdown List
      TabIndex        =   1
      Top             =   600
      Width           =   2655
   End
   Begin VB.CommandButton Command3 
      Caption         =   "START"
      Height          =   375
      Left            =   480
      TabIndex        =   0
      Top             =   1080
      Width           =   1815
   End
   Begin MSCommLib.MSComm MSComm1 
      Left            =   4560
      Top             =   120
      _ExtentX        =   1005
      _ExtentY        =   1005
      _Version        =   393216
      DTREnable       =   -1  'True
   End
   Begin VB.Line Line1 
      X1              =   240
      X2              =   5280
      Y1              =   2280
      Y2              =   2280
   End
   Begin VB.Label Label1 
      Alignment       =   1  'Right Justify
      Caption         =   "DSN:"
      Height          =   255
      Index           =   1
      Left            =   1800
      TabIndex        =   19
      Top             =   2640
      Width           =   495
   End
   Begin VB.Label Label2 
      Alignment       =   1  'Right Justify
      Caption         =   "Account:"
      Height          =   255
      Index           =   2
      Left            =   1440
      TabIndex        =   18
      Top             =   3000
      Width           =   855
   End
   Begin VB.Label Label2 
      Alignment       =   1  'Right Justify
      Caption         =   "Pwd"
      Height          =   255
      Index           =   1
      Left            =   1440
      TabIndex        =   17
      Top             =   3360
      Width           =   855
   End
   Begin VB.Label Label3 
      Alignment       =   1  'Right Justify
      Caption         =   "通信超时:"
      Height          =   255
      Index           =   3
      Left            =   1080
      TabIndex        =   16
      Top             =   3840
      Width           =   1215
   End
   Begin VB.Label Label3 
      Alignment       =   1  'Right Justify
      Caption         =   "采样间隔:"
      Height          =   255
      Index           =   1
      Left            =   1080
      TabIndex        =   15
      Top             =   4200
      Width           =   1215
   End
   Begin VB.Label Label3 
      Alignment       =   1  'Right Justify
      Caption         =   "循环间隔:"
      Height          =   255
      Index           =   2
      Left            =   1080
      TabIndex        =   14
      Top             =   4560
      Width           =   1215
   End
   Begin VB.Label Label3 
      Height          =   255
      Index           =   0
      Left            =   5280
      TabIndex        =   7
      Top             =   1920
      Width           =   135
   End
   Begin VB.Label Label1 
      Caption         =   "通信速率、协议："
      Height          =   255
      Index           =   0
      Left            =   240
      TabIndex        =   4
      Top             =   120
      Width           =   1455
   End
   Begin VB.Label Label2 
      Alignment       =   1  'Right Justify
      Caption         =   "端口："
      Height          =   255
      Index           =   0
      Left            =   360
      TabIndex        =   3
      Top             =   600
      Width           =   1335
   End
End
Attribute VB_Name = "DataAccquireForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False


Dim timeOut As Boolean


Private Sub Command1_Click()
    MSComm1.PortOpen = False
    DoingLoop = False
End Sub


Private Sub Command2XX_Click()
    Dim D As DataValue
    ReDim Constants.ReceiveBytes(9)
    Constants.ReceiveBytes(0) = &HFD
    Constants.ReceiveBytes(1) = &HFF
    Constants.ReceiveBytes(2) = &H96
    Constants.ReceiveBytes(3) = &H0
    Constants.ReceiveBytes(4) = &H64
    Constants.ReceiveBytes(5) = &H60
    Constants.ReceiveBytes(6) = &H0
    Constants.ReceiveBytes(7) = &H0
    Constants.ReceiveBytes(8) = &HF8
    Constants.ReceiveBytes(9) = &H60
    
    Dim p(1) As Byte
    p(0) = 0
    p(1) = 0
    Constants.sendCmd p
    
    Set D = parseResult
    
End Sub

Private Sub Command2_Click()
    MsgBox Constants.getSelfTime
    
End Sub

Private Sub Command3_Click()
    Dim CommPort As String
    Dim CommSet As String
    
    CommPort = portsList.List(portsList.ListIndex)
    CommSet = CommSpeedList.List(CommSpeedList.ListIndex)

    MSComm1.CommPort = CInt(Mid(CommPort, 4))
    MSComm1.Settings = CommSet
    MSComm1.RThreshold = 1
    MSComm1.PortOpen = True
    MSComm1.InputMode = comInputModeBinary
    MSComm1.NullDiscard = False
    
    Constants.DoingLoop = True
    
    
    Call Constants.BeginAcc
End Sub

Private Sub Form_Load()
    globalInitialize

    If UBound(availablePorts) = 0 Then
        For i = 1 To 24
            portOK = checkCommPort(i)
            If portOK Then
                ReDim Preserve availablePorts(UBound(availablePorts) + 1)
                availablePorts(UBound(availablePorts)) = i
            End If
        Next i
    End If
    portsList.Clear
    For i = 1 To UBound(availablePorts)
        portsList.AddItem ("COM " & availablePorts(i))
    Next i
    
    If UBound(availablePorts) > 0 Then portsList.ListIndex = 0
    For i = 0 To portsList.ListCount - 1
        If portsList.List(i) = CommPort Then portsList.ListIndex = i
    Next
    
    CommSpeedList.ListIndex = 0
    For i = 0 To CommSpeedList.ListCount - 1
        If CommSpeedList.List(i) = CommSet Then CommSpeedList.ListIndex = i
    Next
    
    Initialize
End Sub

Private Function checkCommPort(ByVal portNumber As Integer) As Boolean
    Dim portIsOpen As Boolean
    On Error GoTo ERR
    MSComm1.CommPort = portNumber
    portIsOpen = MSComm1.PortOpen
    MSComm1.PortOpen = (Not PortOpen)
    MSComm1.PortOpen = PortOpen
    checkCommPort = True
    Exit Function
ERR:
    checkCommPort = False
End Function


Private Sub Form_Terminate()
    On Error Resume Next
    Constants.conn.Close
    MSComm1.PortOpen = False
    
    End
End Sub


Private Sub MSComm1_OnComm()
    Dim rec() As Byte
    Dim i As Integer
    Dim inc As Integer
    Dim p As Byte
    Dim X As String
    Select Case MSComm1.CommEvent
    ' 错误
        Case comEventBreak      ' 收到 Break。
        Case comEventCDTO       ' CD (RLSD) 超时。
        Case comEventCTSTO      ' CTS Timeout。
        Case comEventDSRTO      ' DSR Timeout。
        Case comEventFrame      ' Framing Error
        Case comEventOverrun    '数据丢失。
        Case comEventRxOver     '接收缓冲区溢出。

        Case comEventRxParity ' Parity 错误。
        Case comEventTxFull '传输缓冲区已满。
        Case comEventDCB    '获取 DCB] 时意外错误
    ' 事件
        Case comEvCD        ' CD 线状态变化。
        Case comEvCTS       ' CTS 线状态变化。
        Case comEvDSR       ' DSR 线状态变化。
        Case comEvRing      ' Ring Indicator 变化。
        Case comEvSend      ' 传输缓冲区有 Sthreshold 个字符
        Case comEvEOF       ' 输入数据流中发现 EOF 字符
        Case comEvReceive   ' 收到 RThreshold # of chars.
            inc = MSComm1.InBufferCount
            If inc > 1 Then
                Dim buffer As Variant
                MSComm1.InputLen = 0
                buffer = MSComm1.Input
                Dim orginaLength As Integer
                orginaLength = UBound(Constants.ReceiveBytes)
                ReDim Preserve Constants.ReceiveBytes(orginaLength + inc) As Byte
                For i = 0 To inc - 1
                    Constants.ReceiveBytes(orginaLength + i) = buffer(i)
                Next
                
                'Call splitCommands
            End If
    End Select
End Sub

Private Sub Timer1_Timer()
     
End Sub
