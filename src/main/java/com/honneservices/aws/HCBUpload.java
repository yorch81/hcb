package com.honneservices.aws;

import java.io.File;

/**
 * HCBUpload<br>
 * 
 * HCBUpload: Abtract class to manage cloud storage type<br><br>
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
public abstract class HCBUpload {
	/**
	 * Abstract method to upload file
	 * 
	 * @param filePath Full file name to upload
	 * @param fileName File name in cloud storage
	 * @return boolean
	 */
	public abstract boolean upload(String filePath, String fileName);
	
	/**
	 * Determine if file exists
	 * 
	 * @param fileName File name
	 * @return boolean
	 */
	protected boolean fileExists(String fileName){
		File fileUpload = new File(fileName);
	
		return fileUpload.exists();
	}
}
