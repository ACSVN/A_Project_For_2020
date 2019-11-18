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

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Folder extends DefaultJavaTestScript  {

  public void test() {
    try {
      String web_name = getContext().getVariableAsString("web_name");

      DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
      Date date = new Date();

      String path = this.getClass().getClassLoader().getResource("").getPath();
      String fullPathImages = URLDecoder.decode(path, "UTF-8");
      //fullPathImages = fullPathImages.replace("classes/", "data_storge/" + web_name + "/" + dateFormat.format(date));
      fullPathImages = fullPathImages.replace("src/", "data_storge/" + web_name + "/" + dateFormat.format(date));

      File file = new File(fullPathImages);
      fullPathImages = fullPathImages.substring(1, fullPathImages.length());
      fullPathImages = fullPathImages.replace("/", "\\");
      if (!file.exists()) {
        if (file.mkdir()) {
          getContext().setVariable("str_path_image", fullPathImages);
        } else {
          getContext().setVariable("str_path_image", "Failed to create directory!");
        }
      }else{
          getContext().setVariable("str_path_image", fullPathImages);
      }

    } catch (StopRequestException ex) {
        getContext().setVariable("str_path_image", "StopRequestException!: " + ex.toString());
      throw ex;
    }catch(IOException e){
      getContext().setVariable("str_path_image", "copy file excel error. " + e.toString());
    }
  }
   
  public static void main(String args[]) {
    Folder script = new Folder();
    ApplicationSupport robot = new ApplicationSupport();
    AutomatedRunnable runnable = robot.createAutomatedRunnable(script, "Folder@" + Integer.toHexString(script.hashCode()), args, System.out, false);
    new Thread(runnable).start();
  }
}
