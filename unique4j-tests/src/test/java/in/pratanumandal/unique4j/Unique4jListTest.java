package in.pratanumandal.unique4j;

import in.pratanumandal.unique4j.exception.Unique4jException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static in.pratanumandal.unique4j.Unique4jTest.getAppId;

public class Unique4jListTest {

    @Test
    public void testUnique4jList() throws Unique4jException {
        final String APP_ID = getAppId();

        final Object lock = new Object();

        final List<String> received = new ArrayList<String>();

        final List<String> messageList = new ArrayList<String>();
        messageList.add("ijvnfpp389528$#$@520sdf.213sgv6");
        messageList.add("ijvnfpp389528$#$@520sdf.213sgv7");
        messageList.add("ijvnfpp389528$#$@520sdf.213sgv8");
        messageList.add("ijvnfpp389528$#$@520sdf.213sgv9");

        Unique4j unique1 = new Unique4jList(APP_ID, false) {
            @Override
            protected List<String> sendMessageList() {
                // send null
                return null;
            }

            @Override
            protected void receiveMessageList(List<String> message) {
                // to assert on main thread
                received.addAll(message);

                // notify that message has been received
                synchronized (lock) {
                    lock.notify();
                }
            }
        };

        Unique4j unique2 = new Unique4jList(APP_ID, false) {
            @Override
            protected List<String> sendMessageList() {
                // send message list
                return messageList;
            }

            @Override
            protected void receiveMessageList(List<String> message) {
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
            Assert.assertEquals(messageList, received);
        } finally {
            // try to free the locks before exiting program
            unique1.releaseLock();

            unique2.releaseLock();
        }
    }

    @Test
    public void testUnique4jListNewline() throws Unique4jException {
        final String APP_ID = getAppId();

        final Object lock = new Object();

        final List<String> received = new ArrayList<String>();

        final List<String> messageList = new ArrayList<String>();
        messageList.add("C:\\Users\\Pratanu\nMandal\\Desktop\\rptMrExam.pdf");
        messageList.add("C:\\Users\\Pratanu Mandal\\Desktop\\nptMrExam.pdf");
        messageList.add("C:\\Users\\Pratanu Mandal\\Desktop\\'rptMrExam.pdf");
        messageList.add("C:\\Users\\Pratanu Mandal\\Desktop\\rptMrExam.pdf");

        Unique4j unique1 = new Unique4jList(APP_ID, false) {
            @Override
            protected List<String> sendMessageList() {
                // send null
                return null;
            }

            @Override
            protected void receiveMessageList(List<String> message) {
                // to assert on main thread
                received.addAll(message);

                // notify that message has been received
                synchronized (lock) {
                    lock.notify();
                }
            }
        };

        Unique4j unique2 = new Unique4jList(APP_ID, false) {
            @Override
            protected List<String> sendMessageList() {
                // send message list
                return messageList;
            }

            @Override
            protected void receiveMessageList(List<String> message) {
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
            Assert.assertEquals(messageList, received);
        } finally {
            // try to free the locks before exiting program
            unique1.releaseLock();

            unique2.releaseLock();
        }
    }

    @Test
    public void testUnique4jListNull() throws Unique4jException {
        final String APP_ID = getAppId();

        final Object lock = new Object();

        final List<Object> received = new ArrayList<Object>();

        Unique4j unique1 = new Unique4jList(APP_ID, false) {
            @Override
            protected List<String> sendMessageList() {
                // send null
                return null;
            }

            @Override
            protected void receiveMessageList(List<String> message) {
                // to assert on main thread
                received.add(message);

                // notify that message has been received
                synchronized (lock) {
                    lock.notify();
                }
            }
        };

        Unique4j unique2 = new Unique4jList(APP_ID, false) {
            @Override
            protected List<String> sendMessageList() {
                // send null
                return null;
            }

            @Override
            protected void receiveMessageList(List<String> message) {
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
