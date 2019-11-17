# Unique4j
Java library to allow only single instance of a java application to run and enable communication between first instance and subsequent instances.

## Sample Usage

    // unique application ID
    String APP_ID = "tk.pratanumandal.unique4j-mlsdvo-20191511-#j.6";

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
    
    // try to obtain lock
    try {
        unique.acquireLock();
    } catch (Unique4jException e) {
        e.printStackTrace();
    }
    
    ...
    
    // try to free the lock before exiting program
    try {
        unique.freeLock();
    } catch (Unique4jException e) {
        e.printStackTrace();
    }
