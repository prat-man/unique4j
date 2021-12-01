package in.pratanumandal.unique4j;

import in.pratanumandal.unique4j.exception.Unique4jException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.pratanumandal.unique4j.Unique4jTest.getAppId;

public class Unique4jMapTest {

    @Test
    public void testUnique4jMap() throws Unique4jException {
        final String APP_ID = getAppId();

        final Object lock = new Object();

        final Map<String, String> received = new HashMap<String, String>();

        final Map<String, String> messageMap = new HashMap<String, String>();
        messageMap.put("23vvf1", "ijvnfpp389528$#$@520sdf.213sgv6");
        messageMap.put("23vvf2", "ijvnfpp389528$#$@520sdf.213sgv7");
        messageMap.put("23vvf3", "ijvnfpp389528$#$@520sdf.213sgv8");
        messageMap.put("23vvf4", "ijvnfpp389528$#$@520sdf.213sgv9");

        Unique4j unique1 = new Unique4jMap(APP_ID, false) {
            @Override
            protected Map<String, String> sendMessageMap() {
                // send null
                return null;
            }

            @Override
            protected void receiveMessageMap(Map<String, String> message) {
                // to assert on main thread
                received.putAll(message);

                // notify that message has been received
                synchronized (lock) {
                    lock.notify();
                }
            }
        };

        Unique4j unique2 = new Unique4jMap(APP_ID, false) {
            @Override
            protected Map<String, String> sendMessageMap() {
                // send message list
                return messageMap;
            }

            @Override
            protected void receiveMessageMap(Map<String, String> message) {
                // do nothing
            }
        };

        // try to obtain lock
        unique1.acquireLock();

        unique2.acquireLock();

        try {
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
            Assert.assertEquals(messageMap, received);
        } finally {
            // try to free the locks before exiting program
            unique1.releaseLock();

            unique2.releaseLock();
        }
    }

    @Test
    public void testUnique4jMapNewline() throws Unique4jException {
        final String APP_ID = getAppId();

        final Object lock = new Object();

        final Map<String, String> received = new HashMap<String, String>();

        final Map<String, String> messageMap = new HashMap<String, String>();
        messageMap.put("23vvf1", "C:\\Users\\Pratanu\nMandal\\Desktop\\rptMrExam.pdf");
        messageMap.put("23vvf2", "C:\\Users\\Pratanu Mandal\\Desktop\\nptMrExam.pdf");
        messageMap.put("23vvf3", "C:\\Users\\Pratanu Mandal\\Desktop\\'rptMrExam.pdf");
        messageMap.put("23vvf4", "C:\\Users\\Pratanu Mandal\\Desktop\\rptMrExam.pdf");

        Unique4j unique1 = new Unique4jMap(APP_ID, false) {
            @Override
            protected Map<String, String> sendMessageMap() {
                // send null
                return null;
            }

            @Override
            protected void receiveMessageMap(Map<String, String> message) {
                // to assert on main thread
                received.putAll(message);

                // notify that message has been received
                synchronized (lock) {
                    lock.notify();
                }
            }
        };

        Unique4j unique2 = new Unique4jMap(APP_ID, false) {
            @Override
            protected Map<String, String> sendMessageMap() {
                // send message list
                return messageMap;
            }

            @Override
            protected void receiveMessageMap(Map<String, String> message) {
                // do nothing
            }
        };

        // try to obtain lock
        unique1.acquireLock();

        unique2.acquireLock();

        try {
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
            Assert.assertEquals(messageMap, received);
        } finally {
            // try to free the locks before exiting program
            unique1.releaseLock();

            unique2.releaseLock();
        }
    }

    @Test
    public void testUnique4jMapNull() throws Unique4jException {
        final String APP_ID = getAppId();

        final Object lock = new Object();

        final List<Object> received = new ArrayList<Object>();

        Unique4j unique1 = new Unique4jMap(APP_ID, false) {
            @Override
            protected Map<String, String> sendMessageMap() {
                // send null
                return null;
            }

            @Override
            protected void receiveMessageMap(Map<String, String> message) {
                // to assert on main thread
                received.add(message);

                // notify that message has been received
                synchronized (lock) {
                    lock.notify();
                }
            }
        };

        Unique4j unique2 = new Unique4jMap(APP_ID, false) {
            @Override
            protected Map<String, String> sendMessageMap() {
                // send null
                return null;
            }

            @Override
            protected void receiveMessageMap(Map<String, String> message) {
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
            Assert.assertNull(received.get(0));
        } finally {
            // try to free the locks before exiting program
            unique1.releaseLock();

            unique2.releaseLock();
        }
    }
}
