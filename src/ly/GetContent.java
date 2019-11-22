/**
 * Created on Fri May 24 12:12:00 ICT 2019
 * HeartCore Robo Desktop v5.0.1 (Build No. 5.0.1-20190308.1)
 **/
package JP;
import com.tplan.robot.scripting.*;
import com.tplan.robot.*;
import java.awt.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
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
	public ArrayList<String> loadDataStorage(String excelFile, ArrayList<String> lstKey){                                
        int[] col = {0};
        ArrayList<String> lststorage = new ArrayList<>();
        try {
            for(String k : lstKey){
              String[][] tests = this.readExcelFile(excelFile, k, true, col);
              for(int i = 0 ; i < tests.length ; i++){
                  for(int j = 0 ; j < tests[i].length ; j++){
                      lststorage.add(tests[i][j]);
                  }
              }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(ExcelWritten.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lststorage;
    }
	public  String[][] readExcelFile(String fileName, String sheetName, 
            boolean isReadAll, int[] columnArray) throws Exception{
        try{
            ArrayList<String> arrs = new ArrayList<String>();
            String strPathInFile = URLDecoder.decode(fileName, "UTF-8");
            FileInputStream fileInputStream = new FileInputStream(strPathInFile);
            String [][] arrData = null;
            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();
            
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fileInputStream);
            XSSFSheet mySheet = myWorkBook.getSheet(sheetName);
            Iterator<Row> rowIterator = mySheet.iterator();
            int colDataSize = 0;
            if(isReadAll){//read all data from excel file
                colDataSize = (rowIterator.next()).getLastCellNum();
            }else{//read array column data from excel file
                colDataSize = columnArray.length;
            }            
            int rowDataSize = mySheet.getLastRowNum();
            arrData = new String[rowDataSize][colDataSize];
            
            //Assign data to array            
            int i = 0;
            while(rowIterator.hasNext()){
                //Skip first row (title row)
                Row row = rowIterator.next();
                if(row.getRowNum() > 0){                    
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int j = 0;
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        String cellValue = "";
                        if(isReadAll){
                            cellValue = dataFormatter.formatCellValue(cell);     
                            arrData[i][j] = cellValue;
                            j++;
                        }else{
                            for(int iter = 0; iter < columnArray.length; iter++){
                                if(cell.getColumnIndex() == (columnArray[iter]-1)){
                                    cellValue = dataFormatter.formatCellValue(cell); 
                                    arrData[i][j] = cellValue;
                                    j++;
                                }
                            }                            
                        }
                    }
                    i++;
                }
            }
            myWorkBook.close();
            fileInputStream.close();
            return arrData;
        }catch(Exception ex){
            throw ex;
        }
    }
	public ArrayList<String> loadDataFromExcelFile(String excelFile, String sheetName, boolean bool, int[] columns , int colData){
        
        ArrayList<String> lstData = new ArrayList<>();
        try {
            String[][] arrDatas = this.readExcelFile(excelFile, sheetName, bool, columns);
            for(int i = 0 ; i < arrDatas.length ; i++){
                lstData.add(arrDatas[i][colData].toString());
            }
        } catch (Exception ex) {
            Logger.getLogger(ExcelWritten.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lstData;
    }
	public ArrayList<String> Urlsearch(String fileDatastorage, ArrayList<String> urls , ArrayList<String> keys, ArrayList<String> arrele){
        
        ArrayList<String> arrUrlget= new ArrayList<>();
        for(String ele : arrele){
            for(String url : urls){
                ArrayList<String> arrUrlsearch = new ArrayList<>();
                for(String key : keys){
                    if(url.indexOf("gigazine") < 0){  
                        try {
                                Document doc = Jsoup.connect(url.replace("(keyword)", key)).get();
                                Elements arrhrefs = doc.select(ele);
                                for (Element arrhref : arrhrefs) {
                                    arrUrlsearch.add(arrhref.attr("href")); 
                                }
                            
                            } catch (IOException ex) {
                                Logger.getLogger(ExcelWritten.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                       }
                }
                ArrayList<String> dts = loadDataStorage(fileDatastorage, keys);
                arrUrlget = getItemNotExist(arrUrlsearch, dts);
            }
        }

        return arrUrlget;
    }
	public ArrayList<String> typeFillter(String exFile, int coltype){
        int[] col = {0};
        ArrayList<String> urlFilters = new ArrayList<>();
        try {
            String[][] tests = this.readExcelFile(exFile, "website", true, col);
            for(int i = 0 ; i < tests.length ; i++){
                    if((tests[i][3].toString()).equals("1") == true){
                       urlFilters.add(tests[i][coltype]); 
                    }
            }
        } catch (Exception ex) {
            Logger.getLogger(ExcelWritten.class.getName()).log(Level.SEVERE, null, ex);
        }
        return urlFilters;   
    }
	public  ArrayList<String> getItemNotExist(ArrayList<String> arrChild, ArrayList<String> arrFather){
        ArrayList<String> myArray = new ArrayList<>();
        int childLength = arrChild.size();
        for(int i = 0 ; i < childLength; i++){
            if(!arrFather.contains(arrChild.get(i))){
                //Write to new array
                myArray.add(arrChild.get(i).trim());
                //Write to end Father array
                arrFather.add(arrChild.get(i).trim());
            }
        }
        return myArray;
    }
  
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
        String class_title = getContext().getVariableAsString("class_title");
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

            if(web_name.indexOf("itmedia") >= 0){
                title = doc_charset.select("#cmsTitle > div > h1 > big").text();
            }

            String content = "";
            for(Element ele : contents){
                if(content.length() == 0){
                    content = content + ele.text();
                }else{
                    content = content + "\n\r" + ele.text();
                }
            }

            if(web_name.indexOf("nikkan") >= 0 && content == ""){
                Elements contents2 = doc_charset.select("#changeArea > div.txt > div > p");
                for(Element ele : contents2){
                    if(content.length() == 0){
                        content = content + ele.text();
                    }else{
                        content = content + "\n\r" + ele.text();
                    }
                }
            }
            
            if(web_name.indexOf("tech") >= 0 && content == ""){
                Elements contents2 = doc_charset.select("#mainContent > div > p");
                for(Element ele : contents2){
                    if(content.length() == 0){
                        content = content + ele.text();
                    }else{
                        content = content + "\n\r" + ele.text();
                    }
                }
            }
            if(web_name.indexOf("tech") >= 0 && post_date == ""){
                post_date = doc_charset.select(" #mainContent > div > p.pubdate").text();
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
        
        String No = getContext().getVariableAsString("no_num");
        String url_htt="";
        if(url_item.indexOf("http://") >= 0){
            url_htt=url_item.replace("http://", "");

        }else if(url_item.indexOf("https://") >= 0){
            url_htt=url_item.replace("https://", "");
        }
        String linkurl = "https://vp-web-crawl"+".s3-ap-northeast1.amazonaws.com/"+fridayString+"HTTrack/"+url_htt+"index.html";
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
		if(url_htt.length() <=0){
            row.createCell(9).setCellValue(url_htt);
        }else{
            row.createCell(9).setCellValue(linkurl);
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
                fullPath = fullPath.replace("classes/",  fileName);
            }
            if(fullPath.indexOf("src") >= 0){
                fullPath = fullPath.replace("src/",  fileName);
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
            getContext().setVariable("check_detail", "Load data storge error. " + e.toString());
            throw new RuntimeException(e);
        }
    }
    
    public void writeDataToStorge(String sheetName, String fileName, ArrayList<String> arr_data){
        try{
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            //fullPath = fullPath.replace("classes/", fileName);
            if(fullPath.indexOf("classes") >= 0){
                fullPath = fullPath.replace("classes/",  fileName);
            }
            if(fullPath.indexOf("src") >= 0){
                fullPath = fullPath.replace("src/",  fileName);
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
