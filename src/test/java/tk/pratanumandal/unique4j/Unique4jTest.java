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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import tk.pratanumandal.unique4j.exception.Unique4jException;

public class Unique4jTest {
	
	private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
	
	private static final String APP_ID = "tk.pratanumandal.unique4j-mlsdvo-20191511-#j.6";
	
	@Test
	public void testUnique4j() throws Unique4jException {
		
		Unique4j unique = new Unique4j(APP_ID) {
			@Override
			protected String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			protected void receiveMessage(String arg0) {
				// do nothing
			}
			
			@Override
			protected void handleException(Exception exception) {
				// do nothing
			}
			
			@Override
			protected void beforeExit() {
				// do nothing
			}
		};
		
		// try to obtain lock
		unique.acquireLock();
		
		// try to free the lock before exiting program
		unique.freeLock();
		
	}
	
	@Test
	public void testUnique() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final String message = "ijvnfpp389528$#$@520sdf.213sgv8";
		
		Unique4j unique1 = new Unique4j(APP_ID, false) {
			@Override
			protected String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			protected void receiveMessage(String arg0) {
				// to assert on main thread
				received.add(arg0);
				
				// notify that message has been received
				synchronized (lock) {
					lock.notify();
				}
			}
		};
		
		// try to obtain lock
		unique1.acquireLock();
		
		Unique4j unique2 = new Unique4j(APP_ID, false) {
			@Override
			protected String sendMessage() {
				// send message
				return message;
			}
			
			@Override
			protected void receiveMessage(String arg0) {
				// do nothing
			}
		};
		
		// try to obtain lock
		unique2.acquireLock();
		
		// wait until message is received
		if (received.isEmpty()) {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		// assert if message is sent correctly
		Assert.assertEquals(received.get(0), message);
		
		// try to free the locks before exiting program
		unique1.freeLock();
		
		unique2.freeLock();
		
	}
	
	@Test
	public void testUniqueList() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final List<String> messageList = new ArrayList<String>();
		messageList.add("ijvnfpp389528$#$@520sdf.213sgv6");
		messageList.add("ijvnfpp389528$#$@520sdf.213sgv7");
		messageList.add("ijvnfpp389528$#$@520sdf.213sgv8");
		messageList.add("ijvnfpp389528$#$@520sdf.213sgv9");
		
		Unique4j unique1 = new Unique4jList(APP_ID, false) {
			@Override
			protected List<String> sendMessageList() {
				// send null
				return null;
			}

			@Override
			protected void receiveMessageList(List<String> message) {
				// to assert on main thread
				received.addAll(message);
				
				// notify that message has been received
				synchronized (lock) {
					lock.notify();
				}
			}
		};
		
		// try to obtain lock
		unique1.acquireLock();
		
		Unique4j unique2 = new Unique4jList(APP_ID, false) {
			@Override
			protected List<String> sendMessageList() {
				// send message list
				return messageList;
			}

			@Override
			protected void receiveMessageList(List<String> message) {
				// do nothing
			}
		};
		
		// try to obtain lock
		unique2.acquireLock();
		
		// wait until message is received
		if (received.isEmpty()) {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		// assert if message is sent correctly
		Assert.assertEquals(received, messageList);
		
		// try to free the locks before exiting program
		unique1.freeLock();
		
		unique2.freeLock();
		
	}
	
	@Test
	public void testUniqueMap() throws Unique4jException {
		
		final Object lock = new Object();
		
		final Map<String, String> received = new HashMap<String, String>();
		
		final Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("23vvf1", "ijvnfpp389528$#$@520sdf.213sgv6");
		messageMap.put("23vvf2", "ijvnfpp389528$#$@520sdf.213sgv7");
		messageMap.put("23vvf3", "ijvnfpp389528$#$@520sdf.213sgv8");
		messageMap.put("23vvf4", "ijvnfpp389528$#$@520sdf.213sgv9");
		
		Unique4j unique1 = new Unique4jMap(APP_ID, false) {
			@Override
			protected Map<String, String> sendMessageMap() {
				// send null
				return null;
			}

			@Override
			protected void receiveMessageMap(Map<String, String> message) {
				// to assert on main thread
				received.putAll(message);
				
				// notify that message has been received
				synchronized (lock) {
					lock.notify();
				}
			}
		};
		
		// try to obtain lock
		unique1.acquireLock();
		
		Unique4j unique2 = new Unique4jMap(APP_ID, false) {
			@Override
			protected Map<String, String> sendMessageMap() {
				// send message list
				return messageMap;
			}

			@Override
			protected void receiveMessageMap(Map<String, String> message) {
				// do nothing
			}
		};
		
		// try to obtain lock
		unique2.acquireLock();
		
		// wait until message is received
		if (received.isEmpty()) {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		// assert if message is sent correctly
		Assert.assertEquals(received, messageMap);
		
		// try to free the locks before exiting program
		unique1.freeLock();
		
		unique2.freeLock();
		
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
		Unique4j unique = new Unique4j(APP_ID) {
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
