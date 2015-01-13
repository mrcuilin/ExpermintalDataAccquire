VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
  Persistable = 0  'NotPersistable
  DataBindingBehavior = 0  'vbNone
  DataSourceBehavior  = 0  'vbNone
  MTSTransactionMode  = 0  'NotAnMTSObject
END
Attribute VB_Name = "DBControll"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = False



Public Sub insert(v As DataValue)
    Dim SQL As String
    SQL = "insert into sampledata values(" + _
            "'" + v.nodeName + "'," + _
            "'" + v.SampleTime + "'," + _
            "'" + v.SP_Value + "'," + _
            "'" + v.SP_Raw + "'," + _
            "'" + v.SP_Appendix + "'," + _
            "'" + v.PV_Value + "'," + _
            "'" + v.PV_Raw + "'," + _
            "'" + v.PV_Appendix + "'," + _
            "'" + v.OP_Value + "'," + _
            "'" + v.OP_Raw + "'," + _
            "'" + v.OP_Appendix + "')"
            
    If Constants.conn Is Nothing Then
        Call getConnection
    End If
    
    Constants.conn.Execute SQL
    
End Sub

Public Function getConnection() As ADODB.Connection
    Set Constants.conn = New ADODB.Connection
    Constants.conn.Open DataAccquireForm.DSN.Text, DataAccquireForm.Account.Text, DataAccquireForm.Pwd.Text
    
   
End Function

