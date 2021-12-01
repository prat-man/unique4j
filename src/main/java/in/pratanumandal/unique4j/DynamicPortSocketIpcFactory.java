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
    public ServerSocket createServerSocket(File parentDirectory, String appId) throws IOException {
        // use dynamic port policy
        ServerSocket socket;
        actualPort = port;
        while (true) {
            try {
                socket = new ServerSocket(actualPort, 0, address);
                break;
            } catch (IOException e) {
                actualPort++;
            }
        }

        BufferedWriter bw = null;
        try {
            final File portFile = new File(parentDirectory, appId + ".port");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(portFile), charset));
            bw.write(String.valueOf(actualPort));
            bw.close();
            return socket;
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
