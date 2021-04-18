package com.honneservices.aws;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * MSSQLServer<br>
 * 
 * MSSQLServer: Implements SQL Server backups<br><br>
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
public class MSSQLServer extends Backup {
	/**
	 * Properties configuration
	 */
	Properties _config = null;
	
	/**
	 * SQL Server connection
	 */
	Connection conn = null;
	
	/**
	 * Constructor
	 * 
	 * @param config Properties configuration
	 */
	public MSSQLServer(Properties config) {
		this._config = config;
	}

	/**
	 * Connect to SQL Server
	 */
	private void connect() {
		String dbName = this._config.getProperty("DB_NAME");
		String dbHost = this._config.getProperty("DB_HOSTNAME");
		String user = this._config.getProperty("DB_USER");
		String pwd = this._config.getProperty("DB_PASSWORD");
		String port = this._config.getProperty("DB_PORT");
		
		String connectionUrl = "jdbc:sqlserver://" + dbHost + ":" + port + ";databaseName=" + dbName + ";user=" + user + ";password=" + pwd + ";queryTimeout=7200";

        try {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        	
        	this.conn = DriverManager.getConnection(connectionUrl);
        } catch (SQLException  e) {
        	HCBLog.print("ERROR", e.getMessage());
			this.connected = false;
        } catch (ClassNotFoundException e) {
        	HCBLog.print("ERROR", e.getMessage());
			this.connected = false;
        }
        
		this.connected = true;
	}
	
	/**
	 * Execute backup
	 * 
	 * @param fileName Backup file name 
	 * @return boolean
	 */
	@Override
	public boolean backup(String fileName) {
		this.connect();
		
		if (!this.isConnected()) {
			HCBLog.print("INFO", "Could not connect to SQL Server");
			return false;
		}
		
		String database = this._config.getProperty("DB_NAME");
		
		String backupName = this._config.getProperty("BACKUP_DIR") + fileName;
		
		if (System.getProperty("os.name").contains("Windows"))
			backupName = backupName.replace("/", "\\");
		
		int compression = Integer.valueOf(this._config.getProperty("SQL_COMPRESSION", "0")).intValue();
		int diff = Integer.valueOf(this._config.getProperty("SQL_DIFFERENTIAL", "0")).intValue();
		
		StringBuffer query = new StringBuffer("BACKUP DATABASE ");
		query.append(database);
		query.append(" TO  DISK = '");
		query.append(backupName);		
		query.append("' WITH NOFORMAT, INIT, NAME = N'Full Backup', SKIP, NOREWIND, NOUNLOAD,  STATS = 10");
		
		if (compression == 1)
			query.append(", COMPRESSION");
		
		if (diff == 1)
			query.append(", DIFFERENTIAL");
		
		Statement stmt = null;
		
		try {
			stmt = (Statement) this.conn.createStatement();
			stmt.execute(query.toString());
			
			this.conn.close();
			
			// Get Storage Type
			int type = Integer.valueOf(this._config.getProperty("STORAGE_TYPE")).intValue();
			Uploader u = new Uploader(type, this._config);
			int purge = Integer.valueOf(this._config.getProperty("PURGE")).intValue();
			
			u.upload(backupName, fileName);
			
			if (purge == 1)
				Util.purge(backupName);
		} catch (SQLException e) {
			HCBLog.print("ERROR", e.getMessage());
		} finally {
	    	if (this.conn != null)
				try {
					this.conn.close();
				} catch (SQLException e) {
					HCBLog.print("ERROR", e.getMessage());
				}
	    }
		
		return true;
	}

}
