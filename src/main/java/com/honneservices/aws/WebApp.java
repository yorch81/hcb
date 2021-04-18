package com.honneservices.aws;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.Properties;

import org.json.JSONObject;

/**
 * WebApp<br>
 * 
 * WebApp: Run web configurator application<br><br>
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
public class WebApp {
	/**
	 * Constructor
	 */
	public WebApp() {
	}
	
	/**
	 * Start web application
	 * 
	 * @param webPort Web port
	 */
	@SuppressWarnings("unchecked")
	public void start(int webPort) {
		port(webPort);
		
		spark.Spark.staticFiles.location("/public");
    	
    	get("/", (req, res) -> "Welcome to Honne Cloud Backup !!!");
    	
    	post("/create", (req, res) -> {
    		JSONObject jRes = new JSONObject();
    		Properties jConfig = new Properties();
    		JSONObject jData = new JSONObject(req.body());
    		
    		String fileName = jData.getString("CFG_FILE");
    		jData.remove("CFG_FILE");
    		
    		if (Util.fileExists(fileName)) {
    			jRes.put("ERROR", 1);
        		jRes.put("MSG", "The file already exists");
    		}
    		else {
    			jData.keys().forEachRemaining(key -> {
    	            final Object value = jData.get((String) key);
    	            jConfig.put(key, value);
    	    	});
    			
    			Util.saveConfiguration(jConfig, fileName);
    			
    			jRes.put("ERROR", 0);
        		jRes.put("MSG", "The configuration file was created succesfully");
    		}    		
    		
    		res.type("application/json");
    		
    		return jRes.toString();
    	});
	}
}
