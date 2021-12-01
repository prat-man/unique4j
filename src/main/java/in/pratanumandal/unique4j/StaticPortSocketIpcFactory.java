package in.pratanumandal.unique4j;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class StaticPortSocketIpcFactory implements SocketIpcFactory, PortIpcFactory {

    private final InetAddress address;
    private final int port;

    public StaticPortSocketIpcFactory(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public ServerSocket getServerSocket(File parentDirectory, String appId) throws IOException {
        return new ServerSocket(port, 0, address);
    }

    @Override
    public Socket getClientSocket(File parentDirectory, String appId) throws IOException {
        return new Socket(address, port);
    }

    @Override
    public int getPort() {
        return port;
    }
}
