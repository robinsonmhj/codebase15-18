/**
 * 
 */
package com.appannie.stock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

import com.appannie.stock.DAO.StockDaoImpl;

/**
 * @author Haojie Ma
 * @date Jun 5, 2016
 */
@Component("RTTransaction")
@Scope("prototype")
public class RealTimeTransaction implements Runnable{
	
	private static Logger log=Logger.getLogger(RealTimeTransaction.class);
	
	
	@Autowired
	@Qualifier("StockDAO")
	private StockDaoImpl stockImp;
	
	
	private int threadId;
	private Map<Integer,BigDecimal> changeMap;
	private Map<Integer,String> symbolMap;
	
	
	public void setstockImp(StockDaoImpl stockImp) {
		this.stockImp = stockImp;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public void setChangeMap(Map<Integer, BigDecimal> changeMap) {
		this.changeMap = changeMap;
	}

	public void setSymbolMap(Map<Integer, String> symbolMap) {
		this.symbolMap = symbolMap;
	}

	public RealTimeTransaction(){
		
	}

	public void run(){
		int threadNo=Integer.valueOf(Properties.getProperty("stock.thread.no"));
		int maxLine=Integer.valueOf(Properties.getProperty("stock.imprort2db.threshold"));
		int waitTime=Integer.valueOf(Properties.getProperty("stock.waiting.time"));
		String path=Properties.getProperty("stock.flatfile.dir");
		String fileName=path+threadId+"_"+System.currentTimeMillis()+".txt";
		String tableName="transaction";
		int interval=symbolMap.size()/threadNo;
		int start=threadId*interval+1;
		int end=0;
		if(threadId==threadNo-1){
			end=symbolMap.size()+1;
		}else{
			end=(threadId+1)*interval+1;
		}
		int current=start;
		int count=0;//store the number of lines which already written to file
		Stock stock=null;
		StockQuote quote=null;
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");//used for insertTime and LastTrade time
		SimpleDateFormat sdf2=new SimpleDateFormat("hh:mm:ss");//used for sleep after market is closed
		BufferedWriter writer=null;
		while(true){
			if(current==end){
				try{
					Thread.sleep(waitTime);
				}catch(Exception ex){
					log.info("I cannot sleep",ex);
				}
				
				current=start;
			}
				
			log.info("I am retriving data for id "+current);
			String symbol=symbolMap.get(current);
			try{
				stock=YahooFinance.get(symbol);
			}catch(Exception e){
				current++;
				log.info("symbol:"+symbol,e);
				try{
					Thread.sleep(60*60*1000);
				}catch(Exception ex){
					log.info("I cannot sleep",ex);
				}
				continue;
			}
			BigDecimal lastChange=changeMap.get(current);
			
			try{
				quote=stock.getQuote(true);
			}catch(Exception e){
				current++;
				log.info("",e);
				try{
					Thread.sleep(60*60*1000);
				}catch(Exception ex){
					log.info("I cannot sleep",ex);
				}
				continue;
			}
			Date insertTime=new Date();
			BigDecimal change=quote.getChange();
			if(change.compareTo(BigDecimal.ZERO)==0||(change.equals(lastChange))){
				current++;
				continue;
			}
			changeMap.put(current, change);//put the change as the last change
			StringBuilder quoteSb=new StringBuilder();
			BigDecimal ask=quote.getAsk();
			Long askSize=quote.getAskSize();
			Long avgVolume=quote.getAvgVolume();
			avgVolume=avgVolume==null?0l:avgVolume;
			BigDecimal bid=quote.getBid();
			Long bidSize=quote.getBidSize();
			BigDecimal changeFromAvg200=quote.getChangeFromAvg200();
			BigDecimal changeFromAvg200InPercent=quote.getChangeFromAvg200InPercent();
			BigDecimal changeFromAvg50=quote.getChangeFromAvg50();
			BigDecimal changeFromAvg50InPercent=quote.getChangeFromAvg50InPercent();
			BigDecimal changeFromYearHigh=quote.getChangeFromYearHigh();
			BigDecimal changeFromYearHighInPercent=quote.getChangeFromYearHighInPercent();
			BigDecimal changeFromYearLow=quote.getChangeFromYearLow();
			BigDecimal changeFromYearLowInPercent=quote.getChangeFromYearLowInPercent();
			BigDecimal changeInPercent=quote.getChangeInPercent();
			BigDecimal dayHigh=quote.getDayHigh();
			BigDecimal dayLow=quote.getDayLow();
			//String lastTradeDate=quote.getLastTradeDateStr();
			Calendar lastTradeTime=quote.getLastTradeTime();//combinethese2into1column
			Long lastTradeSize=quote.getLastTradeSize();
			//BigDecimal open=quote.getOpen();//canbecalculate
			//BigDecimal previousClose=quote.getPreviousClose();//canbecalculate
			BigDecimal price=quote.getPrice();
			BigDecimal priceAvg200=quote.getPriceAvg200();
			BigDecimal priceAvg50=quote.getPriceAvg50();
			//BigDecimal yearHigh=quote.getYearHigh();//canbecalculate
			//BigDecimal yearLow=quote.getYearLow();//canbecalculate
			quoteSb.append(current).append(",").append(ask).append(",").append(askSize).append(",").append(avgVolume).append(",").append(bid).append(",").append(bidSize).append(",").append(change).append(",").append(changeFromAvg200).append(",").append(changeFromAvg200InPercent).append(",").append(changeFromAvg50).append(",").append(changeFromAvg50InPercent).append(",").append(changeFromYearHigh).append(",").append(changeFromYearHighInPercent).append(",").append(changeFromYearLow).append(",").append(changeFromYearLowInPercent).append(",").append(changeInPercent).append(",").append(dayHigh).append(",").append(dayLow).append(",").append(lastTradeSize).append(",").append(sdf1.format(lastTradeTime.getTime())).append(",").append(price).append(",").append(priceAvg200).append(",").append(priceAvg50).append(",").append(sdf1.format(insertTime));
			//System.out.println(quoteSb);
			
			try{
				if(count==0)
					writer= new BufferedWriter(new FileWriter(new File(fileName)));
				writer.write(quoteSb.toString());
				writer.write("\n");
				count++;
				if(count==maxLine){
					writer.flush();
					stockImp.importFromFile(tableName, fileName);
					try{
						writer.close();
					}catch(Exception e){
						log.info("close writer error",e);
					}
					fileName=path+threadId+"_"+System.currentTimeMillis()+".txt";
					count=0;
				}
				
				//write to file and import to db after 16:00:00
				Date currentTime = new Date();
				if(sdf2.format(currentTime).compareTo("16:00:00")>0){
					log.info("The market is closed, I am going to sleep, current time is "+sdf2.format(currentTime));
					writer.flush();
					stockImp.importFromFile(tableName, fileName);
					try{
						writer.close();
					}catch(Exception e){
						log.info("close writer error",e);
					}
					fileName=path+threadId+"_"+System.currentTimeMillis()+".txt";
					count=0;
					Thread.sleep(17*60*60*1000);
				}
				current++;
				
			}catch(Exception e){
				log.info("",e);
			}
		}
		
		
		
		
		
	}

}


