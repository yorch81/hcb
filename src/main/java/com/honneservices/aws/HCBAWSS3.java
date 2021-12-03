package com.honneservices.aws;

import java.io.File;
import java.util.Properties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

/**
 * HCBAWSS3<br>
 * 
 * HCBAWSS3: Implements AWS S3 Storage<br><br>
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
public class HCBAWSS3 extends HCBUpload {
	/**
	 * Properties configuration
	 */
	Properties _config = null;
	
	/**
	 * Constructor
	 * 
	 * @param config Properties configuration
	 */
	public HCBAWSS3(Properties config) {
		this._config = config;		
	}
	
	
	/**
	 * Upload file to AWS S3 Bucket
	 * 
	 * @param filePath Full file name to upload
	 * @param fileName File name in AWS S3
	 * @return boolean
	 */
	@Override
	public boolean upload(String filePath, String fileName) {
		if (!(this.fileExists(filePath))) {
			HCBLog.print("ERROR", "The file " + filePath + " does not exists");
			return false;
		}
		
		String awsKey = this._config.getProperty("AWS_KEY");
		String awsSecret = this._config.getProperty("AWS_SECRET");
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsKey, awsSecret);
		
		try {
			// .withForceGlobalBucketAccessEnabled(true)
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
	                .withRegion(this._config.getProperty("AWS_REGION"))
	                .build();
	                
			File f = new File(filePath);
			TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3Client).build();
			
			String bucketName = this._config.getProperty("S3_BUCKET");
			String path = this._config.getProperty("S3_PATH");
			String s3Path = path + fileName;
			
		    Upload xfer = xfer_mgr.upload(bucketName, s3Path, f);
		    
		    xfer.waitForCompletion();
		    
		    xfer_mgr.shutdownNow();
		} catch (AmazonServiceException e) {
			HCBLog.print("ERROR", e.getMessage());
	    } catch (SdkClientException e) {
	    	HCBLog.print("ERROR", e.getMessage());
	    } catch (AmazonClientException e) {
	    	HCBLog.print("ERROR", e.getLocalizedMessage());
    	} catch (InterruptedException e) {
    		HCBLog.print("ERROR", e.getLocalizedMessage());
    	}
		
		return true;
	}
}
