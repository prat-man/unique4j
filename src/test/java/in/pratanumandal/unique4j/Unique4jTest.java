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

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@RunWith(Parameterized.class)
public class Unique4jTest {

	private static final AtomicInteger APP_ID_COUNT = new AtomicInteger();
	public static String getAppId() {
		return "in.pratanumandal.unique4j-mlsdvo-20191511-#j.6-" + APP_ID_COUNT.getAndIncrement();
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ new FactoryImpl(new DynamicPortSocketIpcFactory(InetAddress.getLoopbackAddress(), 3000)) }
		});
	}

	interface Factory {

		ComposableUnique4j create(String APP_ID,
								  ComposableUnique4j.MessageHandler MESSAGE_HANDLER,
								  Consumer<Throwable> EXCEPTION_HANDLER,
								  Runnable EXIT_HANDLER);

		ComposableUnique4j create(String APP_ID, ComposableUnique4j.MessageHandler MESSAGE_HANDLER);
	}

	static class FactoryImpl implements Factory {

		private final IpcFactory IPC_FACTORY;

		public FactoryImpl(IpcFactory IPC_FACTORY) {
			this.IPC_FACTORY = IPC_FACTORY;
		}

		@Override
		public ComposableUnique4j create(String APP_ID,
										 ComposableUnique4j.MessageHandler MESSAGE_HANDLER,
										 Consumer<Throwable> EXCEPTION_HANDLER,
										 Runnable EXIT_HANDLER) {
			return new ComposableUnique4j(APP_ID, IPC_FACTORY, MESSAGE_HANDLER, EXCEPTION_HANDLER, EXIT_HANDLER);
		}

		@Override
		public ComposableUnique4j create(String APP_ID, ComposableUnique4j.MessageHandler MESSAGE_HANDLER) {
			return new ComposableUnique4j(APP_ID, IPC_FACTORY, MESSAGE_HANDLER, null, null);
		}
	}

	private final Factory factory;

	public Unique4jTest(Factory factory) {
		this.factory = factory;
	}

	@Test
	public void testUnique4jBasic() throws Unique4jException {
		final String APP_ID = getAppId();

		Unique4j unique = factory.create(
				APP_ID,
				new ComposableUnique4j.MessageHandler() {
					@Override
					public String sendMessage() {
						// send null
						return null;
					}

					@Override
					public void receiveMessage(String arg0) {
						// do nothing
					}
				},
				t -> {},
				() -> {});

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
		final String APP_ID = getAppId();
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final String message = "ijvnfpp389528$#$@520sdf.213sgv8";

		Unique4j unique1 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send null
				return null;
			}

			@Override
			public void receiveMessage(String arg0) {
				// to assert on main thread
				received.add(arg0);

				// notify that message has been received
				synchronized (lock) {
					lock.notify();
				}
			}
		});
		
		Unique4j unique2 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send message
				return message;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// do nothing
			}
		});

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
		final String APP_ID = getAppId();

		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final String message = new String();
		
		Unique4j unique1 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// to assert on main thread
				received.add(arg0);
				
				// notify that message has been received
				synchronized (lock) {
					lock.notify();
				}
			}
		});
		
		Unique4j unique2 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send message
				return message;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// do nothing
			}
		});

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
		final String APP_ID = getAppId();
		
		final Object lock = new Object();
		
		final List<Object> received = new ArrayList<Object>();
		
		Unique4j unique1 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// to assert on main thread
				received.add(arg0);
				
				// notify that message has been received
				synchronized (lock) {
					lock.notify();
				}
			}
		});
		
		Unique4j unique2 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// do nothing
			}
		});

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
		final String APP_ID = getAppId();
		
		final Object lock = new Object();
		
		final List<Object> received = new ArrayList<Object>();
		
		Unique4j unique1 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// to assert on main thread
				received.add(arg0);
				
				// notify that message has been received
				synchronized (lock) {
					lock.notify();
				}
			}
		});
		
		Unique4j unique2 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send null as a string
				return "null";
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// do nothing
			}
		});

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
		final String APP_ID = getAppId();
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final String message = "hello\nworld";
		
		Unique4j unique1 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// to assert on main thread
				received.add(arg0);
				
				// notify that message has been received
				synchronized (lock) {
					lock.notify();
				}
			}
		});
		
		Unique4j unique2 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send message
				return message;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// do nothing
			}
		});

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
		final String APP_ID = getAppId();
		
		final Object lock = new Object();
		
		final List<String> received = new ArrayList<String>();
		
		final String message = "hello\r\nworld";
		
		Unique4j unique1 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// to assert on main thread
				received.add(arg0);
				
				// notify that message has been received
				synchronized (lock) {
					lock.notify();
				}
			}
		});
		
		Unique4j unique2 = factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send message
				return message;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// do nothing
			}
		});

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
		final String APP_ID = getAppId();

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

		final AtomicReference<Unique4j> uniqueRef = new AtomicReference<>();
		uniqueRef.set(factory.create(APP_ID, new ComposableUnique4j.MessageHandler() {
			@Override
			public String sendMessage() {
				// send messages
				return null;
			}

			@Override
			public void receiveMessage(String message) {
				try {
					// release lock on first instance
					boolean lockReleased = uniqueRef.get().releaseLock();
					
					// assert if lock has been released
					Assert.assertEquals(true, lockReleased);
				} catch (Unique4jException e) {
					e.printStackTrace();
				}
			}
		}));
		Unique4j unique = uniqueRef.get();
		
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
