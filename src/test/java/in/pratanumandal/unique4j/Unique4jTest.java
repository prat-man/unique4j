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

import in.pratanumandal.unique4j.exception.Unique4jException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Unique4jTest {
	
	public static final String BASE_APP_ID = "in.pratanumandal.unique4j-mlsdvo-20191511-#j.6";
	public static final AtomicInteger APP_ID_COUNT = new AtomicInteger();

	@Test
	public void testUnique4jBasic() throws Unique4jException {
		final String APP_ID = BASE_APP_ID + "-" + APP_ID_COUNT.getAndIncrement();

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

		try {
			// try to obtain lock
			unique.acquireLock();
		} finally {
			// try to free the lock before exiting program
			unique.releaseLock();
		}
	}
	
	@Test
	public void testUnique4j() throws Unique4jException {
		final String APP_ID = BASE_APP_ID + "-" + APP_ID_COUNT.getAndIncrement();
		
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

		try {
			// try to obtain lock
			unique1.acquireLock();

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
		} finally {
			// try to free the locks before exiting program
			unique1.releaseLock();

			unique2.releaseLock();
		}
	}
	
	@Test
	public void testUnique4jEmpty() throws Unique4jException {
		final String APP_ID = BASE_APP_ID + "-" + APP_ID_COUNT.getAndIncrement();

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

		try {
			// try to obtain lock
			unique1.acquireLock();

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
		} finally {
			// try to free the locks before exiting program
			unique1.releaseLock();

			unique2.releaseLock();
		}
	}
	
	@Test
	public void testUnique4jNull1() throws Unique4jException {
		final String APP_ID = BASE_APP_ID + "-" + APP_ID_COUNT.getAndIncrement();
		
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

		try {
			// try to obtain lock
			unique1.acquireLock();

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
		} finally {
			// try to free the locks before exiting program
			unique1.releaseLock();

			unique2.releaseLock();
		}
	}
	
	@Test
	public void testUnique4jNull2() throws Unique4jException {
		final String APP_ID = BASE_APP_ID + "-" + APP_ID_COUNT.getAndIncrement();
		
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

		try {
			// try to obtain lock
			unique1.acquireLock();

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
		} finally {
			// try to free the locks before exiting program
			unique1.releaseLock();

			unique2.releaseLock();
		}
	}
	
	@Test
	public void testUnique4jNewline1() throws Unique4jException {
		final String APP_ID = BASE_APP_ID + "-" + APP_ID_COUNT.getAndIncrement();
		
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

		try {
			// try to obtain lock
			unique1.acquireLock();

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
		} finally {
			// try to free the locks before exiting program
			unique1.releaseLock();

			unique2.releaseLock();
		}
	}
	
	@Test
	public void testUnique4jNewline2() throws Unique4jException {
		final String APP_ID = BASE_APP_ID + "-" + APP_ID_COUNT.getAndIncrement();
		
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

		try {
			// try to obtain lock
			unique1.acquireLock();

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
		} finally {
			// try to free the locks before exiting program
			unique1.releaseLock();

			unique2.releaseLock();
		}
	}
	
	@Test
	public void testSubsequentAcquireLock() throws Unique4jException {
		final String APP_ID = BASE_APP_ID + "-" + APP_ID_COUNT.getAndIncrement();

		Unique4j first = null, second = null;
		try {
			// first instance
			first = initializeUnique4j(APP_ID);

			// second instance
			second = initializeUnique4j(APP_ID);

			// release lock for last instance only
			second.releaseLock();
		} catch (Unique4jException t) {
			// If anything fails, clean the lock
			if(first != null)
				first.releaseLock();

			if(second != null)
				second.releaseLock();

			throw t;
		}
	}
	
	private Unique4j initializeUnique4j(String APP_ID) throws Unique4jException {
		
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
