package com.honneservices.aws;

import java.util.Properties;

/**
 * Uploader<br>
 * 
 * Uploader: Factory to manage Cloud Storage<br><br>
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
public class Uploader {
	/**
	 * Cloud Storage uploader
	 */
	private HCBUpload uploader = null;

	/**
	 * Factory constructor
	 * 
	 * @param type Cloud Storage type
	 * @param config Properties configuration
	 */
	public Uploader(int type, Properties config) {
		switch (type) {
			// AWS S3
			case 1:
				this.uploader = new HCBAWSS3(config);
				break;
			// Azure Blob
			case 2:
				this.uploader = new HCBAzureBlob(config);
				break;
			// Google Cloud Storage
			case 3:
				this.uploader = new HCBGCS(config);
				break;
			default:
				this.uploader = new HCBAWSS3(config);
		}
	}
	
	/**
	 * Upload file to Cloud Storage
	 * 
	 * @param filePath Full file name
	 * @param fileName File name in Cloud Storage
	 * @return boolean
	 */
	public boolean upload(String filePath, String fileName) {
		return this.uploader.upload(filePath, fileName);
	}
}
