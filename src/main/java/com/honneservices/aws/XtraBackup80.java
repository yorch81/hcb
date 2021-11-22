package com.honneservices.aws;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * XtraBackup 8.0<br>
 * 
 * XtraBackup 8.0: Implements MySQL hot backups with xtrabackup command<br><br>
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
 * @version    1.0.0, 2021-11-22
 * @author     <a href="mailto:jorge.ponce@honneservices.com">Jorge Alberto Ponce Turrubiates</a>
 */
public class XtraBackup80 extends Backup {
	/**
	 * Properties configuration
	 */
	Properties _config = null;
	
	/**
	 * Constructor
	 * 
	 * @param config Properties configuration
	 */
	public XtraBackup80(Properties config) {
		this._config = config;
	}
	
	/**
	 * Connect to MySQL server
	 */
	private void connect() {
		String dbName = this._config.getProperty("DB_NAME");
		String dbHost = this._config.getProperty("DB_HOSTNAME");
		String user = this._config.getProperty("DB_USER");
		String pwd = this._config.getProperty("DB_PASSWORD");
		String port = this._config.getProperty("DB_PORT");
		
		/*
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://" + dbHost + ":" + port + "/" + dbName;
			
			Connection conn = (Connection) DriverManager.getConnection(url, user, pwd);
			
			conn.close();
			this.connected = true;
		} catch (ClassNotFoundException e) {
			HCBLog.print("ERROR", e.getMessage());
			this.connected = false;
		} catch (SQLException e) {
			HCBLog.print("ERROR", e.getMessage());
			this.connected = false;
		}
		*/
		
		this.connected = true;
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
	 * Execute backup with xtrabackup<br><br>
	 * 
	 * Notes: xtrabackup must be accessible<br><br>
	 * 
	 * @param fileName Backup file name 
	 * @return boolean
	 */
	@Override
	public boolean backup(String fileName) {
		this.connect();
		
		if (!this.isConnected()) {
			HCBLog.print("INFO", "Could not connect to MySQL");
			return false;
		}
		
		StringBuffer command = new StringBuffer("xtrabackup");
		
		String user = this._config.getProperty("DB_USER");
		String pwd = this._config.getProperty("DB_PASSWORD");
		
		String xbDir = this._config.getProperty("BACKUP_DIR") + "xb_" + Util.getDateTime();
		
		this.mkDir(xbDir);
		
		String xbFile = this._config.getProperty("BACKUP_DIR") + fileName;
		
		command.append(" --user=");
		command.append(user);
		command.append(" --password=");
		command.append(pwd);
		command.append(" --backup --no-server-version-check --target-dir=");
		command.append(xbDir);
		
		String c = command.toString();
		Util.runCommand(c);
		
		// Get Storage Type
		int type = Integer.valueOf(this._config.getProperty("STORAGE_TYPE")).intValue();
		Uploader u = new Uploader(type, this._config);
		int purge = Integer.valueOf(this._config.getProperty("PURGE")).intValue();
				
		try {    		
    		new ZipFile(xbFile).addFolder(new File(xbDir));
    		
    		u.upload(xbFile, fileName);
    		
    		if (purge == 1) {
    			Util.purge(xbFile);
    			Util.purge(xbDir);
    		}
		} catch (ZipException e) {
			HCBLog.print("ERROR", e.getMessage());
			return false;
		}
		
		return true;
	}

}

