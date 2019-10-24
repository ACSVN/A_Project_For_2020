/**
 * Created on Fri May 24 12:12:00 ICT 2019
 * HeartCore Robo Desktop v5.0.1 (Build No. 5.0.1-20190308.1)
 **/
import com.tplan.robot.scripting.*;
import com.tplan.robot.*;
import java.awt.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

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

public class GetHref extends DefaultJavaTestScript  {

    public Map<String, String> map_item = new HashMap<String, String>();
    public ArrayList<String> arr_hrefs = new ArrayList<String>();
    public ArrayList<String> arr_name_com = new ArrayList<String>();
    public ArrayList<String> arr_title = new ArrayList<String>();

    public void test() {
        try {
            String out_file = getContext().getVariableAsString("out_file");
            String path_file_out_put = getContext().getVariableAsString("str_output");

            String web_name = getContext().getVariableAsString("web_name");
            String keyword_name = getContext().getVariableAsString("keyword_name");
            String str_page_num = getContent().getVariableAsString("page_num");
            int page_num = Integer.parseInt(str_page_num)

            String str_url = getContext().getVariableAsString("URL");
            String class_href = getContext().getVariableAsString("class_href");
            String class_title = getContext().getVariableAsString("class_title");
            String class_date_post = getContext().getVariableAsString("class_postdate");
            String class_source = getContext().getVariableAsString("class_source");
            String class_content = getContext().getVariableAsString("class_content");
            String class_next_page = getContext().getVariableAsString("class_next_page");

            String domain = getContext().getVariableAsString("domain");
            String replace_nxt_p = getContext().getVariableAsString("replace_nxt_p");
            String case_display = getContext().getVariableAsString("case_display");
            String class_tr_dl = getContext().getVariableAsString("class_tr_dl");


            int p = 0;

            String href_file_name = "href.xlsx";
            this.deleteFile(href_file_name);

            String fileNameDS = "data_storage/" + web_name.toLowerCase() + ".xlsx";
            String sheetName = keyword_name;
            ArrayList<String> href_storage = this.loadDataToArrayList(sheetName, fileNameDS, 0);

            int ipage = 1;

            this.arr_hrefs = new ArrayList<String>();

            try {
                Document docpage = Jsoup.connect(str_url).get();
                Elements page = docpage.select("#left_col > div > ul > li");
                if (page.size() > 0) {
                    p = page.size();
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }

            if (URL.contains("(pagenum)")) {
                this.getLinkItem(url_page, class_href, domain, class_title, href_storage);
            } else {
                for (i = 1; i <= pagenum; i++) {
                    URL.replace("(pagenum)", i)
                    this.getLinkItem(url_page, class_href, domain, class_title, href_storage);
                }
            }

/* (comment out on 10/24 by nose)

            if (class_next_page.equals("null")) {
                while (ipage < 3 && (ipage == 1 || p > 1)) {
                    String url_page = "";
                    if (ipage < 2) {
                        url_page = url_item;
                    } else {
                        if (web_name.equals("ainow") || web_name.equals("moguravr") || web_name.equals("wirelesswire") || web_name.equals("techable")) {
                            String str_rp = replace_nxt_p + keyword_name;
                            url_page = url_item.replace(str_rp, "") + class_next_page + ipage +  "/" + str_rp;
                        } else if (web_name.equals("newsmynavi")) {
                            url_page = url_item.replace(replace_nxt_p, "") + class_next_page + ipage +  "&" + replace_nxt_p;
                        } else if (web_name.equals("gigazine")) {
                            int offset = Integer.parseInt(case_display);
                            url_page = url_item.replace(replace_nxt_p, "") + class_next_page + (ipage-1)*offset;
                        } else if (web_name.equals("robotstart")) {
                            url_page = url_item.replace(replace_nxt_p + keyword_name, "") + class_next_page + ipage + "&s="+keyword_name;
                        } else {
                            url_page = url_item.replace(replace_nxt_p, "") + class_next_page + ipage;
                        }
                    }
                    this.getLinkItem(url_page, class_href, domain, class_title, href_storage);
                    ipage++;
                }
            } else {
                String url_page = url_item;
                this.getLinkItem(url_page, class_href, domain, class_title, href_storage);
            }
            this.copyFile(href_file_name);
*/

            //get top 20
            this.arr_hrefs = this.GetTop20(this.arr_hrefs);
            this.arr_title = this.GetTop20(this.arr_title);

            this.writeListHrefToFile(arr_hrefs, arr_title, href_file_name);
            getContext().setVariable("total_href", this.arr_hrefs.size());
        } catch (StopRequestException ex) {
            throw ex;
        }
    }




    public ArrayList<String> GetTop20(ArrayList<String> arr_str) {
        ArrayList<String> arr_top20 = new ArrayList<String>();
        int loop = 5;

        for (int i = 0; (i < arr_str.size()) && (i < loop); i++) {
            arr_top20.add(arr_str.get(i));
        }
        int count = arr_top20.size();
        while (count < 20) {
            arr_top20.add("null");
            count++;
        }
        return arr_top20;
    }




    public void getLinkItem(String url_item, String class_href, String domain, String class_title, ArrayList<String> href_storage) {
        try {
            URL u;
            Scanner s;
            String content = "";
            u = new URL(url_item);

            Document doc = Jsoup.connect(url_item).get();
            String charset = doc.charset().toString();

            String arr_class_href[] = class_href.split(",");
            String arr_class_title[] = class_title.split(",");

            for (int cls = 0; cls < arr_class_href.length; cls++) {
                Elements hrefs = doc.select(arr_class_href[cls]);
                Elements titles = doc.select(arr_class_title[cls]);

                if (titles.size() < hrefs.size()) {
                    titles = doc.select(arr_class_href[cls]);
                }

                for (int i = 0; i < hrefs.size(); i++) {
                    String str_url_tmp = "";
                    String title_tmp = titles.get(i).text();

                    if (hrefs.get(i).attr("href").indexOf(domain) < 0) {
                        str_url_tmp = domain + hrefs.get(i).attr("href");
                    } else {
                        str_url_tmp = hrefs.get(i).attr("href");
                    }

                    if (str_url_tmp.indexOf("/msg/?ty") > 0) {
                        str_url_tmp = str_url_tmp.substring(0, str_url_tmp.indexOf("/msg/?ty"));
                    } else if (str_url_tmp.indexOf("/?ty") > 0) {
                        str_url_tmp = str_url_tmp.substring(0, str_url_tmp.indexOf("/?ty"));
                    } else if (str_url_tmp.indexOf("_message/") > 0) {
                        str_url_tmp = str_url_tmp.replace("_message/", "_detail/");
                    } else if (str_url_tmp.indexOf("/-tab__pr/") > 0) {
                        str_url_tmp = str_url_tmp.replace("/-tab__pr/", "/-tab__jd/");
                    } else if (str_url_tmp.indexOf("nx1_") > 0) {
                        str_url_tmp = str_url_tmp.replace("nx1_", "nx2_");
                        str_url_tmp = str_url_tmp.replace("&list_disp_no=1", "");
                        str_url_tmp = str_url_tmp.replace("n_ichiran_cst_n5_ttl", "ngen_tab-top_info");
                    } else {
                        str_url_tmp = str_url_tmp.replace(",", "");
                    }

                    if (!href_storage.contains(str_url_tmp) && !this.arr_hrefs.contains(str_url_tmp) && (str_url_tmp.indexOf("/tag/") < 0)) {
                        this.arr_hrefs.add(str_url_tmp);
                        this.arr_title.add(title_tmp);
                    }
                }
            }
        } catch (StopRequestException ex) {
            throw ex;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }




    public int getCountPage(String url_item, String class_total, String replace_nxt_p, String case_display) {
        int total_q = 0;
        int dis_case = 0;

        if (this.isNullOrEmpty(replace_nxt_p)) {
            dis_case = 0;
        } else {
            dis_case = Integer.parseInt(case_display);
        }

        try {
            URL u;
            Scanner s;
            String content = "";

            u = new URL(url_item);
            Document doc = Jsoup.connect(url_item).get();
            String charset = doc.charset().toString();

            String num_total = doc.select(class_total).first().text();

            if (!this.isNullOrEmpty(replace_nxt_p)) {
                num_total = num_total.replace(replace_nxt_p, "");
                num_total = num_total.replace("(", "");
                num_total = num_total.replace(")", "");
                num_total = num_total.replace(" 1 / ", "");
                num_total = num_total.replace(",", "");
            }

            total_q = Integer.parseInt(num_total);

            if (this.isNullOrEmpty(replace_nxt_p)) {
                return total_q;
            } else {
                if (total_q % dis_case == 0) {
                    return (total_q/dis_case);
                } else {
                    return ((total_q/dis_case) + 1);
                }
            }
        } catch (StopRequestException ex) {
            throw ex;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    public ArrayList<String> loadDataToArrayList(String sheetName, String fileName, int num_col) {
        try{
            ArrayList<String> arr_data = new ArrayList<String>();
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");

            if (fullPath.indexOf("classes") >= 0) {
                fullPath = fullPath.replace("classes/", fileName);
            }
            if (fullPath.indexOf("src") >= 0) {
                fullPath = fullPath.replace("src/", fileName);
            }

            File excelFile = new File(fullPath);
            FileInputStream fis = new FileInputStream(excelFile);

            // we create an XSSF Workbook object for our XLSX Excel File
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            // choose sheet name
            XSSFSheet sheet = workbook.getSheet(sheetName);

            //we iterate on rows
            Iterator<Row> rowIt = sheet.iterator();

            while (rowIt.hasNext()) {
                Row row = rowIt.next();

                //get value of first column 0
                Cell cell = row.getCell(num_col);

                //add cell value to array string (storge data)
                arr_data.add(cell.getStringCellValue());
            }
            fis.close();
            return arr_data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    public void writeListHrefToFile(ArrayList<String> arr_hrefs, ArrayList<String> arr_titles, String name_file) {
        try{
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");

            if (fullPath.indexOf("classes") >= 0) {
                fullPath = fullPath.replace("classes/",  "src/" + name_file);
            } else if(fullPath.indexOf("src") >= 0) {
                fullPath = fullPath.replace("src/", "src/" + name_file);
            }

            File excelFile = new File(fullPath);
            FileInputStream fis = new FileInputStream(excelFile);

            // we create an XSSF Workbook object for our XLSX Excel File
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            // choose sheet name
            XSSFSheet sheet = workbook.getSheet("Sheet1");

            int lastRow=sheet.getLastRowNum();
            int length = arr_hrefs.size();

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
            getContext().setVariable("content", "write data storge success");

        } catch (Exception e) {
            getContext().setVariable("content", "write data storge error. " + e.toString());
        }
    }




    public String copyFile(String name_file) {
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");

            if (fullPath.indexOf("classes") >= 0) {
                fullPath = fullPath.replace("classes/", name_file);
            } else if (fullPath.indexOf("src") >= 0) {
                fullPath = fullPath.replace("src/", name_file);
            }

            FileInputStream fis = new FileInputStream(fullPath);

            String fullPathHref = fullPath.replace(name_file, "src/" + name_file);

            OutputStream fout = new FileOutputStream(fullPathHref);
            getContext().setVariable("content", fullPathHref);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fout.write(buffer, 0, length);
            }

            fis.close();
            fout.close();
            return fullPathHref;
        } catch (IOException e) {
            getContext().setVariable("content", "copy file excel error. " + e.toString());
            return "";
        }
    }




    public void deleteFile(String name_file) {
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");

            if (fullPath.indexOf("classes") >= 0) {
                fullPath = fullPath.replace("classes/", "src/" + name_file);
            } else if (fullPath.indexOf("src") >= 0) {
                fullPath = fullPath.replace("src/", "src/" + name_file);
            }

            File file = new File(fullPath);
            if (file.exists()) {
                boolean success = file.delete();
            }
        } catch (IOException e) {
            getContext().setVariable("content", "copy file excel error. " + e.toString());
        }
    }




    public boolean isNullOrEmpty(String str) {
        if (str == null || str.isEmpty() || str == "" || str.equals("null"))
            return true;
        return false;
    }




    public ArrayList<String> mergeArrayString(ArrayList<String> arr_item, ArrayList<String> arr_dest) {
        for (String item : arr_item) {
            arr_dest.add(item);
        }
        return arr_dest;
    }




    public static void main(String args[]) {
        GetHref script = new GetHref();
        ApplicationSupport robot = new ApplicationSupport();
        AutomatedRunnable runnable = robot.createAutomatedRunnable(script, "GetHref@" + Integer.toHexString(script.hashCode()), args, System.out, false);
        new Thread(runnable).start();
    }
}
