# Unique4j
Java library to allow only single instance of a java application to run and enable communication between first instance and subsequent instances.

## Sample Usage

    // unique application ID
    String APP_ID = "tk.pratanumandal.unique-mlsdvo-20191511-#j.6";

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
    };

    // try to lock
    unique.lock();

    ...

    // try to free the lock before exiting program
    unique.free();
