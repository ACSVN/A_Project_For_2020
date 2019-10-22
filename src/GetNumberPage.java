/**
 * Created on Tue May 28 07:43:52 ICT 2019
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

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetNumberPage extends DefaultJavaTestScript  {

	public void test() {
		try {

			String filename = getContext().getVariableAsString("fullPathHtml");
			String gsc_cursor_page = getContext().getVariableAsString("class_gsc_cursor_page");

			int totalpage = 0;
			Document doc = Jsoup.parse(new File(filename), "utf-8");
			Elements divTag = doc.getElementsByClass(gsc_cursor_page);
			if(divTag.size() != 0 ){
			totalpage = divTag.size();
			}
			//return totalpage;
			getContext().setVariable("number_pages", totalpage);
		} catch (StopRequestException ex) {
			throw ex;
		}catch (IOException ex) {
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
	}

	public static void main(String args[]) {
		GetNumberPage script = new GetNumberPage();
		ApplicationSupport robot = new ApplicationSupport();
		AutomatedRunnable runnable = robot.createAutomatedRunnable(script, "GetNumberPage@" + Integer.toHexString(script.hashCode()), args, System.out, false);
		new Thread(runnable).start();
	}
}
