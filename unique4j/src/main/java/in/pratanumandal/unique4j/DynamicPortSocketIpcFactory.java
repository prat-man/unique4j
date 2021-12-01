package in.pratanumandal.unique4j;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class DynamicPortSocketIpcFactory extends SocketIpcFactory implements PortIpcFactory {

    private final InetAddress address;
    private final int port;
    private final Charset charset;

    private int actualPort = -1;

    public DynamicPortSocketIpcFactory(InetAddress address, int port) {
        this(address, port, Charset.defaultCharset());
    }

    public DynamicPortSocketIpcFactory(InetAddress address, int port, Charset charset) {
        this.address = address;
        this.port = port;
        this.charset = charset;
    }

    @Override
    public IpcServer createIpcServer(File parentDirectory, String appId) throws IOException {
        final ServerSocket socket = createServerSocket(parentDirectory, appId);

        BufferedWriter bw = null;
        try {
            final File portFile = new File(parentDirectory, appId + ".port");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(portFile), charset));
            bw.write(String.valueOf(actualPort));
            bw.close();
            return new SocketIpcServer(socket) {
                @Override
                public void close() throws IOException {
                    super.close();
                    if(!portFile.delete())
                        throw new IOException("Failed to delete port file " + portFile);
                }
            };
        } catch (IOException ex) {

            try {
                if(bw != null)
                    bw.close();
            } catch (IOException ignored) {
                // We don't want to swallow the other exception
            }

            try {
                socket.close();
            } catch (IOException ignored) {
                // We don't want to swallow the other exception
            }

            throw ex;
        }
    }

    @Override
    public ServerSocket createServerSocket(File parentDirectory, String appId) {
        // use dynamic port policy
        actualPort = port;
        while (true) {
            try {
                return new ServerSocket(actualPort, 0, address);
            } catch (IOException e) {
                actualPort++;
            }
        }
    }

    @Override
    public Socket createClientSocket(File parentDirectory, String appId) throws IOException {
        BufferedReader br = null;
        try {
            final File portFile = new File(parentDirectory, appId + ".port");
            br = new BufferedReader(new InputStreamReader(new FileInputStream(portFile), charset));

            try {
                this.actualPort = Integer.parseInt(br.readLine());
            } catch (NumberFormatException ex) {
                throw new IOException("Corrupted port file " + portFile, ex);
            }

            br.close();
            return new Socket(address, actualPort);
        } catch (IOException ex) {
            try {
                if(br != null)
                    br.close();
            } catch (IOException ignored) {
                // We don't want to swallow the other exception
            }
            throw ex;
        }
    }

    @Override
    public int getPort() {
        return actualPort;
    }
}
