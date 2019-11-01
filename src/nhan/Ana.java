/**
 * Created on Wed Oct 30 13:33:27 ICT 2019
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
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class Ana extends DefaultJavaTestScript  {
    //Global variables
    private ArrayList<String> arr_hrefs    = new ArrayList<String>();
    private ArrayList<String> arr_title    = new ArrayList<String>();
    
    public void test() {
        try {
            getHref();
        } catch (StopRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }
    
    public void writeFile(String fileName, String link){
        try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(fileName, true);//set for append mode
            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //Write data.              
            bufferedWriter.write(link); bufferedWriter.newLine();
            
            // Always close files.
            bufferedWriter.close();
        }
        catch(Exception ex) {
            getContext().setVariable("err_txtWriteFile",  ex.toString());
        }
    } 
      
    public void setVariableHref(MyHref myHref){
        myHref.setStr_url(getContext().getVariableAsString("url_for_gethref"));        
        myHref.setWeb_name(getContext().getVariableAsString("web_name"));
        //myHref.setOut_file(getContext().getVariableAsString("out_file"));
        //myHref.setPath_file_out_put(getContext().getVariableAsString("str_output"));
        
        myHref.setClass_href(getContext().getVariableAsString("class_href"));
        myHref.setClass_title(getContext().getVariableAsString("class_title"));
        myHref.setClass_date_post(getContext().getVariableAsString("class_postdate"));
        
        myHref.setClass_source(getContext().getVariableAsString("class_source"));
        myHref.setClass_content(getContext().getVariableAsString("class_content"));

       
        myHref.setClass_next_page(getContext().getVariableAsString("class_next_page"));        
        myHref.setDomain(getContext().getVariableAsString("domain"));        
        myHref.setReplace_nxt_p(getContext().getVariableAsString("replace_nxt_p"));        
        myHref.setCase_display(getContext().getVariableAsString("case_display"));        
        myHref.setClass_tr_dl(getContext().getVariableAsString("class_tr_dl"));        
        
        myHref.setKeyword(getContext().getVariableAsString("keyword_name"));   
//writeFile("D:\\log.txt", "URL: " + getContext().getVariableAsString("URL"));        
//writeFile("D:\\log.txt", "url_for_gethref: " + getContext().getVariableAsString("url_for_gethref"));
    }
    
    public void getLinkItem(String url_item, String class_href, String domain, String class_title, ArrayList<String> href_storge){        
        try {
            URL u;
            Scanner s;
            String content = "";
            u = new URL(url_item);
            Document doc = Jsoup.connect(url_item).get();
            String charset = doc.charset().toString();
            //getContext().setVariable("charset", charset);
            //getContext().setVariable("check_detail", "url_item: " + url_item);
            String arr_class_href[] = class_href.split(",");
            String arr_class_title[] = class_title.split(",");
           
            for(int cls = 0; cls < arr_class_href.length; cls++) {       
                Elements hrefs = doc.select(arr_class_href[cls]);
                Elements titles = doc.select(arr_class_title[cls]);
                if(titles.size() < hrefs.size()){
                    titles = doc.select(arr_class_href[cls]);
                }
                
                for(int i = 0; i <  hrefs.size(); i++){
                String str_url_tmp = "";
                String title_tmp = titles.get(i).text();
                if(hrefs.get(i).attr("href").indexOf(domain) < 0){
                    str_url_tmp = domain + hrefs.get(i).attr("href");                        
                }else{
                    str_url_tmp = hrefs.get(i).attr("href");                        
                }
                if(str_url_tmp.indexOf("/msg/?ty") > 0){
                    str_url_tmp = str_url_tmp.substring(0, str_url_tmp.indexOf("/msg/?ty"));
                }else if(str_url_tmp.indexOf("/?ty") > 0){
                    str_url_tmp = str_url_tmp.substring(0, str_url_tmp.indexOf("/?ty"));
                }else if(str_url_tmp.indexOf("_message/") > 0){
                    str_url_tmp = str_url_tmp.replace("_message/", "_detail/");
                }else if(str_url_tmp.indexOf("/-tab__pr/") > 0){
                    str_url_tmp = str_url_tmp.replace("/-tab__pr/", "/-tab__jd/");
                }else if(str_url_tmp.indexOf("nx1_") > 0){
                    str_url_tmp = str_url_tmp.replace("nx1_", "nx2_");
                    str_url_tmp = str_url_tmp.replace("&list_disp_no=1", "");
                    str_url_tmp = str_url_tmp.replace("n_ichiran_cst_n5_ttl", "ngen_tab-top_info");
                }else{
                    str_url_tmp = str_url_tmp.replace(",", "");
                }

                if(!href_storge.contains(str_url_tmp) && !this.arr_hrefs.contains(str_url_tmp) && (str_url_tmp.indexOf("/tag/") < 0)){
                    //getContext().setVariable("check_detail", "this.arr_hrefs: " + this.arr_hrefs.size());                    
                    this.arr_hrefs.add(str_url_tmp);
                    this.arr_title.add(title_tmp);
                }
                //getContext().setVariable("check_detail", "titles.get(3).text(): " + titles.size());                
                }
            }
        } 
        catch (StopRequestException ex) {
            //getContext().setVariable("check_detail", "Error StopRequestException: " + ex.toString());            
            throw ex;
        } 
        catch(IOException e){
            //getContext().setVariable("check_detail", "Error IOException getLinkItem: " + url_item);
            //System.out.println("Error IOException getLinkItem: " + url_item);
            throw new RuntimeException(e);
        }
    }
    
    public String copyFile(String name_file){
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            //fullPath = fullPath.replace("classes/", name_file);
            if(fullPath.indexOf("classes") >= 0){
                fullPath = fullPath.replace("classes/", name_file);
            }else if(fullPath.indexOf("src") >= 0){
                fullPath = fullPath.replace("src/", name_file);
            }

            FileInputStream fis = new FileInputStream(fullPath);

            String fullPathHref = fullPath.replace(name_file, "src/" + name_file);

            OutputStream fout = new FileOutputStream(fullPathHref);
            //getContext().setVariable("content", fullPathHref);            

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fout.write(buffer, 0, length);
            }

            fis.close();
            fout.close();
            return fullPathHref;
        }catch(IOException e){
            //getContext().setVariable("content", "copy file excel error. " + e.toString());            
            return "";
        }
    }
    
    public void deleteFile(String name_file){
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            //fullPath = fullPath.replace("classes/", name_file);
            if(fullPath.indexOf("classes") >= 0){
                fullPath = fullPath.replace("classes/", "src/"+name_file);
            }else if(fullPath.indexOf("src") >= 0){
                fullPath = fullPath.replace("src/", "src/"+name_file);
            }

            String fullPathHref = fullPath;

            File file = new File(fullPathHref);
            if(file.exists()){
                boolean success = file.delete();
            }
        }catch(IOException e){
            //getContext().setVariable("content", "copy file excel error. " + e.toString());
            //System.out.println("copy file excel error. " + e.toString());
        }
    }
    
    public void getHref(){
        try{
            MyHref myHref = new MyHref();
            setVariableHref(myHref);
            WorkingExcelFile myExcel = new WorkingExcelFile();
            Utilities myUtil = new Utilities();
            int p=0;            
            String href_file_name = "href.xlsx";
            this.deleteFile(href_file_name);

            String fileNameDS = "data_storge/" + myHref.getWeb_name().toLowerCase() + ".xlsx";

            String sheetName = myHref.getKeyword();            
            ArrayList<String> href_storge = myExcel.loadDataToArrayList(sheetName, fileNameDS, 0);
            int ipage = 1;
                        
            this.arr_hrefs = new ArrayList<String>();
            String url_item = myHref.getStr_url();

            String ele_page = "#left_col > div > ul > li";            
            try{
                Document docpage = Jsoup.connect(url_item).get();
                Elements page = docpage.select(ele_page);
                if(page.size() > 0){
                    p=page.size();
                }
            } catch(IOException e){
                //getContext().setVariable("numpage", "Error IOException: " + e.toString());
                throw new RuntimeException(e);
            }
            
            getContext().setVariable("numpage", p); 
            
            if(myHref.getClass_next_page().indexOf("null") < 0 ){            
                while( ipage < 3 && (ipage == 1 ||  p > 1)){
                    String url_page = "";
                    if(ipage < 2){
                        url_page = url_item;
                    }else{
                        if((myHref.getWeb_name().indexOf("ainow") >= 0) || 
                            (myHref.getWeb_name().indexOf("moguravr") >= 0) || 
                            (myHref.getWeb_name().indexOf("wirelesswire") >= 0) || 
                            (myHref.getWeb_name().indexOf("techable") >= 0)){

                            String str_rp = myHref.getReplace_nxt_p() + myHref.getKeyword();
                            url_page = url_item.replace(str_rp, "") + myHref.getClass_next_page() + ipage +  "/" + str_rp;                            
                        }else if(myHref.getWeb_name().indexOf("newsmynavi") >= 0){
                            url_page = url_item.replace(myHref.getReplace_nxt_p(), "") + myHref.getClass_next_page() + ipage +  "&" + myHref.getReplace_nxt_p();
                        }else if(myHref.getWeb_name().indexOf("gigazine") >= 0){
                            int offset = Integer.parseInt(myHref.getCase_display());
                            url_page = url_item.replace(myHref.getReplace_nxt_p(), "") + myHref.getClass_next_page() + (ipage-1)*offset;
                        }else if(myHref.getWeb_name().indexOf("robotstart") >= 0){
                            url_page = url_item.replace(myHref.getReplace_nxt_p() + myHref.getKeyword(), "") + myHref.getClass_next_page() + ipage + "&s=" + myHref.getKeyword();
                        }else{
                            url_page = url_item.replace(myHref.getReplace_nxt_p(), "") + myHref.getClass_next_page() + ipage;
                        }
                    }

                    
                    getLinkItem(url_page, myHref.getClass_href(), myHref.getDomain(), myHref.getClass_title(), href_storge);
                    ipage++;                    
                }
            }else{
                String url_page = url_item;
                this.getLinkItem(url_page, myHref.getClass_href(), myHref.getDomain(), myHref.getClass_title(), href_storge);
                //getContext().setVariable("check_detail", " Website only one page");                
            }            
            this.copyFile(href_file_name);

            //get top 20        
            this.arr_hrefs = myUtil.getTop20(arr_hrefs);
            this.arr_title = myUtil.getTop20(arr_title);
            myExcel.writeListHrefToFile(arr_hrefs, arr_title, href_file_name);

            //getContext().setVariable("check_detail", "total_page count: " +  this.arr_hrefs.size());
            //getContext().setVariable("total_href", this.arr_hrefs.size());         
        } catch (Exception ex){ //(StopRequestException ex) {
            //getContext().setVariable("check_detail", "Get list url item error: " + ex.toString());            
            throw ex;
        } 

    }
   
   public static void main(String args[]) {
      Ana script = new Ana();
      ApplicationSupport robot = new ApplicationSupport();
      AutomatedRunnable runnable = robot.createAutomatedRunnable(script, "Ana@" + Integer.toHexString(script.hashCode()), args, System.out, false);
      new Thread(runnable).start();
   }
}
