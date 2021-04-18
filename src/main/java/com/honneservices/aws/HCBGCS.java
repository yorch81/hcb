package com.honneservices.aws;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/**
 * HCBGCS<br>
 * 
 * HCBGCS: Implements Google Cloud Storage<br><br>
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
public class HCBGCS extends HCBUpload {
	/**
	 * Properties configuration
	 */
	Properties _config = null;
	
	/**
	 * Constructor
	 * 
	 * @param config Properties configuration
	 */
	public HCBGCS(Properties config) {
		this._config = config;
	}
	
	/**
	 * Upload file to Google Cloud Storage
	 * 
	 * @param filePath Full file name to upload
	 * @param fileName File name in Google Cloud Storage
	 * @return boolean
	 */
	@Override
	public boolean upload(String filePath, String fileName) {
		if (!(this.fileExists(filePath))) {
			HCBLog.print("ERROR", "The file " + filePath + " does not exists");
			return false;
		}
		
		try {
    		String credentialsFile = this._config.getProperty("GCS_AUTHFILE");
			Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsFile));
			
			String projectId = this._config.getProperty("GCS_PROJECT");
			
			Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
			
			String gcsPath = this._config.getProperty("GCS_PATH");
			
			String bucketName = this._config.getProperty("GCS_BUCKET");
			
			String objectName = gcsPath + fileName;
			BlobId blobId = BlobId.of(bucketName, objectName);
		    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
		    
		    storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
		} catch (FileNotFoundException e) {
			HCBLog.print("ERROR", e.getMessage());
		} catch (IOException e) {
			HCBLog.print("ERROR", e.getMessage());
		}
		
		return true;
	}

}
