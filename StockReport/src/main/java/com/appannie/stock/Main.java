/**
 * 
 */
package com.appannie.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.appannie.http.Http;
import com.appannie.stock.DAO.StockDaoImpl;


import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * @author Haojie Ma
 * @date Jun 2, 2016
 */
public class Main {

	private static Logger log = Logger.getLogger(Main.class);
	private static GenericXmlApplicationContext ctx=null;

	public static void main(String[] args) {
		/*
		int len=args.length;
		if(len!=1&&len!=3){
			
			System.out.println("=============================Usage====================================");
			System.out.println("java -jar StockReport.jar getTransaction||getCompnayInfo||getHistoryData startDate endDate");
			for(String arg:args){
				log.info(arg);
				//System.out.println(arg);
			}
			return;
			
		}
*/
		ctx=new GenericXmlApplicationContext();  
		ctx.load("classpath:applicationContext.xml");  
		ctx.refresh();
		
		
		
		
		
		
		String proxyHost=Properties.getProperty("http.proxyHost");
		if(!StringUtils.isEmpty(proxyHost)){
			System.setProperty("http.proxyHost",proxyHost);
			log.info("http.proxyHost:"+proxyHost);
		}
			
		String proxyPort=Properties.getProperty("http.proxyPort");
		if(!StringUtils.isEmpty(proxyPort)){
			System.setProperty("http.proxyPort",proxyPort);
			log.info("http.proxyPort:"+proxyPort);
		}
			
		
		//getGeoInfo();
		//getScheoolInfo();
		//getHomeData();
		//getContactinfo();
		//splitAddress();
		getDistance();
		/*
		
		if("getCompnayInfo".equals(args[0])){
			IndustryCrawler.getCompanyInfo();
		}else if("getTransaction".equals(args[0])){
			getTransactions();
		}else if("getHistoryData".equals(args[0])&&len==3){
			getHistory(args[1],args[2]);
		}
		*/

	}
	
	public static void getHistory(String startDate,String endDate){
		
		StockDaoImpl stockDAO=ctx.getBean("StockDAO", StockDaoImpl.class);
		Map<Integer,String> symbolMap=stockDAO.getSymbol();
		Map<String,Integer> symbolMap2= new HashMap<String,Integer>();
		
		
		for(Map.Entry<Integer,String> entry:symbolMap.entrySet()){
			symbolMap2.put(entry.getValue(),entry.getKey());
		}

		String stockName = Properties.getProperty("stock.name");
		String[] stocks = stockName.split(",");
		
		try {
			Calendar from = Calendar.getInstance(),to = Calendar.getInstance();
			String[] start=startDate.split("-");
			String[] end=endDate.split("-");
			from.set(Integer.valueOf(start[0]), Integer.valueOf(start[1]), Integer.valueOf(start[2]));
			to.set(Integer.valueOf(end[0]), Integer.valueOf(end[1]), Integer.valueOf(end[2]));
			long start1 = System.currentTimeMillis();
			Map<String, Stock> stockMap = YahooFinance.get(stocks, from, to,Interval.DAILY);
			long end1 = System.currentTimeMillis();
			log.info("get info using " + (end1 - start1));
			BufferedWriter writer = null;
			try {
				String fileName = "historyData.sql";
				String filePath = Properties.getProperty("stock.flatfile.dir");
				writer = new BufferedWriter(new FileWriter(filePath+ fileName));
				for (String symbol : stockMap.keySet()) {
					Stock stock = stockMap.get(symbol);
					System.out.println("symbol:"+symbol);
					if(symbolMap2==null)
						System.out.println("symbolMap2 is "+symbolMap2);
					int symbolId=symbolMap2.get(symbol);
					List<HistoricalQuote> hqList = stock.getHistory();
					StringBuilder sb = new StringBuilder();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					for (HistoricalQuote quote : hqList) {
						Calendar date = quote.getDate();
						BigDecimal openPrice = quote.getOpen();
						BigDecimal high = quote.getHigh();
						BigDecimal low = quote.getLow();
						BigDecimal adj = quote.getAdjClose();
						BigDecimal close = quote.getClose();
						Long volume = quote.getVolume();
						sb.append(symbolId).append(",")
								.append(sdf.format(date.getTime())).append(",")
								.append(openPrice).append(",").append(high)
								.append(",").append(low).append(",")
								.append(adj).append(",").append(close)
								.append(",").append(volume).append("\n");
					}
					//System.out.println(sb);
					writer.write(sb.toString());
				}

			} catch (Exception e) {
				log.info("",e);
			}finally{
				try{
					writer.close();
				}catch(Exception e){
					log.info("close file exception",e);
				}
				
				
			}


		} catch (Exception e) {

			log.info("", e);
		}

	}
	
	
	public static void  getTransactions(){
		
		
		int threadNo=Integer.valueOf(Properties.getProperty("stock.thread.no"));
		
		StockDaoImpl stockDAO=ctx.getBean("StockDAO", StockDaoImpl.class);
		Map<Integer,String> symbolMap=stockDAO.getSymbol();
		Map<Integer,BigDecimal> changeMap=stockDAO.getLastChange();
		
		
		
		RealTimeTransaction[] transactions= new RealTimeTransaction[threadNo];
		Thread[] threads= new Thread[threadNo];
		
		for(int i=0;i<threadNo;i++){
			transactions[i]=ctx.getBean("RTTransaction", RealTimeTransaction.class);
			transactions[i].setThreadId(i);
			transactions[i].setSymbolMap(symbolMap);
			transactions[i].setChangeMap(changeMap);
			threads[i]= new Thread(transactions[i],"Thread"+i);
			threads[i].start();
		}
		
		
	}
	
	public static void getGeoInfo(){
		
		String baseUrl="https://maps.googleapis.com/maps/api/geocode/json?address=";
		String key="AIzaSyCycPcDQ2HW09sYYAh1KzFDay4ncc5Mxu4";
		StockDaoImpl stockDAO=ctx.getBean("StockDAO", StockDaoImpl.class);
		Map<String,String> addressMap=stockDAO.addressInfo();
		Http http= new Http();
		StringBuilder jresult=new StringBuilder("{\n");
		try{
			for(Entry<String,String> entry:addressMap.entrySet()){
				String url=baseUrl+java.net.URLEncoder.encode(entry.getValue(),"UTF-8")+"&"+key;
				//System.out.println(url);
				String response=http.getReponse(url);
				System.out.println(response);
				JSONObject json=new JSONObject(response);
				//System.out.println("============");
				JSONArray result=(JSONArray)json.get("results");
				String placeId=((JSONObject)result.get(0)).getString("place_id");
				JSONObject geometry=(JSONObject)((JSONObject)result.get(0)).get("geometry");
				JSONObject location=(JSONObject)geometry.get("location");
				double lng=location.getDouble("lng");
				double lat=location.getDouble("lat");
				jresult.append("\"").append(entry.getKey()).append("\":[\n");
				jresult.append(lng).append(",\n");
				jresult.append(lat).append("\n");
				jresult.append("],\n");
			
			}			
		jresult.deleteCharAt(jresult.lastIndexOf(","));
		jresult.append("}\n");
		System.out.println(jresult);
		log.info(jresult);
		}catch(Exception e){
			log.info("",e);
		}
		
	}
	
	
	
	
	public static void getScheoolInfo(){
		
		
		BufferedReader reader=null;
		BufferedWriter writer=null;
		Http http= new Http();
		String delimiter="||";
		try{
			String path=Properties.getProperty("stock.flatfile.dir");
			//String file=path+"school.csv";
			//String toFile=path+"school.sql";
			String file=path+"homeDetails_18301.txt";
			String toFile=path+"homeDetails_18301_rent.txt";
			reader= new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toFile)));
			String line=null;
			String baseUrl="https://maps.googleapis.com/maps/api/geocode/json?address=";
			String key=Properties.getProperty("google.map.api.key");
			
			while((line=reader.readLine())!=null){
				StringBuilder result= new StringBuilder();
				/*
				String[] lines= line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				collegeName=lines[0].replace("\"", "");
				*/
				String[] lines=line.split("\\|\\|");
				StringBuilder url= new StringBuilder(baseUrl);
				/*
				for(String s:lines){
					if(StringUtil.isNotBlank(s))
						url.append(java.net.URLEncoder.encode(s,"UTF-8")).append(",");
				}
				
				//remove the last comma
				url.deleteCharAt(url.length()-1);
				url.append("&").append("key=").append(key);
				line=line.replace("\\r\\n|\\r|\\n", "");
				result.append(line);
				*/
				
				String rent=lines[lines.length-1];
				if(rent.equals("1"))
					continue;
				String sourceId=lines[0];
				String address=lines[1];
				url.append(java.net.URLEncoder.encode(address,"UTF-8"));
				url.append("&").append("key=").append(key);
				result.append(sourceId);
				String response=null;
				try{
					response=http.getReponse(url.toString());
					JSONObject json=new JSONObject(response);
					JSONObject result1=(JSONObject)((JSONArray)json.get("results")).get(0);
					JSONObject geometry=(JSONObject)result1.get("geometry");
					String placeId=result1.getString("place_id");
					JSONObject location=(JSONObject)geometry.get("location");
					double lng=location.getDouble("lng");
					double lat=location.getDouble("lat");
					result.append(delimiter).append(lat);
					result.append(delimiter).append(lng);
					result.append(delimiter).append(placeId);
					result.append("\n");
				}catch(Exception e){
					log.info("url is \n"+url);
					log.info("response is \n"+response);
					log.info("exception is ",e);
					result.append(delimiter);
					result.append(delimiter);
					result.append(delimiter);
					result.append("\n");
				}			
				writer.write(result.toString());
			}
		}catch(Exception e){
			
			log.info("",e);
			
			
		}finally{
			
			try{
				
				reader.close();
				writer.close();
				
			}catch(Exception e){
				
				log.info("",e);
			}
		}
	}
	
	public static void getDistance(){
		
		BufferedReader reader=null;
		BufferedWriter writer=null;
		String delimiter="||";
		Http http= new Http();
		
		
		try{
			String path=Properties.getProperty("stock.flatfile.dir");
			String file=path+"distance.txt";
			String toFile=path+"distance_detail.txt";
			reader= new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toFile)));
			String line=null;
			int collegeId=2306;
			String collge_place_id="ChIJUzo_x2GIxIkRIgGGGvvCuxY";
			String key="AIzaSyDy8Z_W5N41vFii_iKyajzl2Y_Bm0-koxQ";
			String baseUrl="https://maps.googleapis.com/maps/api/distancematrix/json?mode=walking&key=AIzaSyDy8Z_W5N41vFii_iKyajzl2Y_Bm0-koxQ&origins=place_id:ChIJUzo_x2GIxIkRIgGGGvvCuxY&destinations=place_id:";
			
			/*
			 * String response=null;
			try{
				response=http.getReponse(baseUrl);
				JSONObject json=new JSONObject(response);
				JSONObject rows=(JSONObject)json.getJSONArray("rows").get(0);
				JSONObject elements=(JSONObject)rows.getJSONArray("elements").get(0);
				int d=elements.getJSONObject("distance").getInt("value");
				System.out.println(d);
				
			}catch(Exception e){
				
				log.info("parse json file error"+response,e);
			}
			*/
			
			while((line=reader.readLine())!=null){
				String[] lines=line.split("\\|\\|");
				StringBuilder result= new StringBuilder();
				result.append(collegeId).append(delimiter);
				result.append(lines[0]).append(delimiter);
				String placeId=lines[1];
				String response=null;
				try{
					response=http.getReponse(baseUrl+placeId);
					JSONObject json=new JSONObject(response);
					JSONObject rows=(JSONObject)json.getJSONArray("rows").get(0);
					JSONObject elements=(JSONObject)rows.getJSONArray("elements").get(0);
					int d=elements.getJSONObject("distance").getInt("value");	
					result.append(d).append("\n");
					writer.write(result.toString());
				}catch(Exception e){
					
					log.info("parse json file error\n"+response,e);
				}
				
			}
		}catch(Exception e){
			
			log.info("",e);
			
			
		}finally{
			
			try{
				
				reader.close();
				writer.close();
				
			}catch(Exception e){
				
				log.info("",e);
			}
		}
		
		
	}
	
	
	public static void splitAddress(){
		BufferedReader reader=null;
		BufferedWriter writer=null;
		String delimiter="||";
		try{
			String path=Properties.getProperty("stock.flatfile.dir");
			//String file=path+"school.csv";
			//String toFile=path+"school.sql";
			String file=path+"homeDetails_18301.txt";
			String toFile=path+"homeDetails_18301_detail.txt";
			reader= new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			writer= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toFile)));
			String line=null;
			
			while((line=reader.readLine())!=null){
				String[] lines=line.split("\\|\\|");
				writer.write(lines[0]);
				writer.write(delimiter);
				String[] address=lines[1].split(",");
				if(address.length!=3){
					System.out.println(line);
					continue;
				}
				String street=address[0].trim();
				String city=address[1].trim();
				String[] states=address[2].trim().split(" ");
				if(states.length!=2)
				{
					System.out.println(line);
					continue;
				}
				String state=states[0];
				String zip=states[1];
				writer.write(street);
				writer.write(delimiter);
				writer.write(city);
				writer.write(delimiter);
				writer.write(state);
				writer.write(delimiter);
				writer.write(zip);
				writer.write(delimiter);
				
				for(int i=2;i<lines.length-1;i++){
					writer.write(lines[i]);
					writer.write(delimiter);
				}
				writer.write(lines[lines.length-1]);
				writer.write("\n");
			}
		}catch(Exception e){
			
			log.info("",e);
			
			
		}finally{
			
			try{
				
				reader.close();
				writer.close();
				
			}catch(Exception e){
				
				log.info("",e);
			}
		}
		
		
	}
	
	
	public static void getContactinfo(){
		
		String delimiter="||";
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\hma\\workspace\\StockReport\\chromedriver.exe");
		long waitTime=4000L;
		WebDriver driver = new ChromeDriver();
		
		BufferedReader reader=null;
		BufferedWriter writer=null;
		String original="C:\\project\\SchoolHouse\\homeDetails_18301.txt";
		String target="C:\\project\\SchoolHouse\\homeDetails_18301_rent.txt";
		String baseUrl="http://www.zillow.com/homes/for_rent/68722/house,condo,apartment_duplex,mobile,townhouse_type/";
		try{
			reader= new BufferedReader(new FileReader(new File(original)));
			writer= new BufferedWriter(new FileWriter(new File(target)));
			String line;
			while((line=reader.readLine())!=null){
				String[] lines=line.split("\\|\\|");
				int len=lines.length;
				String zid=lines[0];
				if(lines[len-1].equals("1")){
					driver.get(baseUrl+zid+"_zpid");
					int count=3;
					String name=null,company=null,phone=null;
					while(count>0){
						try{
							Thread.sleep(waitTime);
							name=driver.findElement(By.xpath("//a[@class='profile-name-link']")).getText();
						}catch(Exception e){
							count--;
						}
						count=0;
					}
					
					if(name==null){
						try{
							name=driver.findElement(By.xpath("//span[@class='property-info notranslate building-name']")).getText();
						}catch(Exception e){
							log.info("name not found",e);
							log.error("name not found,id="+zid);
						}
					}
					
					
					try{
						company=driver.findElement(By.xpath("//span[@class='property-info company-name']")).getText();
					}catch(Exception e){
						log.info("company name not found",e);
					}
					try{
						phone=driver.findElement(By.xpath("//span[@class='property-info contact-phone']")).getText().replace("Call: ","");
						phone=phone.replaceAll("\\s", "");
					}catch(Exception e){
						log.info("phone not found",e);
						log.error("phone not found,id="+zid);
					}
					
					 
					writer.write(line);
					writer.write(delimiter);
					if(name!=null)
						writer.write(name);
					writer.write(delimiter);
					if(company!=null)
						writer.write(company);
					writer.write(delimiter);
					if(phone!=null)
					writer.write(phone);
					writer.write("\n");
					
				}
			}
		}catch(Exception e){
			
			log.info("",e);
			
		}finally{
			
			try{
				reader.close();
				writer.close();
				
			}catch(Exception e){
				
				log.info("close file error",e);
			}
			
		}
	}
	
	public static void getHomeData()
	{
		
		Http http=new Http();
		String url="http://www.zillow.com/search/RealEstateSearch.htm?citystatezip=18301";
		String key="Set-Cookie";
		String cookie=http.getHeader(url, key);
		String rect=cookie.split("&")[2].split("=")[1];
		String[] rects=rect.split(",");
		for(int i=0;i<4;i++){
			String location=rects[i];
			if(location.substring(location.indexOf(".")+1, location.length()).length()!=6){
				location=location+"0";
			}
			location=location.replace(".", "");
			rects[i]=location;
		}
		StringBuilder urlsb=new StringBuilder("http://www.zillow.com/search/GetResults.htm?search=map&zoom=12&rect=");
		urlsb.append(rects[3]).append(",");
		urlsb.append(rects[2]).append(",");
		urlsb.append(rects[1]).append(",");
		urlsb.append(rects[0]);
		String houseList=http.getReponse(urlsb.toString());
		//String houseDetail=http.getReponse("http://www.zillow.com/jsonp/Hdp.htm?callback=YUI&zpid=9804682");
		//log.info(houseDetail);
		
		BufferedWriter writer=null;
		String file="C:\\project\\SchoolHouse\\homeDetails_18301.txt";
		String delimiter="||";
		try{
			writer= new BufferedWriter(new FileWriter(new File(file)));
			JSONObject json=new JSONObject(houseList);
			JSONObject result=json.getJSONObject("map");
			JSONArray properties=result.getJSONArray("properties");
			//System.out.println("properties"+properties);
			for(int i=0;i<properties.length();i++){
				JSONArray house=properties.getJSONArray(i);
				int houseId=(int)house.get(0);
				String value=(String)house.get(3);
				value=value.replace("$", "");
				//System.out.println(houseId+":"+value);
				int price=0;
				try{
					if(value.contains("K")||value.contains("k")){
						price=(int)(Double.valueOf(value.substring(0, value.length()-1))*1000);
					}else
						price=Integer.valueOf(value);
				}catch(Exception e){
					log.info("formated integer error,value="+value,e);
				}
				
				StringBuilder detailUrl=new StringBuilder("http://www.zillow.com/jsonp/Hdp.htm?callback=YUI&zpid=");
				detailUrl.append(houseId);
				//System.out.println(houseId);
				String houseDetail=http.getReponse(detailUrl.toString());
				if(houseDetail==null||houseDetail.length()==0)
					continue;
				//log.info(houseDetail);
				StringBuilder home=new StringBuilder();
				home.append(houseId).append(delimiter);
				houseDetail=houseDetail.substring(37, houseDetail.length()-5);//remove the if (typeof YUI!=="undefined") { YUI( and the end
				int end=houseDetail.indexOf("\"bodyScript\"");
				String houseDetail1=houseDetail.substring(0, end);
				int start=houseDetail.indexOf("\"responsive\"");
				String houseDetail2=houseDetail.substring(start, houseDetail.length());
				houseDetail=houseDetail1+houseDetail2;
				end=houseDetail.indexOf("\"tapBody\"");
				houseDetail1=houseDetail.substring(0, end);
				start=houseDetail.indexOf("\"keystone\"");
				houseDetail2=houseDetail.substring(start, houseDetail.length());
				houseDetail=houseDetail1+houseDetail2;
				//log.info(houseDetail);
				json=new JSONObject(houseDetail);
				String address=json.getString("title");
				address=address.split("\\|")[0].replace("- Zillow", "").trim();
				String[] addresses=address.split(",");
				if(addresses.length!=3){
					log.error(houseId+":"+address);
					continue;
				}
				String street=addresses[0].trim();
				String city=addresses[1].trim();
				String[] stateZip=addresses[2].trim().split(" ");
				if(stateZip.length!=2){
					log.error(houseId+":"+address);
					continue;
				}
				String state=stateZip[0];
				String zip=stateZip[1];
				home.append(street).append(delimiter);
				home.append(city).append(delimiter);
				home.append(state).append(delimiter);
				home.append(zip).append(delimiter);
				String bodyContent=json.getString("bodyContent");
				//log.info("bodyContent="+bodyContent);
				start=bodyContent.indexOf("<div class=\"notranslate zsg-content-item\">");
				bodyContent=bodyContent.substring(start, bodyContent.length());
				end=bodyContent.indexOf("</div>");
				String summary=bodyContent.substring(42,end);
				summary=summary.replaceAll("[\\r\\n|\\r|\\n]+", "");
				home.append(summary).append(delimiter);
				
				start=bodyContent.indexOf("class=\"profile-name-link\">");
				if(start==-1){
					start=bodyContent.indexOf("class=\"property-info notranslate building-name\">");
					if(start!=-1){
						bodyContent=bodyContent.substring(start, bodyContent.length());
						end=bodyContent.indexOf("</span>");
						String name=bodyContent.substring(48,end);
						home.append(name);
					}
				}else{
					bodyContent=bodyContent.substring(start, bodyContent.length());
					end=bodyContent.indexOf("</a>");
					String name=bodyContent.substring(26,end);
					home.append(name);
				}
				home.append(delimiter);
				
				start=bodyContent.indexOf("class=\"property-info company-name\">");
				if(start!=-1){
					bodyContent=bodyContent.substring(start, bodyContent.length());
					end=bodyContent.indexOf("</span>");
					String company=bodyContent.substring(35,end);
					home.append(company);
				}
				home.append(delimiter);
				
				
				start=bodyContent.indexOf("class=\"property-info contact-phone\">");
				if(start!=-1){
					bodyContent=bodyContent.substring(start, bodyContent.length());
					end=bodyContent.indexOf("</span>");
					String phone=bodyContent.substring(36,end).replace("Call: ","");
					home.append(phone);
				}
				home.append(delimiter);
				
				JSONObject keystone=json.getJSONObject("keystone");
				double bath=0;
				int bed=0;
				try{
					bath=keystone.getDouble("baths");
				}catch(Exception e){
					log.info("houseId="+houseId,e);
				}
				try{
					bed=keystone.getInt("beds");
				}catch(Exception e){
					log.info("houseId="+houseId,e);
				}
				String type=keystone.getString("type");
				String pageName=json.getString("pageName");
				home.append(bath).append(delimiter);
				home.append(bed).append(delimiter);
				home.append(price).append(delimiter);
				home.append(type).append(delimiter);
				if(pageName.contains("ForSale"))
					home.append(0);
				else if(pageName.contains("ForRent"))
					home.append(1);
				else
					home.append(3);
				writer.write(home.toString());
				writer.write("\n");
			}
			
		}catch(Exception e){
			log.info(houseList,e);
		}finally{
			
			try{
				writer.close();
			}catch(Exception e){
				
				log.info("close file error",e);
			}
		}
		
		
		
	}
	
	
}
