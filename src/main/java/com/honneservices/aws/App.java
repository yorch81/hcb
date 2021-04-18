package com.honneservices.aws;

import java.util.Properties;

/**
 * App<br>
 * 
 * App: Main class<br><br>
 * 
 * How to use:<br><br>
 * java -jar HCB.jar<br>
 * java -jar HCB.jar start<br>
 * java -jar HCB.jar start hcb_mysql_s3.properties<br>
 * java -jar HCB.jar configure<br>
 * java -jar HCB.jar configure 8888<br><br>
 * 
 * Notes: The default properties file is hcb.properties and the default port is 10080<br><br>
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
public class App {
	/**
	 * Run the application
	 * 
	 * @param args "start" for run the application or "configure" for run web configurator
	 */
    public static void main( String[] args ) {
    	String action = "start";
    	String configFile = "hcb.properties";
    	String port = "10080";
    	
    	Properties config = new Properties();
    	
    	if (args.length == 0) {
    		config = Util.getConfiguration(configFile);
    	}
    	else {
    		action = args[0];
    		
    		if (action.equals("start")) {
    			try {
    				configFile = args[1];
    			} catch (ArrayIndexOutOfBoundsException e) {
    				configFile = "hcb.properties";
    			}
    			
    			config = Util.getConfiguration(configFile);
    		}
    		else if (action.equals("configure")) {
    			try {
    				port = args[1];
    			} catch (ArrayIndexOutOfBoundsException e) {
    				port = "10080";
    			}
    			
    			WebApp webServer = new WebApp();
    			
    			webServer.start(Integer.valueOf(port));
    			
    			HCBLog.print("INFO", "Starting configurator on http://0.0.0.0:" + port);
    			HCBLog.print("INFO", "Press CTRL + C to exit");
    		}
    		else {
    			HCBLog.print("ERROR", "Invalid option");
    			System.exit(0);
    		}
    	}
        
    	if (action.equals("start")) {
    		HCBLog.print("INFO", "Starting Honne Cloud Backup ...");
            
            try {
            	// Load Notification Message
            	String nm = config.getProperty("NOTIFICATION", "");
                Util.setNotification(nm);
                
                // Execute Backup
                HCBackup hcb = new HCBackup(config);
                hcb.backup();
            } catch (Exception e) {
            	HCBLog.print("ERROR", e.getMessage());
            }
            
            HCBLog.print("INFO", "Terminating Honne Cloud Backup");
    	}
    }
}
