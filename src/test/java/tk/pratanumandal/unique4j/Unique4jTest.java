/**
 * Copyright 2019 Pratanu Mandal
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
 */

package tk.pratanumandal.unique4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import tk.pratanumandal.unique4j.exception.Unique4jException;

public class Unique4jTest {
	
	private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
	private static final String APP_ID = "tk.pratanumandal.unique4j-mlsdvo-20191511-#j.6";
	
	@Test
	public void testUnique4j() throws Unique4jException {
		
		Unique unique = new Unique(APP_ID) {
			@Override
			public String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// do nothing
			}
			
			@Override
			public void handleException(Exception exception) {
				// do nothing
			}
			
			@Override
			public void beforeExit() {
				// do nothing
			}
		};
		
		// try to obtain lock
		unique.acquireLock();
		
		// try to free the lock before exiting program
		unique.freeLock();
		
	}
	
	@Test
	public void testCorruptedLockFile() throws Unique4jException, IOException {
		
		String filePath = TEMP_DIR + File.separator + APP_ID + ".lock";
		
		File file = new File(filePath);
		
		// if lock file exists, delete it
		if (file.exists()) {
			FileUtils.forceDelete(file);
		}

		// create a corrupted lock file
		FileUtils.writeStringToFile(file, "abcdefghi\njklmnop\n\rqrst", Charset.forName("UTF-8"));

		// create instance of Unique
		Unique unique = new Unique(APP_ID) {
			@Override
			public String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			public void receiveMessage(String message) {
				// do nothing
			}
		};

		// try to obtain lock
		unique.acquireLock();

		// try to free the lock before exiting program
		unique.freeLock();
		
	}
	
}
