/**
 * Created on Fri May 24 12:12:00 ICT 2019
 * HeartCore Robo Desktop v5.0.1 (Build No. 5.0.1-20190308.1)
 **/
package ana;
import com.tplan.robot.scripting.*;
import com.tplan.robot.*;
import java.awt.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class CopyFile extends DefaultJavaTestScript  {
   public void test() {
       String web_name = getContext().getVariableAsString("web_name");
       String out_file = getContext().getVariableAsString("out_file");
       
       try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            //fullPath = fullPath.replace("classes/",  out_file);
            //fullPath = fullPath.replace("src/",  out_file);
            if(fullPath.indexOf("classes") >= 0){
                fullPath = fullPath.replace("classes/", out_file);
            }
            
            if(fullPath.indexOf("src") >= 0){
                fullPath = fullPath.replace("src/", out_file);
            }
            
            //File excelFile = new File(fullPath);
            //FileInputStream fis = new FileInputStream(excelFile);
            FileInputStream fis = new FileInputStream(fullPath);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            
            String fullPathOutFile = fullPath.replace(out_file,  "data_page_source/HTML/" + web_name + "/" + dateFormat.format(date)+ "_" + web_name + ".xlsx");
            String fullPathSave = fullPath.replace(out_file,  "data_page_source/HTML/" + web_name + "/" + "HTML");
            fullPathSave = fullPathSave.substring(1, fullPathSave.length()).replace("/", "\\");
            OutputStream fout = new FileOutputStream(fullPathOutFile);
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fout.write(buffer, 0, length);
            }
            
            fis.close();
            fout.close();
            getContext().setVariable("fullPathOutFile", fullPathOutFile);
            getContext().setVariable("path_save", fullPathSave);
            getContext().setVariable("special_char", "\\");
        }catch(IOException e){
            getContext().setVariable("fullPathOutFile", "copy file excel error. " + e.toString());
        }
   }
   
   public static void main(String args[]) {
      CopyFile script = new CopyFile();
      ApplicationSupport robot = new ApplicationSupport();
      AutomatedRunnable runnable = robot.createAutomatedRunnable(script, "CopyFile@" + Integer.toHexString(script.hashCode()), args, System.out, false);
      new Thread(runnable).start();
   }
}
