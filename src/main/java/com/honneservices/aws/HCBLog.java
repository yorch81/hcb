package com.honneservices.aws;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * HCBLog<br>
 * 
 * HCBLog: Print application log and send error log to Honne Support<br><br>
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
public class HCBLog {
	/**
	 * Web service URL
	 */
	private static String url = "";
	
	/**
	 * Web service key
	 */
	private static String key = "";
	
	/**
	 * Honne Support mail
	 */
	private static String support = "";
	
	/**
	 * Default subject
	 */
	private static String subject = "HCB error alert";
	
	/**
	 * Print error message
	 * 
	 * @param errorType Error type (if type is ERROR send the exception message to Honne Support)
	 * @param errorMessage Error message
	 */
	public static void print(String errorType, String errorMessage) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		
		StringBuffer b = new StringBuffer();
		
		b.append(date);
		b.append(" - ");
		b.append(errorType);
		b.append(" - ");
		b.append(errorMessage);
	
		System.out.println(b.toString());
		
		// Send mail to support
		if (errorType.equals("ERROR")) {
			HonneMail mail = new HonneMail(HCBLog.url, HCBLog.key);
			
			String body = Util.getNotification() + ": " + errorMessage;
			
			mail.send(HCBLog.support, "", HCBLog.subject, body);
		}
	}
}
