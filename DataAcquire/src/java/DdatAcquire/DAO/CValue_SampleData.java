/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DdatAcquire.DAO;

import General.DAOs.CValue;

/**
 *DROP TABLE IF EXISTS `dataservice`.`sampledata`;
CREATE TABLE  `dataservice`.`sampledata` (
  `DataNode` varchar(255) NOT NULL,
  `SampleTime` varchar(255) NOT NULL,
  `SP_DataValue` varchar(255) NOT NULL,
  `SP_RawValue` varchar(255) NOT NULL,
  `SP_App` varchar(255) NOT NULL,
  `PV_DataValue` varchar(255) NOT NULL,
  `PV_RawValue` varchar(255) NOT NULL,
  `PV_App` varchar(255) NOT NULL,
  `OP_DataValue` varchar(255) NOT NULL,
  `OP_RawValue` varchar(255) NOT NULL,
  `OP_App` varchar(255) NOT NULL,
  PRIMARY KEY (`DataNode`,`SampleTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 * @author cuilin
 */
public class CValue_SampleData extends CValue {
  public String DataNode;
  public String SampleTime;
  public String SP_DataValue;
  public String SP_RawValue;
  public String SP_App;
  public String PV_DataValue;
  public String PV_RawValue;
  public String PV_App;
  public String OP_DataValue;
  public String OP_RawValue;
  public String OP_App;
}
