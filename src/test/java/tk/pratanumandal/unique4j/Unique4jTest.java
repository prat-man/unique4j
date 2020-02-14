/**
 * Copyright 2019 Pratanu Mandal
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tk.pratanumandal.unique4j;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import tk.pratanumandal.unique4j.exception.Unique4jException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Random;

public class Unique4jTest {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    @Test
    public void testUnique4j() throws Unique4jException {

        Unique unique = new Unique("tk.pratanumandal.unique4j-mlsdvo-20191511-#j.6") {
            @Override
            public String sendMessage() {
                // send null
                return null;
            }

            @Override
            public void receiveMessage(String arg0) {
                // do nothing
            }

            @Override
            public void handleException(Exception exception) {
                // do nothing
            }

            @Override
            public void beforeExit() {
                // do nothing
            }
        };

        // try to obtain lock
        unique.acquireLock();

        // try to free the lock before exiting program
        unique.freeLock();

    }

    @Test
    public void illegalLockFileContentTest() throws InterruptedException, IOException, Unique4jException {
        String APP_ID = "tk.pratanumandal.unique4j.unittest." + new Random().nextInt();
        String filePath = TEMP_DIR + "/" + APP_ID + ".lock";
        final File file = new File(filePath);
        // Another unit test instance might use this file, wait for the file to be gone by itself
        for (int i = 0; i < 20; i++) {
            if (!file.exists()) break;
            Thread.sleep(1000);
        }
        // If it still exists, delete it
        if (file.exists())
            FileUtils.forceDelete(file);

        // Create a file whose content is not an integer
        FileUtils.writeStringToFile(file, "abcdefghi", Charset.forName("UTF-8"));

        Unique uniqueObject = new Unique(APP_ID) {
            @Override
            public void receiveMessage(String message) {

            }

            @Override
            public String sendMessage() {
                return null;
            }
        };

        // No exception shall be thrown
        uniqueObject.acquireLock();
        uniqueObject.freeLock();
    }
}
