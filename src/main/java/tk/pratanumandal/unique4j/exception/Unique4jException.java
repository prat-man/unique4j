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

package tk.pratanumandal.unique4j.exception;

/**
 * The <code>Unique4jException</code> class is a wrapper for all exceptions thrown from Unique4j.
 * 
 * @author Pratanu Mandal
 * @since 1.1
 *
 */
public class Unique4jException extends Exception {
	
	private static final long serialVersionUID = 268060627071973613L;

	public Unique4jException() {
		super();
	}

	public Unique4jException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public Unique4jException(String arg0) {
		super(arg0);
	}

	public Unique4jException(Throwable arg0) {
		super(arg0);
	}

}
