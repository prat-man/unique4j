package in.pratanumandal.unique4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public abstract class SocketChannelIpcFactory implements IpcFactory {

    @Override
    public IpcServer createIpcServer(File parentDirectory, String appId) throws IOException {
        return new SocketChannelIpcServer(createServerSocket(parentDirectory, appId));
    }

    @Override
    public IpcClient createIpcClient(File parentDirectory, String appId) throws IOException {
        return new SocketChannelIpcClient(createClientSocket(parentDirectory, appId));
    }

    protected abstract ServerSocketChannel createServerSocket(File parentDirectory, String appId) throws IOException;

    protected abstract SocketChannel createClientSocket(File parentDirectory, String appId) throws IOException;

    protected static class SocketChannelIpcServer implements IpcServer {

        protected final ServerSocketChannel channel;

        public SocketChannelIpcServer(ServerSocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public boolean isClosed() {
            return !channel.isOpen();
        }

        @Override
        public IpcClient accept() throws IOException {
            return new SocketChannelIpcClient(channel.accept());
        }

        @Override
        public void close() throws IOException {
            channel.close();
        }
    }

    protected static class SocketChannelIpcClient implements IpcClient {

        protected final SocketChannel channel;

        public SocketChannelIpcClient(SocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public InputStream getInputStream() {
            return Channels.newInputStream(channel);
        }

        @Override
        public OutputStream getOutputStream() {
            return Channels.newOutputStream(channel);
        }

        @Override
        public void close() throws IOException {
            channel.close();
        }
    }
}
