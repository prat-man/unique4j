package in.pratanumandal.unique4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public interface SocketIpcFactory {

    ServerSocket getServerSocket(OutputStream fileOutputStream) throws IOException;

    Socket getClientSocket(InputStream fileInputStream) throws IOException;
}
