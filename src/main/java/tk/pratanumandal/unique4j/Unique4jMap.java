package tk.pratanumandal.unique4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The <code>Unique</code> class is the logical entry point to the library.<br>
 * It allows to create an application lock or free it and send and receive messages between first and subsequent instances.<br><br>
 * 
 * <pre>
 *	// unique application ID
 *	String APP_ID = "tk.pratanumandal.unique4j-mlsdvo-20191511-#j.6";
 *	
 *	// create unique instance
 *	Unique unique = new UniqueList(APP_ID) {
 *	&nbsp;&nbsp;&nbsp;&nbsp;&#64;Override
 *	&nbsp;&nbsp;&nbsp;&nbsp;public void receiveMessage(String message) {
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// print received message (timestamp)
 *	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(message);
 *	&nbsp;&nbsp;&nbsp;&nbsp;}
 *	&nbsp;&nbsp;&nbsp;&nbsp;
 *	&nbsp;&nbsp;&nbsp;&nbsp;&#64;Override
 *	&nbsp;&nbsp;&nbsp;&nbsp;public String sendMessage() {
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

	@Override
	protected void receiveMessage(String message) {
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

	@Override
	protected String sendMessage() {
		// convert arguments to JSON array string
        JsonObject jsonObj = new JsonObject();
        
        Map<String, String> stringArgs = sendMessageMap();
        
        for (Entry<String, String> entry : stringArgs.entrySet()) {
        	jsonObj.addProperty(entry.getKey(), entry.getValue());
        }

        // return the JSON array string
        return jsonObj.toString();
	}
	
	protected abstract void receiveMessageMap(Map<String, String> messageMap);
	
	protected abstract Map<String, String> sendMessageMap();
	
}
