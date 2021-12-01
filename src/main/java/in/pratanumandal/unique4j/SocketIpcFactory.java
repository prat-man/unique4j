package in.pratanumandal.unique4j;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface SocketIpcFactory {

    ServerSocket getServerSocket(File parentDirectory, String appId) throws IOException;

    Socket getClientSocket(File parentDirectory, String appId) throws IOException;
}
