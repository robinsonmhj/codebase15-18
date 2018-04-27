/**
 * 
 */
package com.appannie.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.log4j.Logger;

/**
 * @author Haojie Ma
 * @date Jun 6, 2016
 */
public class Http {

	private static Logger log=Logger.getLogger(Http.class);
	private HttpClient http=null;
	
	
	public Http(){
		String proxyServer=System.getProperty("http.proxyHost");
		int proxyPort=Integer.valueOf(System.getProperty("http.proxyPort"));
		HttpHost proxy=null;
		if(proxyServer!=null){
			proxy= new HttpHost(proxyServer,proxyPort, "http");
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
			http= HttpClients.custom().setRoutePlanner(routePlanner).build();
		}else{
			http= HttpClientBuilder.create().build();
		}
	}
	
	
	
	public String getReponse(String url){
		StringBuilder sb= new StringBuilder();
		BufferedReader reader=null;
		try{
			HttpGet getRequest = new HttpGet(url);
			//getRequest.addHeader("accept", "application/json");

			HttpResponse response = http.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				log.info("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			reader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			//System.out.println("before while");
			String output;
			//int i=0;
			while ((output = reader.readLine()) != null) {
				sb.append(output).append("\n");
				//System.out.println("In while "+i++);
				//log.info(output);
			}
		}catch(Exception e){
			log.info("",e);
		}finally{
			try{
				reader.close();
			}catch(Exception e){
				log.info("close reader error",e);
			}
			
		}
		
		//log.info(sb);
		return sb.toString();
		
		
	}
	
	
	public String getHeader(String url,String key){
		
		
		try{
			HttpGet request = new HttpGet(url);
			HttpResponse response = http.execute(request);
			
			/*
			//get all headers		
			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				System.out.println("Key:" + header.getName() 
				      + " ,Value:" + java.net.URLDecoder.decode(java.net.URLDecoder.decode(header.getValue()), "UTF-8"));
			}
			*/
			
			//get header by 'key'
			String cookie = response.getFirstHeader(key).getValue();
			//System.out.println("cookie="+cookie);
			String cookieUTF = java.net.URLDecoder.decode(java.net.URLDecoder.decode(cookie,"UTF-8"),"UTF-8");
			
			return cookieUTF;
			
			
			
		}catch(Exception e){
			
			log.info("",e);
			
		}
		
		return null;
		
		
	}
	
	
}


