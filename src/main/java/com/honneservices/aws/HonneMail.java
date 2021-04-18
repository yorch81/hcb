package com.honneservices.aws;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * HonneMail<br>
 * 
 * HonneMail: Class to send emails using Honne web service<br><br>
 * 
 * Copyright 2021 Jorge Alberto Ponce Turrubiates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @version    1.0.0, 2021-01-29
 * @author     <a href="mailto:jorge.ponce@honneservices.com">Jorge Alberto Ponce Turrubiates</a>
 */
public class HonneMail {
	/**
	 * Web service URL
	 */
	private String mUrl = "";
	
	/**
	 * Web service key
	 */
	private String mApiKey = "";
	
	/**
	 * Constructor
	 * 
	 * @param url Web service URL
	 * @param apiKey Web service key
	 */
	public HonneMail(String url, String apiKey) {
		this.mUrl = url;
		this.mApiKey = apiKey;
	}
	
	/**
	 * Send text email
	 * 
	 * @param to To send email
	 * @param cc CC send email
	 * @param subject Subject email
	 * @param body Body email
	 */
	public void send(String to, String cc, String subject, String body) {
		HttpClient httpclient = HttpClientBuilder.create().build();
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("to", to);
		jsonData.put("cc", cc);
		jsonData.put("subject", subject);
		jsonData.put("body", body);
		jsonData.put("attachName", "");
		jsonData.put("attach", "");
		
        try{
        	HttpPost httppost = new HttpPost(this.mUrl);        	
        	StringEntity entJson = new StringEntity(jsonData.toString());
        	
        	httppost.setEntity(entJson);
        	
        	httppost.setHeader("x-api-key", this.mApiKey);
        	httppost.setHeader("Accept", "application/json");
        	
            HttpResponse httpResponse = httpclient.execute(httppost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            int status = httpResponse.getStatusLine().getStatusCode();
            StringBuffer response = new StringBuffer();
            
            if (status == 200) {
            	String line = "";
           
                while ((line = rd.readLine()) != null)
                    response.append(line);
            }
        }
        catch (IOException e){
        	HCBLog.print("ERROR", e.getMessage());
        }
        catch (JSONException e){
        	HCBLog.print("ERROR", e.getMessage());
        }
        
        httpclient = null;
	}
	
	/**
	 * Send mail with attachment
	 * 
	 * @param to To send email
	 * @param cc CC send email
	 * @param subject Subject email
	 * @param body Body email
	 * @param attachName Name of attachment
	 * @param attachFile File to attach
	 */
	public void sendWithAttachment(String to, String cc, String subject, String body, String attachName, String attachFile) {
		HttpClient httpclient = HttpClientBuilder.create().build();
		
		try{
			JSONObject jsonData = new JSONObject();
			jsonData.put("to", to);
			jsonData.put("cc", cc);
			jsonData.put("subject", subject);
			jsonData.put("body", body);
			jsonData.put("attachName", attachName);
			
			// Get Base64 from file
			File file = new File(attachFile);
			
			if (file.exists()) {
				byte[] fileContent = Files.readAllBytes(file.toPath());
		        String b64 = Base64.getEncoder().encodeToString(fileContent);
		        
		        jsonData.put("attach", b64);
			}
			else 
				jsonData.put("attach", "");
			
        	HttpPost httppost = new HttpPost(this.mUrl);        	
        	StringEntity entJson = new StringEntity(jsonData.toString());
        	
        	httppost.setEntity(entJson);
        	
        	httppost.setHeader("x-api-key", this.mApiKey);
        	httppost.setHeader("Accept", "application/json");
        	
            HttpResponse httpResponse = httpclient.execute(httppost);

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            int status = httpResponse.getStatusLine().getStatusCode();
            StringBuffer response = new StringBuffer();
            
            if (status == 200) {
            	String line = "";
           
                while ((line = rd.readLine()) != null)
                    response.append(line);
            }
        }
        catch (IOException e){
        	HCBLog.print("ERROR", e.getMessage());
        }
		catch (JSONException e){
			HCBLog.print("ERROR", e.getMessage());
        }
        
        httpclient = null;
	}
}
