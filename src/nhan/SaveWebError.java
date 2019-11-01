/**
 * Created on Fri Sep 13 15:59:12 ICT 2019
 * HeartCore Robo Desktop v5.0.1 (Build No. 5.0.1-20190308.1)
 **/
package ana;
import com.tplan.robot.scripting.*;
import com.tplan.robot.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Header; 
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.BorderStyle;

public class SaveWebError extends DefaultJavaTestScript  {
    public String fileNameDS = "";
  public void writeDataError(String fileName, String web_name, String keyword, String status, String total_href){
        try{
            
            // String path = this.getClass().getClassLoader().getResource("").getPath();
            // String fullPath = URLDecoder.decode(path, "UTF-8");
            //fullPath = fullPath.replace("classes/", fileName);
            // if(fullPath.indexOf("data_page_source") >= 0){
            //     fullPath = fullPath.replace("data_page_source/",  fileName);
            // }
             String path = this.getClass().getClassLoader().getResource("").getPath();
             path =  path.substring(1, path.length());
            String fullPath = URLDecoder.decode(path, "UTF-8");
            //fullPath = fullPath.replace("classes/",  "src/" + name_file);
            if(fullPath.indexOf("classes") >= 0){
                fullPath = fullPath.replace("classes/",  "src/" + fileName);
            }else if(fullPath.indexOf("src") >= 0){
                fullPath = fullPath.replace("src/",  "src/" + fileName);
            }
            getContext().setVariable("check_path", "fullPath: " + fullPath);
            getContext().setVariable("check_detail", "fullPath: " + fullPath);


            File excelFile = new File(fullPath);
            FileInputStream fis = new FileInputStream(excelFile);
            //getContext().setVariable("check_path", "fullPath: " + fullPath);
            //getContext().setVariable("check_detail", "fullPath: " + fullPath);

            //// we create an XSSF Workbook object for our XLSX Excel File
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            //// choose sheet name
            XSSFSheet sheet = workbook.getSheetAt(0);
            int lastRow=sheet.getLastRowNum();
            Font font1 = workbook.createFont();
            font1.setColor(IndexedColors.BLACK.getIndex());
            
            Font font2 = workbook.createFont();
            font2.setColor(IndexedColors.WHITE.getIndex());

            //backgroundStyle.setFont(font);
            //
            //CellStyle backgroundStyle2 = workbook.createCellStyle();
            //backgroundStyle2.setFillForegroundColor(IndexedColors.RED.getIndex());
            //backgroundStyle2.setFont(font2);
            //getContext().setVariable("check_path", "fullPath: " + fullPath);
            //getContext().setVariable("check_detail", "fullPath: " + fullPath);
            ////backgroundStyle2.setFillPattern(CellStyle.SOLID_FOREGROUND);
            Row row = sheet.createRow(++lastRow);
            
            CellStyle stylebd = workbook.createCellStyle();
            stylebd.setBorderBottom(BorderStyle.MEDIUM);
            stylebd.setBorderLeft(BorderStyle.MEDIUM);
            stylebd.setBorderRight(BorderStyle.MEDIUM);
            stylebd.setBorderTop(BorderStyle.MEDIUM);
            
            
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font1);
            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
            cellStyle.setBorderRight(BorderStyle.MEDIUM);
            cellStyle.setBorderTop(BorderStyle.MEDIUM);
            cellStyle.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            cellStyle.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setFont(font1);
            
            CellStyle cellStyle2 = workbook.createCellStyle();
            cellStyle2.setFont(font2);
            cellStyle2.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle2.setBorderLeft(BorderStyle.MEDIUM);
            cellStyle2.setBorderRight(BorderStyle.MEDIUM);
            cellStyle2.setBorderTop(BorderStyle.MEDIUM);
            cellStyle2.setFillBackgroundColor(IndexedColors.RED.getIndex());
            cellStyle2.setFillForegroundColor(IndexedColors.RED.getIndex());
            cellStyle2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle2.setFont(font2);
            
            CellStyle cellStyle3 = workbook.createCellStyle();
            cellStyle3.setFont(font2);
            cellStyle3.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle3.setBorderLeft(BorderStyle.MEDIUM);
            cellStyle3.setBorderRight(BorderStyle.MEDIUM);
            cellStyle3.setBorderTop(BorderStyle.MEDIUM);
            cellStyle3.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
            cellStyle3.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            cellStyle3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle3.setFont(font1);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(web_name);
            cell0.setCellStyle(stylebd);
            
            Cell cell1 = row.createCell(1);           
            cell1.setCellValue(keyword);
            cell1.setCellStyle(stylebd);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(total_href);
            cell2.setCellStyle(stylebd);
            //row.createCell(0).setCellValue(web_name);
            //row.createCell(1).setCellValue(keyword);
            //row.createCell(2).setCellValue(total_href);
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(status);

            if(status.indexOf("Success") >= 0){
                //
                cell3.setCellStyle(cellStyle);
                //
            }else if(status.indexOf("Error")>=0){
                
              //row.createCell(3).setCellValue(status);
              //row.getCell(3).setCellStyle(backgroundStyle2);
                  
                  cell3.setCellStyle(cellStyle2);
                  //
            }else{
                cell3.setCellStyle(cellStyle3);
            }
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(web_name + " - " + keyword);
            cell4.setCellStyle(stylebd);
            ////row.createCell(4).setCellValue(web_name + " - " + keyword);//

            //
 
            fis.close();
            FileOutputStream output_file =new FileOutputStream(new File(fullPath));
            getContext().setVariable("check_path", "fullPath: " + fullPath);
            getContext().setVariable("stus", "fullPath: " + status);
            //write changes
            workbook.write(output_file);
            output_file.close();
            getContext().setVariable("check_write", "write data error success");

        }catch(Exception e){
                    getContext().setVariable("check_write", "write data error failse. " + e.toString());
        }
    }

   public void test() {
      try {
            String web_name = getContext().getVariableAsString("web_name");
            String out_file = getContext().getVariableAsString("out_file");
            String path_file_out_put = getContext().getVariableAsString("str_output");
            String keyword = getContext().getVariableAsString("keyword");
            String url_web = getContext().getVariableAsString("url_web");
            String status = getContext().getVariableAsString("status");
            String total_href = getContext().getVariableAsString("total_href");
            String path_pj = getContext().getVariableAsString("path_error");
            String path_write = getContext().getVariableAsString("fullPathOutFile"); 
            String filname = "Status_website.xlsx";
            this.writeDataError(filname, web_name, keyword, status, total_href);

      } catch (StopRequestException ex) {
         throw ex;
      }
   }
   
   public static void main(String args[]) {
      SaveWebError script = new SaveWebError();
      ApplicationSupport robot = new ApplicationSupport();
      AutomatedRunnable runnable = robot.createAutomatedRunnable(script, "SaveWebError@" + Integer.toHexString(script.hashCode()), args, System.out, false);
      new Thread(runnable).start();
   }
}
