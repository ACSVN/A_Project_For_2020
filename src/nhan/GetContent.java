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
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Header; 
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetContent extends DefaultJavaTestScript  {
    
  public ArrayList<String> arr_hrefs = new ArrayList<String>();
  public ArrayList<String> arr_names = new ArrayList<String>();
  public ArrayList<String> arr_titles = new ArrayList<String>();
  public String fileNameDS = "";
  public String sheetName ="";
  
  public void test() {
    try {
        String web_name = getContext().getVariableAsString("web_name");
        String out_file = getContext().getVariableAsString("out_file");
        String path_file_out_put = getContext().getVariableAsString("str_output");
        String class_date_post = getContext().getVariableAsString("class_date_post");
        String class_source = getContext().getVariableAsString("class_source");
        String class_content = getContext().getVariableAsString("class_content");
        String class_tr_dl = getContext().getVariableAsString("class_tr_dl");
        String keyword = getContext().getVariableAsString("keyword");

        String url_web = getContext().getVariableAsString("url_web");
        String path_write = getContext().getVariableAsString("fullPathOutFile"); 
        String str_url = getContext().getVariableAsString("str_url_item_tmp");
        String str_title_item = getContext().getVariableAsString("str_title_item");

        this.fileNameDS = "data_storge/" + web_name.toLowerCase() + ".xlsx";
        this.sheetName = keyword;
        ArrayList<String> href_storge = this.loadDataToArrayList(this.sheetName, this.fileNameDS, 0);
      
        if(str_url.indexOf("null") < 0){
            if(!href_storge.contains(str_url)){
                ArrayList<String> arr_href = new ArrayList<String>();
                arr_href.add(str_url);
                this.getItemWork(str_url, str_title_item);

                this.writeDataToStorge(sheetName, fileNameDS, arr_href);
                getContext().setVariable("check_detail", "write to storge");
            }
        }else{
            this.writeDataToFile(path_write, "Sheet1", web_name, url_web, keyword, "", "", "", "", "");
        }
      
    } catch (StopRequestException ex) {
        throw ex;
    }
  }
  
  public void getItemWork(String str_url, String title){
    String web_name = getContext().getVariableAsString("web_name");
    String out_file = getContext().getVariableAsString("out_file");
    String path_file_out_put = getContext().getVariableAsString("str_output");
    String class_date_post = getContext().getVariableAsString("class_date_post");
    String class_source = getContext().getVariableAsString("class_source");
    String class_content = getContext().getVariableAsString("class_content");
    String class_tr_dl = getContext().getVariableAsString("class_tr_dl");
    String keyword = getContext().getVariableAsString("keyword");
    
    String url_web = getContext().getVariableAsString("url_web");
    String path_write = getContext().getVariableAsString("fullPathOutFile"); 
    
    try {
        URL u;
        Scanner s;
        //String content = "";
        u = new URL(str_url);

        Document doc_charset = Jsoup.connect(str_url).get();
        String charset = doc_charset.charset().toString();

        String post_date = doc_charset.select(class_date_post).text();
        String source = doc_charset.select(class_source).text();
        Elements contents = doc_charset.select(class_content);
        
        String content = "";
        for(Element ele : contents){
            if(content.length() == 0){
                content = content + ele.text();
            }else{
                content = content + "\n\r" + ele.text();
            }
        }

        String sheeName = "Sheet1";
        this.writeDataToFile(path_write, sheeName, web_name, url_web, keyword, str_url, title, post_date, source, content);
        getContext().setVariable("check_detail", "writeDataToFile completed!!!!!!");

    } catch (StopRequestException ex) {
        getContext().setVariable("check_detail", "error: " + str_url);
        throw ex;
    } catch(IOException e){
        getContext().setVariable("check_detail", "error: " + str_url);
        throw new RuntimeException(e);
    }
  }
  
  public void writeDataToFile(String path_out_file, String sheeName, String web_name, String url_web, String keyword, String url_item, String title, String date_posts, String sources, String contents){
    try{
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        
        String str = formatter.format(date).toString();
  
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(str, formatter1);
        
        String fridayString = dateTime.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).toString();
        fridayString = fridayString.replace("-", "");
        String url_htt = "";
        if(url_item.indexOf("https://") >= 0){
            url_htt = url_item.replace("https://", "");
        }else if(url_item.indexOf("http://") >= 0){
            url_htt = url_item.replace("http://", "");
        }

        String linkURL="https://s3.console.aws.amazon.com/s3/buckets/avatar-rpa-products/"+ fridayString + "HTTrack/"+url_htt+"index.html";
        String No = getContext().getVariableAsString("no_num");
        File excelFile = new File(path_out_file);
        FileInputStream fis = new FileInputStream(excelFile);

        // we create an XSSF Workbook object for our XLSX Excel File
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        // choose sheet name
        XSSFSheet sheet = workbook.getSheetAt(0);

        int lastRow = sheet.getLastRowNum();
        Row row = sheet.createRow(++lastRow);
        row.createCell(0).setCellValue(No);
        row.createCell(1).setCellValue(web_name);
        row.createCell(2).setCellValue(url_web);
        row.createCell(3).setCellValue(keyword);
        row.createCell(4).setCellValue(url_item);
        row.createCell(5).setCellValue(title);
        row.createCell(6).setCellValue(date_posts);
        row.createCell(7).setCellValue(sources);
        row.createCell(8).setCellValue(contents);
        if(url_item.length() <= 0){
            row.createCell(9).setCellValue(url_item);
        }else{
            row.createCell(9).setCellValue(linkURL);
        }
        
        fis.close();
        FileOutputStream output_file =new FileOutputStream(new File(path_out_file));
        //write changes
        workbook.write(output_file);
        output_file.close();
            
        getContext().setVariable("check_detail", "writeDataToFile success!!!!!!");
    }catch(Exception e){
      getContext().setVariable("check_detail", "write data content to excel file error. " + e.toString());
    }
  }
  
  public ArrayList<String> loadDataToArrayList(String sheetName, String fileName, int num_col){
      try{
        ArrayList<String> arr_data = new ArrayList<String>();

        //String basePath = new File(fileName).getAbsolutePath();
        String path = this.getClass().getClassLoader().getResource("").getPath();
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
        getContext().setVariable("check_detail", "Load data storge success. sheetName: " + sheetName);
        //we iterate on rows
        Iterator<Row> rowIt = sheet.iterator();

        while(rowIt.hasNext()) {
            Row row = rowIt.next();

            //get value of first column 0
            Cell cell = row.getCell(num_col);

            //add cell value to array string (storge data)
            arr_data.add(cell.getStringCellValue());
        }
        getContext().setVariable("check_detail", "Load data storge success.");
        fis.close();
        return arr_data;
    } catch(IOException e){
        getContext().setVariable("check_detail", "Load data storge error. " + e.toString() + ", file name: " + fileName);
        throw new RuntimeException(e);
    }
  }
    
  public void writeDataToStorge(String sheetName, String fileName, ArrayList<String> arr_data){
      try{
        String path = this.getClass().getClassLoader().getResource("").getPath();
        String fullPath = URLDecoder.decode(path, "UTF-8");
        //fullPath = fullPath.replace("classes/", fileName);
        //fullPath = fullPath.replace("src/", fileName);
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
        int lastRow=sheet.getLastRowNum();

        int length = arr_data.size();
        for(String href : arr_data){
            Row row = sheet.createRow(++lastRow);
            row.createCell(0).setCellValue(href);
        }
        fis.close();
        FileOutputStream output_file =new FileOutputStream(new File(fullPath));
        //write changes
        workbook.write(output_file);
        output_file.close();
        getContext().setVariable("check_detail", "write data storge success");
       
    }catch(Exception e){
        getContext().setVariable("check_detail", "write data storge error. " + e.toString());
    }
  }
   
  public static void main(String args[]) {
    GetContent script = new GetContent();
    ApplicationSupport robot = new ApplicationSupport();
    AutomatedRunnable runnable = robot.createAutomatedRunnable(script, "GetContent@" + Integer.toHexString(script.hashCode()), args, System.out, false);
    new Thread(runnable).start();
  }
}
