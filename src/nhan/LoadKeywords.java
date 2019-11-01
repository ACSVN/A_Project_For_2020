/**
 * Created on Fri May 24 12:50:38 ICT 2019
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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadKeywords extends DefaultJavaTestScript  {

  public void test() {
    try {
      String web_name = getContext().getVariableAsString("web_name");
      String fileName = getContext().getVariableAsString("input_file");
      String sheetName = getContext().getVariableAsString("sheet_keyword");
      getContext().setVariable("check_detail", sheetName);
      String path = this.getClass().getClassLoader().getResource("").getPath();
      String fullPath = URLDecoder.decode(path, "UTF-8");
      if(fullPath.indexOf("src") >= 0){
          fullPath = fullPath.replace("src/", fileName);
      }
      if(fullPath.indexOf("classes") >= 0){
        fullPath = fullPath.replace("classes/", fileName);
      }

      File excelFile = new File(fullPath);
      FileInputStream fis = new FileInputStream(excelFile);

      // we create an XSSF Workbook object for our XLSX Excel File
      XSSFWorkbook workbook = new XSSFWorkbook(fis);

      // choose sheet name
      XSSFSheet sheet = workbook.getSheet(sheetName);
      getContext().setVariable("check_detail", "Load list keywords success. Keyword of sheet: " + sheetName);

      //we iterate on rows
      Iterator<Row> rowIt = sheet.iterator();
      int i = 0;

      while(rowIt.hasNext()) {
        Row row = rowIt.next();
        Cell cell = row.getCell(0);
        Cell cell2 = row.getCell(1);
        if(web_name.indexOf("gigazine") >= 0){
            if(cell.getStringCellValue().length() > 2){
                getContext().setVariable("keyword"+i, cell.getStringCellValue());
                getContext().setVariable("keyname"+i, cell2.getStringCellValue());
                
                i++;
            }
        }else{
            getContext().setVariable("keyword"+i, cell.getStringCellValue());
            getContext().setVariable("keyname"+i, cell2.getStringCellValue());
            i++;
        }
      }
      getContext().setVariable("total_keyword", i);
      fis.close();
    }catch (StopRequestException ex) {
      getContext().setVariable("check_detail", "error: " + ex.toString());
      throw ex;
    } catch(IOException e){
      getContext().setVariable("check_detail", "error: " + e.toString());
      throw new RuntimeException(e);
    }
  }
   
  public static void main(String args[]) {
    LoadKeywords script = new LoadKeywords();
    ApplicationSupport robot = new ApplicationSupport();
    AutomatedRunnable runnable = robot.createAutomatedRunnable(script, "LoadKeywords@" + Integer.toHexString(script.hashCode()), args, System.out, false);
    new Thread(runnable).start();
  }
}
