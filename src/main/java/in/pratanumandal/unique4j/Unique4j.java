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

import java.io.*;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import in.pratanumandal.unique4j.exception.Unique4jException;

/**
 * The <code>Unique4j</code> class is the primary logical entry point to the library.<br>
 * It allows to create an application lock or free it and send and receive messages between first and subsequent instances.<br><br>
 *
 * <pre>
 *	// unique application ID
 *	String APP_ID = "in.pratanumandal.unique4j-mlsdvo-20191511-#j.6";
 *
 *	// create Unique4j instance
 *	Unique4j unique = new Unique4j(APP_ID) {
 *	&nbsp;&nbsp;&nbsp;&nbsp;&#64;Override
 *	&nbsp;&nbsp;&nbsp;&nbsp;protected void receiveMessage(String message) {
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// print received message (timestamp)
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(message);
 *	&nbsp;&nbsp;&nbsp;&nbsp;}
 *	&nbsp;&nbsp;&nbsp;&nbsp;
 *	&nbsp;&nbsp;&nbsp;&nbsp;&#64;Override
 *	&nbsp;&nbsp;&nbsp;&nbsp;protected String sendMessage() {
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// send timestamp as message
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Timestamp ts = new Timestamp(new Date().getTime());
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return "Another instance launch attempted: " + ts.toString();
 *	&nbsp;&nbsp;&nbsp;&nbsp;}
 *	};
 *
 *	// try to obtain lock
 *	try {
 *	&nbsp;&nbsp;&nbsp;&nbsp;unique.acquireLock();
 *	} catch (Unique4jException e) {
 *	&nbsp;&nbsp;&nbsp;&nbsp;e.printStackTrace();
 *	}
 *
 *	...
 *
 *	// try to free the lock before exiting program
 *	try {
 *	&nbsp;&nbsp;&nbsp;&nbsp;unique.freeLock();
 *	} catch (Unique4jException e) {
 *	&nbsp;&nbsp;&nbsp;&nbsp;e.printStackTrace();
 *	}
 * </pre>
 *
 * @author Pratanu Mandal
 * @since 1.3
 *
 */
public abstract class Unique4j {

	// starting position of port check
	private static final int PORT_START = 3000;

	// system temporary directory path
	private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

	/**
	 * Unique string representing the application ID.<br><br>
	 *
	 * The APP_ID must be as unique as possible.
	 * Avoid generic names like "my_app_id" or "hello_world".<br>
	 * A good strategy is to use the entire package name (group ID + artifact ID) along with some random characters.
	 */
	public final String APP_ID;

	// auto exit from application or not
	private final boolean AUTO_EXIT;

	private final IpcFactory IPC_FACTORY;

	// lock server socket
	private IpcServer server;

	// lock file RAF object
	private RandomAccessFile lockRAF;

	// file lock for the lock file RAF object
	private FileLock fileLock;

	/**
	 * Parameterized constructor.<br>
	 * This constructor configures to automatically exit the application for subsequent instances.<br><br>
	 *
	 * The APP_ID must be as unique as possible.
	 * Avoid generic names like "my_app_id" or "hello_world".<br>
	 * A good strategy is to use the entire package name (group ID + artifact ID) along with some random characters.
	 *
	 * @param APP_ID Unique string representing the application ID
	 */
	public Unique4j(final String APP_ID) {
		this(APP_ID, true, PORT_START, PortPolicy.DYNAMIC);
	}

	/**
	 * Parameterized constructor.<br>
	 * This constructor allows to explicitly specify the exit strategy for subsequent instances.<br><br>
	 *
	 * The APP_ID must be as unique as possible.
	 * Avoid generic names like "my_app_id" or "hello_world".<br>
	 * A good strategy is to use the entire package name (group ID + artifact ID) along with some random characters.
	 *
	 * @since 1.2
	 *
	 * @param APP_ID Unique string representing the application ID
	 * @param AUTO_EXIT If true, automatically exit the application for subsequent instances
	 */
	public Unique4j(final String APP_ID, final boolean AUTO_EXIT) {
		this(APP_ID, AUTO_EXIT, PORT_START, PortPolicy.DYNAMIC);
	}

	/**
	 * Parameterized constructor.<br>
	 * This constructor allows to explicitly specify the exit strategy for subsequent instances.<br><br>
	 *
	 * The APP_ID must be as unique as possible.
	 * Avoid generic names like "my_app_id" or "hello_world".<br>
	 * A good strategy is to use the entire package name (group ID + artifact ID) along with some random characters.
	 *
	 * @since 1.5
	 *
	 * @param APP_ID Unique string representing the application ID
	 * @param PORT Port (or starting port in case of dynamic port policy) of the server socket
	 * @param PORT_POLICY Port policy to use - STATIC or DYNAMIC
	 */
	public Unique4j(final String APP_ID, final int PORT, final PortPolicy PORT_POLICY) {
		this(APP_ID, true, PORT, PORT_POLICY);
	}

	/**
	 * Parameterized constructor.<br>
	 * This constructor allows to explicitly specify the exit strategy for subsequent instances.<br><br>
	 *
	 * The APP_ID must be as unique as possible.
	 * Avoid generic names like "my_app_id" or "hello_world".<br>
	 * A good strategy is to use the entire package name (group ID + artifact ID) along with some random characters.
	 *
	 * @since 1.5
	 *
	 * @param APP_ID Unique string representing the application ID
	 * @param AUTO_EXIT If true, automatically exit the application for subsequent instances
	 * @param PORT Port (or starting port in case of dynamic port policy) of the server socket
	 * @param PORT_POLICY Port policy to use - STATIC or DYNAMIC
	 */
	public Unique4j(final String APP_ID, final boolean AUTO_EXIT, final int PORT, final PortPolicy PORT_POLICY) {
		this(APP_ID, AUTO_EXIT, PORT_POLICY == PortPolicy.DYNAMIC ?
				new DynamicPortSocketIpcFactory(InetAddress.getLoopbackAddress(), PORT) :
				new StaticPortSocketIpcFactory(InetAddress.getLoopbackAddress(), PORT));
	}

	/**
	 * Parameterized constructor.<br>
	 * This constructor allows to explicitly specify the exit strategy for subsequent instances.<br><br>
	 *
	 * The APP_ID must be as unique as possible.
	 * Avoid generic names like "my_app_id" or "hello_world".<br>
	 * A good strategy is to use the entire package name (group ID + artifact ID) along with some random characters.
	 *
	 * @since 1.5
	 *
	 * @param APP_ID Unique string representing the application ID
	 * @param AUTO_EXIT If true, automatically exit the application for subsequent instances
	 * @param IPC_FACTORY
	 */
	public Unique4j(final String APP_ID, final boolean AUTO_EXIT, final IpcFactory IPC_FACTORY) {
		this.APP_ID = APP_ID;
		this.AUTO_EXIT = AUTO_EXIT;
		this.IPC_FACTORY = IPC_FACTORY;
	}

	/**
	 * Try to obtain lock. If not possible, send data to first instance.
	 *
	 * @deprecated Use <code>acquireLock()</code> instead.
	 * @throws Unique4jException throws Unique4jException if it is unable to start a server or connect to server
	 */
	@Deprecated
	public void lock() throws Unique4jException {
		acquireLock();
	}

	/**
	 * Try to obtain lock. If not possible, send data to first instance.
	 *
	 * @since 1.2
	 *
	 * @return true if able to acquire lock, false otherwise
	 * @throws Unique4jException throws Unique4jException if it is unable to start a server or connect to server
	 */
	public boolean acquireLock() throws Unique4jException {
		// try to lock file
		final boolean locked = tryWriteLockingFile();
		if (locked) {
			// locked file, we are the first to arrive
			// try to start server
			startServer();
		}
		else {
			// couldn't lock file, we are not the first instance
			// try to start client
			doClient();
		}
		return locked;
	}

	// start the server
	private void startServer() throws Unique4jException {
		// try to start the server
		try {
			server = IPC_FACTORY.createIpcServer(new File(TEMP_DIR), APP_ID);
		} catch (IOException e) {
			throw new Unique4jException(e);
		}

		// server created successfully; this is the first instance
		// keep listening for data from other instances
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (!server.isClosed()) {
					try {
						// establish connection
						final IpcClient client = server.accept();

						// handle socket on a different thread to allow parallel connections
						Thread thread = new Thread() {
							@Override
							public void run() {
								try {
									// open writer
									OutputStream os = client.getOutputStream();
									DataOutputStream dos = new DataOutputStream(os);

									// open reader
									InputStream is = client.getInputStream();
									DataInputStream dis = new DataInputStream(is);

									// read message length from client
									int length = dis.readInt();

									// read message string from client
									String message = null;
									if (length > -1) {
										byte[] messageBytes = new byte[length];
										int bytesRead = dis.read(messageBytes, 0, length);
										message = new String(messageBytes, 0, bytesRead, "UTF-8");
									}

									// write response to client
									if (APP_ID == null) {
										dos.writeInt(-1);
									}
									else {
										byte[] appId = APP_ID.getBytes("UTF-8");

										dos.writeInt(appId.length);
										dos.write(appId);
									}
									dos.flush();

									// close writer and reader
									dos.close();
									dis.close();

									// perform user action on message
									receiveMessage(message);

									// close socket
									client.close();
								} catch (IOException e) {
									handleException(new Unique4jException(e));
								}
							}
						};

						// start socket thread
						thread.start();
					} catch (IOException e) {
						if (!server.isClosed()) {
							handleException(new Unique4jException(e));
						}
					}
				}
			}
		};

		thread.start();
	}

	// do client tasks
	private void doClient() throws Unique4jException {
		// try to establish connection to server
		final IpcClient client;
		try {
			client = IPC_FACTORY.createIpcClient(new File(TEMP_DIR), APP_ID);
		} catch (IOException e) {
			// connection failed try to start server
			startServer();
			return;
		}

		// connection successful try to connect to server
		try {
			// get message to be sent to first instance
			String message = sendMessage();

			// open writer
			OutputStream os = client.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);

			// open reader
			InputStream is = client.getInputStream();
			DataInputStream dis = new DataInputStream(is);

			// write message to server
			if (message == null) {
				dos.writeInt(-1);
			}
			else {
				byte[] messageBytes = message.getBytes("UTF-8");

				dos.writeInt(messageBytes.length);
				dos.write(messageBytes);
			}

			dos.flush();

			// read response length from server
			int length = dis.readInt();

			// read response string from server
			String response = null;
			if (length > -1) {
				byte[] responseBytes = new byte[length];
				int bytesRead = dis.read(responseBytes, 0, length);
				response = new String(responseBytes, 0, bytesRead, "UTF-8");
			}

			// close writer and reader
			dos.close();
			dis.close();

			if (response != null && response.equals(APP_ID)) {
				// validation successful
				if (AUTO_EXIT) {
					// perform pre-exit tasks
					beforeExit();
					// exit this instance
					System.exit(0);
				}
			}
			else {
				// validation failed, this is the first instance
				try {
					// close socket
					client.close();
				} catch (IOException ignored) {
					// Ignored, validation failed anyway
				}

				startServer();
			}

			// close socket
			client.close();
		} catch (IOException e) {
			// close socket
			try {
				client.close();
			} catch (IOException ignored) {
				// We don't want to swallow an exception if thrown from the catch block
			}

			throw new Unique4jException(e);
		}
	}

	private File getLockFile() {
		return new File(TEMP_DIR + File.separator + APP_ID + ".lock");
	}

	// try to obtain file lock
	private boolean tryWriteLockingFile() {
		try {
			lockRAF = new RandomAccessFile(getLockFile(), "rws");
			FileChannel fc = lockRAF.getChannel();
			fileLock = fc.tryLock();
			return fileLock != null;
		} catch (IOException e) {
			return false;
		} catch (OverlappingFileLockException e) {
			return false;
		}
	}

	/**
	 * Free the lock if possible. This is only required to be called from the first instance.
	 *
	 * @deprecated Use <code>releaseLock()</code> instead.
	 * @throws Unique4jException throws Unique4jException if it is unable to stop the server or release file lock
	 */
	@Deprecated
	public void free() throws Unique4jException {
		releaseLock();
	}

	/**
	 * Free the lock if possible. This is only required to be called from the first instance.
	 *
	 * @since 1.2
	 *
	 * @deprecated Use <code>releaseLock()</code> instead.
	 * @return true if able to release lock, false otherwise
	 * @throws Unique4jException throws Unique4jException if it is unable to stop the server or release file lock
	 */
	@Deprecated
	public boolean freeLock() throws Unique4jException {
		return releaseLock();
	}

	/**
	 * Release the lock if possible. This is only required to be called from the first instance.
	 *
	 * @since 1.5
	 *
	 * @return true if able to release lock, false otherwise
	 * @throws Unique4jException throws Unique4jException if it is unable to stop the server or release file lock
	 */
	public boolean releaseLock() throws Unique4jException {
		try {
			// close server socket
			if (server != null) {
				server.close();

				// lock file path
				File file = getLockFile();

				// try to release file lock
				if (fileLock != null) {
					fileLock.release();
				}

				// try to close lock file RAF object
				if (lockRAF != null) {
					lockRAF.close();
				}

				// try to delete lock file
				if (file.exists()) {
					file.delete();
				}

				return true;
			}

			return false;
		} catch (IOException e) {
			throw new Unique4jException(e);
		}
	}

	/**
	 * Get the possible port of server socket.<br>
	 * Use this method after invoking <code>acquireLock()</code> method. The returned port is not a guarantee.<br><br>
	 *
	 * For DYNAMIC_PORT policy, it returns -1 before invoking <code>acquireLock()</code>.
	 * After invoking <code>acquireLock()</code>, it returns a possible port of the server.<br><br>
	 *
	 * For a static port policy, it always returns the static port.
	 *
	 * @since 1.5
	 *
	 * @return the port of the server socket
	 */
	public int getPort() {
		return IPC_FACTORY instanceof PortIpcFactory ? ((PortIpcFactory) IPC_FACTORY).getPort() : -1;
	}

	/**
	 * Method used in first instance to receive messages from subsequent instances.<br><br>
	 *
	 * This method is not synchronized.
	 *
	 * @param message message received by first instance from subsequent instances
	 */
	protected abstract void receiveMessage(String message);

	/**
	 * Method used in subsequent instances to send message to first instance.<br><br>
	 *
	 * It is not recommended to perform blocking (long running) tasks here. Use <code>beforeExit()</code> method instead.<br>
	 * One exception to this rule is if you intend to perform some user interaction before sending the message.<br><br>
	 *
	 * This method is not synchronized.
	 *
	 * @return message sent from subsequent instances
	 */
	protected abstract String sendMessage();

	/**
	 * Method to receive and handle exceptions occurring while first instance is listening for subsequent instances.<br><br>
	 *
	 * By default prints stack trace of all exceptions. Override this method to handle exceptions explicitly.<br><br>
	 *
	 * This method is not synchronized.
	 *
	 * @param exception exception occurring while first instance is listening for subsequent instances
	 */
	protected void handleException(Exception exception) {
		exception.printStackTrace();
	}

	/**
	 * This method is called before exiting from subsequent instances.<br><br>
	 *
	 * Override this method to perform blocking tasks before exiting from subsequent instances.<br>
	 * This method is not invoked if auto exit is turned off.<br><br>
	 *
	 * This method is not synchronized.
	 *
	 * @since 1.2
	 */
	protected void beforeExit() {}

}
