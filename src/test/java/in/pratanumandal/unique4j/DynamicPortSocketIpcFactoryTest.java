package in.pratanumandal.unique4j;

import in.pratanumandal.unique4j.exception.Unique4jException;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static in.pratanumandal.unique4j.Unique4jTest.getAppId;

public class DynamicPortSocketIpcFactoryTest {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    @Test
    public void testCustomPortDynamic() throws Unique4jException {
        final String APP_ID = getAppId();

        final Object lock = new Object();

        final List<String> received = new ArrayList<String>();

        final String message = "ijvnfpp389528$#$@520sdf.213sgv8";

        Unique4j unique1 = new Unique4j(APP_ID, false, 8080, PortPolicy.DYNAMIC) {
            @Override
            protected String sendMessage() {
                // send null
                return null;
            }

            @Override
            protected void receiveMessage(String arg0) {
                // to assert on main thread
                received.add(arg0);

                // notify that message has been received
                synchronized (lock) {
                    lock.notify();
                }
            }
        };

        Unique4j unique2 = new Unique4j(APP_ID, false, 8080, PortPolicy.DYNAMIC) {
            @Override
            protected String sendMessage() {
                // send message
                return message;
            }

            @Override
            protected void receiveMessage(String arg0) {
                // do nothing
            }
        };

        try {
            // try to obtain lock
            unique1.acquireLock();

            unique2.acquireLock();

            // wait until message is received
            if (received.isEmpty()) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // assert if message is sent correctly
            Assert.assertEquals(message, received.get(0));
        } finally {
            // try to free the locks before exiting program
            unique1.releaseLock();

            unique2.releaseLock();
        }
    }

    @Test
    public void testCorruptedLockFile() throws Unique4jException, IOException {
        final String APP_ID = getAppId();

        String filePath = TEMP_DIR + File.separator + APP_ID + ".lock";

        File file = new File(filePath);

        // if lock file exists, delete it
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }

        // create a corrupted lock file
        FileUtils.writeStringToFile(file, "abcdefghi\njklmnop\n\rqrst", Charset.forName("UTF-8"));

        // create instance of Unique
        Unique4j unique = new Unique4j(APP_ID) {
            @Override
            public String sendMessage() {
                // send null
                return null;
            }

            @Override
            public void receiveMessage(String message) {
                // do nothing
            }
        };

        try {
            // try to obtain lock
            unique.acquireLock();
        } finally {
            // try to free the lock before exiting program
            unique.releaseLock();
        }
    }
}
