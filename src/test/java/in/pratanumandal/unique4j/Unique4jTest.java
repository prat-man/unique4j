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

package in.pratanumandal.unique4j;

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

import in.pratanumandal.unique4j.exception.Unique4jException;

public class Unique4jTest {
	
	private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");
	
	private static final String APP_ID = "in.pratanumandal.unique4j-mlsdvo-20191511-#j.6";
	
	@Test
	public void testUnique4jBasic() throws Unique4jException {
		
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
		unique.releaseLock();
		
	}
	
	@Test
	public void testUnique4j() throws Unique4jException {
		
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
		Assert.assertEquals(message, received.get(0));
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jEmpty() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final String message = new String();
		
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
		Assert.assertEquals(message, received.get(0));
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jNull1() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<Object> received = new ArrayList<Object>();
		
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
				// send null
				return null;
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
		Assert.assertNull(received.get(0));
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jNull2() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<Object> received = new ArrayList<Object>();
		
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
				// send null as a string
				return "null";
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
		Assert.assertNotNull(received.get(0));
		Assert.assertEquals("null", received.get(0));
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jNewline1() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final String message = "hello\nworld";
		
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
		Assert.assertEquals(message, received.get(0));
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jNewline2() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final String message = "hello\r\nworld";
		
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
		Assert.assertEquals(message, received.get(0));
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jList() throws Unique4jException {
		
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
		Assert.assertEquals(messageList, received);
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jListNewline() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final List<String> messageList = new ArrayList<String>();
		messageList.add("C:\\Users\\Pratanu\nMandal\\Desktop\\rptMrExam.pdf");
		messageList.add("C:\\Users\\Pratanu Mandal\\Desktop\\nptMrExam.pdf");
		messageList.add("C:\\Users\\Pratanu Mandal\\Desktop\\'rptMrExam.pdf");
		messageList.add("C:\\Users\\Pratanu Mandal\\Desktop\\rptMrExam.pdf");
		
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
		Assert.assertEquals(messageList, received);
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jListNull() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<Object> received = new ArrayList<Object>();
		
		Unique4j unique1 = new Unique4jList(APP_ID, false) {
			@Override
			protected List<String> sendMessageList() {
				// send null
				return null;
			}

			@Override
			protected void receiveMessageList(List<String> message) {
				// to assert on main thread
				received.add(message);
				
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
				// send null
				return null;
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
		Assert.assertNull(received.get(0));
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jMap() throws Unique4jException {
		
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
		Assert.assertEquals(messageMap, received);
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jMapNewline() throws Unique4jException {
		
		final Object lock = new Object();
		
		final Map<String, String> received = new HashMap<String, String>();
		
		final Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put("23vvf1", "C:\\Users\\Pratanu\nMandal\\Desktop\\rptMrExam.pdf");
		messageMap.put("23vvf2", "C:\\Users\\Pratanu Mandal\\Desktop\\nptMrExam.pdf");
		messageMap.put("23vvf3", "C:\\Users\\Pratanu Mandal\\Desktop\\'rptMrExam.pdf");
		messageMap.put("23vvf4", "C:\\Users\\Pratanu Mandal\\Desktop\\rptMrExam.pdf");
		
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
		Assert.assertEquals(messageMap, received);
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testUnique4jMapNull() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<Object> received = new ArrayList<Object>();
		
		Unique4j unique1 = new Unique4jMap(APP_ID, false) {
			@Override
			protected Map<String, String> sendMessageMap() {
				// send null
				return null;
			}

			@Override
			protected void receiveMessageMap(Map<String, String> message) {
				// to assert on main thread
				received.add(message);
				
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
				// send null
				return null;
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
		Assert.assertNull(received.get(0));
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
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
		unique.releaseLock();
		
	}
	
	@Test
	public void testCustomPortStatic() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final String message = "ijvnfpp389528$#$@520sdf.213sgv8";
		
		Unique4j unique1 = new Unique4j(APP_ID, false, 8080, PortPolicy.STATIC) {
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
		
		Unique4j unique2 = new Unique4j(APP_ID, false, 8080, PortPolicy.STATIC) {
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
		Assert.assertEquals(message, received.get(0));
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testCustomPortDynamic() throws Unique4jException {
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final String message = "ijvnfpp389528$#$@520sdf.213sgv8";
		
		Unique4j unique1 = new Unique4j(APP_ID, false, 8080, PortPolicy.DYNAMIC) {
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
		
		Unique4j unique2 = new Unique4j(APP_ID, false, 8080, PortPolicy.DYNAMIC) {
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
		Assert.assertEquals(message, received.get(0));
		
		// try to free the locks before exiting program
		unique1.releaseLock();
		
		unique2.releaseLock();
		
	}
	
	@Test
	public void testSubsequentAcquireLock() throws Unique4jException {
		// first instance
		initializeUnique4j();
		
		// second instance
		Unique4j unique = initializeUnique4j();
		
		// release lock for last instance only
		unique.releaseLock();
	}
	
	private Unique4j initializeUnique4j() throws Unique4jException {
		
		Unique4j unique = new Unique4jList(APP_ID, false) {
			@Override
			protected List<String> sendMessageList() {
				// send messages
				return null;
			}

			@Override
			protected void receiveMessageList(List<String> message) {
				try {
					// release lock on first instance
					boolean lockReleased = this.releaseLock();
					
					// assert if lock has been released
					Assert.assertEquals(true, lockReleased);
				} catch (Unique4jException e) {
					e.printStackTrace();
				}
			}
		};
		
		// try to acquire lock
		boolean lockAcquired = unique.acquireLock();
		
		// failed to acquire lock, first instance had lock
		// therefore, now first instance has released lock
		// try to acquire lock again
		if (!lockAcquired) {
			lockAcquired = unique.acquireLock();
		}

		// assert if lock has been acquired
		Assert.assertEquals(true, lockAcquired);
		
		return unique;
	}
	
}
