/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServicioCurl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author test
 */
public class CurlServiceImpl implements CurlService {

    Calendar calendar = new GregorianCalendar();
    String dia = Integer.toString(calendar.get(Calendar.DATE));
    String mes = Integer.toString(calendar.get(Calendar.MONTH));
    String anio = Integer.toString(calendar.get(Calendar.YEAR));

    static CurlServiceImpl curlServiceImpl = new CurlServiceImpl();

    String newFileCsd = " part-csd_" + dia + "-" + mes + "-" + anio;

    @Override
    public byte[] downloadFileByParts(int from, int to) {
        ProcessBuilder processBuilder = new ProcessBuilder("curl", "--range",
                from + "-" + to, "-o", ftpDirTarget + "part-csd_" + dia + "-" + mes + "-" + anio,
                "ftp://ftp2.sat.gob.mx/agti_ftp/cfds_ftp/CSD.txt");
        System.out.println("curl" + from + "-" + to + " -o" + " " + newFileCsd + "  " + "ftp://ftp2.sat.gob.mx/agti_ftp/cfds_ftp/CSD.txt");

        try {
            Process process = processBuilder.start();
            InputStream fileStream = process.getInputStream();
            byte[] bytes = IOUtils.toByteArray(fileStream);

            return bytes;

        } catch (IOException e) {

            System.out.println("Error" + e);

        }
        return null;

    }

    @Override
    public long getSizeFtpFile(String fileName, String path) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        // TODO Auto-generated method stub
        if (fileName != null && path != null) {
            FTPClient client = new FTPClient();
            try {
                client.connect("ftp2.sat.gob.mx");
                client.enterLocalPassiveMode();
                if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
                } else if (client.login("anonymous", "anonymous")) {
                    client.sendCommand("SIZE", "/agti_ftp/cfds_ftp/CSD.txt");
                    String result = client.getReplyString();
                    String[] temp = result.split(" ");
                    int size = Integer.parseInt(temp[1].trim());
                    client.logout();
                    return size;
                }
            } catch (Exception e) {

            } finally {
                try {
                    client.disconnect();
                } catch (Exception e) {

                }
            }
        }
        return 0;
    }

    public static void main(String args[]) throws IOException, InterruptedException {
     
        
            long ftpFile = curlServiceImpl.getSizeFtpFile(cerName, host);
            long localFile = curlServiceImpl.getsizeFileLocal(ftpDirTarget, csdTemp);

            curlServiceImpl.renameFile(ftpDirTarget, csdTemp);
            curlServiceImpl.downloadFileByParts((int) localFile, (int) ftpFile);
            Thread.sleep(1000* 2);
            curlServiceImpl.comandCat();

        
    }

    @Override
    public long getsizeFileLocal(String path, String fileName) {
        //To change body of generated methods, choose Tools | Templates.
        String dirTemp = path + fileName;
        File csdLocal = new File(dirTemp);
        long bytes = csdLocal.length();

        if (bytes > 0) {
            return bytes;
        }
        return 0;
    }

    @Override
    public String renameFile(String fileName, String path) {

        File csd = new File(fileName + path);
        File renameCSD = new File(fileName + "CSD.Indvalid" + anio + mes + dia + ".txt");
        boolean correcto = csd.renameTo(renameCSD);
        if (correcto) {
            System.out.println("archivo correcto " + renameCSD.toString());
        } else {
            System.out.println("error archivo....");
        }
        return renameCSD.toString();

//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void comandCat() throws IOException {

      ProcessBuilder builder = new ProcessBuilder("cat", ftpDirTarget+"CSD.Indvalid"+anio+mes+dia+".txt"
             ,ftpDirTarget+"part-csd_" + dia + "-" + mes + "-" + anio);
      
      
      File combinedFile = new File(ftpDirTarget+csdTemp);
 
         Process p = builder.start();
       
    
        InputStream isfromCat = p.getInputStream();
        OutputStream osCombinedFile = new FileOutputStream(combinedFile);

        byte [] buffer = new byte[1024];
        int read =0;
        while((read = isfromCat.read(buffer)) != -1){
        
        osCombinedFile.write(buffer,0,read);
        }
    }
}
