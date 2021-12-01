package in.pratanumandal.unique4j;

import in.pratanumandal.unique4j.exception.Unique4jException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StaticPortSocketIpcFactoryTest {

    public static final String BASE_APP_ID = Unique4jTest.BASE_APP_ID;
    public static final AtomicInteger APP_ID_COUNT = Unique4jTest.APP_ID_COUNT;

    @Test
    public void testCustomPortStatic() throws Unique4jException {
        final String APP_ID = BASE_APP_ID + "-" + APP_ID_COUNT.getAndIncrement();

        final Object lock = new Object();

        final List<String> received = new ArrayList<String>();

        final String message = "ijvnfpp389528$#$@520sdf.213sgv8";

        Unique4j unique1 = new Unique4j(APP_ID, false, 8080, PortPolicy.STATIC) {
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

        Unique4j unique2 = new Unique4j(APP_ID, false, 8080, PortPolicy.STATIC) {
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
}
