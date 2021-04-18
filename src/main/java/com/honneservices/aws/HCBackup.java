package com.honneservices.aws;

import java.util.Properties;

/**
 * HCBackup<br>
 * 
 * HCBackup: Factory for backup type<br><br>
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
public class HCBackup {
	/**
	 * Backup
	 */
	private Backup backup = null;
	
	/**
	 * Backup name
	 */
	private String backupName = "";
	
	/**
	 * Factory constructor
	 * 
	 * @param config Properties configuration
	 */
	public HCBackup(Properties config) {
		int type = Integer.valueOf(config.getProperty("BACKUP_TYPE")).intValue();;
		
		switch (type) {
			// Zip Directory
			case 1:
				this.backup = new ZipDir(config);
				this.backupName = config.getProperty("ZIP_NAME") + "_" + Util.getDateTime() + ".zip";
				break;
			// MySQLDump
			case 2:
				this.backup = new MySQLDump(config);
				this.backupName = config.getProperty("DB_NAME") + "_" + Util.getDateTime() + ".sql";
				break;
			// MySQLPump
			case 3:
				this.backup = new MySQLPump(config);
				this.backupName = config.getProperty("DB_NAME") + "_" + Util.getDateTime() + ".sql";
				break;
			// MSSQLServer
			case 4:
				this.backup = new MSSQLServer(config);
				
				int diff = Integer.valueOf(config.getProperty("SQL_DIFFERENTIAL", "0")).intValue();
				
				if (diff == 1)
					this.backupName = config.getProperty("DB_NAME") + "_diff_" + Util.getDateTime() + ".bak";
				else
					this.backupName = config.getProperty("DB_NAME") + "_" + Util.getDateTime() + ".bak";
				break;
			// XtraBackup
			case 5:
				this.backup = new XtraBackup(config);
				this.backupName = "mysql_xb_" + Util.getDateTime() + ".zip";
				break;
			// Oracle Data Pump
			case 6:
				this.backup = new OracleExp(config);
				this.backupName = config.getProperty("DB_NAME") + "_" + Util.getDateTime() + ".dmp";
				break;
			// PgDump
			case 7: 
				this.backup = new PgDump(config);
				this.backupName = config.getProperty("DB_NAME") + "_" + Util.getDateTime() + ".tar";
				break;
			// MongoDump
			case 8:
				this.backup = new MongoDump(config);
				this.backupName = "mg_" + config.getProperty("DB_NAME") + "_" + Util.getDateTime() + ".zip";
				break;
			default:
				HCBLog.print("ERROR", "Backup type is not defined");
		}
	}
	
	/**
	 * Execute backup
	 * 
	 * @param fileName Backup file name
	 * @return boolean
	 */
	public boolean backup(String fileName) {
		return this.backup.backup(fileName);
	}
	
	/**
	 * Execute backup
	 * 
	 * @return boolean
	 */
	public boolean backup() {
		return this.backup.backup(this.backupName);
	}
}
