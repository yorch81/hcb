package com.honneservices.aws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * PgDump<br>
 * 
 * PgDump: Implements Oracle backups with pg_dump command<br><br>
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
public class PgDump extends Backup {
	/**
	 * Properties configuration
	 */
	Properties _config = null;
	
	/**
	 * Constructor
	 * 
	 * @param config Properties configuration
	 */
	public PgDump(Properties config) {
		this._config = config;
	}
	
	/**
	 * Connect to PostgreSQL
	 */
	private void connect() {
		String dbName = this._config.getProperty("DB_NAME");
		String dbHost = this._config.getProperty("DB_HOSTNAME");
		String user = this._config.getProperty("DB_USER");
		String pwd = this._config.getProperty("DB_PASSWORD");
		String port = this._config.getProperty("DB_PORT");
		
    	String url = "jdbc:postgresql://" + dbHost + ":" + port + "/" + dbName;
    	
    	Properties props = new Properties();
    	props.setProperty("user", user);
    	props.setProperty("password", pwd);
    	
    	try {
    		Class.forName("org.postgresql.Driver");

			Connection conn = DriverManager.getConnection(url, props);
			
			this.connected = true;
			
			conn.close();
		} catch (ClassNotFoundException e) {
			HCBLog.print("ERROR", e.getMessage());
			this.connected = false;
		} catch (SQLException e) {
			HCBLog.print("ERROR", e.getMessage());
			this.connected = false;
		}
	}
	
	/**
	 * Execute backup with pg_dump<br><br>
	 * 
	 * Notes: pg_dump must be accessible<br><br>
	 * 
	 * @param fileName Backup file name 
	 * @return boolean
	 */
	@Override
	public boolean backup(String fileName) {
		this.connect();
		
		if (!this.isConnected()) {
			HCBLog.print("INFO", "Could not connect to PostgreSQL");
			return false;
		}
		
		StringBuffer command = new StringBuffer("pg_dump");
		
		if (System.getProperty("os.name").contains("Windows"))
			command.append(".exe");
		
		String dbName = this._config.getProperty("DB_NAME");
		String dbHost = this._config.getProperty("DB_HOSTNAME");
		String user = this._config.getProperty("DB_USER");
		String pwd = this._config.getProperty("DB_PASSWORD");
		String port = this._config.getProperty("DB_PORT");
		
		String tarFile = this._config.getProperty("BACKUP_DIR") + fileName;
		
		command.append(" --dbname=postgresql://");
		command.append(user);
		command.append(":");
		command.append(pwd);
		command.append("@");
		command.append(dbHost);
		command.append(":");
		command.append(port);
		command.append("/");
		command.append(dbName);
		command.append(" -F t ");
		command.append(" > ");
		command.append(tarFile);
		
		String c = command.toString();
		Util.runCommand(c);
		
		// Get Storage Type
		int type = Integer.valueOf(this._config.getProperty("STORAGE_TYPE")).intValue();
		Uploader u = new Uploader(type, this._config);
		int purge = Integer.valueOf(this._config.getProperty("PURGE")).intValue();
		
		u.upload(tarFile, fileName);
		
		if (purge == 1) {
			Util.purge(tarFile);
		}
		
		return true;
	}
}
