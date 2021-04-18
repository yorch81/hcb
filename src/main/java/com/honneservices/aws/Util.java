package com.honneservices.aws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

/**
 * Util<br>
 * 
 * Util: Honne Cloud Backup utils<br><br>
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
public class Util {
	/**
	 * Notification message
	 */
	@SuppressWarnings("unused")
	private static String notification = "";
	
	/**
	 * Gets datetime for backup file name
	 * @return String
	 */
	public static String getDateTime() {
		String pattern = "yyyyMMdd_HHmmss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		
		return date;
	}
	
	/**
	 * Purge backup file
	 * 
	 * @param fileName Backups file name
	 */
	public static void purge(String fileName) {
		File f = new File(fileName);
		
		try {
			if (f.isDirectory())
				FileUtils.deleteDirectory(f);
			else
				f.delete();
		}
		catch (IOException e) {
			HCBLog.print("ERROR", e.getMessage());
		}
	}
	
	/**
	 * Execute OS command
	 * 
	 * @param command OS command
	 * @return int
	 */
	public static int runCommand(String command) {
	    int returnValue = -1;
	    
	    try {
	    	String[] aCommand; 
			
			if (System.getProperty("os.name").contains("Windows"))
				aCommand = new String[]{"cmd.exe","/c",command};
			else
				aCommand = new String[]{"/bin/bash","-c",command};
			
	        Process process = Runtime.getRuntime().exec(aCommand);
	        process.waitFor();
	        returnValue = process.exitValue();
	    } catch (InterruptedException e) {
	    	HCBLog.print("ERROR", e.getMessage());
	    } catch (IOException e) {
	    	HCBLog.print("ERROR", e.getMessage());
	    }

	    return returnValue;
	}
	
	/**
	 * Sets notification message
	 * 
	 * @param notification Notification message
	 */
	public static void setNotification(String notification) {
		Util.notification = notification;
	}
	
	/**
	 * Gets notification message
	 * 
	 * @return String
	 */
	public static String getNotification() {
		return Util.notification;
	}
	
	/**
	 * Save properties configuration file
	 * 
	 * @param config Properties configuration
	 * @param filePath Full file name
	 */
	public static void saveConfiguration(Properties config, String filePath) {
		try {
			FileOutputStream propFile = new FileOutputStream(filePath);
			
			config.store(propFile, null);

			if(propFile != null){
				propFile.close();
			}
		} catch (FileNotFoundException e) {
			HCBLog.print("ERROR", e.getMessage());
		} catch (IOException e) {
			HCBLog.print("ERROR", e.getMessage());
		}
	}
	
	/**
	 * Gets configuration as Properties
	 * 
	 * @param filePath Full file name
	 * @return Properties
	 */
	public static Properties getConfiguration(String filePath) {
		Properties config = new Properties();
		
		try {
			config.load(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			HCBLog.print("ERROR", e.getMessage());
		} catch (IOException e) {
			HCBLog.print("ERROR", e.getMessage());
		}
		
		return config;
	}
	
	/**
	 * Determine if file exists
	 * 
	 * @param filePath Full file name
	 * @return boolean
	 */
	public static boolean fileExists(String filePath) {
		File fileName = new File(filePath);
		
		return fileName.exists();
	}
}
