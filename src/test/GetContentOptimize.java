/**
 * Created on Thu Nov 21 15:44:02 ICT 2019
 * HeartCore Robo Desktop v5.0.1 (Build No. 5.0.1-20190308.1)
 **/
package test;
import com.tplan.robot.scripting.*;
import com.tplan.robot.*;
import java.awt.*;
import java.io.*;

public class GetContentOptimize extends DefaultJavaTestScript  {
	public String[][] readExcelFile(String fileName, String sheetName,  boolean isReadAll, int[] columnArray) {
        String [][] arrData = null;
        try {
            
            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();
            
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fileName);
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
                //Skip row (title row)                                   
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
            //return arrData;
        }catch(IOException  e){
            e.printStackTrace();
        }
        return arrData;

    }

   public void test() {
      try {
          report(new File("results.xml"), "");

      } catch (StopRequestException ex) {
         throw ex;
      } catch (IOException ex) {
         ex.printStackTrace();
        throw new IllegalStateException(ex);
      }
   }
   
   public static void main(String args[]) {
      GetContentOptimize script = new GetContentOptimize();
      ApplicationSupport robot = new ApplicationSupport();
      AutomatedRunnable runnable = robot.createAutomatedRunnable(script, "GetContentOptimize@" + Integer.toHexString(script.hashCode()), args, System.out, false);
      new Thread(runnable).start();
   }
}
