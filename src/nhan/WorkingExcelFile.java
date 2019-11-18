/**
 * Created on Wed Oct 30 13:39:56 ICT 2019
 * HeartCore Robo Desktop v5.0.5 (Build No. 5.0.5-20190816.1)
 **/
package nhan;
import com.tplan.robot.scripting.*;
import com.tplan.robot.*;
import java.awt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import java.io.*;
import java.nio.file.FileSystems;

public class WorkingExcelFile extends DefaultJavaTestScript  {
    
    /**
 * loadDataToArrayList()
 * @param sheetName
 * @param fileName
 * @param num_col
 * @return 
 */    
    public ArrayList<String> loadDataToArrayList(String sheetName, String fileName, int num_col){
        try{
            ArrayList<String> arr_data = new ArrayList<String>();
            //String basePath = new File(fileName).getAbsolutePath();
            String path = this.getClass().getClassLoader().getResource("").getPath();
            path = path.substring(1, path.length());
            String fullPath = URLDecoder.decode(path, "UTF-8");
            //fullPath = fullPath.replace("classes/", fileName);
            if(fullPath.indexOf("classes") >= 0){
                fullPath = fullPath.replace("classes/", fileName);
            }
            
            if(fullPath.indexOf("src") >= 0){
                fullPath = fullPath.replace("src/", fileName);
            }
            
            File excelFile = new File(fullPath);
            FileInputStream fis = new FileInputStream(excelFile);

            // we create an XSSF Workbook object for our XLSX Excel File
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            // choose sheet name
            XSSFSheet sheet = workbook.getSheet(sheetName);
            //getContext().setVariable("check_detail", "Load data storge success. sheetName: " + sheetName);
            //System.out.println("Load data storge success. sheetName: " + sheetName);
            //we iterate on rows
            Iterator<Row> rowIt = sheet.iterator();

            while(rowIt.hasNext()) {
                Row row = rowIt.next();

                //get value of first column 0
                Cell cell = row.getCell(num_col);

                //add cell value to array string (storge data)
                arr_data.add(cell.getStringCellValue());
            }
            //getContext().setVariable("check_detail", "Load data storge success.");
            System.out.println("Load data storge success.");
            fis.close();
            return arr_data;
        } catch(IOException e){
            //getContext().setVariable("check_detail", "Load data storge error. " + e.toString());
            //System.err.println("Load data storge error. " + e.toString());
            throw new RuntimeException(e);
        }
    }
    
    public void writeListHrefToFile(ArrayList<String> arr_hrefs, ArrayList<String> arr_titles, String name_file){
        try{
            String path = this.getClass().getClassLoader().getResource("").getPath();
            if(path.substring(0, 1).equals("/")){
                path = path.substring(1, path.length());
            }
            String fullPath = URLDecoder.decode(path, "UTF-8");
            //fullPath = fullPath.replace("classes/",  "src/" + name_file);
            if(fullPath.indexOf("classes") >= 0){
                fullPath = fullPath.replace("classes/",  "src/" + name_file);
            }else if(fullPath.indexOf("src") >= 0){
                fullPath = fullPath.replace("src/", "src/" + name_file);
            }
            
            //getContext().setVariable("check_detail", "fullPath: " + fullPath);
            //System.out.println("fullPath: " + fullPath);
            //String fullPath  = this.deleteContentOfSheet(name_file);

            File excelFile = new File(fullPath);
            FileInputStream fis = new FileInputStream(excelFile);

            // we create an XSSF Workbook object for our XLSX Excel File
            XSSFWorkbook workbook = new XSSFWorkbook(fis);//

            // choose sheet name
            XSSFSheet sheet = workbook.getSheet("Sheet1");
            int lastRow=sheet.getLastRowNum();
            //getContext().setVariable("check_detail", "lastRow: " + lastRow);
            //System.out.println("lastRow: " + lastRow);
            int length = arr_hrefs.size();
            //getContext().setVariable("check_detail", "length: " + length);
            //System.out.println("length: " + length);
            for(int i = 0; i < arr_hrefs.size(); i++){
                Row row = sheet.createRow(++lastRow);
                row.createCell(0).setCellValue(arr_hrefs.get(i));
                row.createCell(1).setCellValue(arr_titles.get(i));
            }
            fis.close();
            FileOutputStream output_file =new FileOutputStream(new File(fullPath));
            //write changes
            workbook.write(output_file);
            output_file.close();
            //getContext().setVariable("content", "write data storge success");
            //System.out.println("write data storge success");

        }catch(Exception e){
            //getContext().setVariable("content", "write data storge error. " + e.toString());
            //System.out.println("write data storge error. " + e.toString());
        }
    }
    
   public void test() {
   }
   

}
