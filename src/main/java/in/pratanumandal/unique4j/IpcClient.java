package in.pratanumandal.unique4j;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IpcClient extends Closeable {

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;
}
