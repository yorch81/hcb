package com.honneservices.aws;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * MySQLDump<br>
 * 
 * MySQLDump: Implements MySQL backups with mysqldump command<br><br>
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
public class MySQLDump extends Backup {
	/**
	 * Properties configuration
	 */
	Properties _config = null;
	
	/**
	 * Constructor
	 * 
	 * @param config Properties configuration
	 */
	public MySQLDump(Properties config) {
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
	}
	
	/**
	 * Execute backup with mysqldump<br><br>
	 * 
	 * Notes: mysqldump must be accessible<br><br>
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
		
		StringBuffer command = new StringBuffer("mysqldump");
		
		if (System.getProperty("os.name").contains("Windows"))
			command.append(".exe");
		
		String dbName = this._config.getProperty("DB_NAME");
		String dbHost = this._config.getProperty("DB_HOSTNAME");
		String user = this._config.getProperty("DB_USER");
		String pwd = this._config.getProperty("DB_PASSWORD");
		String port = this._config.getProperty("DB_PORT");
		
		String sqlFile = this._config.getProperty("BACKUP_DIR") + fileName;
		
		command.append(" --single-transaction --routines --add-drop-table --add-drop-database -u ");
		command.append(user);
		command.append(" -p");
		command.append(pwd);
		command.append(" -h ");
		command.append(dbHost);
		command.append(" -P ");
		command.append(port);
		command.append(" ");
		command.append(dbName);
		command.append(" > ");
		command.append(sqlFile);
		
		String c = command.toString();
		Util.runCommand(c);
		
		// Get Storage Type
		int type = Integer.valueOf(this._config.getProperty("STORAGE_TYPE")).intValue();
		Uploader u = new Uploader(type, this._config);
		int purge = Integer.valueOf(this._config.getProperty("PURGE")).intValue();
				
		String zipFile = this._config.getProperty("BACKUP_DIR") + fileName + ".zip";
		
		try {    		
    		new ZipFile(zipFile).addFile(new File(sqlFile));
    		
    		u.upload(zipFile, (fileName + ".zip"));
    		
    		if (purge == 1) {
    			Util.purge(sqlFile);
    			Util.purge(zipFile);
    		}
		} catch (ZipException e) {
			HCBLog.print("ERROR", e.getMessage());
			return false;
		}
		
		return true;
	}
}
