/**
 * 
 */
package com.appannie.stock.selenium;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.appannie.stock.Properties;

/**
 * @author Haojie Ma
 * @date Jun 2, 2016
 */
public class IndustryCrawler {
	
	private static Logger log=Logger.getLogger(IndustryCrawler.class);
	
	
	
	public static void main(String[] args){
		//getCompanyInfo();
		//getSchoolInfo();
		getCollegeAddress();
		//findDups();
	}
	
	
	
	public static void getSchoolInfo(){
		
		long waitTime=5000L;
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\hma\\workspace\\StockReport\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		String baseUrl="https://en.wikipedia.org/wiki/List_of_colleges_and_universities_in_";
		
		String[] states={"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","Florida","Georgia_(U.S._state)","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};
		
		
		BufferedWriter writer=null;
		String file="C:\\project\\SchoolHouse\\schoolName.txt";
		try{
			
			writer= new BufferedWriter(new FileWriter(new File(file)));
			
			for(String state:states){
				//String state="Alabama";
				driver.get(baseUrl+state);
				List<WebElement> list=driver.findElements(By.xpath("//a[contains(@href,'College') or contains(@href,'University')]"));
				
				for(WebElement element:list){
					StringBuilder sb= new StringBuilder();
					String college=element.getText();
					if(college==null||(!college.contains("University")&&!college.contains("College"))||college.equals("Colleges")||college.equalsIgnoreCase("\"History of the University\""))
							continue;
					sb.append(college).append("||");
					String link=element.getAttribute("href");
					sb.append(link).append("||");
					
					/*
					String city=null;
					try{
						city=driver.findElement(By.xpath("//span[@class='locality']")).getText();
					}catch(Exception e){
						
						System.out.println("didn't get city for "+college);
						
					}
					
					*/
					sb.append(state).append("\n");
					
					writer.write(sb.toString());
					
				}

			}
		}catch(Exception e){
			
			e.printStackTrace();
			
		}finally{
			
			try{
				writer.close();
				
			}catch(Exception e){
				
				e.printStackTrace();
				
			}
		}
	}
	
	
	public static void findDups(){
		BufferedReader reader=null;
		BufferedWriter writer=null;
		String target="C:\\project\\SchoolHouse\\schoolAddressNoDups1.txt";
		String fileName="C:\\project\\SchoolHouse\\schoolAddressNoDups.txt";
		try{
			reader= new BufferedReader(new FileReader(new File(fileName)));
			writer= new BufferedWriter(new FileWriter(new File(target)));
			String line=reader.readLine();
			Set<String> set= new HashSet<String>();
			while(line!=null){
				
				String[] lines=line.split("\\|\\|");
				/*
				String collegeName=lines[0];
				boolean f=set.add(collegeName);
				if(f){
					writer.write(line);
					writer.write("\n");
				}
				*/	
				if(lines.length>5){
					while(lines.length>5){
						set.add(lines[0]);
						line=line.substring(line.indexOf("||")+1, line.length());
						lines=line.split("\\|\\|");
					}
					writer.write(line);
					writer.write("\n");
				}else{
					writer.write(line);
					writer.write("\n");
				}
					
				line=reader.readLine();
			}
			
			
			for(String college:set)
				System.out.println(college);
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}finally{
			
			try{
				reader.close();
				writer.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public static void getCollegeAddress(){

		System.setProperty("webdriver.chrome.driver", "C:\\Users\\hma\\workspace\\StockReport\\chromedriver.exe");
		long waitTime=3000L;
		WebDriver driver = new ChromeDriver();
		BufferedReader reader=null;
		BufferedWriter writer=null;
		String fileName="C:\\project\\SchoolHouse\\additionCollegeName.txt";
		String target="C:\\project\\SchoolHouse\\additionCollegeAddress.txt";
		String baseUrl="https://www.google.com/#safe=active&q=";
		try{
			reader= new BufferedReader(new FileReader(new File(fileName)));
			writer= new BufferedWriter(new FileWriter(new File(target)));
			
			String line=reader.readLine();
			
			while(line!=null){
				StringBuilder sb= new StringBuilder();
				/*
				String[] lines=line.split("\\|\\|");
				String collegeName=lines[0];
				*/
				String collegeName=line;
				sb.append(collegeName).append("||");
				driver.get(baseUrl+collegeName);
				String address=null;
				try{
					Thread.sleep(waitTime);
					address=driver.findElement(By.xpath("//span[@class='_Xbe']")).getText();
				}catch(Exception e){
					line=reader.readLine();
					System.out.println("no such college:"+collegeName);
					e.printStackTrace();
					sb.append("||||||||\n");
					continue;
				}
				
				//System.out.println(collegeName+":"+address.length());
				if(StringUtils.isEmpty(address.trim())){
					System.out.println(collegeName+"||");
					line=reader.readLine();
					continue;
				}
					
				String[] addresses=address.split(",");
				if(addresses.length==3){
					String street=addresses[0];
					sb.append(street).append("||");
					String city=addresses[1].trim();
					sb.append(city).append("||");
					String[] tmp=addresses[2].trim().split(" ");
					if(tmp.length==2){
						String state=tmp[0];
						String zip=tmp[1];
						sb.append(state).append("||");
						sb.append(zip).append("\n");
					}else{
						String state=tmp[0];
						sb.append(state).append("||");
						sb.append("\n");
					}
				}else if(addresses.length==2){
					sb.append("||");
					String city=addresses[0].trim();
					sb.append(city).append("||");
					String[] tmp=addresses[1].trim().split(" ");
					if(tmp.length==2){
						String state=tmp[0];
						String zip=tmp[1];
						sb.append(state).append("||");
						sb.append(zip).append("\n");
					}else{
						String state=tmp[0];
						sb.append(state).append("||");
						sb.append("\n");
					}
				}else if(addresses.length>3){
					System.out.println(collegeName+"||"+address);
					line=reader.readLine();
					continue;
				}
				writer.write(sb.toString());
				line=reader.readLine();
			}
			
			
			
			
		}catch(Exception e){
			
			e.printStackTrace();
			
			
		}finally{
			
			try{
				
				reader.close();
				writer.close();
				
			}catch(Exception e){
				
				e.printStackTrace();
			}
			
		}
		
		
		
	}
	
	
	public static void getCompanyInfo(){
		
		long waitTime=5000L;
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\hma\\workspace\\StockReport\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		String baseURL="https://finance.yahoo.com/q/pr?s=";
		String company=Properties.getProperty("stock.name");
		String[] companyNames=company.split(",");
		String delimiter="||";
		BufferedWriter writer=null;
		String path=Properties.getProperty("stock.flatfile.dir");
		String fileName=path+"companyInfo.txt";
		try{
			writer= new BufferedWriter(new FileWriter(new File(fileName)));
		}catch(Exception e){
			log.info("",e);
			return;
		}
		for(String companyName:companyNames){
			StringBuilder sb= new StringBuilder(companyName);
			//System.out.println(baseURL+companyName);
			driver.get(baseURL+companyName);
			try{
				Thread.sleep(waitTime);
			}catch(Exception e){
				
				e.printStackTrace();
			}
			
			String info=null;;
			try{
				info=driver.findElement(By.xpath("//td[@class='yfnc_modtitlew1']")).getText();
			}catch(Exception e){
				System.err.println(companyName+"'s website is changed,please check");
				e.printStackTrace();
				continue;
			}
			
			
			int count=driver.findElements(By.xpath("//td[@class='yfnc_modtitlew1']/br")).size();
			
			
			
			//System.out.println("===================="+count);
			
			boolean fax=info.contains("Fax");
			boolean phone=info.contains("Phone");
			info=info.replace("Website: ","").replace("- Map","").replace("Phone: ","");
			String[] infoes=info.split("\n");
			for(int i=0;i<count;i++){
				if(count==10){
					if(i>6)
						break;
					if(!fax){
						if(i==1)
							//System.out.println(infoes[i]+"\n"+infoes[++i]);
							sb.append(delimiter).append(infoes[i]).append("\t").append(infoes[++i]);
						else{
							/*
							if(i==3){
								System.out.println(infoes[i].split(",")[0]);//city
								System.out.println(infoes[i].split(",")[1].trim().replace(" ",","));//state
							}else
								System.out.println(infoes[i]);
							*/
							sb.append(delimiter).append(infoes[i]);
						}
							
					}else if(fax){
						if(i==5)
							continue;
						else{
							/*
							if(i==2){
								System.out.println(infoes[i].split(",")[0]);//city
								System.out.println(infoes[i].split(",")[1].trim().replace(" ",","));//state
							}else
								System.out.println(infoes[i]);
							*/
							sb.append(delimiter).append(infoes[i]);
						}
					}
				}else if(count==6){//special case https://finance.yahoo.com/q/pr?s=LMCK,LMCA
					if(i==3)
						//System.out.println(infoes[i]);
						sb.append(delimiter).append(infoes[2]);
					else if(i==0||i==4||i==5){
						if(!infoes[i].contains("Details")&&!infoes[i].contains("N/A"))
							sb.append(delimiter).append(infoes[i]);
						else
							sb.append(delimiter);
					}else
						sb.append(delimiter);
				}else if(count==11){//special case https://finance.yahoo.com/q/pr?s=CA
					if(i>7)
						break;
					if(i==6)
						continue;
					else{
						if(i==3){
							//System.out.println(infoes[i].split(",")[0]);//city
							//System.out.println(infoes[i].split(",")[1].trim().replace(" ",","));//state
							sb.append(delimiter).append(infoes[i]);
						}else if(i==1){
								//System.out.println(infoes[i]+"\n"+infoes[++i]);
							sb.append(delimiter).append(infoes[i]).append("\t").append(infoes[++i]);
						}else
							//System.out.println(infoes[i]);
							sb.append(delimiter).append(infoes[i]);
							
					}
				}else if(count==9){
					if(i>5)
						break;
					
					if(!phone){// sepcail case https://finance.yahoo.com/q/pr?s=JD
						if(i==1)
							//System.out.println(infoes[i]+"\n"+infoes[++i]);
							sb.append(delimiter).append(infoes[i]).append("\t").append(infoes[++i]);
						else if(i==5)
							//System.out.println("N/A\n"+infoes[i]);
							sb.append(delimiter).append(delimiter).append(infoes[i]);
						else
							//System.out.println(infoes[i]);
							sb.append(delimiter).append(infoes[i]);
					}else{
						/*
						if(i==2){
							System.out.println(infoes[i].split(",")[0]);//city
							System.out.println(infoes[i].split(",")[1].trim().replace(" ",","));//state
						}else
							System.out.println(infoes[i]);
						*/
						sb.append(delimiter).append(infoes[i]);
					}
					
				}
					
					
				
			}
			
			/*
			String name=infoes[0];
			System.out.println(name);
			if(count==10&&!info.contains("FAX")){
				String street=infoes[1]+"\n"+infoes[2];
				System.out.println(street);
			}
				
			String city=infoes[2].split(",")[0];
			System.out.println(city);
			String state=infoes[2].split(",")[1].trim().split("\\s")[0];
			System.out.println(state);
			String zipcode=infoes[2].split(",")[1].trim().split("\\s")[1];
			System.out.println(zipcode);
			String county=infoes[3].split("-")[0];
			System.out.println(county);
			String phone=infoes[4].split(": ")[1];
			System.out.println(phone);
			String website=infoes[5].split(": ")[1];
			System.out.println(website);
			
			//System.out.println(info);
			*/
			
			
			List<WebElement> list=driver.findElements(By.xpath("//td[@class='yfnc_tabledata1']/a"));
			
			//special case https://finance.yahoo.com/q/pr?s=DISCK doesn't have any sector and industry info
			if(list.isEmpty()){
				sb.append(delimiter).append(delimiter);
			}else{
				String sector=list.get(0).getText();
				String industry=list.get(1).getText();
				sb.append(delimiter).append(sector);
				sb.append(delimiter).append(industry);
			}
			//System.out.println(sb);
			try{
				writer.write(sb.toString());
				writer.write("\n");
			}catch(Exception e){
				log.info("",e);
				
			}
			
		}
		
		driver.close();
		
		try{
			writer.flush();
			writer.close();
			
		}catch(Exception e){
			
			log.info("close file error",e);
		}
		
	}
	
	

}


