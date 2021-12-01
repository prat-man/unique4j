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

/**
 * Policy used to assign port.
 * 
 * @author Pratanu Mandal
 * @since 1.5
 */
public enum PortPolicy {
	
	/**
	 * Use static port if available; throw exception otherwise.
	 */
	STATIC,
	
	/**
	 * Dynamically assign a port based on availability.
	 */
	DYNAMIC

}
