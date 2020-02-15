package tk.pratanumandal.unique4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The <code>Unique4jMap</code> class is a logical entry point to the library which extends the functionality of the <code>Unique4j</code> class.<br>
 * It allows to create an application lock or free it and send and receive messages between first and subsequent instances.<br><br>
 * 
 * This class is intended for passing a map of string key-value pairs instead of a single string from the subsequent instance to the first instance.<br><br>
 * 
 * <pre>
 *	// unique application ID
 *	String APP_ID = "tk.pratanumandal.unique4j-mlsdvo-20191511-#j.6";
 *	
 *	// create Unique4j instance
 *	Unique4j unique = new Unique4jMap(APP_ID) {
 *	&nbsp;&nbsp;&nbsp;&nbsp;&#64;Override
 *	&nbsp;&nbsp;&nbsp;&nbsp;protected Map&lt;String, String&gt; sendMessageMap() {
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Map&lt;String, String&gt; messageMap = new HashMap&lt;String, String&gt;();
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;messageMap.put("key1", "Message 1");
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;messageMap.put("key2", "Message 2");
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;messageMap.put("key3", "Message 3");
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;messageMap.put("key4", "Message 4");
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return messageMap;
 *	&nbsp;&nbsp;&nbsp;&nbsp;}
 *	
 *	&nbsp;&nbsp;&nbsp;&nbsp;&#64;Override
 *	&nbsp;&nbsp;&nbsp;&nbsp;protected void receiveMessageMap(Map&lt;String, String&gt; messageMap) {
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for (Entry&lt;String, String&gt; entry : messageMap.entrySet()) {
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(entry.getKey() + " : " + entry.getValue());
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}
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
public abstract class Unique4jMap extends Unique4j {
	
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
	public Unique4jMap(String APP_ID) {
		super(APP_ID);
	}
	
	/**
	 * Parameterized constructor.<br>
	 * This constructor allows to explicitly specify the exit strategy for subsequent instances.<br><br>
	 * 
	 * The APP_ID must be as unique as possible.
	 * Avoid generic names like "my_app_id" or "hello_world".<br>
	 * A good strategy is to use the entire package name (group ID + artifact ID) along with some random characters.
	 * 
	 * @param APP_ID Unique string representing the application ID
	 * @param AUTO_EXIT If true, automatically exit the application for subsequent instances
	 */
	public Unique4jMap(String APP_ID, boolean AUTO_EXIT) {
		super(APP_ID, AUTO_EXIT);
	}

	/**
	 * Internal method used in first instance to receive and parse messages from subsequent instances.<br>
	 * The use of this method directly in <code>Unique4jMap</code> is discouraged. Use <code>receiveMessageMap()</code> instead.<br><br>
	 * 
	 * This method is not synchronized.
	 * 
	 * @param message message received by first instance from subsequent instances
	 */
	@Override
	protected final void receiveMessage(String message) {
		if (message == null) {
			receiveMessageMap(null);
		}
		else {
			// parse the JSON array string into an array of string arguments
	        JsonObject jsonObj = JsonParser.parseString(message).getAsJsonObject();
	        
	        Map<String, String> stringMap = new HashMap<String, String>();
	        
	        for (Entry<String, JsonElement> entry : jsonObj.entrySet()) {
	            JsonElement element = entry.getValue();
	            stringMap.put(entry.getKey(), element.getAsString());
	        }
	        
	        // return the parsed string list
	        receiveMessageMap(stringMap);
		}
	}

	/**
	 * Internal method used in subsequent instances to parse and send message to first instance.<br>
	 * The use of this method directly in <code>Unique4jMap</code> is discouraged. Use <code>sendMessageMap()</code> instead.<br><br>
	 * 
	 * It is not recommended to perform blocking (long running) tasks here. Use <code>beforeExit()</code> method instead.<br>
	 * One exception to this rule is if you intend to perform some user interaction before sending the message.<br><br>
	 * 
	 * This method is not synchronized.
	 * 
	 * @return message sent from subsequent instances
	 */
	@Override
	protected final String sendMessage() {
		// convert arguments to JSON array string
        JsonObject jsonObj = new JsonObject();
        
        Map<String, String> stringArgs = sendMessageMap();
        
        if (stringArgs == null) return null;
        
        for (Entry<String, String> entry : stringArgs.entrySet()) {
        	jsonObj.addProperty(entry.getKey(), entry.getValue());
        }

        // return the JSON array string
        return jsonObj.toString();
	}
	
	/**
	 * Method used in first instance to receive map of messages from subsequent instances.<br><br>
	 * 
	 * This method is not synchronized.
	 * 
	 * @param messageMap map of messages received by first instance from subsequent instances
	 */
	protected abstract void receiveMessageMap(Map<String, String> messageMap);
	
	/**
	 * Method used in subsequent instances to send map of messages to first instance.<br><br>
	 * 
	 * It is not recommended to perform blocking (long running) tasks here. Use <code>beforeExit()</code> method instead.<br>
	 * One exception to this rule is if you intend to perform some user interaction before sending the message.<br><br>
	 * 
	 * This method is not synchronized.
	 * 
	 * @return map of messages sent from subsequent instances
	 */
	protected abstract Map<String, String> sendMessageMap();
	
}
