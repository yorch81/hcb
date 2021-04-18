package com.honneservices.aws;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Properties;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

/**
 * HCBAzureBlob<br>
 * 
 * HCBAzureBlob: Implements Azure Blob Storage<br><br>
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
public class HCBAzureBlob extends HCBUpload {
	/**
	 * Properties configuration
	 */
	Properties _config = null;
	
	/**
	 * Constructor
	 * 
	 * @param config Properties configuration
	 */
	public HCBAzureBlob(Properties config) {
		this._config = config;
	}

	/**
	 * Upload file to Azure Blob Storage
	 * 
	 * @param filePath Full file name to upload
	 * @param fileName File name in Azure Blob
	 * @return boolean
	 */
	@Override
	public boolean upload(String filePath, String fileName) {
		if (!(this.fileExists(filePath))) {
			HCBLog.print("ERROR", "The file " + filePath + " does not exists");
			return false;
		}
		
		try {
			String azUrl = this._config.getProperty("AZBLOB_URL");
			String azContainer = this._config.getProperty("AZBLOB_CONTAINER");
			String azPath = this._config.getProperty("AZBLOB_PATH");
			
			CloudStorageAccount account = CloudStorageAccount.parse(azUrl);
			CloudBlobClient client = account.createCloudBlobClient();
			
			CloudBlobContainer container = client.getContainerReference(azContainer);
	        CloudBlockBlob blob = container.getBlockBlobReference(azPath + fileName);
	        
	        blob.uploadFromFile(filePath);
		} catch (InvalidKeyException e) {
			HCBLog.print("ERROR", e.getMessage());
		} catch ( URISyntaxException e) {
			HCBLog.print("ERROR", e.getMessage());
		} catch (StorageException e) {
			HCBLog.print("ERROR", e.getMessage());
		} catch (IOException e) {
			HCBLog.print("ERROR", e.getMessage());
		}
		
		return true;
	}

}
