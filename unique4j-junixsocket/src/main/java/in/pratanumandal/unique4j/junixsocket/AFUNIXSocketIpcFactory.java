package in.pratanumandal.unique4j.junixsocket;

import in.pratanumandal.unique4j.IpcServer;
import in.pratanumandal.unique4j.SocketIpcFactory;
import org.newsclub.net.unix.AFUNIXServerSocket;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class AFUNIXSocketIpcFactory extends SocketIpcFactory {

    @Override
    public IpcServer createIpcServer(File parentDirectory, String appId) throws IOException {
        return new SocketIpcServer(createServerSocket(parentDirectory, appId)) {
            @Override
            public void close() throws IOException {
                final File file = isClosed() ?
                        null :
                        ((AFUNIXSocketAddress) socket.getLocalSocketAddress()).getFile();
                super.close();

                if(file != null)
                    Files.deleteIfExists(file.toPath());
            }
        };
    }

    @Override
    protected ServerSocket createServerSocket(File parentDirectory, String appId) throws IOException {
        File socketFile = new File(parentDirectory, appId + ".socket");
        AFUNIXServerSocket socket = AFUNIXServerSocket.newInstance();
        socket.bind(AFUNIXSocketAddress.of(socketFile));
        return socket;
    }

    @Override
    protected Socket createClientSocket(File parentDirectory, String appId) throws IOException {
        File socketFile = new File(parentDirectory, appId + ".socket");
        AFUNIXSocket socket = AFUNIXSocket.newInstance();
        socket.connect(AFUNIXSocketAddress.of(socketFile));
        return socket;
    }
}
