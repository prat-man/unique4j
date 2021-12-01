package in.pratanumandal.unique4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class SocketIpcFactory implements IpcFactory {

    @Override
    public IpcServer createIpcServer(File parentDirectory, String appId) throws IOException {
        return new SocketIpcServer(createServerSocket(parentDirectory, appId));
    }

    @Override
    public IpcClient createIpcClient(File parentDirectory, String appId) throws IOException {
        return new SocketIpcClient(createClientSocket(parentDirectory, appId));
    }

    protected abstract ServerSocket createServerSocket(File parentDirectory, String appId) throws IOException;

    protected abstract Socket createClientSocket(File parentDirectory, String appId) throws IOException;

    protected static class SocketIpcServer implements IpcServer {

        protected final ServerSocket socket;

        public SocketIpcServer(ServerSocket socket) {
            this.socket = socket;
        }

        @Override
        public boolean isClosed() {
            return socket.isClosed();
        }

        @Override
        public IpcClient accept() throws IOException {
            return new SocketIpcClient(socket.accept());
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }
    }

    protected static class SocketIpcClient implements IpcClient {

        protected final Socket socket;

        public SocketIpcClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return socket.getInputStream();
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return socket.getOutputStream();
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }
    }
}
