package in.pratanumandal.unique4j;

import java.io.Closeable;
import java.io.IOException;

public interface IpcServer extends Closeable {

    boolean isClosed();

    IpcClient accept() throws IOException;
}
