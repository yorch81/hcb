package com.honneservices.aws;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Properties;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * MongoDump<br>
 * 
 * MongoDump: Implements MongoDb backups with mongodump command<br><br>
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
 * @version    1.0.0, 2021-04-12
 * @author     <a href="mailto:jorge.ponce@honneservices.com">Jorge Alberto Ponce Turrubiates</a>
 */
public class MongoDump extends Backup {
	/**
	 * Properties configuration
	 */
	Properties _config = null;

	/**
	 * Constructor
	 * 
	 * @param config Properties configuration
	 */
	public MongoDump(Properties config) {
		this._config = config;
	}

	/**
	 * Connect to MongoDb Server
	 */
	private void connect() {
		String dbName = this._config.getProperty("DB_NAME");
		String dbHost = this._config.getProperty("DB_HOSTNAME");
		String user = this._config.getProperty("DB_USER");
		String pwd = this._config.getProperty("DB_PASSWORD");
		String port = this._config.getProperty("DB_PORT");
		
		String url = "mongodb://" + user + ":" + pwd + "@" + dbHost + ":" + port + "/" + dbName;
    	
    	try {
			MongoClient mc = new MongoClient(new MongoClientURI(url));
			mc = null;
			this.connected = true;
		} catch (UnknownHostException e) {
			this.connected = false;
			HCBLog.print("ERROR", e.getMessage());
		}
	}
	
	/**
	 * Makes directory
	 * 
	 * @param dirPath Directory path
	 * @return boolean
	 */
	private boolean mkDir(String dirPath) {
		File dir = new File(dirPath);
		
		return dir.mkdir();
	}
	
	/**
	 * Execute backup with mongodump<br><br>
	 * 
	 * Notes: mongodump must be accessible<br><br>
	 * 
	 * @param fileName Backup file name 
	 * @return boolean
	 */
	@Override
	public boolean backup(String fileName) {
		this.connect();
		
		if (!this.isConnected()) {
			HCBLog.print("INFO", "Could not connect to MongoDb");
			return false;
		}
		
		String dbName = this._config.getProperty("DB_NAME");
		String dbHost = this._config.getProperty("DB_HOSTNAME");
		String user = this._config.getProperty("DB_USER");
		String pwd = this._config.getProperty("DB_PASSWORD");
		String port = this._config.getProperty("DB_PORT");
		
		String mgDir = this._config.getProperty("BACKUP_DIR") + "mg_" + Util.getDateTime();
		
		this.mkDir(mgDir);
		
		String mgFile = this._config.getProperty("BACKUP_DIR") + fileName;
		
		StringBuffer command = new StringBuffer("mongodump");
		
		command.append(" --host=");
		command.append(dbHost);
		command.append(" --port=");
		command.append(port);
		command.append(" --username=");
		command.append(user);
		command.append(" --db=");
		command.append(dbName);
		command.append(" --password=\"");
		command.append(pwd);
		command.append("\"");
		command.append(" --out=");
		command.append(mgDir);
		
		String c = command.toString();
		Util.runCommand(c);
		
		// Get Storage Type
		int type = Integer.valueOf(this._config.getProperty("STORAGE_TYPE")).intValue();
		Uploader u = new Uploader(type, this._config);
		int purge = Integer.valueOf(this._config.getProperty("PURGE")).intValue();
				
		try {    		
    		new ZipFile(mgFile).addFolder(new File(mgDir));
    		
    		u.upload(mgFile, fileName);
    		
    		if (purge == 1) {
    			Util.purge(mgFile);
    			Util.purge(mgDir);
    		}
		} catch (ZipException e) {
			HCBLog.print("ERROR", e.getMessage());
			return false;
		}
		
		return true;
	}
}
