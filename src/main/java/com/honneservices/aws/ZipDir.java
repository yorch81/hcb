package com.honneservices.aws;

import java.io.File;
import java.util.Properties;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * ZipDir<br>
 * 
 * ZipDir: Implements zip directory backup<br><br>
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
public class ZipDir extends Backup {
	/**
	 * Properties configuration
	 */
	Properties _config = null;
	
	/**
	 * Constructor
	 * 
	 * @param config Properties configuration
	 */
	public ZipDir(Properties config) {
		this._config = config;
	}

	/**
	 * Execute zip backup<br><br>
	 * 
	 * @param fileName Backup file name 
	 * @return boolean
	 */
	@Override
	public boolean backup(String fileName) {
		String bDir = this._config.getProperty("BACKUP_DIR");
		String dir = this._config.getProperty("DIR_ZIP");
		
		String zipFile = bDir + fileName;
		
		// Get Storage Type
		int type = Integer.valueOf(this._config.getProperty("STORAGE_TYPE")).intValue();
		
		Uploader u = new Uploader(type, this._config);
		
		int purge = Integer.valueOf(this._config.getProperty("PURGE")).intValue();
		
		try {    		
    		new ZipFile(zipFile).addFolder(new File(dir));
    		
    		u.upload(zipFile, fileName);
    		
    		if (purge == 1)
    			Util.purge(zipFile);
		} catch (ZipException e) {
			HCBLog.print("ERROR", e.getMessage());
			return false;
		}
		
		return true;
	}

}
