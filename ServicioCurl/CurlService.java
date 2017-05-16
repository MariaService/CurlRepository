/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServicioCurl;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author test
 */
public interface CurlService {
    
  
   static String host ="ftp2.sat.gob.mx";
   static String cerName ="CSD.txt";
   static String ftpDir ="/agti_ftp/cfds_ftp/";
   static String ftpDirTarget ="/tmp/CSD/";
   static String csdTemp="CSD.txt";


public byte[] downloadFileByParts(int from, int to);
public void comandCat() throws  IOException; 

public long getSizeFtpFile(String fileName, String path);

public long getsizeFileLocal(String fileName , String path);

public String renameFile(String fileName,String path); 
    
}
