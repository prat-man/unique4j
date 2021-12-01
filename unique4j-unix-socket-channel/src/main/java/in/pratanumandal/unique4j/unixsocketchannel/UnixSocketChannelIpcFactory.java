package in.pratanumandal.unique4j.unixsocketchannel;

import in.pratanumandal.unique4j.IpcServer;
import in.pratanumandal.unique4j.SocketChannelIpcFactory;

import java.io.File;
import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class UnixSocketChannelIpcFactory extends SocketChannelIpcFactory {

    @Override
    public IpcServer createIpcServer(File parentDirectory, String appId) throws IOException {
        return new SocketChannelIpcFactory.SocketChannelIpcServer(createServerSocket(parentDirectory, appId)) {
            @Override
            public void close() throws IOException {
                final Path file = isClosed() ?
                        null :
                        ((UnixDomainSocketAddress) channel.getLocalAddress()).getPath();
                super.close();

                if(file != null)
                    Files.deleteIfExists(file);
            }
        };
    }

    @Override
    protected ServerSocketChannel createServerSocket(File parentDirectory, String appId) throws IOException {
        Path socketPath = parentDirectory.toPath().resolve(appId + ".socket");
        Files.deleteIfExists(socketPath);

        UnixDomainSocketAddress socketAddress = UnixDomainSocketAddress.of(socketPath);
        ServerSocketChannel serverChannel = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
        serverChannel.bind(socketAddress);
        return serverChannel;
    }

    @Override
    protected SocketChannel createClientSocket(File parentDirectory, String appId) throws IOException {
        Path socketPath = parentDirectory.toPath().resolve(appId + ".socket");
        UnixDomainSocketAddress socketAddress = UnixDomainSocketAddress.of(socketPath);
        SocketChannel channel = SocketChannel.open(StandardProtocolFamily.UNIX);
        channel.connect(socketAddress);
        return channel;
    }
}
