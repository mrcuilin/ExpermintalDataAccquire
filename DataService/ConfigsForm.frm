VERSION 5.00
Begin VB.Form ConfigsForm 
   Caption         =   "Configs"
   ClientHeight    =   3090
   ClientLeft      =   60
   ClientTop       =   450
   ClientWidth     =   4680
   LinkTopic       =   "Form1"
   ScaleHeight     =   3090
   ScaleWidth      =   4680
   StartUpPosition =   3  'Windows Default
   Begin VB.TextBox loopInterval 
      Height          =   285
      Left            =   1440
      TabIndex        =   11
      Text            =   "500"
      Top             =   2040
      Width           =   1455
   End
   Begin VB.TextBox SampleInterval 
      Height          =   285
      Left            =   1440
      TabIndex        =   10
      Text            =   "300"
      Top             =   1680
      Width           =   1455
   End
   Begin VB.TextBox CommWaitReply 
      Height          =   285
      Left            =   1440
      TabIndex        =   9
      Text            =   "600"
      Top             =   1320
      Width           =   1455
   End
   Begin VB.TextBox Pwd 
      Height          =   285
      IMEMode         =   3  'DISABLE
      Left            =   1440
      PasswordChar    =   "*"
      TabIndex        =   5
      Text            =   "root"
      Top             =   840
      Width           =   1455
   End
   Begin VB.TextBox Account 
      Height          =   285
      IMEMode         =   3  'DISABLE
      Left            =   1440
      PasswordChar    =   "*"
      TabIndex        =   4
      Text            =   "root"
      Top             =   480
      Width           =   1455
   End
   Begin VB.TextBox DSN 
      Height          =   285
      Left            =   1440
      TabIndex        =   1
      Text            =   "DataService"
      Top             =   120
      Width           =   1455
   End
   Begin VB.Label Label3 
      Alignment       =   1  'Right Justify
      Caption         =   "循环间隔:"
      Height          =   255
      Index           =   2
      Left            =   120
      TabIndex        =   8
      Top             =   2040
      Width           =   1215
   End
   Begin VB.Label Label3 
      Alignment       =   1  'Right Justify
      Caption         =   "采样间隔:"
      Height          =   255
      Index           =   1
      Left            =   120
      TabIndex        =   7
      Top             =   1680
      Width           =   1215
   End
   Begin VB.Label Label3 
      Alignment       =   1  'Right Justify
      Caption         =   "通信超时:"
      Height          =   255
      Index           =   0
      Left            =   120
      TabIndex        =   6
      Top             =   1320
      Width           =   1215
   End
   Begin VB.Label Label2 
      Alignment       =   1  'Right Justify
      Caption         =   "Pwd"
      Height          =   255
      Index           =   1
      Left            =   480
      TabIndex        =   3
      Top             =   840
      Width           =   855
   End
   Begin VB.Label Label2 
      Alignment       =   1  'Right Justify
      Caption         =   "Account:"
      Height          =   255
      Index           =   0
      Left            =   480
      TabIndex        =   2
      Top             =   480
      Width           =   855
   End
   Begin VB.Label Label1 
      Alignment       =   1  'Right Justify
      Caption         =   "DSN:"
      Height          =   255
      Left            =   840
      TabIndex        =   0
      Top             =   120
      Width           =   495
   End
End
Attribute VB_Name = "ConfigsForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private Sub Text3_Change()

End Sub

Private Sub Form_Load()

End Sub
