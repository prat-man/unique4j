package tk.pratanumandal.unique4j;

import org.junit.Test;

public class Unique4jTest {
	
    @Test
    public void testUnique4j() throws Unique4jException {
    	
    	Unique unique = new Unique("unique-app-id-mlsdvo-20191511-#j.6") {
			@Override
			public String sendMessage() {
				// send null
				return null;
			}
			
			@Override
			public void receiveMessage(String arg0) {
				// do nothing
			}
		};
		
		// try to obtain lock
		unique.lock();
		
		// try to free the lock before exiting program
		unique.free();
		
    }
    
}
