package com.honneservices.aws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * OracleExp<br>
 * 
 * OracleExp: Implements Oracle backups with expdp command<br><br>
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
public class OracleExp extends Backup {
	/**
	 * Properties configuration
	 */
	Properties _config = null;
	
	/**
	 * Constructor
	 * 
	 * @param config Properties configuration
	 */
	public OracleExp(Properties config) {
		this._config = config;
	}
	
	/**
	 * Connect to Oracle
	 */
	private void connect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			String dbHost = this._config.getProperty("DB_HOSTNAME");
			String user = this._config.getProperty("DB_USER");
			String pwd = this._config.getProperty("DB_PASSWORD");
			String port = this._config.getProperty("DB_PORT");
			String sid = this._config.getProperty("ORACLE_SID");
			
			String url = "jdbc:oracle:thin:@" + dbHost + ":" + port + ":" + sid;
			
			Connection conn = DriverManager.getConnection(url, user, pwd);
			
			conn.close();
			this.connected = true;
		} catch (ClassNotFoundException e) {
			HCBLog.print("ERROR", e.getMessage());
		} catch (SQLException e) {
			HCBLog.print("ERROR", e.getMessage());
		}
	}
	
	/**
	 * Execute backup with expdp<br><br>
	 * 
	 * Notes: expdp must be accessible<br><br>
	 * 
	 * @param fileName Backup file name 
	 * @return boolean
	 */
	@Override
	public boolean backup(String fileName) {
		this.connect();
		
		if (!this.isConnected()) {
			HCBLog.print("INFO", "Could not connect to Oracle");
			return false;
		}
		
		String dbName = this._config.getProperty("DB_NAME");
		String user = this._config.getProperty("DB_USER");
		String pwd = this._config.getProperty("DB_PASSWORD");
		String dumpDir = this._config.getProperty("ORACLE_DUMPDIR");
		String logFile = "expdp_" + Util.getDateTime() + ".log";
		int compression = Integer.valueOf(this._config.getProperty("SQL_COMPRESSION", "0")).intValue();
		
		StringBuffer command = new StringBuffer("expdp");
		
		command.append(" ");
		command.append(user);
		command.append("/");
		command.append(pwd);
		command.append(" schemas=");
		command.append(dbName);
		command.append(" dumpfile=");
		command.append(fileName);
		command.append(" directory=");
		command.append(dumpDir);
		command.append(" logfile=");
		command.append(logFile);
		
		if (compression == 1)
			command.append(" compression=all");
		
		String c = command.toString();
		Util.runCommand(c);
		
		String dumpFile = this._config.getProperty("BACKUP_DIR") + fileName;
		
		// Get Storage Type
		int type = Integer.valueOf(this._config.getProperty("STORAGE_TYPE")).intValue();
		Uploader u = new Uploader(type, this._config);
		int purge = Integer.valueOf(this._config.getProperty("PURGE")).intValue();
		
		u.upload(dumpFile, fileName);
		
		if (purge == 1)
			Util.purge(dumpFile);
					
		return true;
	}

}
