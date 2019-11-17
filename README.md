# Unique4j
Java library to allow only single instance of a java application to run and enable communication between first instance and subsequent instances.

## Sample Usage

Declare a application unique ID which is a common constant for all the instances

    // unique application ID
    public static String APP_ID = "tk.pratanumandal.unique4j-mlsdvo-20191511-#j.6";

Create an instance of <code>Unique</code> class

    // create unique instance
    Unique unique = new Unique(APP_ID) {
        @Override
        public void receiveMessage(String message) {
            // print received message (timestamp)
            System.out.println(message);
        }

        @Override
        public String sendMessage() {
            // send timestamp as message
            Timestamp ts = new Timestamp(new Date().getTime());
            return "Another instance launch attempted: " + ts.toString();
        }
        
        /* It is not mandatory to override this method
         * By default, the stack trace is printed
         */
        @Override
        public void handleException(Exception exception) {
            // display the exception message
            System.out.println(exception.getMessage());
        }

        /* It is not mandatory to override this method
         * By default, the subsequent instance simply exits
         * This method is not invoked if AUTO_EXIT is turned off
         */
        @Override
        public void beforeExit() {
            // display exit message
            System.out.println("Exiting subsequent instance.");
        }
    };
   
Alternatively, you can declare to turn off automatic exit for subsequent instances

    // create unique instance with AUTO_EXIT turned off
    Unique unique = new Unique(APP_ID,
                               false) // second parameter is for AUTO_EXIT (false turns it off)
    { 
        ...
        // Note: beforeExit() method even if declared is never invoked if AUTO_EXIT is turned off
    }
   
Try to obtain a lock using the <code>Unique</code> object
    
    // try to obtain lock
    boolean lockFlag = false;
    try {
        lockFlag = unique.acquireLock();
    } catch (Unique4jException e) {
        e.printStackTrace();
    }
    
    // perform long running tasks here
    
Free the lock using the <code>Unique</code> object
    
    // long running tasks end here
    
    // try to free the lock before exiting program
    boolean lockFreeFlag = false;
    try {
        lockFreeFlag = unique.freeLock();
    } catch (Unique4jException e) {
        e.printStackTrace();
    }
