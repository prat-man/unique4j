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

package in.pratanumandal.unique4j.exception;

/**
 * The <code>Unique4jException</code> class is a wrapper for all exceptions thrown from Unique4j.
 * 
 * @author Pratanu Mandal
 * @since 1.1
 *
 */
public class Unique4jException extends Exception {
	
	private static final long serialVersionUID = 268060627071973613L;

	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public Unique4jException() {
		super();
	}
	
	/**
	 * Constructs a new exception with the specified detail message.<br>
	 * The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
	 * 
	 * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
	 */
	public Unique4jException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new exception with the specified detail message and cause.<br><br>
	 * Note that the detail message associated with cause is not automatically incorporated in this exception's detail message.
	 * 
	 * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
	 * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public Unique4jException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).<br>
	 * This constructor is useful for exceptions that are little more than wrappers for other throwables (for example, {@link java.security.PrivilegedActionException}).
	 * 
	 * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public Unique4jException(Throwable cause) {
		super(cause);
	}

}
