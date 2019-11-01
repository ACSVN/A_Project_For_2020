/**
 * Created on Wed Oct 30 13:38:18 ICT 2019
 * HeartCore Robo Desktop v5.0.5 (Build No. 5.0.5-20190816.1)
 **/
package nhan;
import com.tplan.robot.scripting.*;
import com.tplan.robot.*;
import java.awt.*;
import java.util.ArrayList;
public class Utilities extends DefaultJavaTestScript  {
/**
 * getTop20(): return 20 elements in array
 * @param arrStr: array list which we want to get 20 elements
 * @return 
 */
    public ArrayList<String> getTop20(ArrayList<String> arrStr){
        ArrayList<String> arrTop20 = new ArrayList<String>();
        int noLoop = 5;
        for(int i = 0; (i < arrStr.size()) && (i < noLoop); i++){
            arrTop20.add(arrStr.get(i));
        }
        int count = arrTop20.size();
        while(count < 20){
            arrTop20.add("null");
            count++;
        }
        return arrTop20;
    }

    public boolean isNull(String str){
        if((str == null) || (str.equals("null"))){
            return true;
        }            
        return false;
    }
    public boolean isEmpty(String str){
        if((str.isEmpty()) || (str == "")){
            return true;
        }
        return false;
    }
/**
 * checkString(): return true if string not empty and not null otherwise return false
 * @param str: which we want to check
 */
    public boolean checkString(String str){
        if(isNull(str) && isEmpty(str)){
            return true;
        }
        return false;
    }
/**
 * mergeArrayString(): copy arraySrc and paste to end arrDest
 * @param arrSrc array source which you want to copy
 * @param arrDest which you want to receive after connect 2 array
 * @return arrDest
 */
    public ArrayList<String> mergeArrayString(ArrayList<String> arrSrc, ArrayList<String> arrDest){
        for(String item : arrSrc){
            arrDest.add(item);
        }
        return arrDest;
    }
    
   public void test() {
   }

}
