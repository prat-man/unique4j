package in.pratanumandal.unique4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public ServerSocket getServerSocket(OutputStream fileOutputStream) throws IOException {
        return new ServerSocket(port, 0, address);
    }

    @Override
    public Socket getClientSocket(InputStream fileInputStream) throws IOException {
        return new Socket(address, port);
    }

    @Override
    public int getPort() {
        return port;
    }
}
