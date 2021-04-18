package com.honneservices.aws;

import java.io.File;

/**
 * Backup<br>
 * 
 * Backup: Abtract class to manage backup type<br><br>
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
public abstract class Backup {
	/**
	 * Connected to database
	 */
	protected boolean connected = false;

	/**
	 * Abstract method for backup
	 * 
	 * @param fileName Backup file name 
	 * @return boolean
	 */
	public abstract boolean backup (String fileName);
	
	/**
	 * Determine if connected to database
	 * 
	 * @return boolean
	 */
	protected boolean isConnected() {
		return connected;
	}
	
	/**
	 * Determine if file exists
	 * 
	 * @param fileName Backup file name
	 * @return boolean
	 */
	protected boolean fileExists(String fileName){
		File fileUpload = new File(fileName);
	
		return fileUpload.exists();
	}
}
