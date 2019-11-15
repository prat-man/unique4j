package tk.pratanumandal.unique4j;

import org.junit.Test;

public class Unique4jTest {
	
    @Test
    public void testUnique4j() throws Unique4jException {
    	
    	Unique unique = new Unique("unique-app-id-dfbbd-$%%231pqxzf5145-34dmnfzq") {
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
		
		unique.lock();
		
		unique.free();
		
    }
    
}
